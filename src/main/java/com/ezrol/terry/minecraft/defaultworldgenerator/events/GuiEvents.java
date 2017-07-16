package com.ezrol.terry.minecraft.defaultworldgenerator.events;

import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.BooleanTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.datapack.WorldWrapper;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.DefaultWorldSelectionList;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiCreateCustomWorld;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiReflectHelper;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;
import java.util.List;

public class GuiEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onButtonClickPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiWorldSelection) {
            GuiButton b = event.getButton();

            //even if not a listed action ensure the selected level is clear so no unexpected default is used
            DefaultWorldGenerator.selectedLevel = null;
            if (b.id == 3) {
                //New World Button
                DefaultWorldGenerator.modConfig.restoreSettings();
                TriggerNewWorld((GuiWorldSelection)gui);
                b.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                event.setCanceled(true);
            }
            if (b.id == 5) {
                //Copy Button
                DefaultWorldGenerator.modConfig.restoreSettings();
                TriggerCopyWorld((GuiWorldSelection)gui);
                b.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                event.setCanceled(true);
            }
        }
        else if(gui instanceof GuiCreateCustomWorld){
            GuiButton b = event.getButton();
            if(b.enabled && b.id == 0){
                Log.info("New World Click");
                //call here in case something overrides
                ((GuiCreateCustomWorld)gui).createNewWorldClick();
            }
        }
        // Log.error(event.button.id + " - " + event.gui.toString());
    }

    /**
     * Open the Customized New World Gui
     *
     * @param gui the active WorldSelectionScreen
     */
    private void TriggerNewWorld(GuiWorldSelection gui){
        List<WorldTypeNode> types = DefaultWorldGenerator.modConfig.getSettings().getWorldList();
        List<WorldTypeNode> selectable = new LinkedList<>();
        GuiScreen screen;

        for(WorldTypeNode n : types){
            if(((BooleanTypeNode)n.getField(WorldTypeNode.Fields.SHOW_IN_LIST)).getValue()){
                selectable.add(n);
            }
        }
        if(selectable.size()==0){
            if(types.size()==0){
                screen = new GuiCreateCustomWorld(Minecraft.getMinecraft().currentScreen,new WorldTypeNode(null));
            }
            else{
                screen = new GuiCreateCustomWorld(Minecraft.getMinecraft().currentScreen,types.get(0));
            }
        }
        else{
            screen = new DefaultWorldSelectionList(Minecraft.getMinecraft().currentScreen, selectable);
        }
        gui.mc.displayGuiScreen(screen);
    }

    /**
     * Copy the world using the Customized New World Gui
     *
     * @param gui the active WorldSelectionScreen
     */
    private void TriggerCopyWorld(GuiWorldSelection gui){
        try {
            //first extract the current selected entry via some reflection magic
            GuiListWorldSelection worldList = (GuiListWorldSelection) GuiReflectHelper.selectionList.get(gui);
            GuiListWorldSelectionEntry entry = worldList.getSelectedWorld();
            WorldSummary summary = (WorldSummary) GuiReflectHelper.worldSummary.get(entry);

            ISaveHandler isavehandler=gui.mc.getSaveLoader().getSaveLoader(summary.getFileName(),false);
            WorldInfo worldinfo = isavehandler.loadWorldInfo();
            WorldWrapper oldWorld = new WorldWrapper(isavehandler);
            isavehandler.flush();

            GuiCreateCustomWorld screen;

            if (worldinfo != null){
                Log.info(String.format("Copying World of Type: %s",
                        oldWorld.getWorldType().getField(WorldTypeNode.Fields.CONFIGURATION_NAME)));
                screen = new GuiCreateCustomWorld(Minecraft.getMinecraft().currentScreen,oldWorld.getWorldType());
                screen.recreateFromExistingWorld(worldinfo);
                gui.mc.displayGuiScreen(screen);
            }
        } catch (IllegalAccessException e) {
            throw(new RuntimeException(e));
        }
    }
}