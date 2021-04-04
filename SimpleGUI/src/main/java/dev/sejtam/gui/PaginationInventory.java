package dev.sejtam.gui;

import dev.sejtam.gui.utils.HeadUtilities;
import dev.sejtam.gui.utils.ItemBuilder;
import dev.sejtam.gui.utils.Pagination;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class PaginationInventory extends SimpleInventory {

    @Getter(AccessLevel.PUBLIC)
    private final Pagination<ItemStack> pagination;

    private int page = 0;

    public PaginationInventory(@NotNull Player player, Rows rows, @NotNull String title) {
        this(player, rows, title, null);
    }

    public PaginationInventory(@NotNull Player player, Rows rows, @NotNull String title, SimpleInventory returnInventory) {
        super(player, rows, title, returnInventory);

        // Create pagination
        this.pagination = new Pagination<>(rows.getSlots() - 18);

        // Add left arrow button
        this.setItem(new ClickableItem(getLeftArrow(),
                event -> {
                    if (this.page <= 0)
                        return;

                    this.page--;
                    updateInventory();
                }), this.rows.getSlots() - 2);
        // Add right arrow button
        this.setItem(new ClickableItem(getRightArrow(),
                event -> {
                    if (this.page >= this.pagination.totalPages() - 1)
                        return;

                    this.page++;
                    updateInventory();
                }), this.rows.getSlots() - 1);

        updateInventory();
    }

    @Override
    public void updateInventory() {
        for (int i = 9; i < rows.getSlots() - 9; i++) {
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
    @NotNull
    public ItemStack getLeftArrow() {
        return new ItemBuilder(HeadUtilities.getSkull("8550b7f74e9ed7633aa274ea30cc3d2e87abb36d4d1f4ca608cd44590cce0b")).setName("&aPrevious Page").build();
    }

    @NotNull
    public ItemStack getRightArrow() {
        return new ItemBuilder(HeadUtilities.getSkull("96339ff2e5342ba18bdc48a99cca65d123ce781d878272f9d964ead3b8ad370")).setName("&aNext Page").build();
    }

}
