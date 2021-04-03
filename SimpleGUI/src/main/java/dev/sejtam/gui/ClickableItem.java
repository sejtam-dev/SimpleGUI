package dev.sejtam.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ClickableItem extends ItemStack {

    private Consumer<InventoryClickEvent> consumer;

    public ClickableItem(@NotNull ItemStack item, @NotNull Consumer<InventoryClickEvent> consumer) {
        super(item);
        this.consumer = consumer;
    }

    public void onClick(@NotNull InventoryClickEvent e) {
        consumer.accept(e);
    }

    public static class Builder {

        private ItemStack item;
        private Consumer<InventoryClickEvent> consumer = event -> {
        };

        public Builder(@NotNull ItemStack item) {
            this.item = item;
        }

        public Builder onClick(@NotNull Consumer<InventoryClickEvent> consumer) {
            this.consumer = consumer;
            return this;
        }

        public ClickableItem build() {
            return new ClickableItem(item, consumer);
        }

        public ClickableItem empty() {
            return new ClickableItem(new ItemStack(Material.AIR), event -> {
            });
        }

    }

}
