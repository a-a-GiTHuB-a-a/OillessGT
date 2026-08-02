package com.daboxen.oillessgt.data;

import com.daboxen.oillessgt.additions.ProgressionPatches;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import static com.daboxen.oillessgt.OillessGTMod.LOGGER;

public class OillessLangHandler {

    public static void init(RegistrateLangProvider provider) {
        LOGGER.info("Generating lang values…");
        ProgressionPatches.initLang(provider);
    }
}
