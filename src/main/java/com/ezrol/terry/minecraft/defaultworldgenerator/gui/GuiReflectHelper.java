package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;

import net.minecraft.client.gui.GuiCreateWorld;

public class GuiReflectHelper {
	public static Field selectedIndex;
	public static Field btnMapType;
	public static Field btnCustomizeType;
	public static Field btnMapFeatures;
	public static Field btnAllowCommands;
	public static Field worldSeedField;
	public static Field inMoreWorldOptionsDisplay;
	public static Field worldSeed;

	public static Method updateDisplayState;

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

			updateDisplayState.setAccessible(true);
		} catch (Exception ex) {
			Log.fatal("Unable to find Gui classes for reflection: ");
			Log.fatal(ex);
			Field fields[] = GuiCreateWorld.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Log.fatal("data: " + fields[i].getName());
			}
			throw new RuntimeException(ex);
		}
	}
}