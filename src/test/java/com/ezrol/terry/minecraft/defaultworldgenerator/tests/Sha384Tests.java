package com.ezrol.terry.minecraft.defaultworldgenerator.tests;

import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Sha384;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Some simple test cases to ensure the sha-384 wrapper works
 * Created by ezterry on 7/15/17.
 */
public class Sha384Tests {
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");


    @Test (testName = "Single Checksum")
    public void singleChecksum(){
        Sha384 checksum = new Sha384("HelloWorld".getBytes(UTF8_CHARSET));

        Assert.assertEquals(checksum.getHexDigest(),
                "293cd96eb25228a6fb09bfa86b9148ab69940e68903cbc0527a4fb150eec1ebe0f1ffce0bc5e3df312377e0a68f1950a",
                "Checksum of \"HelloWorld\"");
    }

    @Test (testName = "Multi Part Message")
    public void multiPartMessage(){
        Sha384 checksum = new Sha384();

        checksum.appendToMessage("What if you ".getBytes(UTF8_CHARSET));
        checksum.appendToMessage("change a file?".getBytes(UTF8_CHARSET));

        Assert.assertEquals(checksum.getHexDigest(),
                "a2072b651650d2e22e4569f68fd83e9280c097c3dd2bd53f3083681f2250dbd0c1bf912e9ef1a9a0d3f928f20d94e1be",
                "Multi Part Checksum");
    }

    @Test (testName = "Run Multiple Sums At Once")
    public void runMultipleSumsAtOnce(){
        Sha384 checksuma = new Sha384();
        Sha384 checksumb = new Sha384();

        checksuma.appendToMessage("A - part 1|".getBytes(UTF8_CHARSET));
        checksumb.appendToMessage("B - part 1|".getBytes(UTF8_CHARSET));
        checksuma.appendToMessage("A - part 2".getBytes(UTF8_CHARSET));
        checksumb.appendToMessage("B - part 2".getBytes(UTF8_CHARSET));

        Assert.assertEquals(checksuma.getHexDigest(),
                "420238bb0f7b84842b9286553e9bdf6a87dc9bfddf85071a8bbf65406475f105eb9798f7d617c996303fd5675c0dcd3b",
                "Checksum A");
        Assert.assertEquals(checksumb.getHexDigest(),
                "9a148fb8bbf6f670e7669438146d46cf0cc9d6791a5b733afc37db426a2a854591c0cba61cf4167f91fc370a6c55c480",
                "Checksum B");
    }

    @Test (testName = "partial String Append")
    public void partialStringAppend(){
        Sha384 check = new Sha384();

        check.appendToMessage("Check this! Not this!".getBytes(UTF8_CHARSET),11);

        Assert.assertEquals(check.getHexDigest(),
                "f68bb8eeff02dc6fba298646a02e9e255039995048bb678942d845b00c71785a1d982bfa09a5901946e09e47ae1e7c4b",
                "Check of first part of byte[]");
    }

    @Test (testName = "small File")
    public void smallFile() throws IOException {
        File in = File.createTempFile("smalldata",".blob");

        FileOutputStream fp = new FileOutputStream(in);
        fp.write("A Small\nFile".getBytes(UTF8_CHARSET));
        fp.close();

        Sha384 check = new Sha384(in);
        Assert.assertEquals(check.getHexDigest(),
                "96ae7ca722e1ee0b78c50f890ada37d161e20467198be42cf01e07bfce90e5886dad8a7b69d779b0129eeef08419d98f",
                "Checking a small file is read correctly");

        in.deleteOnExit();
    }

    @Test (testName = "medium File")
    public void mediumFile() throws IOException {
        File in = File.createTempFile("meddata",".blob");

        FileOutputStream fp = new FileOutputStream(in);
        byte[] base = "abcdefghijklmnopqrstuvwxyz".getBytes(UTF8_CHARSET);
        for(int i=0;i<250;i++) {
            fp.write(base);
        }
        fp.close();

        Sha384 check = new Sha384(in);
        Assert.assertEquals(check.getHexDigest(),
                "5ccb90571b88cd2b9277682bd08cc327f46b442319b8d096af6a77fbd8f3f8079b53f77172ac883225e9abfd5361f522",
                "Checking a medium file is read correctly");

        in.deleteOnExit();
    }

    @Test (testName = "large File")
    public void largeFile() throws IOException {
        File in = File.createTempFile("largedata",".blob");

        FileOutputStream fp = new FileOutputStream(in);
        byte[] base = "abcdefghijklmnopqrstuvwxyz0123456789~".getBytes(UTF8_CHARSET);
        for(int i=0;i<2048;i++) {
            fp.write(base);
        }
        fp.close();

        Sha384 check = new Sha384(in);
        Assert.assertEquals(check.getHexDigest(),
                "f28df495cfdf52653638b3f34ec30c10ec6bc14c9c58b43095087635d81ae0342cd2e1b0e2828d1133aa3c7f0ebdf7f6",
                "Checking a large file is read correctly");

        in.deleteOnExit();
    }
}
