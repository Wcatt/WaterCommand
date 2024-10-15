package waterfun.waterwood.watercommand;


import org.bukkit.Bukkit;
import org.waterwood.plugin.bukkit.BukkitPlugin;
import org.waterwood.plugin.bukkit.Metrics;

public final class WaterCommand extends BukkitPlugin {
    private static BukkitPlugin instance;
    @Override
    public void onEnable() {
        this.initialization();
        this.showPluginTitle("WaterCMD");
        instance = this;
        this.loadConfig(false);
        this.checkUpdate("Wcatt","WaterCommand");
        Methods.init(this);
        if(Methods.hasPapi()){
            logMsg(getPluginMessage("papi-enabled-message"));
        }
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this,getConfigs().getInteger("doBounceDelay")),this);
        getCommand("WaterCommand").setExecutor(new MainCommands(this));
        getCommand("WaterCommand").setTabCompleter(new MainCommands(this));
        Metrics metrics = new Metrics(this,23401);
    }

    public static BukkitPlugin getInstance(){
        return instance;
    }

}
