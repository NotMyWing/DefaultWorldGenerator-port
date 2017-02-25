package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import com.ezrol.terry.minecraft.defaultworldgenerator.helper.ConfigurationHelper;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationFile {
    public static Configuration configuration;

    public static Configuration init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
        return configuration;
    }

    public static void loadConfiguration() {
        ConfigGeneralSettings.cfgWorldGenerator = ConfigurationHelper.getString(configuration, "World Generator",
                "general", "default", "The world generator to select by default", true);
        ConfigGeneralSettings.cfgSeed = ConfigurationHelper.getString(configuration, "Seed",
                "general", "", "The Suggested Seed for the pack, leave blank for a random seed, note" +
                        "the player can change this even if locked", true);
        ConfigGeneralSettings.cfgWorldGenerator = ConfigurationHelper.getString(configuration, "World Generator",
                "general", "default", "The world generator to select by default", true);
        ConfigGeneralSettings.cfgCustomizationJson = ConfigurationHelper.getString(configuration, "CustomizationJson",
                "general", "", "The world customization string (JSON, or super flat string)", true);
        ConfigGeneralSettings.cfgLockWorldGenerator = ConfigurationHelper.getBoolean(configuration,
                "Lock Worldtype", "general", false, "Prevent the user from changing the world type.");
        ConfigGeneralSettings.cfgBonusChestState = ConfigurationHelper.getInt(configuration,"Bonus Chest State","general",0,
                "State of the Bonus Chest Button 0=disabled+shown, 1=enabled+shown, 2=disable+hidden, 3=enable+hidden");
        ConfigGeneralSettings.cfgCopyDefaultWorldData = ConfigurationHelper.getBoolean(configuration,
                "Copy DefaultWorldData","general",true,"Copy the DefaultWorldData directory into the new world data directory");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
