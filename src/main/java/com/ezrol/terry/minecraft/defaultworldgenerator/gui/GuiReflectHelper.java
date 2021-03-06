package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.gui.GuiWorldSelection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("WeakerAccess")
public class GuiReflectHelper {
    //GuiCreateWorld fields
    public static Field selectedIndex;
    public static Field btnMapType;
    public static Field btnCustomizeType;
    public static Field btnMapFeatures;
    public static Field btnAllowCommands;
    public static Field worldSeedField;
    public static Field inMoreWorldOptionsDisplay;
    public static Field worldSeed;
    public static Field bonusChestEnabled;
    public static Field btnBonusItems;
    public static Field generateStructuresEnabled;
    public static Field worldName;

    //GuiWorldSelection Fields
    public static Field selectionList;

    //GuiListWorldSelectionEntry Fields
    public static Field worldSummary;

    //GuiCreateWorld methods
    public static Method updateDisplayState;

    @SuppressWarnings("JavaReflectionMemberAccess")
    public static void initReflect() {
        /*
         * determine if we are in dev or not, then load the private
		 * fields/methods we will access via reflection.
		 */
        boolean deob;
        try {
            GuiCreateWorld.class.getDeclaredField("selectedIndex");
            deob = true;
        } catch (NoSuchFieldException e) {
            deob = false;
        }

        try {
            if (deob) {
                selectedIndex = GuiCreateWorld.class.getDeclaredField("selectedIndex");
                btnMapType = GuiCreateWorld.class.getDeclaredField("btnMapType");
                btnCustomizeType = GuiCreateWorld.class.getDeclaredField("btnCustomizeType");
                btnMapFeatures = GuiCreateWorld.class.getDeclaredField("btnMapFeatures");
                btnAllowCommands = GuiCreateWorld.class.getDeclaredField("btnAllowCommands");
                worldSeedField = GuiCreateWorld.class.getDeclaredField("worldSeedField");
                inMoreWorldOptionsDisplay = GuiCreateWorld.class.getDeclaredField("inMoreWorldOptionsDisplay");
                worldSeed = GuiCreateWorld.class.getDeclaredField("worldSeed");
                bonusChestEnabled = GuiCreateWorld.class.getDeclaredField("bonusChestEnabled");
                btnBonusItems = GuiCreateWorld.class.getDeclaredField("btnBonusItems");
                generateStructuresEnabled = GuiCreateWorld.class.getDeclaredField("generateStructuresEnabled");
                btnMapFeatures = GuiCreateWorld.class.getDeclaredField("btnMapFeatures");
                worldName = GuiCreateWorld.class.getDeclaredField("worldName");

                selectionList = GuiWorldSelection.class.getDeclaredField("selectionList");

                worldSummary = GuiListWorldSelectionEntry.class.getDeclaredField("worldSummary");

                updateDisplayState = GuiCreateWorld.class.getDeclaredMethod("updateDisplayState");
            } else {
                selectedIndex = GuiCreateWorld.class.getDeclaredField("field_146331_K");
                btnMapType = GuiCreateWorld.class.getDeclaredField("field_146320_D");
                btnCustomizeType = GuiCreateWorld.class.getDeclaredField("field_146322_F");
                btnMapFeatures = GuiCreateWorld.class.getDeclaredField("field_146325_B");
                btnAllowCommands = GuiCreateWorld.class.getDeclaredField("field_146321_E");
                worldSeedField = GuiCreateWorld.class.getDeclaredField("field_146335_h");
                inMoreWorldOptionsDisplay = GuiCreateWorld.class.getDeclaredField("field_146344_y");
                worldSeed = GuiCreateWorld.class.getDeclaredField("field_146329_I");
                bonusChestEnabled = GuiCreateWorld.class.getDeclaredField("field_146338_v");
                btnBonusItems = GuiCreateWorld.class.getDeclaredField("field_146326_C");
                generateStructuresEnabled = GuiCreateWorld.class.getDeclaredField("field_146341_s");
                btnMapFeatures = GuiCreateWorld.class.getDeclaredField("field_146325_B");
                worldName = GuiCreateWorld.class.getDeclaredField("field_146330_J");

                selectionList = GuiWorldSelection.class.getDeclaredField("field_184866_u");

                worldSummary = GuiListWorldSelectionEntry.class.getDeclaredField("field_186786_g");

                updateDisplayState = GuiCreateWorld.class.getDeclaredMethod("func_146319_h");
            }
            selectedIndex.setAccessible(true);
            btnMapType.setAccessible(true);
            btnCustomizeType.setAccessible(true);
            btnMapFeatures.setAccessible(true);
            btnAllowCommands.setAccessible(true);
            worldSeedField.setAccessible(true);
            worldSeed.setAccessible(true);
            inMoreWorldOptionsDisplay.setAccessible(true);
            bonusChestEnabled.setAccessible(true);
            btnBonusItems.setAccessible(true);
            generateStructuresEnabled.setAccessible(true);
            worldName.setAccessible(true);

            selectionList.setAccessible(true);
            worldSummary.setAccessible(true);

            updateDisplayState.setAccessible(true);
        } catch (Exception ex) {
            Log.fatal("Unable to find Gui classes for reflection: ");
            Log.fatal(ex);
            Field fields[] = GuiCreateWorld.class.getDeclaredFields();
            for (Field field : fields) {
                Log.fatal("data: " + field.getName());
            }
            throw new RuntimeException(ex);
        }
    }
}