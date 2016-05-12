package com.ezrol.terry.minecraft.defaultworldgenerator;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigurationFile;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.ServerDefaults;
import com.ezrol.terry.minecraft.defaultworldgenerator.events.GuiEvents;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.guiReflectHelper;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Reference;

import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION_BUILD, name = Reference.MOD_NAME, guiFactory = "com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiFactory", acceptableRemoteVersions = "*")
public class DefaultWorldGenerator {
	public static Configuration configuration = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configuration = ConfigurationFile.init(event.getSuggestedConfigurationFile());
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
				if (WorldType.worldTypes[i] != null && WorldType.worldTypes[i].getCanBeCreated()) {
					Log.info("Name: " + WorldType.worldTypes[i].getWorldTypeName());
				}
			}
			Log.info("==================================================================");
		}
		if (event.getSide() == Side.SERVER) {
			Log.info("Injecting Server Defaults");
			ServerDefaults.SetDefaults();
		}

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		String eventModId = event.getModID();

		if (eventModId.equals(Reference.MOD_ID) && configuration != null) {
			Log.info("Updating config: " + Reference.MOD_ID);
			ConfigurationFile.loadConfiguration();
		}
	}
}
