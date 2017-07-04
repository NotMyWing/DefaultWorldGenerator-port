package com.ezrol.terry.minecraft.defaultworldgenerator.config;

import com.ezrol.terry.lib.huffstruct.StructNode;
import org.apache.logging.log4j.core.util.UuidUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.UUID;

import static java.nio.ByteBuffer.wrap;

/**
 * Simple representation of a utf8 string in a struct node
 * (pass the original node into the constructure and substitute with this one)
 *
 * Created by ezterry on 7/2/17.
 */
public class UuidTypeNode extends StructNode {
    private UUID value;

    public UuidTypeNode(StructNode base){
        if(base == null || base.getBinaryString() == null){
            value=UuidUtil.getTimeBasedUuid();
        } else {
            value = extractUUID(base.getBinaryString());
            if(value == null){
                value=UuidUtil.getTimeBasedUuid();
            }
        }
    }

    private UUID extractUUID(byte[] data){
        if(data.length != 16){
            return null;
        }
        ByteBuffer buf = wrap(data);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        long low = buf.getLong();
        long high = buf.getLong();
        return(new UUID(high,low));
    }

    @Override
    public List<StructNode> getArray() {
        return null;
    }

    @Override
    public byte[] getBinaryString() {
        ByteBuffer buf = wrap(new byte[16]);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putLong(value.getLeastSignificantBits());
        buf.putLong(value.getMostSignificantBits());
        return(buf.array());
    }

    public UUID getValue() {
        return value;
    }

    public void setValue(UUID value) {
        this.value = value;
    }
}
