package com.example.fbrckmacro;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FbrckMacroMod implements ModInitializer {
    public static final String MOD_ID = "fbrckmacro";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("FbrckMacro initialized.");
    }
}
