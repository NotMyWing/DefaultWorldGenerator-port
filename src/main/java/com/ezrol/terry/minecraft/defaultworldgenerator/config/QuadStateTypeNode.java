package com.ezrol.terry.minecraft.defaultworldgenerator.config;
import com.ezrol.terry.lib.huffstruct.StructNode;

import java.util.Arrays;
import java.util.List;

/**
 * Represntation for quad states
 * (pass the original node into the constructure and substitute with this one)
 *
 * Created by ezterry on 7/2/17.
 */
public class QuadStateTypeNode extends StructNode {
    public enum States {
        STATE_DISABLED ((byte)'d'),
        STATE_ENABLED ((byte)'e'),
        STATE_BLOCKED ((byte)'b'),
        STATE_FORCED ((byte)'f');

        private final byte value;

        States(byte b){
            value = b;
        }

        public byte[] getBytes(){
            return(new byte[] {value});
        }
    }

    private States value;

    @SuppressWarnings("WeakerAccess")
    public QuadStateTypeNode(StructNode base, States def){
        value=def;
        if(base != null && base.getBinaryString() != null){
            byte[] data = base.getBinaryString();
            for(States test: States.values()){
                if(Arrays.equals(test.getBytes(),data)){
                    value=test;
                }
            }
        }
    }

    @Override
    public List<StructNode> getArray() {
        return null;
    }

    @Override
    public byte[] getBinaryString() {
        return value.getBytes();
    }

    public States getValue() {
        return value;
    }

    public void setValue(States value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "QuadStateTypeNode{" +
                "value=" + value +
                '}';
    }
}
