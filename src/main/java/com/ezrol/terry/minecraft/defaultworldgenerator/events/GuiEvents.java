package com.ezrol.terry.minecraft.defaultworldgenerator.events;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiCreateCustomWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraftforge.client.event.GuiScreenEvent;
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
                if (ConfigGeneralSettings.generalLockWorldGenerator) {
                    b.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                    event.setCanceled(true);
                }
            }
        }

        // Log.error(event.button.id + " - " + event.gui.toString());
    }
}
