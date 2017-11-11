package com.ezrol.terry.minecraft.defaultworldgenerator;

import com.ezrol.terry.minecraft.defaultworldgenerator.events.GuiEvents;
import com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiReflectHelper;
import com.ezrol.terry.minecraft.defaultworldgenerator.integration.WrongPackModeGui;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;


/**
 * Client only logic
 *
 * Created by ezterry on 2/24/17.
 */
@SuppressWarnings("unused,WeakerAccess")
public class ClientProxy extends CommonProxy{
    private String newServerMode = "";
    @Override
    public void init(FMLInitializationEvent event) {
        GuiReflectHelper.initReflect();
        MinecraftForge.EVENT_BUS.register(new GuiEvents());
    }

    @Override
    public void wrongServerMode(String newMode) {
        newServerMode = newMode;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onServerConnection(FMLNetworkEvent.ClientConnectedToServerEvent event){
        Log.info("in On Server Connection");
        if(event.isLocal()){
            Log.error("Inform user of wrong pack mode");
            FMLClientHandler client = FMLClientHandler.instance();
            client.showGuiScreen(new WrongPackModeGui(newServerMode));
            //noinspection InfiniteLoopStatement
            while (true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
