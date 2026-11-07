package com.sasha.beareconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BearEconomyTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        String cmdName = command.getName().toLowerCase();

        // Tabbing for /eco
        if (cmdName.equals("eco")) {
            if (args.length == 1) {
                return List.of("give", "take", "set", "reset").stream()
                        .filter(s -> s.startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            } else if (args.length == 2) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            } else if (args.length == 3 && !args[0].equalsIgnoreCase("reset")) {
                // Quick suggestions for amounts
                return List.of("10", "100", "1000", "10000").stream()
                        .filter(s -> s.startsWith(args[2]))
                        .collect(Collectors.toList());
            }
        }
        // Tabbing for /pay, /balance, and /bal
        else if (cmdName.equals("pay") || cmdName.equals("balance") || cmdName.equals("bal")) {
            if (args.length == 1) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            } else if (args.length == 2 && cmdName.equals("pay")) {
                return List.of("10", "100", "1000", "10000").stream()
                        .filter(s -> s.startsWith(args[1]))
                        .collect(Collectors.toList());
            }
        }

        return completions;
    }
}