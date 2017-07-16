package com.ezrol.terry.minecraft.defaultworldgenerator.datapack;

import com.ezrol.terry.lib.huffstruct.Huffstruct;
import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigurationFile;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringListTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.UuidTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Reference;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Sha384;
import net.minecraft.world.storage.ISaveHandler;
import sun.security.provider.ConfigFile;

import javax.management.RuntimeErrorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Created by ezterry on 7/15/17.
 */
public class WorldWrapper {

    private File runtimeSettingFile;
    private RuntimeSettings settings=null;
    private WorldTypeNode worldConfig;

    public WorldWrapper(ISaveHandler worldSave) {
        runtimeSettingFile = new File(worldSave.getWorldDirectory(), Reference.WORLD_DATA_FILE);
        internalInit();
    }

    public WorldWrapper(File worldDirectory) {
        runtimeSettingFile = new File(worldDirectory, Reference.WORLD_DATA_FILE);
        internalInit();
    }

    private void internalInit(){
        if(runtimeSettingFile.exists()){
            //load it in
            byte[] currentData;
            try {
                currentData = Files.readAllBytes(runtimeSettingFile.toPath());
                settings = new RuntimeSettings(Huffstruct.loadData(currentData));
            } catch (IOException e) {
                Log.error("Error reading in runtime settings: " + runtimeSettingFile.toString());
            }
        }
        if(settings == null){
            Log.info("Generating new runtime file for the world");
            settings = new RuntimeSettings(null);
            if(DefaultWorldGenerator.selectedLevel != null) {
                settings.setSaveUUID(((UuidTypeNode) DefaultWorldGenerator
                        .selectedLevel.getField(WorldTypeNode.Fields.UUID)).getValue());
            }
        }

        //based on UUID lookup the world type
        worldConfig = DefaultWorldGenerator.modConfig.lookupUUID(settings.getSaveUUID()).orElse(
                DefaultWorldGenerator.modConfig.getSettings().getWorldList().get(0)
        );

        //if the save file UUID is not the current one update it
        UuidTypeNode worldUUID = worldConfig.getField(WorldTypeNode.Fields.UUID);
        if(worldUUID.getValue() != settings.getSaveUUID()){
            settings.setSaveUUID(worldUUID.getValue());
        }

        //just in case we no longer want to use the previously selected level
        DefaultWorldGenerator.selectedLevel=null;
    }


    public WorldTypeNode getWorldType(){
        return worldConfig;
    }

    public void saveWorldData(){
        byte[] tmp;

        tmp= Huffstruct.dumpData(settings);
        try {
            Files.write(runtimeSettingFile.toPath(),tmp);
        } catch (IOException e) {
            Log.error(String.format("Error writing file %s: %s",runtimeSettingFile.getName(),e.toString()));
        }
    }

    /**
     * Load a map of all the files in a data pack
     *
     * @param name name of the data pack
     * @return Map of the files
     */
    private Map<String,File> dataPackList(String name){
        Map<String,File> rval = new HashMap<>();
        LinkedList<File> cwd=new LinkedList<>();
        File basePath;

        if(ConfigurationFile.safeFileName(name)){
            //we only load safe directory names
            basePath=new File(DefaultWorldGenerator.modSettingsDir,name);
            cwd.addFirst(basePath);

            while(cwd.size() > 0){
                File dir = cwd.pop();
                String [] dirlist = dir.list();
                if(dirlist == null || dirlist.length == 0){
                    //nothing to do
                    continue;
                }
                for(String el :  dirlist){
                    File file = new File(dir,el);
                    if(file.getName().equals(".") || file.getName().equals("..")){
                        //this is not supposed to be listed but... skip in case
                        continue;
                    }
                    else if(file.isDirectory()){
                        //its a directory, add it to the list to scan
                        cwd.addLast(file);
                    }
                    else if(file.isFile()){
                        //we seem to have a file, map it
                        if(file.getAbsolutePath().startsWith(basePath.getAbsolutePath())){
                            String w = file.getAbsolutePath().substring(basePath.getAbsolutePath().length());
                            if(w.startsWith("/")){
                                w=w.substring(1);
                            }
                            rval.put(w,file);
                        }
                        else{
                            throw(new RuntimeException("child path not in base path (" + file.getAbsolutePath() +
                                    " under " + basePath.getAbsolutePath()));
                        }
                    }
                }
            }
        }
        return rval;
    }

