package com.sasha.beareconomy.commands;

import com.sasha.beareconomy.BearEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PayCommand implements CommandExecutor {

    private final BearEconomy plugin;

    public PayCommand(BearEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getRawMsg("not-player"));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cUsage: /pay <player> <amount>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(plugin.getMsg("player-not-found"));
            return true;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getMsg("pay-self"));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getMsg("invalid-amount"));
            return true;
        }

        if (amount <= 0) {
            player.sendMessage(plugin.getMsg("invalid-amount"));
            return true;
        }

        if (!plugin.getEconomyManager().removeBalance(player.getUniqueId(), amount)) {
            player.sendMessage(plugin.getMsg("pay-insufficient")
                    .replace("%amount%", plugin.getEconomyManager().format(amount)));
            return true;
        }

        plugin.getEconomyManager().addBalance(target.getUniqueId(), amount);

        player.sendMessage(plugin.getMsg("pay-success-sender")
                .replace("%amount%", plugin.getEconomyManager().format(amount))
                .replace("%player%", target.getName() != null ? target.getName() : "Unknown"));

        if (target.isOnline() && target.getPlayer() != null) {
            target.getPlayer().sendMessage(plugin.getMsg("pay-success-receiver")
                    .replace("%amount%", plugin.getEconomyManager().format(amount))
                    .replace("%player%", player.getName()));
        }

        return true;
    }
}