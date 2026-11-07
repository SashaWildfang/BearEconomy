package com.sasha.beareconomy.managers;

import com.sasha.beareconomy.BearEconomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {

    private final BearEconomy plugin;
    private final Map<UUID, Double> balances = new HashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public EconomyManager(BearEconomy plugin) {
        this.plugin = plugin;
        loadData();
    }

    // --- Core API Methods ---

    public double getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, plugin.getConfig().getDouble("settings.starting-balance", 0.0));
    }

    public void setBalance(UUID uuid, double amount) {
        // Prevent negative balances
        double finalAmount = Math.max(0.0, amount);
        balances.put(uuid, finalAmount);
        saveDataAsync();
    }

    public void addBalance(UUID uuid, double amount) {
        if (amount <= 0) return;
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public boolean removeBalance(UUID uuid, double amount) {
        if (amount <= 0) return true;
        double current = getBalance(uuid);
        if (current < amount) return false; // Insufficient funds

        setBalance(uuid, current - amount);
        return true;
    }

    public boolean hasBalance(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }

    // --- Utility Methods ---

    public String format(double amount) {
        String symbol = plugin.getConfig().getString("settings.currency-symbol", "$");
        String format = plugin.getConfig().getString("settings.currency-format", "%symbol%%amount%");

        return format.replace("%symbol%", symbol)
                .replace("%amount%", formatter.format(amount));
    }

    // --- Data Management ---

    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "balances.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try { dataFile.createNewFile(); } catch (IOException ignored) {}
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        balances.clear();
        if (dataConfig.contains("balances")) {
            for (String key : dataConfig.getConfigurationSection("balances").getKeys(false)) {
                try {
                    balances.put(UUID.fromString(key), dataConfig.getDouble("balances." + key));
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }

    private void saveDataAsync() {
        // Run asynchronously to prevent lag spikes during rapid economy transactions
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
                dataConfig.set("balances." + entry.getKey().toString(), entry.getValue());
            }
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to save balances.yml!");
            }
        });
    }

    public Map<UUID, Double> getAllBalances() {
        return balances;
    }
}