    /**
     * In some cases a pack maker may have some of these files, we can inject them if they don't already exist
     * however we must not overwrite the local copy
     *
     * @param filename filename to check
     * @return true if a modpack can overwrite this file, else false
     */
    @SuppressWarnings("RedundantIfStatement")
    private boolean canOverwrite(File filename){
        //files not to overwrite
        if(filename.getName().endsWith(".mca") ||
                filename.getName().endsWith(".dat") ||
                filename.getName().endsWith(".lock") ||
                filename.getName().endsWith("defaultworldgenerator-port.data")){
            return false;
        }
        //stats files not to overwrite
        if(filename.getParentFile().getName().endsWith("stats") ||
                filename.getParentFile().getName().endsWith("playerdata") ||
                filename.getParentFile().getName().endsWith("advancements")) {
            return false;
        }
        return true;
    }

    /**
     * Import the data packs (initially or update)
     */
    public void importData(){
        //import data files into the world save
        boolean modified=false;

        Map<String,File> fullList = new HashMap<>();
        for(String dataPack : ((StringListTypeNode)worldConfig.getField(WorldTypeNode.Fields.DATA_PACKS)).getValue()){
            fullList.putAll(dataPackList(dataPack));
        }
        List<String> missing = settings.findMissing(fullList);

        //if applicable remove the missing files
        for(String m : missing){
            File target = new File(runtimeSettingFile.toPath().getParent().toFile(),m);
            if(target.exists() && canOverwrite(target)){
                //target can be deleted
                Log.warn("Deleting " + target.getAbsolutePath());
                Log.warn("Data Pack File removed from upstream datapack, deleting local copy");
                if(!target.delete()){
                    Log.error("Unable to delete datapack file");
                } else{
                    modified=true;
                }
            }
        }
        for(String p : fullList.keySet()){
            //First see if the file exists in the target directory
            File target = new File(runtimeSettingFile.toPath().getParent().toFile(),p);

            Sha384 currentChecksum = new Sha384(fullList.get(p));

            if(target.exists()){
                //see about updating target
                if(canOverwrite(target)){
                    //ok hopefully its safe to update this file
                    if(!settings.getCachedChecksum(p).equals(currentChecksum.getHexDigest())){
                        //the source file has changed overwrite
                        try {
                            Log.warn("Overwriting " + target.getAbsolutePath());

                            Files.copy(fullList.get(p).toPath(),target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            settings.updateChecksum(p,currentChecksum.getHexDigest());
                            modified=true;
                        } catch (IOException e) {
                            Log.error("Unable to install file: " + p);
                            Log.error(e);
                        }
                    }
                }
            }
            else{
                //see about creating target
                //noinspection ResultOfMethodCallIgnored
                target.getParentFile().mkdirs();

                try {
                    Log.info("Installing " + target.getAbsolutePath());
                    Files.copy(fullList.get(p).toPath(),target.toPath());
                    settings.updateChecksum(p,currentChecksum.getHexDigest());
                } catch (IOException e) {
                    Log.error("Unable to install file: " + p);
                    Log.error(e);
                }
            }
        }

        if(modified){
            Log.warn("*********************************************************");
            Log.warn("data pack files updated, if you manually changed a");
            Log.warn("file used by this modpack, you will need to re-install");
            Log.warn("such changes by hand from backup");
            Log.warn("*********************************************************");
        }
    }
}
