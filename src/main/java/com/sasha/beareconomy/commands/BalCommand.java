package com.sasha.beareconomy.commands;

import com.sasha.beareconomy.BearEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BalCommand implements CommandExecutor {

    private final BearEconomy plugin;

    public BalCommand(BearEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getRawMsg("not-player"));
                return true;
            }

            double bal = plugin.getEconomyManager().getBalance(player.getUniqueId());
            player.sendMessage(plugin.getMsg("balance-self").replace("%balance%", plugin.getEconomyManager().format(bal)));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(plugin.getMsg("player-not-found"));
            return true;
        }

        double bal = plugin.getEconomyManager().getBalance(target.getUniqueId());
        sender.sendMessage(plugin.getMsg("balance-other")
                .replace("%player%", target.getName() != null ? target.getName() : "Unknown")
                .replace("%balance%", plugin.getEconomyManager().format(bal)));

        return true;
    }
}