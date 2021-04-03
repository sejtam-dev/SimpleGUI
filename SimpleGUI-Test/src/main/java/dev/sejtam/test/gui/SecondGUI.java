package dev.sejtam.test.gui;

import dev.sejtam.gui.SimpleInventory;
import dev.sejtam.gui.utils.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SecondGUI extends SimpleInventory {

    public SecondGUI(Player player, SimpleInventory returnInventory) {
        super(player, 36, "&cSecondGUI", returnInventory);

        setItem(new ItemBuilder(Material.PAPER).setName("Sample Item").build(), 21);

        updateInventory();
    }

}
