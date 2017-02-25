package com.ezrol.terry.minecraft.defaultworldgenerator;

import com.ezrol.terry.minecraft.defaultworldgenerator.events.GuiEvents;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiReflectHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;

/**
 * Client only logic
 *
 * Created by ezterry on 2/24/17.
 */
@SuppressWarnings("unused,WeakerAccess")
public class ClientProxy extends CommonProxy{
    @Override
    public File getDefaultWorldData() {
        return new File(Minecraft.getMinecraft().mcDataDir.getAbsoluteFile(),"DefaultWorldData");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        GuiReflectHelper.initReflect();
        MinecraftForge.EVENT_BUS.register(new GuiEvents());
    }
}
