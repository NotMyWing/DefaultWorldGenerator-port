package com.ezrol.terry.minecraft.defaultworldgenerator;

import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigurationFile;
import com.ezrol.terry.minecraft.defaultworldgenerator.events.GuiEvents;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.guiReflectHelper;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Reference;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION_BUILD, name = Reference.MOD_NAME)
public class DefaultWorldGenerator {
	public static Configuration configuration;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configuration = ConfigurationFile.init(event
				.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			guiReflectHelper.initReflect();
			MinecraftForge.EVENT_BUS.register(new GuiEvents());
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (ConfigGeneralSettings.generalShowDebugWorldGenerators) {
			Log.info("=======================[ World Generators ]=======================");
			for (int i = 0; i < WorldType.worldTypes.length; i++) {
				if (WorldType.worldTypes[i] != null
						&& WorldType.worldTypes[i].getCanBeCreated()) {
					Log.info("Name: "
							+ WorldType.worldTypes[i].getWorldTypeName());
				}
			}
			Log.info("==================================================================");
		}
	}
}
