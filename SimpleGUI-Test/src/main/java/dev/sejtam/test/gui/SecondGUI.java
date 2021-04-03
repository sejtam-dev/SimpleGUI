package dev.sejtam.test.gui;

import dev.sejtam.gui.SimpleInventory;
import dev.sejtam.gui.utils.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

public class SecondGUI extends SimpleInventory {

    public SecondGUI(@NotNull Player player, @NotNull SimpleInventory returnInventory) {
        super(player, 36, "&cSecondGUI", returnInventory);

        setItem(new ItemBuilder(Material.PAPER).setName("Sample Item").build(), 21);

        updateInventory();
    }

}
