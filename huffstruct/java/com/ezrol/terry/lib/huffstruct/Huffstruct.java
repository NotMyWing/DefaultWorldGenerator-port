package com.ezrol.terry.lib.huffstruct;

import java.util.LinkedList;
import java.util.List;

/**
 * Huffman Structure Reader/Writer
 *
 * Using a static huffman tree definition in Tree.java this will read or write a binary structure
 * and convert it to a tree of StructNodes
 *
 * To Read call:
 * static StructNode loadData(byte[])
 *
 * Wo Write call:
 * static byte[] dumpData(StructNode root)
 *
 * Created by ezterry on 7/2/17.
 */
@SuppressWarnings("unused")
public class Huffstruct {
    //when reading and writing these keep track of out local buffer
    //note an instance of the class ought only be used for reading or writing not both.
    private byte[] buffer;
    private Byte current;
    private int position; //position in byte array
    private int curbit;   //bit position in byte
    private static Tree tree=Tree.getInstance();

    private Huffstruct(byte[] buffer){
        this.buffer=buffer;
        this.current=null;
        this.position=0;
        this.curbit=0;
    }

    /**
     * when writing this adds the partial byte into the byte array extending as needed
     */
    private void flushByte(){
        if(current==null){
            return;
        }
        if(buffer.length < (position+1)){
            byte[] tmpBuff=new byte[position+513];
            System.arraycopy(buffer,0,tmpBuff,0,buffer.length);
            buffer=tmpBuff;
        }
        buffer[position]=current;
        position++;
        current=null;
    }

    /**
     * Write a bit to the buffer
     * @param value the bit value
     */
    private void writeBit(boolean value){
        if(current == null){
            current=(byte)0;
            curbit = 0;
        }
        if(value){
            current = (byte)(current | (1 << (7-curbit)));
        }
        curbit++;
        if(curbit >= 8){
            flushByte();
        }
    }

    /**
     * Write a code (byte value 0-255, or one of the special codes
     * (Tree.CODE_START_ARRAY, Tree.CODE_START_STRING, or CODE_END_ELEMENT)
     *
     * @param code code to write into the bitstream
     */
    private void writeCode(int code){
        String prefix = tree.getPrefix(code);
        if(prefix == null){
            throw(new IndexOutOfBoundsException("Bad code provided: " + code));
        }
        for(char c : prefix.toCharArray()){
            writeBit(c=='1');
        }
    }

    /**
     * Take the value/tree in root and create the serialized raw byte[] data
     *
     * @param root root node to convert to a raw serialized string
     * @return the raw serialized byte[]
     */
    public static byte[] dumpData(StructNode root){

        Huffstruct processor = new Huffstruct(new byte[512]);
        LinkedList<StructNode> stack = new LinkedList<>();

        stack.addFirst(root);

        while(!stack.isEmpty()){
            StructNode node = stack.pop();
            if(node == null){
                //end of an array found
                processor.writeCode(Tree.CODE_END_ELEMENT);
                continue;
            }
            byte[] data = node.getBinaryString();
            if(data != null){
                //binary string found
                processor.writeCode(Tree.CODE_START_STRING);
                for(byte b : data){
                    processor.writeCode(b & 0x00ff);
                }
                processor.writeCode(Tree.CODE_END_ELEMENT);
                continue;
            }
            List<StructNode> array = node.getArray();
            processor.writeCode(Tree.CODE_START_ARRAY);
            stack.addFirst(null);
            for(int i = array.size()-1;i>=0;i--){
                if(array.get(i) == null){
                    throw(new NullPointerException("Cant serialize null (expected StructNode)"));
                }
                stack.addFirst(array.get(i));
            }
        }

        processor.flushByte();
        byte[] tmpBuff=new byte[processor.position];
        System.arraycopy(processor.buffer,0,tmpBuff,0,processor.position);
        return(tmpBuff);
    }

    /**
     * Read bit from the data stream
     * @return the next bit in the sequence
     */

    private boolean readBit(){
        if(current == null){
            current=buffer[position];
            position++;
            curbit=0;
        }
        byte bit=(byte)(((current << curbit) & 0xFF )>>7);
        curbit++;
        if(curbit>=8){
            current=null;
        }
        return(bit == (byte)1);
    }

    /**
     * read in a code from the bitstream
     *
     * @return the next code in the bitstream
     */
    private int readCode(){
        String prefix="";
        int code;

        while(true) {
            prefix += readBit()?"1":"0";
            code = tree.getCode(prefix);
            if (code != -1) {
                return (code);
            }
            if (prefix.length() > 16) {
                //this ought to be impossible unless you edited the huffman tree by hand
                throw (new IndexOutOfBoundsException("Unable to find code for prefix: " + prefix));
            }
        }
    }

    /**
     * Given the raw byte data, load in the structure
     *
     * @param dat raw HuffStruct encoded data
     * @return The root node of the structure
     */
    public static StructNode loadData(byte[] dat){
        Huffstruct processor = new Huffstruct(dat);
        LinkedList<StructNode> stack = new LinkedList<>();
        boolean inString=false;
        LinkedList<Byte> current= new LinkedList<>();
        int code;

        stack.addFirst(StructNode.newArray());

        do{
            code = processor.readCode();
            if(inString){
                if(code == Tree.CODE_END_ELEMENT){
                    inString=false;
                    byte[] ar = new byte[current.size()];
                    for(int i=0;current.size()>0;i++){
                        ar[i]=current.removeFirst();
                    }
                    stack.peekFirst().getArray().add(StructNode.newBinaryString(ar));
                }
                else{
                    if(code >= 256){
                        throw(new IllegalStateException("Unexpected code in byte string: " + Integer.toString(code)));
                    }
                    current.addLast((byte)code);
                }
            }
            else if(code == Tree.CODE_START_STRING){
                inString=true;
            }
            else if(code == Tree.CODE_START_ARRAY){
                stack.addFirst(StructNode.newArray());
            }
            else if(code == Tree.CODE_END_ELEMENT){
                if(stack.size()<2){
                    throw(new IllegalStateException("Unexpected Extra Array Termination"));
                }
                StructNode completed = stack.pop();
                stack.peekFirst().getArray().add(completed);
            }
            else{
                throw(new IllegalStateException("Unexpected Control Code: " + Integer.toString(code)));
            }
        } while(inString || stack.size() != 1);
        if(stack.size() == 0){
            throw(new IllegalStateException("Structure has no elements!"));
        }
        return(stack.getFirst().getArray().get(0));
    }
}
