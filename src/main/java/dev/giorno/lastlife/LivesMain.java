package dev.giorno.lastlife;

import dev.giorno.lastlife.commands.GetDeaths;
import dev.giorno.lastlife.commands.ReloadConfig;
import dev.giorno.lastlife.listeners.DeathListener;
import dev.giorno.lastlife.listeners.Milmk;
import dev.giorno.lastlife.managers.Config;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LivesMain extends JavaPlugin {

    @Getter private static LivesMain instance;
    @Getter private Config cfg = new Config();
    @Getter private static LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;
        luckPerms = getServer().getServicesManager().load(LuckPerms.class);

        this.getCommand("getdeaths").setExecutor(new GetDeaths());
        this.getCommand("reloadlivesconfig").setExecutor(new ReloadConfig());

        PluginManager pm = getServer().getPluginManager();
        this.saveDefaultConfig();

        pm.registerEvents(new DeathListener(), this);
        pm.registerEvents(new Milmk(), this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }

}
