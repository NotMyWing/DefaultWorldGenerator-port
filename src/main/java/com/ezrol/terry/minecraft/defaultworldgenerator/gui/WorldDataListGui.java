package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;

/**
 * Class to configure the new JSON formatted preferences from the gui configure screen.
 * Created by ezterry on 6/17/17.
 */
public class WorldDataListGui extends GuiScreen implements GuiPageButtonList.GuiResponder {
    private static final int DONE_ID = 900;
    private static final int CLEARALL_ID = 901;
    private static final int CANCEL_ID = 902;

    @Override
    public void setEntryValue(int id, boolean value) {

    }

    @Override
    public void setEntryValue(int id, float value) {

    }

    @Override
    public void setEntryValue(int id, String value) {

    }
}
