package com.sasha.beareconomy.commands;

import com.sasha.beareconomy.BearEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EcoCommand implements CommandExecutor {

    private final BearEconomy plugin;

    public EcoCommand(BearEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("bn.beareconomy.admin")) {
            sender.sendMessage(plugin.getRawMsg("no-permission"));
            return true;
        }

        // Reset only requires 2 arguments: /eco reset <player>
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /eco <give|take|set|reset> <player> [amount]");
            return true;
        }

        String action = args[0].toLowerCase();
        String targetName = args[1];
        double amount = 0.0;

        // Only parse an amount if the action isn't 'reset'
        if (!action.equals("reset")) {
            if (args.length < 3) {
                sender.sendMessage("§cUsage: /eco <give|take|set> <player> <amount>");
                return true;
            }
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMsg("invalid-amount"));
                return true;
            }
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(plugin.getMsg("player-not-found"));
            return true;
        }

        switch (action) {
            case "give":
                plugin.getEconomyManager().addBalance(target.getUniqueId(), amount);
                sender.sendMessage(plugin.getMsg("eco-give")
                        .replace("%amount%", plugin.getEconomyManager().format(amount))
                        .replace("%player%", target.getName() != null ? target.getName() : "Unknown")
                        .replace("%balance%", plugin.getEconomyManager().format(plugin.getEconomyManager().getBalance(target.getUniqueId()))));

                if (target.isOnline() && target.getPlayer() != null) {
                    target.getPlayer().sendMessage(plugin.getMsg("eco-received")
                            .replace("%amount%", plugin.getEconomyManager().format(amount)));
                }
                break;

            case "take":
                plugin.getEconomyManager().removeBalance(target.getUniqueId(), amount);
                sender.sendMessage(plugin.getMsg("eco-take")
                        .replace("%amount%", plugin.getEconomyManager().format(amount))
                        .replace("%player%", target.getName() != null ? target.getName() : "Unknown")
                        .replace("%balance%", plugin.getEconomyManager().format(plugin.getEconomyManager().getBalance(target.getUniqueId()))));

                if (target.isOnline() && target.getPlayer() != null) {
                    target.getPlayer().sendMessage(plugin.getMsg("eco-deducted")
                            .replace("%amount%", plugin.getEconomyManager().format(amount)));
                }
                break;

            case "set":
                plugin.getEconomyManager().setBalance(target.getUniqueId(), amount);
                sender.sendMessage(plugin.getMsg("eco-set")
                        .replace("%player%", target.getName() != null ? target.getName() : "Unknown")
                        .replace("%balance%", plugin.getEconomyManager().format(amount)));
                break;

            case "reset":
                plugin.getEconomyManager().setBalance(target.getUniqueId(), 0.0);
                sender.sendMessage(plugin.getMsg("eco-reset")
                        .replace("%player%", target.getName() != null ? target.getName() : "Unknown")
                        .replace("%balance%", plugin.getEconomyManager().format(0.0)));
                break;

            default:
                sender.sendMessage("§cUsage: /eco <give|take|set|reset> <player> [amount]");
                break;
        }

        return true;
    }
}