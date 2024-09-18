package waterfun.waterwood.watercommand;


import me.waterwood.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.waterwood.common.LineFontGenerator;
import me.waterwood.bukkit.BukkitPlugin;

public final class WaterCommand extends BukkitPlugin {
    private static BukkitPlugin instance;
    @Override
    public void onEnable() {
        this.initialization();
        for(String str : LineFontGenerator.parseLineText("WaterCMD")) {
            logMsg("§6%s§r".formatted(str));
        }
        logMsg("§e%s §6author:§7%s §6version:§7%s".formatted(getPluginInfo("name")
                , getPluginInfo("author"), getPluginInfo("version")));
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
