package waterfun.waterwood.watercommand;

import me.waterwood.BukkitPlugin;
import org.bukkit.Bukkit;
import org.waterwood.common.LineFontGenerator;

public final class WaterCommand extends BukkitPlugin {
    private static WaterCommand instance;
    @Override
    public void onEnable() {
        this.initialize();
        for(String str : LineFontGenerator.parseLineText("WaterCMD")) {
            LogMsg("§6%s§r".formatted(str));
        }
        LogMsg("§e%s §6Author:§7%s §6Version:§7%s".formatted(getPluginInfo("name")
                , getPluginInfo("author"), getPluginInfo("version")));
        instance = this;
        this.loadConfig(false);
        this.checkUpdate("Wcatt","WaterCommand");
        Methods.init(this);
        if(Methods.hasPapi()){
            LogMsg(getPluginMessage("papi-enabled-message"));
        }
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(this,getConfigs().getInteger("doBounceDelay")),this);
        getCommand("WaterCommand").setExecutor(new MainCommands(this));
        getCommand("WaterCommand").setTabCompleter(new MainCommands(this));
    }

    public static BukkitPlugin getInstance(){
        return instance;
    }

}
