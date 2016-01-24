package com.ezrol.terry.minecraft.defaultworldgenerator.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiCreateCustomWorld;

public class GuiEvents {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onButtonClickPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
		if (event.gui instanceof GuiSelectWorld) {
			if (event.button.id == 3) {
				GuiCreateCustomWorld guiCreateWorld = new GuiCreateCustomWorld(
						Minecraft.getMinecraft().currentScreen);
				event.gui.mc.displayGuiScreen(guiCreateWorld);
				event.button.playPressSound(Minecraft.getMinecraft()
						.getSoundHandler());
				event.setCanceled(true);
			}
		}

		if (event.gui instanceof GuiCreateWorld) {
			if (event.button.id == 5) {
				if (ConfigGeneralSettings.generalLockWorldGenerator) {
					event.button.playPressSound(Minecraft.getMinecraft()
							.getSoundHandler());
					event.setCanceled(true);
				}
			}
		}

		// Log.error(event.button.id + " - " + event.gui.toString());
	}
}
