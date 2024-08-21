package com.monkey.acromCacciaAlTesoro;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NextChest implements Listener {

    private final AcromCacciaAlTesoro plugin;

    public NextChest(AcromCacciaAlTesoro plugin) {
        this.plugin = plugin;
    }

    public void spawnChest() {
        if (plugin.getSpawnCounter() >= 15) {
            plugin.setEventRunning(false);
            Bukkit.broadcastMessage(ChatColor.RED + "L'evento è terminato!");
            return;
        }

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection coordinatesSection = config.getConfigurationSection("coordinates");

        if (coordinatesSection != null) {
            List<String> availableCoordinates = new ArrayList<>(coordinatesSection.getKeys(false));
            availableCoordinates.removeAll(plugin.getUsedCoordinates());

            if (!availableCoordinates.isEmpty()) {
                Random random = new Random();
                String chosenCoordinate = availableCoordinates.get(random.nextInt(availableCoordinates.size()));

                plugin.getUsedCoordinates().add(chosenCoordinate);

                int x = coordinatesSection.getInt(chosenCoordinate + ".x");
                int y = coordinatesSection.getInt(chosenCoordinate + ".y");
                int z = coordinatesSection.getInt(chosenCoordinate + ".z");

                String worldName = config.getString("world-event");
                World world = Bukkit.getWorld(worldName);

                if (world != null) {
                    Location chestLocation = new Location(world, x, y, z);
                    Block chestBlock = chestLocation.getBlock();

                    BlockState blockState = chestBlock.getState();
                    plugin.getBlockStates().put(chestLocation, blockState);

                    chestBlock.setType(Material.CHEST);

                    Bukkit.broadcastMessage(ChatColor.GREEN + "Una nuova cassa è stata spawnata alle coordinate: " +
                            ChatColor.YELLOW + "X: " + x + " Y: " + y + " Z: " + z);
                } else {
                    plugin.getLogger().warning("Il mondo specificato nel config non esiste: " + worldName);
                }
            }
        }
    }
}
