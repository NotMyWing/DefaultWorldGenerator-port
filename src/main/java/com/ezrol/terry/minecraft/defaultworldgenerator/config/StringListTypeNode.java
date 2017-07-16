package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import com.ezrol.terry.lib.huffstruct.StructNode;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple representation of a utf8 string in a struct node
 * (pass the original node into the constructure and substitute with this one)
 *
 * Created by ezterry on 7/2/17.
 */
public class StringListTypeNode extends StructNode {
    private List<String> value;
    private Charset utf8;

    public StringListTypeNode(StructNode base, String[] def){
        utf8=Charset.forName("UTF-8");

        if(base == null || base.getArray() == null){
            value=new ArrayList<>(Arrays.asList(def));
        } else {
            value = new ArrayList<>();
            for(StructNode n : base.getArray()){
                if(n.getBinaryString() == null){
                    break;
                }
                value.add(new String(n.getBinaryString(),utf8));
            }
        }
    }

    @Override
    public List<StructNode> getArray() {
        List<StructNode> ret = new ArrayList<>();

        for(String s : value){
            ret.add(StructNode.newBinaryString(s.getBytes(utf8)));
        }
        return ret;
    }

    @Override
    public byte[] getBinaryString() {
        return null;
    }

    public List<String> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StringListTypeNode{" +
                "value=" + value +
                '}';
    }
}
