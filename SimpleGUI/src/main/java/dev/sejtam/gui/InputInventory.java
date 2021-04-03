package dev.sejtam.gui;

import dev.sejtam.gui.nms.NMSAnvil;
import dev.sejtam.gui.nms.NMSHelper;
import dev.sejtam.gui.nms.NMSPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class InputInventory {
    private Player player;
    private NMSPlayer nmsPlayer;

    private ItemStack insert;
    private BiFunction<Player, String, Runnable> clickHandler;

    private NMSAnvil nmsAnvil;
    private int containerId;
    private Inventory inventory;
    private final ListenUp listener = new ListenUp();
    private boolean open;

    public InputInventory(@NotNull Player player, String itemName, @NotNull BiFunction<Player, String, Runnable> clickHandler) {
        this.player = player;
        this.nmsPlayer = new NMSPlayer(this.player);
        this.clickHandler = clickHandler;
        this.nmsAnvil = new NMSAnvil();

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();

        if(itemName != null)
            paperMeta.setDisplayName(itemName);
        else
            paperMeta.setDisplayName(" ");

        paper.setItemMeta(paperMeta);
        this.insert = paper;

        this.nmsPlayer.handleInventoryCloseEvent();
        this.nmsPlayer.setActiveContainerDefault();

        Bukkit.getPluginManager().registerEvents(this.listener, InventoryManager.getInstance());

        Object container = this.nmsAnvil.newContainerAnvil(this.player);

        this.inventory = this.nmsAnvil.toBukkitInventory(container);
        this.inventory.setItem(Slot.INPUT_LEFT, this.insert);

        this.containerId = this.nmsPlayer.getNextContainerId();
        this.nmsPlayer.sendPacketOpenWindow(this.containerId, "minecraft:anvil", NMSHelper.getChatMessage("Anvil"));
        this.nmsPlayer.setActiveContainer(container);
        this.nmsAnvil.setActiveContainerId(container, this.containerId);
        this.nmsAnvil.addActiveContainerSlotListener(container, this.player);

        this.open = true;
    }

    public void closeInventory(boolean fromEvent) {
        if(!this.open)
            return;
        this.open = false;

        if(!fromEvent)
            this.nmsPlayer.handleInventoryCloseEvent();

        this.nmsPlayer.setActiveContainerDefault();
        this.nmsPlayer.sendPacketCloseWindow(this.containerId);

        HandlerList.unregisterAll(this.listener);
    }

    private class ListenUp implements Listener {

        @EventHandler
        public void onInventoryClick(@NotNull InventoryClickEvent event) {
            Inventory inventory = event.getInventory();

            if(!inventory.equals(inventory))
                return;

            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();

            if(!p.getName().equals(player.getName()))
                return;

            if(event.getRawSlot() >= inventory.getSize())
                return;

            if(event.getRawSlot() != Slot.OUTPUT)
                return;

            ItemStack clickedItem = inventory.getItem(event.getRawSlot());
            if(clickedItem == null || clickedItem.getType() == Material.AIR)
                return;

            String itemName = clickedItem.hasItemMeta() ? clickedItem.getItemMeta().getDisplayName() : clickedItem.getType().toString();

            if(itemName != null && itemName.startsWith(" ")) {
                itemName = itemName.substring(1);
            }

            if(itemName == null)
                return;

            Runnable ret = clickHandler.apply(p, itemName);

            if(ret != null) {
                ret.run();
            } else {
                closeInventory(false);
            }
        }

        @EventHandler
        public void onInventoryClose(@NotNull InventoryCloseEvent event) {
            if(!event.getPlayer().getName().equals(player.getName()))
                return;

            Inventory inv = event.getInventory();
            if(!inv.equals(inventory))
                return;

            if(open)
                closeInventory(true);
            inv.clear();
        }
    }

    public static class Slot {
        public static final int INPUT_LEFT = 0;
        public static final int INPUT_RIGHT = 1;
        public static final int OUTPUT = 2;
    }

}