package me.georgejr.discordLink;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class DiscordLink extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getLogger().log(Level.INFO, "[DiscordLink] Plugin Starting...");
        //config register
        //getConfig().options().copyDefaults();
        saveDefaultConfig();

        //command
        getCommand("discord").setExecutor(new PluginCommand());
        getCommand("discord").setTabCompleter(new PluginCommand());


        getServer().getLogger().log(Level.INFO, "[DiscordLink] Plugin Started");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getLogger().log(Level.INFO, "[DiscordLink] Plugin Stopping...");
        saveConfig();

        getServer().getLogger().log(Level.INFO, "[DiscordLink] Plugin Stopped");
    }

    public String formatString(String s){

        return ChatColor.translateAlternateColorCodes('&', s.replace("{PREFIX}", getConfig().getString("prefix")));
    }
}
