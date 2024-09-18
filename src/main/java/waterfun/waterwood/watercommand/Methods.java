package waterfun.waterwood.watercommand;

import me.waterwood.bukkit.BukkitPlugin;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Methods {
    private static boolean hasPapi;
    private static Set<Bind> binds;
    protected static BukkitPlugin plugin;
    public static void init(BukkitPlugin plugin){
        Methods.plugin = plugin;
        hasPapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        importBinds();
    }

    public static void importBinds(){
        binds = new HashSet<>();
        Map<String,Object> raw =(Map<String,Object>)plugin.getConfigs().get("Binds");
        Map<String,Object> map;
        int count = 0;
        for(Map.Entry<String,Object> entry : raw.entrySet()){
            map= (Map<String,Object>) entry.getValue();
            try {
                binds.add(new Bind((String) map.get("name"), (String) map.get("key"), (String) map.get("permission"), (String) map.get("command"), (boolean) map.get("cancelAction"),(String) map.get("message")));
                count ++;
            }catch (IllegalArgumentException e){
                plugin.logMsg(
                        BukkitPlugin.getPluginMessage("error-when-load-binds-message").formatted(entry.getKey())
                );
            }
        }
        plugin.logMsg(
                BukkitPlugin.getPluginMessage("load-binds-message").formatted(count)
        );
    }

    public static Set<Bind> getBinds() {
        return binds;
    }

    public static boolean hasPapi(){
        return hasPapi;
    }
}
