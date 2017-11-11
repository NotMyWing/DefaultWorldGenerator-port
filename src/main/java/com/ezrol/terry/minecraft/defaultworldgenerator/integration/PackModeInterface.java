package com.ezrol.terry.minecraft.defaultworldgenerator.integration;

import net.minecraftforge.fml.common.Loader;

import java.util.List;

/**
 * Abstract class to provide an interface to the PackMode API
 * Allows us not to have a filler class if the PackMode mod is not currently installed
 */
abstract public class PackModeInterface {
    public static PackModeInterface getInterface(){
        if(Loader.isModLoaded("packmode")){
            return new PackModeImpl();
        }
        return new PackModeStub();
    }

    abstract public List<String> getPackModes();
    abstract public String getCurrentMode();
    abstract public void setNewMode(String mode);
    abstract public boolean packModeInstalled();
}
