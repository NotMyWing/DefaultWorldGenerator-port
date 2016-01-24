package com.ezrol.terry.minecraft.defaultworldgenerator.events;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiCreateCustomWorld;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;

public class GuiEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onButtonClickPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiSelectWorld) {
            if (event.button.id == 3) {
                GuiCreateCustomWorld guiCreateWorld = new GuiCreateCustomWorld(Minecraft.getMinecraft().currentScreen);
                event.gui.mc.displayGuiScreen(guiCreateWorld);
                event.button.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                event.setCanceled(true);
            }
        }

        if (event.gui instanceof GuiCreateWorld) {
            if (event.button.id == 5) {
                if (ConfigGeneralSettings.generalLockWorldGenerator) {
                    event.button.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                    event.setCanceled(true);
                }
            }
        }

        //Log.error(event.button.id + " - " + event.gui.toString());
    }
}
