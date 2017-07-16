package com.ezrol.terry.minecraft.defaultworldgenerator.lib;

import java.io.*;
import java.nio.file.ProviderNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Small wrapper of java's Message Digest for SHA-384 to get a simple hex digest
 *
 * Created by ezterry on 7/14/17.
 */
public class Sha384 {
    private static MessageDigest digest;
    private MessageDigest local;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-384");
        } catch (NoSuchAlgorithmException e) {
            digest = null;
        }
    }

    public Sha384(){
        if(digest == null){
            throw new ProviderNotFoundException("Your version of java is missing SHA-384 support (required)");
        }
        try {
            local = (MessageDigest) digest.clone();
            local.reset();
        } catch (CloneNotSupportedException e) {
            Log.fatal(e);
            throw new ProviderNotFoundException("could not clone instance of SHA-384, this is unexpected");
        }

    }

    public Sha384(byte[] initialData){
        this();
        appendToMessage(initialData);
    }

    public Sha384(File file){
        this();

        long size = file.length();
        long block_size;

        //get a sane block (read) size
        if(size == 0L){
            //size unknown/or empty read in 0.5k blocks
            block_size=512L;
        }
        else if(size > 10240){
            //large-ish file read in 4k blocks
            block_size=4096L;
        }
        else{
            //small-ish file read in one go
            block_size=size;
        }

        byte block[]=new byte[(int)block_size];
        //open file for reading
        try {
            InputStream f = new FileInputStream(file);
            while(block_size > 0) {
                block_size=f.read(block);
                if(block.length == block_size){
                    appendToMessage(block);
                }
                else if(block_size < 0){
                    break;
                }
                else{
                    appendToMessage(block,(int)block_size);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void appendToMessage(byte[] b){
        local.update(b);
    }

    public void appendToMessage(byte[] b,int size){
        local.update(b,0,size);
    }

    public String getHexDigest(){
        byte[] digest = local.digest();
        StringBuilder rval=new StringBuilder();

        for(byte b : digest){
            //high 4 bits
            char high  = (char)((b & 0xF0) >> 4);
            //low 4 bits
            char low = (char)(b & 0x0F);

            //convert to hex
            high = (char) ((high < 10)?('0'+high):(high-10+'a'));
            low = (char) ((low < 10)?('0'+low):(low-10+'a'));

            //add to string
            rval.append(high);
            rval.append(low);
        }

        return rval.toString();
    }
}
