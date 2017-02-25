package com.ezrol.terry.minecraft.defaultworldgenerator;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.io.File;

/**
 * Common proxy for server vs client logic
 * Created by ezterry on 2/24/17.
 */
public class CommonProxy {

    public File getDefaultWorldData(){
        return new File((new File("")).getAbsoluteFile(),"DefaultWorldData");
    }

    public void init(FMLInitializationEvent event){
        //per side init
    }
    public void postInit(FMLPostInitializationEvent event) {
        //per side postInit
    }
}
