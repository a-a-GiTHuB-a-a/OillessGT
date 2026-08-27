package com.daboxen.oillessgt.recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.recipe.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.network.chat.Component;

import com.mojang.serialization.Codec;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class SubstructureCondition extends RecipeCondition<SubstructureCondition> {

    // spotless:off
    public static final Codec<SubstructureCondition> CODEC = RecipeCondition.simpleCodec(SubstructureCondition::new);
    // spotless:on

    /**
     * The name of the substructure to check for. {@code "main"} is essentially a dummy value that will return
     * {@code true} if the machine is formed.
     */
    @Getter
    @NotNull
    private final String substructureName;

    public SubstructureCondition(@NotNull String substructureName) {
        this.substructureName = substructureName;
    }

    public SubstructureCondition(boolean isReverse, @NotNull String substructureName) {
        super(isReverse);
        this.substructureName = substructureName;
    }

    public SubstructureCondition() {
        this.substructureName = "main";
    }

    public SubstructureCondition(boolean isReverse) {
        super(isReverse);
        this.substructureName = "main";
    }

    @Override
    public RecipeConditionType<SubstructureCondition> getType() {
        return OillessRecipeConditions.SUBSTRUCTURE;
    }

    @Override
    public boolean isOr() {
        return false;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("recipe.condition.substructure.tooltip", getSubstructureName());
    }

    @Override
    protected boolean testCondition(@NotNull GTRecipe gtRecipe, @NotNull RecipeLogic recipeLogic) {
        MetaMachine machine = recipeLogic.getMachine();
        if (machine instanceof MultiblockControllerMachine multi) {
            var patternState = multi.checkStructurePattern(getSubstructureName());
            if (patternState.isFormed()) {
                return true;
            } else if (patternState.hasErrors()) {
                return false;
            } else {
                // This…probably shouldn't happen.
                return false;
            }
        } else {
            return "main".equals(getSubstructureName());
        }
    }

    @Override
    public SubstructureCondition createTemplate() {
        return new SubstructureCondition("");
    }
}
