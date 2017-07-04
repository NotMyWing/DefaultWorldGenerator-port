package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import com.ezrol.terry.lib.huffstruct.Huffstruct;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigurationFile {
    private byte[] currentData;
    private File location;
    SettingsRoot settings;

    public ConfigurationFile(File loc){
        location=loc;
        settings=null;
    }

    /**
     * Restore settings to the data last in the file
     */
    public void restoreSettings(){
        if(currentData == null) {
            settings = new SettingsRoot(null);
        }
        else{
            try{
                settings = new SettingsRoot(Huffstruct.loadData(currentData));
            } catch(Exception e){
                Log.error("Error parsing config: " + e);
                Log.info("using internal configuration");
                settings = new SettingsRoot(null);
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
                Log.info("Config file not found, using internal defaults (" + location.toString() + ")");
            }
        } catch (IOException e) {
            Log.error("Unable to read file: " + location.toString());
            Log.error("Assuming config file is blank");
        }
        restoreSettings();
    }

    /**
     * Get the current Settings structure
     */
    public SettingsRoot getSettings(){
        return settings;
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
