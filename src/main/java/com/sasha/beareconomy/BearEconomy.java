package com.sasha.beareconomy;

import com.sasha.beareconomy.commands.*;
import com.sasha.beareconomy.managers.EconomyManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class BearEconomy extends JavaPlugin {

    private static BearEconomy instance;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config.yml
        saveDefaultConfig();

        // Initialize the manager
        economyManager = new EconomyManager(this);

        // Register Commands
        getCommand("eco").setExecutor(new EcoCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("balance").setExecutor(new BalCommand(this));
        getCommand("balancetop").setExecutor(new BalTopCommand(this));

        // Register Tab Completion
        BearEconomyTabCompleter tabCompleter = new BearEconomyTabCompleter();
        getCommand("eco").setTabCompleter(tabCompleter);
        getCommand("pay").setTabCompleter(tabCompleter);
        getCommand("balance").setTabCompleter(tabCompleter);

        // Hook into PlaceholderAPI if it is installed
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new com.sasha.beareconomy.hooks.EconomyExpansion(this).register();
            getLogger().info("Successfully hooked into PlaceholderAPI!");
        }

        getLogger().info("BearEconomy enabled! Vault replacement active.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BearEconomy disabled. Data saved.");
    }

    public static BearEconomy getInstance() {
        return instance;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    // Message Utilities
    public String getMsg(String path) {
        String prefix = getConfig().getString("messages.prefix", "&8[&aBearEco&8] ");
        String msg = getConfig().getString("messages." + path, "&cMessage missing: " + path);
        return ChatColor.translateAlternateColorCodes('&', prefix + msg);
    }

    public String getRawMsg(String path) {
        String msg = getConfig().getString("messages." + path, "&cMessage missing: " + path);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}