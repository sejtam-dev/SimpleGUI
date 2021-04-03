package dev.sejtam.gui.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSAnvil {

    public static Class<?> nmsContainerClass;
    public static Field windowId;
    public static Class<?> nmsICraftingClass;
    public static Method addSlotListener;
    public static Method getBukkitView;

    public static Class<?> nmsContainerAnvilClass;
    public static Constructor<?> anvilConstructor;
    public static Field checkReachable;

    static {
        nmsContainerClass = NMSHelper.getNMSClass("Container");
        nmsICraftingClass = NMSHelper.getNMSClass("ICrafting");
        nmsContainerAnvilClass = NMSHelper.getNMSClass("ContainerAnvil");

        try {
            windowId = nmsContainerClass.getField("windowId");

            addSlotListener = nmsContainerClass.getMethod("addSlotListener", nmsICraftingClass);

            getBukkitView = nmsContainerClass.getMethod("getBukkitView");

            anvilConstructor = nmsContainerAnvilClass.getConstructor(NMSPlayer.nmsInventoryClass, NMSPlayer.nmsWorldClass, NMSHelper.nmsBlockPositionClass, NMSPlayer.nmsEntityHumanClass);

            checkReachable = nmsContainerClass.getField("checkReachable");
        } catch (NoSuchFieldException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void setActiveContainerId(Object container, int containerId) {
        try {
            windowId.set(container, containerId);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void addActiveContainerSlotListener(Object container, Player player) {
        try {
            addSlotListener.invoke(container, NMSPlayer.toNMS(player));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public Inventory toBukkitInventory(Object container) {
        try {
            return ((InventoryView)getBukkitView.invoke(container)).getTopInventory();
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Object newContainerAnvil(Player player) {
        try {
            final Object nmsPlayer = NMSPlayer.toNMS(player);
            Object obj = anvilConstructor.newInstance(NMSPlayer.inventory.get(nmsPlayer), NMSPlayer.world.get(nmsPlayer), NMSHelper.getBlockPositionZero(), nmsPlayer);
            checkReachable.set(obj, false);
            return obj;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
