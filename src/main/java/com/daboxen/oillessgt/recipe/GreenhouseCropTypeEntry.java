package com.daboxen.oillessgt.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Represents a type of tree from the perspective of the Greenhouse.<br/>
 * To create one, use {@link GreenhouseCropTypeEntry.Builder}.<br/>
 * To automatically add greenhouse recipes based on this, see
 * {@link GreenhouseRecipes#addCropType(GreenhouseCropTypeEntry)}.
 */
public class GreenhouseCropTypeEntry {

    @NotNull
    public String modid;
    @NotNull
    public String name;
    @NotNull
    public final Item seed;
    @Nullable
    public final Item crop;

    public List<ItemStack> getOutputs(int seedAmount, int cropAmount) {
        if (crop == null) {
            return List.of(new ItemStack(seed, seedAmount + cropAmount));
        }
        return List.of(new ItemStack(seed, seedAmount), new ItemStack(crop, cropAmount));
    }

    public boolean isSelfSeeding() {
        return crop == null;
    }

    @ApiStatus.Internal
    protected GreenhouseCropTypeEntry(String modid, String name, @NotNull Item seed, @Nullable Item crop) {
        this.modid = modid;
        this.name = name;
        this.seed = seed;
        this.crop = crop;
    }

    public static class Builder {

        @NotNull
        public String modid;
        @NotNull
        public String name;
        public Item seed;
        public Item crop;

        public Builder(String modid, String name) {
            this.modid = modid;
            this.name = name;
        }

        public Builder seed(Item seed) {
            this.seed = seed;
            return this;
        }

        public Builder crop(Item crop) {
            this.crop = crop;
            return this;
        }

        public GreenhouseCropTypeEntry build() {
            return new GreenhouseCropTypeEntry(modid, name, seed, crop);
        }
    }
}
