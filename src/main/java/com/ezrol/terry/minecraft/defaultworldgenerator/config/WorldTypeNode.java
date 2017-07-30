package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import com.ezrol.terry.lib.huffstruct.StructNode;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static com.ezrol.terry.minecraft.defaultworldgenerator.config.QuadStateTypeNode.States.STATE_DISABLED;
import static com.ezrol.terry.minecraft.defaultworldgenerator.config.QuadStateTypeNode.States.STATE_ENABLED;

/**
 * Holds a static dictionary associated array in the struct node for each World Type configuration
 * The structure is defined in the fields section of the class
 *
 * Created by ezterry on 7/2/17.
 */
public class WorldTypeNode extends StructNode {
    public enum Fields{
        WORLD_GENERATOR ("worldGenerator",a -> new StringTypeNode(a,"default")),
        UUID ("uuid", UuidTypeNode::new),
        SEED ("seed",a -> new StringTypeNode(a,"")),
        CUSTOMIZATION_STRING("customizationString", a -> new StringTypeNode(a,"")),
        CONFIGURATION_NAME ("configurationName",a -> new StringTypeNode(a,"Modded World")),
        CONFIGURATION_DESC ("configurationDesc",a -> new StringTypeNode(a,"A Generic World")),
        CONFIGURATION_IMAGE ("configurationImage",a -> new StringTypeNode(a,"")),
        LOCK_WORLD_TYPE ("lockWorldType",a -> new BooleanTypeNode(a,false)),
        SHOW_IN_LIST ("showInList",a -> new BooleanTypeNode(a,false)),
        BONUS_CHEST_STATE ("bonusChestState",a -> new QuadStateTypeNode(a, STATE_DISABLED)),
        STRUCTURE_STATE ("structureState",a -> new QuadStateTypeNode(a, STATE_ENABLED)),
        WORLD_LOAD_COMMANDS ("worldLoadCommands",a -> new StringListTypeNode(a,new String[] {})),
        DATA_PACKS ("dataPacks",a -> new StringListTypeNode(a,new String[] {"common"}));

        private final String dictName;
        private final Function<StructNode,StructNode> init;

        Fields(String nm, Function<StructNode,StructNode> def){
            dictName=nm;
            init=def;
        }
        public String getKey(){
            return dictName;
        }
    }

    private HashMap<Fields,StructNode> table;
    private Charset utf8;

    public WorldTypeNode(StructNode base){
        HashMap<String,StructNode> loaded=new HashMap<>();
        utf8= Charset.forName("UTF-8");

        if(base != null && base.getArray() != null){
            for(StructNode n : base.getArray()){
                if(n.getArray() != null && n.getArray().size() >= 2) {
                    String key = new String(n.getArray().get(0).getBinaryString(), utf8);
                    StructNode value = n.getArray().get(1);
                    if(value == null){
                        continue;
                    }
                    loaded.put(key,value);
                }
            }
        }

        table = new HashMap<>();
        for(Fields f : Fields.values()){
            StructNode current=null;
            if(loaded.containsKey(f.getKey())){
                current=loaded.get(f.getKey());
            }
            table.put(f,f.init.apply(current));
        }
        //Log.info(table.toString());
    }

    @Override
    public List<StructNode> getArray() {
        List<StructNode> map;
        List<StructNode> pair;

        map = new ArrayList<>();
        for(Fields f : Fields.values()) {
            pair=new ArrayList<>();
            pair.add(StructNode.newBinaryString(f.getKey().getBytes(utf8)));
            pair.add(getField(f));
            map.add(StructNode.newArray(pair));
        }
        return(map);
    }

    @Override
    public byte[] getBinaryString() {
        return null;
    }

    public <T extends StructNode> T getField(Fields f){
        return((T)table.get(f));
    }
}
