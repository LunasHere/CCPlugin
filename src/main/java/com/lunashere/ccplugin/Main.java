package com.lunashere.ccplugin;

import com.lunashere.ccplugin.Commands.SuggestCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    // Make secret global
    public String secret;
    public String URL;

    @Override
    public void onEnable() {
        // Config setup
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Set vars
        secret = getConfig().getString("secret");
        URL = getConfig().getString("url");

        // Register command
        getCommand("suggest").setExecutor(new SuggestCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
