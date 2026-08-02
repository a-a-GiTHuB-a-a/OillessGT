package com.daboxen.oillessgt.data;

import com.tterrag.registrate.providers.ProviderType;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_REGISTRATE;

public class OillessDatagen {

    public static void init() {
        OILLESS_REGISTRATE.addDataGenerator(ProviderType.LANG, OillessLangHandler::init);
    }
}
