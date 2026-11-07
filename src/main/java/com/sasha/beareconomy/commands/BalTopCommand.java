package com.sasha.beareconomy.commands;

import com.sasha.beareconomy.BearEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BalTopCommand implements CommandExecutor {

    private final BearEconomy plugin;

    public BalTopCommand(BearEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Run asynchronously to prevent server lag when sorting large amounts of data
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Map<UUID, Double> balances = plugin.getEconomyManager().getAllBalances();

            List<Map.Entry<UUID, Double>> sortedList = new ArrayList<>(balances.entrySet());
            sortedList.sort((a, b) -> Double.compare(b.getValue(), a.getValue())); // Descending order

            int page = 1;
            if (args.length > 0) {
                try { page = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
            }

            int itemsPerPage = 10;
            int maxPages = (int) Math.ceil(sortedList.size() / (double) itemsPerPage);
            if (page < 1) page = 1;
            if (page > maxPages && maxPages != 0) page = maxPages;

            sender.sendMessage("§8§m--------------------------------");
            sender.sendMessage("§a§lRichest Bears §7- Page " + page + "/" + (maxPages == 0 ? 1 : maxPages));

            if (sortedList.isEmpty()) {
                sender.sendMessage("§cNo economy data available.");
            } else {
                int startIndex = (page - 1) * itemsPerPage;
                int endIndex = Math.min(startIndex + itemsPerPage, sortedList.size());

                for (int i = startIndex; i < endIndex; i++) {
                    Map.Entry<UUID, Double> entry = sortedList.get(i);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
                    String name = offlinePlayer.getName() != null ? offlinePlayer.getName() : "Unknown";

                    sender.sendMessage("§e" + (i + 1) + ". §a" + name + " §8- §7" + plugin.getEconomyManager().format(entry.getValue()));
                }
            }
            sender.sendMessage("§8§m--------------------------------");
        });

        return true;
    }
}