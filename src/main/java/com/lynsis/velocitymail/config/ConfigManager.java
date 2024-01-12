package com.lynsis.velocitymail.config;

import com.lynsis.velocitymail.utils.FileUtils;
import ninja.leaping.configurate.ConfigurationNode;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;

public class ConfigManager {
    public static @NonNull ConfigurationNode config;

    public static void loadConfig() {
        try {
            ConfigManager.config = FileUtils.loadOrCreate("config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
