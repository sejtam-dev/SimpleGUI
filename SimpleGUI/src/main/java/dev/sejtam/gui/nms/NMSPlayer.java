package dev.sejtam.gui.nms;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSPlayer {

    @Getter(AccessLevel.PUBLIC)
    protected Player player;

    public static Method getHandle;
    public static Class<?> nmsCraftPlayerClass;
    public static Class<?> nmsPlayerClass;
    public static Class<?> nmsEntityHumanClass;

    public static Class<?> nmsInventoryClass;
    public static Field inventory;
    public static Class<?> nmsWorldClass;
    public static Field world;

    private static Class<?> nmsCraftEventFactoryClass;
    private static Method handleInventoryCloseEvent;

    private static Class<?> nmsPlayerConnectionClass;
    private static Field playerConnection;
    private static Class<?> nmsPacketClass;
    private static Method sendPacket;
    private static Class nmsPlayOutOpenWindowClass;
    private static Constructor<?> PlayOutOpenWindowConstructor;
    private static Class nmsPlayOutCloseWindowClass;
    private static Constructor<?> PlayOutCloseWindowConstructor;

    private static Method playerNextContainerCounter;
    private static Field activeContainer;
    private static Field defaultContainer;

    private static Class<?> nmsIChatBaseComponentClass;

    static {
        try {
            nmsPlayerClass = NMSHelper.getNMSClass("EntityPlayer");
            nmsCraftPlayerClass = NMSHelper.getCraftClass("entity.CraftPlayer");
            nmsEntityHumanClass = NMSHelper.getNMSClass("EntityHuman");

            getHandle = nmsCraftPlayerClass.getMethod("getHandle");

            playerNextContainerCounter =  nmsPlayerClass.getMethod("nextContainerCounter");

            nmsCraftEventFactoryClass = NMSHelper.getCraftClass("event.CraftEventFactory");

            handleInventoryCloseEvent = nmsCraftEventFactoryClass.getMethod("handleInventoryCloseEvent", nmsEntityHumanClass);

            nmsIChatBaseComponentClass = NMSHelper.getNMSClass("IChatBaseComponent");

            nmsPlayerConnectionClass = NMSHelper.getNMSClass("PlayerConnection");
            nmsPacketClass = NMSHelper.getNMSClass("Packet");
            nmsPlayOutOpenWindowClass = NMSHelper.getNMSClass("PacketPlayOutOpenWindow");
            nmsPlayOutCloseWindowClass = NMSHelper.getNMSClass("PacketPlayOutCloseWindow");

            playerConnection = nmsPlayerClass.getField("playerConnection");

            sendPacket = nmsPlayerConnectionClass.getMethod("sendPacket", nmsPacketClass);
            PlayOutOpenWindowConstructor = nmsPlayOutOpenWindowClass.getConstructor(Integer.TYPE, String.class, nmsIChatBaseComponentClass);
            PlayOutCloseWindowConstructor = nmsPlayOutCloseWindowClass.getConstructor(Integer.TYPE);

            activeContainer = nmsPlayerClass.getField("activeContainer");
            defaultContainer = nmsPlayerClass.getField("defaultContainer");
            nmsInventoryClass = NMSHelper.getNMSClass("PlayerInventory");
            inventory = nmsPlayerClass.getField("inventory");
            nmsWorldClass = NMSHelper.getNMSClass("World");
            world = nmsPlayerClass.getField("world");
        } catch (NoSuchFieldException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public NMSPlayer(Player player) {
        this.player = player;
    }

    public int getNextContainerId() {
        try {
            return (int) playerNextContainerCounter.invoke(toNMS(player));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public void handleInventoryCloseEvent() {
        try {
            handleInventoryCloseEvent.invoke(nmsCraftEventFactoryClass, toNMS(player));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public void sendPacketOpenWindow(int containerId, String inventory, Object name) {
        try {
            sendPacket.invoke(playerConnection.get(toNMS(player)), PlayOutOpenWindowConstructor.newInstance(containerId, inventory, name));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            ex.printStackTrace();
        }
    }

    public void sendPacketCloseWindow(int containerId) {
        try {
            sendPacket.invoke( playerConnection.get(toNMS(player)), PlayOutCloseWindowConstructor.newInstance(containerId));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            ex.printStackTrace();
        }
    }

    public void setActiveContainerDefault() {
        try {
            Object nmsPlayer = toNMS(player);
            activeContainer.set(nmsPlayer, defaultContainer.get(nmsPlayer));
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void setActiveContainer(Object container) {
        try {
            activeContainer.set(toNMS(player), container);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public Object toNMS() {
        return toNMS(player);
    }

    public static Object toNMS(Player player) {
        try {
            return getHandle.invoke(player);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
