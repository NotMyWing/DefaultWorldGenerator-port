package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.gui.GuiCreateWorld;

import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;

public class guiReflectHelper {
	public static Field selectedIndex;
	public static Field btnMapType;
	public static Field btnCustomizeType;
	public static Field btnMapFeatures;
	public static Field btnAllowCommands;
	public static Field field_146335_h;
	public static Field field_146344_y;

	public static Method func_146319_h;

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
				selectedIndex = GuiCreateWorld.class
						.getDeclaredField("selectedIndex");
				btnMapType = GuiCreateWorld.class
						.getDeclaredField("btnMapType");
				btnCustomizeType = GuiCreateWorld.class
						.getDeclaredField("btnCustomizeType");
				btnMapFeatures = GuiCreateWorld.class
						.getDeclaredField("btnMapFeatures");
				btnAllowCommands = GuiCreateWorld.class
						.getDeclaredField("btnAllowCommands");
				field_146335_h = GuiCreateWorld.class
						.getDeclaredField("field_146335_h");
				field_146344_y = GuiCreateWorld.class
						.getDeclaredField("field_146344_y");

				func_146319_h = GuiCreateWorld.class
						.getDeclaredMethod("func_146319_h");
			} else {
				selectedIndex = GuiCreateWorld.class
						.getDeclaredField("field_146331_K");
				btnMapType = GuiCreateWorld.class
						.getDeclaredField("field_146320_D");
				btnCustomizeType = GuiCreateWorld.class
						.getDeclaredField("field_146322_F");
				btnMapFeatures = GuiCreateWorld.class
						.getDeclaredField("field_146325_B");
				btnAllowCommands = GuiCreateWorld.class
						.getDeclaredField("field_146321_E");
				field_146335_h = GuiCreateWorld.class
						.getDeclaredField("field_146335_h");
				field_146344_y = GuiCreateWorld.class
						.getDeclaredField("field_146344_y");

				func_146319_h = GuiCreateWorld.class
						.getDeclaredMethod("func_146319_h");
			}
			selectedIndex.setAccessible(true);
			btnMapType.setAccessible(true);
			btnCustomizeType.setAccessible(true);
			btnMapFeatures.setAccessible(true);
			btnAllowCommands.setAccessible(true);
			field_146335_h.setAccessible(true);
			field_146344_y.setAccessible(true);

			func_146319_h.setAccessible(true);
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