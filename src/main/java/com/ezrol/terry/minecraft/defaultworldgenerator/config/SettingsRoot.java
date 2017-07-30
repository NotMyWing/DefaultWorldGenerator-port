package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import com.ezrol.terry.lib.huffstruct.StructNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Track the root of the application settings (given the config data file struct node)
 * Created by ezterry on 7/2/17.
 */
public class SettingsRoot extends StructNode {
    private ArrayList<WorldTypeNode> worlds;

    @SuppressWarnings("WeakerAccess")
    public SettingsRoot(StructNode origRoot){
        if(origRoot == null || origRoot.getArray() == null){
            worlds=new ArrayList<>();
            worlds.add(new WorldTypeNode(null));
        }
        else{
            worlds=new ArrayList<>();
            for(StructNode n : origRoot.getArray()){
                worlds.add(new WorldTypeNode(n));
            }
        }
    }

    public List<WorldTypeNode> getWorldList() {
        return worlds;
    }

    @Override
    public List<StructNode> getArray() {
        return(worlds.stream().map(a -> (StructNode)a).collect(Collectors.toList()));
    }

    @Override
    public byte[] getBinaryString() {
        return null;
    }
}
