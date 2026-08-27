package com.daboxen.oillessgt.recipe;

import com.gregtechceu.gtceu.data.recipe.WoodTypeEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a type of tree from the perspective of the Greenhouse.<br/>
 * To create one, use {@link Builder}.<br/>
 * To automatically add greenhouse recipes based on this, see
 * {@link GreenhouseRecipes#addTreeType(GreenhouseWoodTypeEntry)}.
 * 
 * @see WoodTypeEntry
 */
public class GreenhouseWoodTypeEntry {

    @NotNull
    public final String modid;
    @NotNull
    public final String woodName;
    @NotNull
    public final Item sapling;
    @NotNull
    public final Item log;
    @NotNull
    public final Item stick;
    /**
     * e.g., apples or sticky resin
     */
    @NotNull
    public final List<ItemStack> bonusItems;

    @ApiStatus.Internal
    protected GreenhouseWoodTypeEntry(@NotNull String modid, @NotNull String woodName, @NotNull Item sapling,
                                      @NotNull Item log, @NotNull Item stick, @NotNull List<ItemStack> bonusItems) {
        this.modid = modid;
        this.woodName = woodName;
        this.sapling = sapling;
        this.log = log;
        this.stick = stick;
        this.bonusItems = bonusItems;
    }

    public static class Builder {

        @NotNull
        private final String modid;
        @NotNull
        private final String woodName;
        private Item sapling;
        private Item log;
        private Item stick;
        @NotNull
        private final List<ItemStack> bonusItems;

        public Builder(@NotNull String modid, @NotNull String woodName) {
            this.modid = modid;
            this.woodName = woodName;
            bonusItems = new ArrayList<>(1);
            this.stick(Items.STICK);
        }

        /**
         * currently unusable because GT keeps their {@link WoodTypeEntry} list private :(
         */
        public static Builder from(WoodTypeEntry entry) {
            return new Builder(entry.modid, entry.woodName)
                    .log(entry.log);
        }

        public Builder sapling(Item sapling) {
            this.sapling = sapling;
            return this;
        }

        public Builder log(Item log) {
            this.log = log;
            return this;
        }

        public Builder stick(Item stick) {
            this.stick = stick;
            return this;
        }

        /**
         * @param items An {@link ItemStack} representing extra items you want the greenhouse to return.<br/>
         *              Balance this stack around 32 logs per cycle (for example, 4 apples or 32 sticky resin).
         * @return The same {@link #Builder}.
         */
        public Builder bonusItem(ItemStack items) {
            this.bonusItems.add(items);
            return this;
        }

        public GreenhouseWoodTypeEntry build() {
            return new GreenhouseWoodTypeEntry(modid, woodName, sapling, log, stick, bonusItems);
        }
    }
}
