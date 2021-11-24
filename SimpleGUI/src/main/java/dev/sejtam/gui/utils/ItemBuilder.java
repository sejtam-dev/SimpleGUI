package dev.sejtam.gui.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(@NotNull ItemStack item) {
        this.itemStack = item;
        this.itemMeta = this.itemStack.getItemMeta();
    }

    @NotNull
    public ItemBuilder setName(@NotNull String name) {
        this.itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    @NotNull
    public ItemBuilder setLore(@NotNull String... lore) {
        List<String> newLore = new ArrayList<>();
        for (String l : lore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', l));
        }

        this.itemMeta.setLore(newLore);
        return this;
    }

    @NotNull
    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @NotNull
    public ItemBuilder setDurability(short data) {
        itemStack.setDurability(data);
        return this;
    }

    @NotNull
    public ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int lvl, boolean bool) {
        this.itemMeta.addEnchant(enchantment, lvl, bool);
        return this;
    }

    @NotNull
    public ItemBuilder hideEnchantments() {
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    @NotNull
    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    public static ItemStack empty() {
        return new ItemStack(Material.AIR);
    }
}
