package com.daboxen.oillessgt.mixin;

import com.daboxen.oillessgt.additions.ProgressionPatches;
import com.google.gson.JsonElement;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RecipeManager.class, priority = 100) //lowk stole this code from phoenixcore
public class RecipeGaslighterMixin {
    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("HEAD"))
    private void oillessGT$removeRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager,
                                         ProfilerFiller profiler, CallbackInfo ci) {
        // 1. Reload config to catch any changes made while the game was running
        //ProgressionPatches.load();
        //not needed since i aint using JSON for ts

        // 2. Filter the map using the entrySet so we have access to the ID and the JSON body
        map.entrySet().removeIf(entry -> ProgressionPatches.shouldDeleteFromJSON(entry.getKey(), entry.getValue()));
    }
}