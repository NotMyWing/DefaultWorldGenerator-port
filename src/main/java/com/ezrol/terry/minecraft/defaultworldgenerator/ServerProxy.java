package com.ezrol.terry.minecraft.defaultworldgenerator;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.io.File;

/**
 * Server only logic
 * Created by ezterry on 2/24/17.
 */
@SuppressWarnings("unused,WeakerAccess")
public class ServerProxy extends CommonProxy {
    @Override
    public File getDefaultWorldData() {
        return super.getDefaultWorldData();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (event.getSide() == Side.SERVER) {
            Log.info("Injecting Server Defaults");

            try {
                MinecraftServer s = FMLServerHandler.instance().getServer();
                if (s.isDedicatedServer()) {
                    DedicatedServer server = (DedicatedServer) s;
                    server.getStringProperty("level-type", ConfigGeneralSettings.cfgWorldGenerator);
                    server.getStringProperty("generator-settings", ConfigGeneralSettings.cfgCustomizationJson);
                    server.getStringProperty("level-seed", ConfigGeneralSettings.cfgSeed);

                    if(ConfigGeneralSettings.cfgBonusChestState == 1 || ConfigGeneralSettings.cfgBonusChestState == 3) {
                        server.canCreateBonusChest(true);
                    }
                }
            } catch (Exception ex) {
                Log.fatal("Unable to set dedicated server properties: ");
                Log.fatal(ex);
            }
        }
    }
}
