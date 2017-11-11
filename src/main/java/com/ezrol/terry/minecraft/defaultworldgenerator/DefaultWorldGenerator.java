package com.ezrol.terry.minecraft.defaultworldgenerator;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigurationFile;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.datapack.WorldWrapper;
import com.ezrol.terry.minecraft.defaultworldgenerator.integration.PackModeInterface;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.io.File;

@SuppressWarnings("unused,WeakerAccess")
@Mod(modid = Reference.MOD_ID,
        version = Reference.VERSION_BUILD,
        name = Reference.MOD_NAME,
        acceptedMinecraftVersions = "[1.11.2,1.12.9]",
        guiFactory = "com.ezrol.terry.minecraft.defaultworldgenerator.gui.GuiFactory",
        dependencies = "required-after:forge@[14.21.1.2406,)",
        acceptableRemoteVersions = "*")
public class DefaultWorldGenerator {
    public static ConfigurationFile modConfig;
    public static File modSettingsDir;
    public static WorldTypeNode selectedLevel = null;
    WorldInitCommands pendingCommands=null;

    @SidedProxy(clientSide = "com.ezrol.terry.minecraft.defaultworldgenerator.ClientProxy",
            serverSide = "com.ezrol.terry.minecraft.defaultworldgenerator.ServerProxy")
    private static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Log.modLog = event.getModLog();

        modSettingsDir = new File(event.getModConfigurationDirectory(),Reference.MOD_ID);
        if(!modSettingsDir.exists()){
            //noinspection ResultOfMethodCallIgnored
            modSettingsDir.mkdir();
        }
        modConfig = new ConfigurationFile(new File(modSettingsDir,"worldsettings.data"));
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        modConfig.readFromDisk();
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarted(FMLServerStartingEvent event){
        Log.info("Verify Tweaks");
        File datadir = event.getServer().getActiveAnvilConverter().getFile(event.getServer().getFolderName(),"data");
        datadir = datadir.toPath().getParent().toFile();

        WorldWrapper ourWorld = new WorldWrapper(datadir);

        String tweaks = ((StringTypeNode)ourWorld.getWorldType().getField(WorldTypeNode.Fields.PACK_MODE)).getValue();
        if(!tweaks.equals("any")){
            PackModeInterface packMode = PackModeInterface.getInterface();
            if(!tweaks.equals("packmode:" + packMode.getCurrentMode())){
                Log.info("Pack Mode not correctly set");
                if(packMode.packModeInstalled()){
                    try {
                        packMode.setNewMode(tweaks.replaceFirst("packmode:",""));
                        Log.info("Pack mode changed, ask user to restart");
                        proxy.wrongServerMode(tweaks);
                    }
                    catch (IllegalArgumentException e){
                        Log.error("Pack mode not found? " + e);
                    }
                }
                else{
                    Log.warn("Pack mode no longer installed, but tweaks defined in DWG config");
                }
            }
        }
    }

    @EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        File datadir = event.getServer().getActiveAnvilConverter().getFile(event.getServer().getFolderName(),"data");
        datadir = datadir.toPath().getParent().toFile();

        WorldWrapper ourWorld = new WorldWrapper(datadir);

        Log.info(String.format("Loading Data For: %s",
                ourWorld.getWorldType().getField(WorldTypeNode.Fields.CONFIGURATION_NAME)));
        ourWorld.importData();
        ourWorld.saveWorldData();

        pendingCommands = new WorldInitCommands(ourWorld);
    }

    @EventHandler
    public void serverStop(FMLServerStoppedEvent event) {
        if(pendingCommands != null){
            pendingCommands.unload();
        }
    }

}
