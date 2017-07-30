package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.BooleanTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.QuadStateTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldType;

import java.io.IOException;

/**
 * The replacement Create world screen, where the defaults are pre-set for the user based on a mod pack
 * creator's settings.
 */
public class GuiCreateCustomWorld extends GuiCreateWorld {
    private WorldTypeNode presetData;
    private boolean cfgLockWorldGenerator;

    public GuiCreateCustomWorld(GuiScreen screen, WorldTypeNode type) {
        super(screen);
        presetData = type;
        // set the customization here so a user can change it
        this.chunkProviderSettingsJson = ((StringTypeNode)presetData.getField(
                WorldTypeNode.Fields.CUSTOMIZATION_STRING)).getValue();

        cfgLockWorldGenerator = ((BooleanTypeNode)presetData.getField(
                WorldTypeNode.Fields.LOCK_WORLD_TYPE)).getValue();
        try {
            int WorldGenerator = 0;
            String generatorName = ((StringTypeNode)presetData.getField(
                    WorldTypeNode.Fields.WORLD_GENERATOR)).getValue();
            for (int i = 0; i < WorldType.WORLD_TYPES.length; i++) {
                if (WorldType.WORLD_TYPES[i] != null && WorldType.WORLD_TYPES[i].canBeCreated()) {
                    if (WorldType.WORLD_TYPES[i].getName()
                            .equalsIgnoreCase(generatorName)) {
                        WorldGenerator = WorldType.WORLD_TYPES[i].getId();
                        Log.info("Changed world type to " + WorldType.WORLD_TYPES[i].getName());
                    }
                }
            }
            GuiReflectHelper.selectedIndex.setInt(this, WorldGenerator);

            String cfgSeed = ((StringTypeNode)presetData.getField(
                    WorldTypeNode.Fields.SEED)).getValue();
            if (!cfgSeed.equals("")) {
                GuiReflectHelper.worldSeed.set(this, cfgSeed);
            }
            String cfgName = ((StringTypeNode)presetData.getField(
                    WorldTypeNode.Fields.CONFIGURATION_NAME)).getValue();
            if(!cfgName.equals("")){
                GuiReflectHelper.worldName.set(this,I18n.format("defaultworldgenerator-port.newworld.gui.name",cfgName));
            }
            QuadStateTypeNode.States bonusChest = ((QuadStateTypeNode)presetData.getField(
                    WorldTypeNode.Fields.BONUS_CHEST_STATE)).getValue();
            if (bonusChest == QuadStateTypeNode.States.STATE_ENABLED || bonusChest == QuadStateTypeNode.States.STATE_FORCED) {
                GuiReflectHelper.bonusChestEnabled.setBoolean(this,true);
            } else{
                GuiReflectHelper.bonusChestEnabled.setBoolean(this,false);
            }
            QuadStateTypeNode.States structures = ((QuadStateTypeNode)presetData.getField(
                    WorldTypeNode.Fields.STRUCTURE_STATE)).getValue();
            if (structures == QuadStateTypeNode.States.STATE_ENABLED || structures == QuadStateTypeNode.States.STATE_FORCED) {
                GuiReflectHelper.generateStructuresEnabled.setBoolean(this,true);
            } else{
                GuiReflectHelper.generateStructuresEnabled.setBoolean(this,false);
            }
        } catch (Exception ex) {
            Log.fatal("Fatal Error:");
            Log.fatal(ex);
        }
    }

    private void hideWorldConfig() {
        try {
            if (cfgLockWorldGenerator) {
                ((GuiButton) GuiReflectHelper.btnMapType.get(this)).visible = false;
                ((GuiButton) GuiReflectHelper.btnCustomizeType.get(this)).visible = false;
                ((GuiButton) GuiReflectHelper.btnMapFeatures.get(this)).x = this.width / 2 - 75;
                if(!((StringTypeNode)presetData.getField(
                        WorldTypeNode.Fields.SEED)).getValue().equals("")) {
                    //seed is set lock it
                    ((GuiTextField) GuiReflectHelper.worldSeedField.get(this)).setEnabled(false);
                }
            }
            QuadStateTypeNode.States bonusChest = ((QuadStateTypeNode)presetData.getField(
                    WorldTypeNode.Fields.BONUS_CHEST_STATE)).getValue();
            if (bonusChest == QuadStateTypeNode.States.STATE_BLOCKED || bonusChest == QuadStateTypeNode.States.STATE_FORCED) {
                ((GuiButton) GuiReflectHelper.btnBonusItems.get(this)).enabled = false;
            }
            QuadStateTypeNode.States structures = ((QuadStateTypeNode)presetData.getField(
                    WorldTypeNode.Fields.STRUCTURE_STATE)).getValue();
            if (structures == QuadStateTypeNode.States.STATE_BLOCKED || structures == QuadStateTypeNode.States.STATE_FORCED) {
                ((GuiButton) GuiReflectHelper.btnMapFeatures.get(this)).enabled = false;
            }
        } catch (Exception ex) {
            Log.fatal("Error Hiding Buttons:");
            Log.fatal(ex);
        }

    }

    @Override
    public void initGui() {
        super.initGui();

        try {

            GuiReflectHelper.updateDisplayState.invoke(this);

        } catch (Exception ex) {
            Log.fatal("Fatal Error:");
            Log.fatal(ex);
        }
        this.hideWorldConfig();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        this.hideWorldConfig();
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY,
     * renderPartialTicks We need to replace the version in CreateWorldGui if we
     * are hiding the override
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!cfgLockWorldGenerator) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            return;
        }
        try {
            if (!GuiReflectHelper.inMoreWorldOptionsDisplay.getBoolean(this)) {
                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }
            // We really need to update the draw
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRenderer, I18n.format("selectWorld.create"),
                    this.width / 2, 20, -1);

            this.drawString(this.fontRenderer, I18n.format("selectWorld.enterSeed"),
                    this.width / 2 - 100, 47, -6250336);
            this.drawString(this.fontRenderer, I18n.format("selectWorld.seedInfo"),
                    this.width / 2 - 100, 85, -6250336);

            if (((GuiButton) GuiReflectHelper.btnMapFeatures.get(this)).visible) {
                this.drawString(this.fontRenderer, I18n.format("selectWorld.mapFeatures.info"),
                        this.width / 2 - 70, 122, -6250336);
            }

            if (((GuiButton) GuiReflectHelper.btnAllowCommands.get(this)).visible) {
                this.drawString(this.fontRenderer, I18n.format("selectWorld.allowCommands.info"),
                        this.width / 2 - 150, 172, -6250336);
            }

            ((GuiTextField) GuiReflectHelper.worldSeedField.get(this)).drawTextBox();

            for (GuiButton aButtonList : this.buttonList) {
                (aButtonList).drawButton(this.mc, mouseX, mouseY, partialTicks);
            }

            for (GuiLabel aLabelList : this.labelList) {
                (aLabelList).drawLabel(this.mc, mouseX, mouseY);
            }
        } catch (Exception ex) {
            Log.fatal("Fatal Error Drawing Screen:");
            Log.fatal(ex);
        }
    }

    /**
     * Triggered on new world click, just copy the selected level as the preset
     */
    public void createNewWorldClick() {
        DefaultWorldGenerator.selectedLevel = presetData;
    }
}
