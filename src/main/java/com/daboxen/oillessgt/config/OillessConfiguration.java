package com.daboxen.oillessgt.config;

import com.daboxen.oillessgt.OillessGTMod;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.UpdateRestrictions;
import dev.toma.configuration.config.format.ConfigFormats;
import org.jetbrains.annotations.ApiStatus;

@Config(id = OillessGTMod.MOD_ID)
public class OillessConfiguration {
    public static OillessConfiguration INSTANCE;
    private static final Object LOCK = new Object();

    @ApiStatus.Internal
    public static dev.toma.configuration.config.ConfigHolder<OillessConfiguration> INTERNAL_INSTANCE;

    public static void init() {
        synchronized (LOCK) {
            if (INSTANCE == null || INTERNAL_INSTANCE == null) {
                INTERNAL_INSTANCE = Configuration.registerConfig(OillessConfiguration.class, ConfigFormats.yaml());
                INSTANCE = INTERNAL_INSTANCE.getConfigInstance();
            }
        }
    }

    public static OillessConfiguration getInstance() {
        init();
        return INSTANCE;
    }

    @Configurable
    @Configurable.Comment({"Whether to register the TREE.", "Default: true"})
    @Configurable.UpdateRestriction(UpdateRestrictions.MAIN_MENU)
    public boolean addLargePyrolyser = true;
}
