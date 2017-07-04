package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import com.ezrol.terry.lib.huffstruct.StructNode;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Simple representation of a utf8 string in a struct node
 * (pass the original node into the constructure and substitute with this one)
 *
 * Created by ezterry on 7/2/17.
 */
public class StringTypeNode extends StructNode {
    private String value;
    Charset utf8;

    public StringTypeNode(StructNode base,String def){
        utf8=Charset.forName("UTF-8");

        if(base == null || base.getBinaryString() == null){
            value=def;
        } else {
            value = new String(base.getBinaryString(),utf8);
        }
    }

    @Override
    public List<StructNode> getArray() {
        return null;
    }

    @Override
    public byte[] getBinaryString() {
        return value.getBytes(utf8);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
