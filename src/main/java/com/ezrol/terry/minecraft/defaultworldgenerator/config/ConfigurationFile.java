package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import com.ezrol.terry.lib.huffstruct.Huffstruct;
import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

public class ConfigurationFile {
    private byte[] currentData;
    private File location;
    private SettingsRoot settings;

    public ConfigurationFile(File loc){
        location=loc;
        settings=null;
    }

    static public boolean safeFileName(String n){
        return(n.matches("[a-z0-9_]*"));
    }

    /**
     * Restore settings to the data last in the file
     */
    public void restoreSettings(){
        if(currentData == null) {
            settings = new SettingsRoot(null);
            writeToDisk();
        }
        else{
            try{
                //read in the settings again
                settings = new SettingsRoot(Huffstruct.loadData(currentData));
                //make sure any datadirs are created (if a valid name)
                for(WorldTypeNode n : settings.getWorldList() ){
                    for(String data : ((StringListTypeNode)n.getField(WorldTypeNode.Fields.DATA_PACKS)).getValue()){
                        if(safeFileName(data)){
                            File datadir = new File(DefaultWorldGenerator.modSettingsDir,data);
                            if(!datadir.exists()){
                                //noinspection ResultOfMethodCallIgnored
                                datadir.mkdir();
                            }
                        }
                    }
                }
            } catch(Exception e){
                Log.error("Error parsing config: " + e);
                Log.info("using internal configuration");
                settings = new SettingsRoot(null);
                writeToDisk();
            }
        }
    }

    /**
     * Read (or reread) the settings file from disk
     */
    public void readFromDisk(){
        currentData = null;
        try {
            if(location != null && location.exists()) {
                currentData = Files.readAllBytes(location.toPath());
            } else {
                Log.info("Config file not found, using internal defaults (" +
                        (location == null ? "NULL" : location.toString()) + ")");
            }
            restoreSettings();
        } catch (IOException e) {
            Log.error("Unable to read file: " + location.toString());
            Log.error("Assuming config file is blank");
            restoreSettings();
            writeToDisk();
        }
    }

    /**
     * Get the current Settings structure
     */
    public SettingsRoot getSettings(){
        return settings;
    }

    /**
     * Lookup the WorldTypeNode based on a provided UUID
     *
     * @param id the UUID to lookup
     * @return The node with the provided UUID if found
     */
    public Optional<WorldTypeNode> lookupUUID(UUID id){
        for(WorldTypeNode n : settings.getWorldList()){
            if(((UuidTypeNode)n.getField(WorldTypeNode.Fields.UUID)).getValue().equals(id)){
                return(Optional.of(n));
            }
        }
        return Optional.empty();
    }

    public void writeToDisk(){
        byte[] tmp;

        if(settings == null){
            Log.error("Settings not generated yet");
        }
        tmp= Huffstruct.dumpData(settings);
        try {
            Files.write(location.toPath(),tmp);
            currentData=tmp;
            restoreSettings();
        } catch (IOException e) {
            Log.error("Unable to save configuration: " + e);
        }
    }
}
