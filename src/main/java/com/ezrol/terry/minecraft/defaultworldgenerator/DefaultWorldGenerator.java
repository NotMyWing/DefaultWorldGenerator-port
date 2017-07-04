package com.ezrol.terry.minecraft.defaultworldgenerator;


import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigurationFile;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Reference;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("unused,WeakerAccess")
@Mod(modid = Reference.MOD_ID,
        version = Reference.VERSION_BUILD,
        name = Reference.MOD_NAME,
        acceptedMinecraftVersions = "[1.11.2,1.12.9]",
        guiFactory = "com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiFactory",
        acceptableRemoteVersions = "*")
public class DefaultWorldGenerator {
    public static ConfigurationFile modConfig;
    public static File modSettingsDir;

    @SidedProxy(clientSide = "com.ezrol.terry.minecraft.defaultworldgenerator.ClientProxy",
            serverSide = "com.ezrol.terry.minecraft.defaultworldgenerator.ServerProxy")
    private static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modSettingsDir = new File(event.getModConfigurationDirectory(),Reference.MOD_ID);
        modConfig = new ConfigurationFile(new File(modSettingsDir,"worldsettings.data"));
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

        if(! ConfigGeneralSettings.cfgCopyDefaultWorldData){
            //pack creator has disabled Default World Data functionality
            return;
        }

        if(!proxy.getDefaultWorldData().exists()){
            Log.warn("DefaultWorldData missing, skipping data injection");
            return;
        }
        File f;
        f=event.getServer().getActiveAnvilConverter().getFile(event.getServer().getFolderName(),"data");
        f=f.getParentFile();

        if(new File(f,"DefaultWorldDataLoaded.txt").exists()){
            Log.info("The Default World Data was already injected into this world");
            Log.info("Skipping, to overwrite delete: data/DefaultWorldDataLoaded.txt");
        }
        else{
            Log.info("Injecting DefaultWorldData into new save file");
            try {
                //copy the directory
                FileUtils.copyDirectory(proxy.getDefaultWorldData(), f,false);
                //make our flag file so we don't copy the directory again
                FileOutputStream note = new FileOutputStream(new File(f,"DefaultWorldDataLoaded.txt"));
                note.write("Delete this file to re-import Default World Data into this save.".getBytes("UTF-8"));
                note.flush();
                note.close();
            }
            catch(IOException e){
                Log.error("There was an error copying DefaultWorldData into your new world");
                Log.error(e.toString());
                for(StackTraceElement ln : e.getStackTrace()) {
                    Log.error(String.format(" > %s",ln.toString()));
                }
            }
        }
    }
}
