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
    public static SimpleInventory get(UUID uuid) {
        return inventories.get(uuid);
    }

    public static void put(UUID uuid, SimpleInventory inventoryContent) {
        inventories.put(uuid, inventoryContent);
    }

    public static void updateInventory(UUID uuid) {
        SimpleInventory inventoryContent = inventories.get(uuid);
        if (inventoryContent == null)
            return;

        inventoryContent.updateInventory();
    }

    public static void openInventory(UUID uuid) {
        inventories.get(uuid).openInventory();
    }

    // Events
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!inventories.containsKey(uuid))
            inventories.put(uuid, new SimpleInventory(player, 54, ""));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        SimpleInventory simpleInventory = inventories.get(uuid);
        if (simpleInventory != null)
            simpleInventory.onInventoryClick(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        SimpleInventory simpleInventory = inventories.get(uuid);
        if (simpleInventory != null)
            simpleInventory.onInventoryDrag(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();

        SimpleInventory simpleInventory = inventories.get(uuid);
        if (simpleInventory != null)
            simpleInventory.onInventoryClose(event);
    }

}
