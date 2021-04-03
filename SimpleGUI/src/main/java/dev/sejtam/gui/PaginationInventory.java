package dev.sejtam.gui;

import dev.sejtam.gui.utils.HeadUtilities;
import dev.sejtam.gui.utils.ItemBuilder;
import dev.sejtam.gui.utils.Pagination;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class PaginationInventory extends SimpleInventory {

    @Getter(AccessLevel.PUBLIC)
    private final Pagination<ItemStack> pagination;

    private int page = 0;

    public PaginationInventory(Player player, int size, String title) {
        this(player, size, title, null);
    }

    public PaginationInventory(Player player, int size, String title, SimpleInventory returnInventory) {
        super(player, size, title, returnInventory);

        // Create pagination
        this.pagination = new Pagination<>(size - 18);

        // Add left arrow button
        this.setItem(new ClickableItem(getLeftArrow(),
                event -> {
                    if (this.page <= 0)
                        return;

                    this.page--;
                    updateInventory();
                }), this.size - 2);
        // Add right arrow button
        this.setItem(new ClickableItem(getRightArrow(),
                event -> {
                    if (this.page >= this.pagination.totalPages() - 1)
                        return;

                    this.page++;
                    updateInventory();
                }), this.size - 1);

        updateInventory();
    }

    @Override
    public void updateInventory() {
        for (int i = 9; i < size - 9; i++) {
            content.set(i, ItemBuilder.empty());
        }

        if (this.pagination == null)
            return;

        // Fill inventory
        List<ItemStack> itemStackList = this.pagination.getPage(this.page);
        for (int i = 0; i < itemStackList.size(); i++) {
            this.getContent().set(9 + i, itemStackList.get(i));
        }

        super.updateInventory();
    }

    // Items getter methods
    public ItemStack getLeftArrow() {
        return new ItemBuilder(HeadUtilities.getSkull("5ae78451bf26cf49fd5f54cd8f2b37cd25c92e5ca76298b3634cb541e9ad89")).setName("&3Previous Page").build();
    }

    public ItemStack getRightArrow() {
        return new ItemBuilder(HeadUtilities.getSkull("117f3666d3cedfae57778c78230d480c719fd5f65ffa2ad3255385e433b86e")).setName("&3Next Page").build();
    }

}
