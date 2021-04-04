package dev.sejtam.gui;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class InventoryManager implements Listener {

    private static final Map<UUID, SimpleInventory> inventories = new HashMap<>();
    @Getter
    @Setter
    private static JavaPlugin instance;

    // Methods
    @Nullable
    public static SimpleInventory get(@NotNull UUID uuid) {
        return inventories.get(uuid);
    }

    public static void put(@NotNull UUID uuid, @NotNull SimpleInventory inventoryContent) {
        inventories.put(uuid, inventoryContent);
    }

    public static void updateInventory(@NotNull UUID uuid) {
        SimpleInventory inventoryContent = inventories.get(uuid);
        if (inventoryContent == null)
            return;

        inventoryContent.updateInventory();
    }

    public static void openInventory(@NotNull UUID uuid) {
        inventories.get(uuid).openInventory();
    }

    // Events
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!inventories.containsKey(uuid))
            inventories.put(uuid, new SimpleInventory(player, Rows.SIX, ""));
    }

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        SimpleInventory simpleInventory = inventories.get(uuid);
        if (simpleInventory != null)
            simpleInventory.onInventoryClick(event);
    }

    @EventHandler
    public void onInventoryDrag(@NotNull InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        SimpleInventory simpleInventory = inventories.get(uuid);
        if (simpleInventory != null)
            simpleInventory.onInventoryDrag(event);
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();

        SimpleInventory simpleInventory = inventories.get(uuid);
        if (simpleInventory != null)
            simpleInventory.onInventoryClose(event);
    }

}
