package com.daboxen.oillessgt.emi;

import com.daboxen.oillessgt.config.OillessConfiguration;
import com.daboxen.oillessgt.machines.multiblock.InertGasBoostEMICategory;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
@SuppressWarnings("unused") //The @EmiEntrypoint annotation ensures that this is in fact used. :)
public class OillessEMIPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        if (OillessConfiguration.INSTANCE.addLargePyrolyser) {
            registry.addCategory(InertGasBoostEMICategory.CATEGORY);
            InertGasBoostEMICategory.registerDisplays(registry);
            InertGasBoostEMICategory.registerWorkstations(registry);
        }
    }
}