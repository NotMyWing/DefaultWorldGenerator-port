package com.ezrol.terry.minecraft.defaultworldgenerator.integration;

import java.util.ArrayList;
import java.util.List;

public class PackModeStub extends PackModeInterface{

    @Override
    public List<String> getPackModes() {
        return new ArrayList<>();
    }

    @Override
    public String getCurrentMode() {
        return "NONE";
    }

    @Override
    public void setNewMode(String mode) {
        //nop
    }

    @Override
    public boolean packModeInstalled() {
        return false;
    }
}
