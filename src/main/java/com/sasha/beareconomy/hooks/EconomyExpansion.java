package com.sasha.beareconomy.hooks;

import com.sasha.beareconomy.BearEconomy;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class EconomyExpansion extends PlaceholderExpansion {

    private final BearEconomy plugin;

    public EconomyExpansion(BearEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "beareconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Sasha";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // Keeps the expansion active even if PAPI is reloaded
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        // Returns the formatted balance (e.g., "$1,500.00") -> %beareconomy_balance%
        if (params.equalsIgnoreCase("balance")) {
            double bal = plugin.getEconomyManager().getBalance(player.getUniqueId());
            return plugin.getEconomyManager().format(bal);
        }

        // Returns just the raw number (e.g., "1500.0") -> %beareconomy_balance_raw%
        if (params.equalsIgnoreCase("balance_raw")) {
            double bal = plugin.getEconomyManager().getBalance(player.getUniqueId());
            return String.valueOf(bal);
        }

        return null;
    }
}