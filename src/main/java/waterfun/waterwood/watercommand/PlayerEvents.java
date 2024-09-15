package waterfun.waterwood.watercommand;

import me.clip.placeholderapi.PlaceholderAPI;
import me.waterwood.BukkitPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class PlayerEvents implements Listener {
    private static final Map<UUID, ScheduledExecutorService> debounceMap = new ConcurrentHashMap<>();

    public static void setDebounceDelayMs(int debounceDelayMs) {
        DEBOUNCE_DELAY_MS = debounceDelayMs;
    }

    private static int DEBOUNCE_DELAY_MS;
    private final CommandSender sender = Bukkit.getConsoleSender();

    public PlayerEvents(BukkitPlugin plugin, int debounceDelay) {
        DEBOUNCE_DELAY_MS = plugin.getConfigs().getInteger("doBounceDelay");
    }

    private void doBounceEvent(UUID playerUUID, Runnable action) {
        // Remove any existing executor service for this player
        ScheduledExecutorService existingExecutor = debounceMap.remove(playerUUID);
        if (existingExecutor != null) {
            existingExecutor.shutdownNow();
        }

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        debounceMap.put(playerUUID, executorService);

        executorService.schedule(() -> {
            try {
                Bukkit.getScheduler().runTask(WaterCommand.getInstance(), action::run);
            } finally {
                // Ensure cleanup even if action throws an exception
                debounceMap.remove(playerUUID);
                executorService.shutdown();
            }
        }, DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent evt) {
        Player player = evt.getPlayer();
        for (Bind bind : Methods.getBinds()) {
            String key = bind.getKey().toString();
            if (key.contains("SHIFT") && key.contains("Q") && isShiftPress(player)) {
                evt.setCancelled(bind.isCancelAction());
                doBounceEvent(player.getUniqueId(), () -> { executeCommand(player, bind);});
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent evt) {
        Player player = evt.getPlayer();
        for (Bind bind : Methods.getBinds()) {
            String key = bind.getKey().toString();
            if (key.contains("SHIFT") && key.contains("F") && isShiftPress(player)) {
                evt.setCancelled(bind.isCancelAction());
                doBounceEvent(player.getUniqueId(), () -> { executeCommand(player, bind);});
            }
        }
    }

    private void executeCommand(Player player, Bind bind) {
        String command = bind.getCommand();
        command = command.replace("{player}",player.getName());
        if(Methods.hasPapi()) {
            command = PlaceholderAPI.setPlaceholders(player, command);
            player.sendMessage(PlaceholderAPI.setPlaceholders(player, bind.getCommandMessage()));
        }
        if (bind.getCommandType() == Bind.CommandType.player) {
            if (!Objects.equals(bind.getPermission(), "") && !player.hasPermission(bind.getPermission())) {
                return;
            }
            player.chat("/" + command);
        } else {
            sender.getServer().dispatchCommand(player,command);
        }
    }

    private boolean isShiftPress(Player player) {
        return player.isSneaking();
    }
}
