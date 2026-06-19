package com.daboxen.oillessgt.mixin;

import com.gregtechceu.gtceu.common.data.GTBedrockFluids;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

// This is a dummy mixin! It doesn't actually do anything.
// Mixins are ways to modify code in other classes.
// They can be very valuable in the right circumstances,
// but it is generally preferred that you try to use other means
// to get your code to work before resorting to mixins,
// as they can be highly invasive.
@SuppressWarnings("unused")
@Mixin(value = GTBedrockFluids.class, remap = false)
public class FluidVeinMixin {
    /***
     * Injects into the fluid vein registrar to prevent registering the oil ones >:D
     */
    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    public void ignoreOilVeins(Args args, CallbackInfo ci) {
        //ResourceLocation id,
        //Consumer<BedrockFluidDefinition.Builder> consumer
        ResourceLocation id = args.get(0);
        if (id.toString().contains("oil") || id.toString().contains("natural_gas")) ci.cancel();
    }
}