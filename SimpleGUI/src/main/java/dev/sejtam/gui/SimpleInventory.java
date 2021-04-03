package dev.sejtam.gui;

import dev.sejtam.gui.utils.ItemBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class SimpleInventory {

    protected Player player;
    public int size;
    public String title;

    // Settings
    @Setter
    private boolean canDrag = true;
    @Setter
    private boolean canEnterItems = false;

    @Setter(AccessLevel.PRIVATE)
    protected List<ItemStack> content;
    private final Inventory inventory;
    private SimpleInventory returnInventory;

    public SimpleInventory(@NotNull Player player, int size, @NotNull String title) {
        this.player = player;
        this.size = size;
        this.title = title;
        this.inventory = Bukkit.createInventory(this.player, this.size, ChatColor.translateAlternateColorCodes('&', this.title));
        this.content = new ArrayList<>(this.size);

        for (int i = 0; i < this.size; i++) {
            this.content.add(ItemBuilder.empty());
        }

        setItem(getDefaultGlass(), 0, 1, 2, 3, 4, 5, 6, 7, 8);
        for (int i = size - 9; i < size; i++) {
            setItem(getDefaultGlass(), i);
        }

        updateInventory();
    }

    public SimpleInventory(@NotNull Player player, int size, @NotNull String title, SimpleInventory returnInventory) {
        this(player, size, title);

        if (returnInventory == null)
            return;

        this.returnInventory = returnInventory;
        setItem(new ClickableItem(getDefaultReturnButton(), event -> {
            UUID uuid = this.player.getUniqueId();

            InventoryManager.put(uuid, this.returnInventory);
            InventoryManager.updateInventory(uuid);
            InventoryManager.openInventory(uuid);
        }), this.size - 5);

        updateInventory();
    }

    // Inventory methods
    public void updateInventory() {
        getInventory().setContents(content.toArray(new ItemStack[0]));
        player.updateInventory();
    }

    public void clearInventory() {
        for (int i = 0; i < this.size; i++) {
            this.content.add(ItemBuilder.empty());
        }
        inventory.clear();

        updateInventory();
    }

    public void clearSlots() {
        for (int i = 9; i < size - 9; i++) {
            content.set(i, ItemBuilder.empty());
        }
        updateInventory();
    }

    public void setItem(@NotNull ItemStack item, int... slots) {
        for (int i : slots)
            content.set(i, item);
    }

    public void openInventory() {
        this.player.openInventory(getInventory());
    }

    // Events
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        int rawSlot = event.getRawSlot();

        //Check is same inventory
        if (!getInventory().equals(inventory))
            return;

        if (!canEnterItems) {
            InventoryAction inventoryAction = event.getAction();
            switch (inventoryAction) {
                case HOTBAR_MOVE_AND_READD:
                case MOVE_TO_OTHER_INVENTORY:
                case HOTBAR_SWAP:
                case NOTHING:
                case UNKNOWN:
                    event.setCancelled(true);
            }

            if (0 <= rawSlot && rawSlot < inventory.getSize())
                event.setCancelled(true);
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;

        if (0 <= rawSlot && rawSlot < inventory.getSize()) {
            ItemStack contentItem = content.get(rawSlot);

            if (contentItem instanceof ClickableItem)
                ((ClickableItem) contentItem).onClick(event);
        }

        onClick(event);
    }

    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        //Check is same inventory
        if (!getInventory().equals(inventory))
            return;

        onClose(event);
    }

    public void onInventoryDrag(@NotNull InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        //Check is same inventory
        if (!getInventory().equals(inventory))
            return;

        if (!canDrag) {
            event.setCancelled(true);
            return;
        }

        Integer rawSlot = (Integer) event.getRawSlots().toArray()[0];
        if (rawSlot == null)
            return;

        if (inventory.getContents().length <= rawSlot)
            return;

        event.setCancelled(true);
        onDrag(event);
    }

    // Methods for overriding
    protected void onClick(@NotNull InventoryClickEvent event) {
    }

    protected void onClose(@NotNull InventoryCloseEvent event) {
    }

    protected void onDrag(@NotNull InventoryDragEvent event) {
    }

    // Item getter methods
    @NotNull
    public ItemStack getDefaultGlass() {
        return new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14)).setName(" ").build();
    }

    @NotNull
    public ItemStack getDefaultReturnButton() {
        return new ItemBuilder(Material.COMPASS).setName("&4&lBack").build();
    }

}
