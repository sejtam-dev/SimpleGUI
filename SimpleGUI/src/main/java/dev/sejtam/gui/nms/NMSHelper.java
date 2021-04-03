package dev.sejtam.gui.nms;

import dev.sejtam.gui.utils.MinecraftVersion;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NMSHelper {

    public static Class<?> nmsBlockPositionClass;

    public static Class<?> nmsChatMessageClass;
    public static Constructor<?> chatMessageConstructor;

    static {
        try {
            nmsBlockPositionClass = getNMSClass("BlockPosition");
            nmsChatMessageClass = NMSHelper.getNMSClass("ChatMessage");

            chatMessageConstructor = nmsChatMessageClass.getConstructor(String.class, Object[].class);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(@NotNull String name) {
        try {
            return Class.forName("net.minecraft.server." + MinecraftVersion.getVersion() + "." + name);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraftClass(@NotNull String path) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + MinecraftVersion.getVersion() + "." + path);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Object getChatMessage(@NotNull String message) {
        try {
            return chatMessageConstructor.newInstance(message, new Object[]{});
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Object getBlockPositionZero() {
        try {
            return nmsBlockPositionClass.getField("ZERO").get(null);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
