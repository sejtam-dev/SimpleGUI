package dev.sejtam.test.gui;

import dev.sejtam.gui.ClickableItem;
import dev.sejtam.gui.InputInventory;
import dev.sejtam.gui.InventoryManager;
import dev.sejtam.gui.SimpleInventory;
import dev.sejtam.gui.utils.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

public class FirstGUI extends SimpleInventory {

    public FirstGUI(@NotNull Player player) {
        super(player, 54, "&cFirstGUI");

        setItem(new ClickableItem(new ItemBuilder(Material.BOOK).setName("Sample Item").build(), event -> {
            new InputInventory(player, null, ((player1, s) -> {
                player.sendMessage(s);
                return null;
            }));
        }), 10);
        setItem(new ClickableItem(new ItemBuilder(Material.STONE).setName("Open second Inventory").build(), event -> {
            InventoryManager.put(player.getUniqueId(), new SecondGUI(player, this));
            InventoryManager.openInventory(player.getUniqueId());
        }), 12);

        updateInventory();
    }

}
