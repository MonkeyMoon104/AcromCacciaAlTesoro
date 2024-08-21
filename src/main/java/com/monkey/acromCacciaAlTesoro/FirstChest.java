package com.monkey.acromCacciaAlTesoro;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirstChest implements Listener {

    private final AcromCacciaAlTesoro plugin;
    private boolean isChestOpened = false;

    public FirstChest(AcromCacciaAlTesoro plugin) {
        this.plugin = plugin;
    }

    public void spawnFirstChest() {
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
                    isChestOpened = false;

                    Bukkit.broadcastMessage(ChatColor.GREEN + "Una cassa è stata spawnata alle coordinate: " +
                            ChatColor.YELLOW + "X: " + x + " Y: " + y + " Z: " + z);
                } else {
                    plugin.getLogger().warning("Il mondo specificato nel config non esiste: " + worldName);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (plugin.isEventRunning() && !isChestOpened && event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getInventory();

            if (inventory.getHolder() instanceof Chest) {
                Chest chest = (Chest) inventory.getHolder();
                Location chestLocation = chest.getLocation();

                FileConfiguration config = plugin.getConfig();
                ConfigurationSection coordinatesSection = config.getConfigurationSection("coordinates");

                if (coordinatesSection != null) {
                    for (String coordinateKey : coordinatesSection.getKeys(false)) {
                        int x = coordinatesSection.getInt(coordinateKey + ".x");
                        int y = coordinatesSection.getInt(coordinateKey + ".y");
                        int z = coordinatesSection.getInt(coordinateKey + ".z");

                        if (chestLocation.getBlockX() == x && chestLocation.getBlockY() == y && chestLocation.getBlockZ() == z) {
                            isChestOpened = true;

                            ConfigurationSection rewardsSection = config.getConfigurationSection("rewards");
                            if (rewardsSection != null) {
                                for (String rewardKey : rewardsSection.getKeys(false)) {
                                    String command = rewardsSection.getString(rewardKey + ".command");
                                    if (command != null) {
                                        command = command.replace("%player%", player.getName());
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                    }
                                }
                            }

                            chest.getBlock().setType(Material.AIR);
                            plugin.getBlockStates().get(chestLocation).update(true, false);
                            plugin.getBlockStates().remove(chestLocation);

                            Bukkit.broadcastMessage(ChatColor.GREEN + "Il vincitore è " + player.getName());
                            plugin.spawnNextChest();
                            plugin.incrementSpawnCounter();

                            break;
                        }
                    }
                }
            }
        }
    }
}
