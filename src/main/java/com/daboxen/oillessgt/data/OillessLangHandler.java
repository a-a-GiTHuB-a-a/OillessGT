package com.daboxen.oillessgt.data;

import com.daboxen.oillessgt.additions.ProgressionPatches;

import com.daboxen.oillessgt.machines.OillessMachines;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;

public class OillessLangHandler {

    public static void init(RegistrateLangProvider provider) {
        OILLESS_LOGGER.info("Generating lang values…");
        ProgressionPatches.initLang(provider);
        OillessMachines.initLang(provider);
    }
}
