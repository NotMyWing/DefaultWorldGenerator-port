package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.ezrol.terry.minecraft.defaultworldgenerator.helper.ConfigurationHelper;

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
		ConfigGeneralSettings.generalWorldGenerator = ConfigurationHelper
				.getString(configuration, "World Generator", "general",
						"default", "The world generator to select by default",
						true);
		ConfigGeneralSettings.generalShowDebugWorldGenerators = ConfigurationHelper
				.getBoolean(configuration, "Show World Generators in Log",
						"general", false,
						"Enabling this will display all world generators installed, useful for debug");
		ConfigGeneralSettings.customizationJson = ConfigurationHelper
				.getString(
						configuration,
						"CustomizationJson",
						"general",
						"",
						"The world customization string (JSON, or super flat string)",
						true);
		ConfigGeneralSettings.generalLockWorldGenerator = ConfigurationHelper
				.getBoolean(
						configuration,
						"Lock Worldtype",
						"general",
						false,
						"Prevent the user from changing the world type.");

		if (configuration.hasChanged()) {
			configuration.save();
		}
	}
}
