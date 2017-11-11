package com.ezrol.terry.minecraft.defaultworldgenerator.integration;

import io.sommers.packmode.api.PackModeAPI;

import java.util.ArrayList;
import java.util.List;

public class PackModeImpl extends PackModeInterface{
    private PackModeAPI instance;

    PackModeImpl(){
        instance = PackModeAPI.getInstance();
    }

    /**
     * Return a copy of the pack modes list
     * @return the copy of the Pack Modes list
     */
    @Override
    public List<String> getPackModes() {
        return new ArrayList<>(instance.getPackModes());
    }

    @Override
    public String getCurrentMode() {
        return instance.getCurrentPackMode();
    }

    @Override
    public void setNewMode(String mode) {
        instance.setNextRestartPackMode(mode);
    }

    @Override
    public boolean packModeInstalled() {
        return true;
    }
}
