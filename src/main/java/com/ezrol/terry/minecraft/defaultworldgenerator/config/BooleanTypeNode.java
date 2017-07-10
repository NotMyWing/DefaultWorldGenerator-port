package com.ezrol.terry.minecraft.defaultworldgenerator.config;
import com.ezrol.terry.lib.huffstruct.StructNode;

import java.util.Arrays;
import java.util.List;

/**
 * Simple representation of a boolean struct node (simply the byte 't' for true and 'f' for false)
 * (pass the original node into the constructure and substitute with this one)
 *
 * Created by ezterry on 7/2/17.
 */
public class BooleanTypeNode extends StructNode {
    private boolean value;
    private static byte[] TRUE_BYTE = new byte[] { 't' };
    private static byte[] FALSE_BYTE = new byte[] { 'f' };

    public BooleanTypeNode(StructNode base,Boolean def){

        if(base == null || base.getBinaryString() == null){
            value=def;
        } else {
            byte[] data = base.getBinaryString();
            if(Arrays.equals(data, TRUE_BYTE)){
                value=true;
            }
            else if(Arrays.equals(data, FALSE_BYTE)){
                value=false;
            }
            else{
                value=def;
            }
        }
    }

    @Override
    public List<StructNode> getArray() {
        return null;
    }

    @Override
    public byte[] getBinaryString() {
        return value ? TRUE_BYTE : FALSE_BYTE;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BooleanTypeNode{" +
                "value=" + value +
                '}';
    }
}
