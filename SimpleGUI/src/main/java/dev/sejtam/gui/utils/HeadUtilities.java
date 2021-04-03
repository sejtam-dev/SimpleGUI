package dev.sejtam.gui.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.apache.commons.codec.binary.Base64;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.UUID;

public class HeadUtilities {

    public static ItemStack getSkull(@NotNull String value) {
        if (!value.startsWith("http"))
            return getSkullFromWeb("http://textures.minecraft.net/texture/" + value);

        return getSkullFromWeb(value);
    }

    public static ItemStack getSkullByName(@NotNull String name) {
        ItemStack item_skull = getPlayerSkullItem();
        SkullMeta sm = (SkullMeta) item_skull.getItemMeta();

        sm.setDisplayName(name);
        sm.setOwner(name);

        item_skull.setItemMeta(sm);
        return item_skull;
    }

    public static ItemStack getSkullFromWeb(@NotNull String url) {
        ItemStack head = getPlayerSkullItem();
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url: \"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        Field profileField = null;
        try {
            assert headMeta != null;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        head.setItemMeta(headMeta);
        return head;
    }

    @NotNull
    @Contract(" -> new")
    public static ItemStack getPlayerSkullItem() {
        if (newerApi()) {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

    private static boolean newerApi() {
        try {
            Material.valueOf("PLAYER_HEAD");
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
