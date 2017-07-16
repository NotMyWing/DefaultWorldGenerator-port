package com.ezrol.terry.minecraft.defaultworldgenerator.datapack;

import com.ezrol.terry.lib.huffstruct.StructNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringListTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.UuidTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;

import java.util.*;

/**
 * Gets the data in the world save to link up UUID and world changes
 *
 * Created by ezterry on 7/13/17.
 */
public class RuntimeSettings extends StructNode {
    private UuidTypeNode saveUUID;
    HashMap<String,String> savedFiles;

    public RuntimeSettings(StructNode base){
        List<StructNode> data=base == null ? null : base.getArray();

        savedFiles = new HashMap<>();
        //read the UUID in the first field
        if(data == null || data.size() == 0){
            //no UUID found, set to the default UUID
            saveUUID = DefaultWorldGenerator.modConfig.getSettings()
                    .getWorldList().get(0).getField(WorldTypeNode.Fields.UUID);
            //this will copy it so if we update we do not impact settings.
            saveUUID = new UuidTypeNode(saveUUID);
            return;
        }
        if(data.size()>=1){
            saveUUID = new UuidTypeNode(data.get(0));
        }
        if(data.size()>=2){
            List<StructNode> fileList = data.get(1).getArray();
            if(fileList != null){
                for(StructNode n : fileList){
                    StringListTypeNode elements = new StringListTypeNode(n,new String[] {});
                    if(elements.getValue().size()==2){
                        savedFiles.put(elements.getValue().get(0), elements.getValue().get(1));
                    }
                }
            }
        }
    }

    public UUID getSaveUUID(){
        return saveUUID.getValue();
    }

    public void setSaveUUID(UUID id){
        saveUUID.setValue(id);
    }

    public String getCachedChecksum(String fileName){
        return savedFiles.getOrDefault(fileName, "");
    }

    public void updateChecksum(String fileName,String checksum){
        if(checksum.equals("")){
            //remove the key
            if(savedFiles.containsKey(fileName)){
                savedFiles.remove(fileName);
            }
        }
        else{
            savedFiles.put(fileName,checksum);
        }
    }

    /**
     * Returns a map of the elements that exist in the checksum list but not your provided source
     *
     * @param source Map where keys represent to be cached files. (value is unused)
     * @return list of cachedChecksums not provided by your map
     */
    public <T> List<String> findMissing(Map<String,T> source){
        List<String> rval = new ArrayList<>();

        for(String key : savedFiles.keySet()){
            if(!source.containsKey(key)){
                rval.add(key);
            }
        }
        return rval;
    }

    @Override
    public List<StructNode> getArray() {
        List<StructNode> data=new LinkedList<>();
        List<StructNode> fileList=new LinkedList<>();

        data.add(saveUUID);

        for(String fileName : savedFiles.keySet()){
            fileList.add(new StringListTypeNode(null,new String[] {fileName,savedFiles.get(fileName)}));
        }
        data.add(StructNode.newArray(fileList));
        return(data);
    }

    @Override
    public byte[] getBinaryString() {
        return null;
    }
}

