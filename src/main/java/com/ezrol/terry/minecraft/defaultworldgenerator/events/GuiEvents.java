package com.ezrol.terry.minecraft.defaultworldgenerator.events;

import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigurationFile;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiCreateCustomWorld;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onButtonClickPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiWorldSelection) {
            GuiButton b = event.getButton();
            if (b.id == 3) {
                GuiCreateCustomWorld guiCreateWorld = new GuiCreateCustomWorld(Minecraft.getMinecraft().currentScreen);
                gui.mc.displayGuiScreen(guiCreateWorld);
                b.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                event.setCanceled(true);
            }
        }

        if (gui instanceof GuiCreateWorld) {
            GuiButton b = event.getButton();
            if (b.id == 5) {
                if (ConfigGeneralSettings.cfgLockWorldGenerator) {
                    b.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                    event.setCanceled(true);
                }
            }
            if (b.id == 7) {
                if (ConfigGeneralSettings.cfgBonusChestState == 2 ||
                        ConfigGeneralSettings.cfgBonusChestState == 3) {
                    b.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                    event.setCanceled(true);
                }
            }
        }

        // Log.error(event.button.id + " - " + event.gui.toString());
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        String eventModId = event.getModID();

        if (eventModId.equals(Reference.MOD_ID) && DefaultWorldGenerator.configuration != null) {
            Log.info("Updating config: " + Reference.MOD_ID);
            ConfigurationFile.loadConfiguration();
        }
    }
}
