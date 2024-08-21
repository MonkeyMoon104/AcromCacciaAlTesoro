package com.monkey.acromCacciaAlTesoro;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class AcromCacciaAlTesoro extends JavaPlugin implements Listener {

    private boolean isEventRunning = false;
    private int spawnCounter = 0;
    private final List<String> usedCoordinates = new ArrayList<>();
    private final Map<Location, BlockState> blockStates = new HashMap<>();
    private WorldEditPlugin worldEditPlugin;

    @Override
    public void onEnable() {
        getCommand("eventcaccialatesororeload").setExecutor(new ReloadEventCommand(this));
        getCommand("start-event-mini").setExecutor(new EventStartForced(this));
        getCommand("start-event-mini").setTabCompleter(new EventStartForced(this));

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new FirstChest(this), this);
        getServer().getPluginManager().registerEvents(new NextChest(this), this);

        worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEditPlugin == null) {
            getLogger().warning("WorldEdit non trovato! Il plugin potrebbe non funzionare correttamente.");
        }
    }

    public void startTreasureHuntEvent() {
        if (isEventRunning) {
            getLogger().warning("L'evento è già in corso!");
            return;
        }

        isEventRunning = true;
        spawnCounter = 0;
        usedCoordinates.clear();
        Bukkit.broadcastMessage(ChatColor.GOLD + "La caccia al tesoro è iniziata!");

        new FirstChest(this).spawnFirstChest();
    }

    public void spawnNextChest() {
        new NextChest(this).spawnChest();
    }

    public boolean isEventRunning() {
        return isEventRunning;
    }

    public void setEventRunning(boolean eventRunning) {
        isEventRunning = eventRunning;
    }

    public int getSpawnCounter() {
        return spawnCounter;
    }

    public void incrementSpawnCounter() {
        spawnCounter++;
    }

    public List<String> getUsedCoordinates() {
        return usedCoordinates;
    }

    public Map<Location, BlockState> getBlockStates() {
        return blockStates;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    @Override
    public void onDisable() {
        if (isEventRunning) {
            isEventRunning = false;
        }
    }
}
