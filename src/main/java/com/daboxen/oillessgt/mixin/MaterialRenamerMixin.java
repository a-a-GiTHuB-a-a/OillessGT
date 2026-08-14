package com.daboxen.oillessgt.mixin;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.MaterialProperties;
import com.gregtechceu.gtceu.api.registry.registrate.BuilderBase;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;

/**
 * jambon123456789 as my witness, I <i>tried</i> to do this without using a mixin. But I couldn't. Screw you.<br/><br/>
 * This doesn't even work, anyway.
 */
@Mixin(value = Material.Builder.class, remap = false)
public abstract class MaterialRenamerMixin extends BuilderBase<Material> {
    public MaterialRenamerMixin(ResourceLocation id) {
        super(id);
    }

    @Shadow
    public abstract Material.Builder langValue(String name);

    @Inject(method = "buildAndRegister", at = @At("HEAD"))
    public void oillessGT$renameMaterialsFromBuilder(CallbackInfoReturnable<Material> ci) {
        if (this.id.toString().equals("gtceu:oilsands")) {
            OILLESS_LOGGER.info("OILSANDS!!!");
            this.langValue("Tar-Rich Sands");
        }
    }
}
