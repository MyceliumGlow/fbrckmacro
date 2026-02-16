package com.example.fbrckmacro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MacroSettings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("fbrckmacro.json");

    private int clickDelayMs = 75;
    private boolean onlyWhenHoldingWeapon = false;
    private boolean holdToClick = false;

    public static MacroSettings load() {
        if (!Files.exists(FILE)) {
            MacroSettings defaults = new MacroSettings();
            defaults.save();
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(FILE)) {
            MacroSettings loaded = GSON.fromJson(reader, MacroSettings.class);
            if (loaded == null) {
                return new MacroSettings();
            }
            loaded.clickDelayMs = Math.max(1, loaded.clickDelayMs);
            return loaded;
        } catch (IOException | JsonParseException e) {
            FbrckMacroMod.LOGGER.warn("Failed to read macro settings, using defaults.", e);
            return new MacroSettings();
        }
    }

    public void save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            FbrckMacroMod.LOGGER.error("Failed to save macro settings.", e);
        }
    }

    public int getClickDelayMs() {
        return clickDelayMs;
    }

    public void setClickDelayMs(int clickDelayMs) {
        this.clickDelayMs = Math.max(1, clickDelayMs);
    }

    public boolean isOnlyWhenHoldingWeapon() {
        return onlyWhenHoldingWeapon;
    }

    public void setOnlyWhenHoldingWeapon(boolean onlyWhenHoldingWeapon) {
        this.onlyWhenHoldingWeapon = onlyWhenHoldingWeapon;
    }

    public boolean isHoldToClick() {
        return holdToClick;
    }

    public void setHoldToClick(boolean holdToClick) {
        this.holdToClick = holdToClick;
    }
}
