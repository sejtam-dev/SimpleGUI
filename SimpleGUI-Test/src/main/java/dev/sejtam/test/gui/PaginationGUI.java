package dev.sejtam.test.gui;

import dev.sejtam.gui.ClickableItem;
import dev.sejtam.gui.PaginationInventory;
import dev.sejtam.gui.utils.HeadUtilities;
import dev.sejtam.gui.utils.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

public class PaginationGUI extends PaginationInventory {

    public PaginationGUI(@NotNull Player player) {
        super(player, 36, "Pagination Test");

        //for (int i = 0; i < 100; i++)
        //    getPagination().add(i, new ItemBuilder(Material.STONE).setName("Item - " + i).build());

        getPagination().setOnClickAdd(new ClickableItem(new ItemBuilder(HeadUtilities.getSkull("3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716")).setName("&aAdd").build(), event -> {
            getPagination().add(new ItemBuilder(Material.PAPER).build());
            updateInventory();
        }));

        updateInventory();
    }

}
