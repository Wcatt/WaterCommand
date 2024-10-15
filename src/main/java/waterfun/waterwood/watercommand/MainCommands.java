package waterfun.waterwood.watercommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.waterwood.plugin.bukkit.BukkitPlugin;

import java.util.ArrayList;
import java.util.List;

public class MainCommands implements CommandExecutor, TabCompleter {
    public BukkitPlugin plugin;
    public MainCommands(BukkitPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender.hasPermission("watercommand.admin")){
            if(args.length > 1){
                sender.sendMessage(plugin.getPluginMessage("illegal-args-message"));
                return false;
            }
            WaterCommand.getInstance().reloadConfig();
            this.plugin =  WaterCommand.getInstance();
            Methods.init(WaterCommand.getInstance());
            PlayerEvents.setDebounceDelayMs(plugin.getConfigs().getInteger("doBounceDelay"));
            sender.sendMessage(plugin.getPluginMessage("config-reload-message"));
        }else{
            sender.sendMessage(plugin.getPluginMessage("no-permission-message"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,String label,String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("reload");
        }
        return suggestions;
    }
}
