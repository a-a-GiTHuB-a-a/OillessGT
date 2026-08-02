package com.daboxen.oillessgt.mixin;

import com.daboxen.oillessgt.additions.ProgressionPatches;

import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static com.daboxen.oillessgt.OillessGTMod.LOGGER;

@Mixin(value = GTRecipeBuilder.class, remap = false)
public class RecipeCensorerMixin {

    @Inject(method = "save", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void oillessGT$censorOilRecipes(Consumer<FinishedRecipe> consumer, CallbackInfo ci) {
        if (ProgressionPatches.shouldRemoveRecipe((GTRecipeBuilder) (Object) this)) ci.cancel();
    }
}
