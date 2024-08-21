package com.monkey.acromCacciaAlTesoro;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadEventCommand implements CommandExecutor {
    private final AcromCacciaAlTesoro plugin;
    public ReloadEventCommand(AcromCacciaAlTesoro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("eventcaccialatesororeload")) {
            if (sender.hasPermission("eventcaccialatesoro.reload")) {

                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Configurazione ricaricata con successo.");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Non hai il permesso per eseguire questo comando.");
            }
        }
        return false;
    }
}

