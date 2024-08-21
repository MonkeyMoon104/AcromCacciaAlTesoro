package com.monkey.acromCacciaAlTesoro;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class EventStartForced implements CommandExecutor, TabCompleter {
    private final AcromCacciaAlTesoro plugin;

    public EventStartForced(AcromCacciaAlTesoro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("start-event-mini")) {
            if (sender.hasPermission("start.eventcaccialatesoro.forced")) {

                boolean forceStart = false;
                for (String arg : args) {
                    if (arg.equals("-forced")) {
                        forceStart = true;
                        break;
                    }
                }

                if (forceStart) {

                    plugin.startTreasureHuntEvent();
                    sender.sendMessage(ChatColor.GREEN + "Evento iniziato forzatamente con successo.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Utilizza il flag -forced per avviare l'evento forzatamente.");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Non hai il permesso per eseguire questo comando.");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("start.eventcaccialatesoro.forced")) {
            completions.add("-forced");
        }
        return completions;
    }
}
