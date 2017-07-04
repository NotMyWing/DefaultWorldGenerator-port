package com.ezrol.terry.lib.huffstruct;

import java.util.HashMap;

/**
 * The static huffman Tree used by Huffstruct
 * 
 * Created by ezterry on 7/1/17.
 */
@SuppressWarnings("WeakerAccess")
class Tree {
    private HashMap<String, Integer> codeTable;
    private HashMap<Integer, String> prefixTable;
    private static Tree instance;

    protected static final int CODE_START_ARRAY=500;
    protected static final int CODE_START_STRING=501;
    protected static final int CODE_END_ELEMENT=502;

    private Tree() {
        codeTable = new HashMap<>();
        prefixTable = new HashMap<>();

        addcode("00000", 99);
        addcode("00001", 117);
        addcode("0001", 97);
        addcode("0010", 110);
        addcode("0011", 105);
        addcode("01000", 100);
        addcode("010010", 119);
        addcode("010011000", 67);
        addcode("010011001", 95);
        addcode("0100110100", 58);
        addcode("0100110101", 47);
        addcode("01001101100", 51);
        addcode("010011011010", 91);
        addcode("010011011011", 93);
        addcode("0100110111", 87);
        addcode("01001110", 80);
        addcode("01001111", 71);
        addcode("01010", 108);
        addcode("010110", 103);
        addcode("010111", 112);
        addcode("0110", 111);
        addcode("011100000", 49);
        addcode("011100001", 68);
        addcode("0111000100", 42);
        addcode("011100010100", 75);
        addcode("011100010101", 122);
        addcode("0111000101100", 36);
        addcode("0111000101101", 64);
        addcode("0111000101110", 81);
        addcode("0111000101111", 88);
        addcode("01110001100000", 0);
        addcode("01110001100001", 1);
        addcode("01110001100010", 2);
        addcode("01110001100011", 3);
        addcode("01110001100100", 4);
        addcode("01110001100101", 5);
        addcode("01110001100110", 6);
        addcode("01110001100111", 7);
        addcode("01110001101000", 8);
        addcode("01110001101001", 9);
        addcode("01110001101010", 11);
        addcode("01110001101011", 12);
        addcode("01110001101100", 14);
        addcode("01110001101101", 15);
        addcode("01110001101110", 16);
        addcode("01110001101111", 17);
        addcode("01110001110000", 18);
        addcode("01110001110001", 19);
        addcode("01110001110010", 20);
        addcode("01110001110011", 21);
        addcode("01110001110100", 22);
        addcode("01110001110101", 23);
        addcode("01110001110110", 24);
        addcode("01110001110111", 25);
        addcode("01110001111000", 26);
        addcode("01110001111001", 27);
        addcode("01110001111010", 28);
        addcode("01110001111011", 29);
        addcode("01110001111100", 30);
        addcode("01110001111101", 31);
        addcode("01110001111110", 38);
        addcode("01110001111111", 43);
        addcode("01110010000000", 60);
        addcode("01110010000001", 61);
        addcode("01110010000010", 62);
        addcode("01110010000011", 92);
        addcode("01110010000100", 94);
        addcode("01110010000101", 96);
        addcode("01110010000110", 123);
        addcode("01110010000111", 124);
        addcode("01110010001000", 125);
        addcode("01110010001001", 126);
        addcode("01110010001010", 127);
        addcode("01110010001011", 128);
        addcode("01110010001100", 129);
        addcode("01110010001101", 130);
        addcode("01110010001110", 131);
        addcode("01110010001111", 132);
        addcode("01110010010000", 133);
        addcode("01110010010001", 134);
        addcode("01110010010010", 135);
        addcode("01110010010011", 136);
        addcode("01110010010100", 137);
        addcode("01110010010101", 138);
        addcode("01110010010110", 139);
        addcode("01110010010111", 140);
        addcode("01110010011000", 141);
        addcode("01110010011001", 142);
        addcode("01110010011010", 143);
        addcode("01110010011011", 144);
        addcode("01110010011100", 145);
        addcode("01110010011101", 146);
        addcode("01110010011110", 147);
        addcode("01110010011111", 148);
        addcode("01110010100000", 149);
        addcode("01110010100001", 150);
        addcode("01110010100010", 151);
        addcode("01110010100011", 152);
        addcode("01110010100100", 153);
        addcode("01110010100101", 154);
        addcode("01110010100110", 155);
        addcode("01110010100111", 156);
        addcode("01110010101000", 157);
        addcode("01110010101001", 158);
        addcode("01110010101010", 159);
        addcode("01110010101011", 160);
        addcode("01110010101100", 161);
        addcode("01110010101101", 162);
        addcode("01110010101110", 163);
        addcode("01110010101111", 164);
        addcode("01110010110000", 165);
        addcode("01110010110001", 166);
        addcode("01110010110010", 167);
        addcode("01110010110011", 168);
        addcode("01110010110100", 169);
        addcode("01110010110101", 170);
        addcode("01110010110110", 171);
        addcode("01110010110111", 172);
        addcode("01110010111000", 173);
        addcode("01110010111001", 174);
        addcode("01110010111010", 175);
        addcode("01110010111011", 176);
        addcode("01110010111100", 177);
        addcode("01110010111101", 178);
        addcode("01110010111110", 179);
        addcode("01110010111111", 180);
        addcode("01110011000000", 181);
        addcode("01110011000001", 182);
        addcode("01110011000010", 183);
        addcode("01110011000011", 184);
        addcode("01110011000100", 185);
        addcode("01110011000101", 186);
        addcode("01110011000110", 188);
        addcode("01110011000111", 189);
        addcode("01110011001000", 190);
        addcode("01110011001001", 192);
        addcode("01110011001010", 193);
        addcode("01110011001011", 194);
        addcode("01110011001100", 195);
        addcode("01110011001101", 196);
        addcode("01110011001110", 197);
        addcode("01110011001111", 198);
        addcode("01110011010000", 199);
        addcode("01110011010001", 200);
        addcode("01110011010010", 201);
        addcode("01110011010011", 202);
        addcode("01110011010100", 203);
        addcode("01110011010101", 204);
        addcode("01110011010110", 205);
        addcode("01110011010111", 206);
        addcode("01110011011000", 207);
        addcode("01110011011001", 208);
        addcode("01110011011010", 209);
        addcode("01110011011011", 210);
        addcode("01110011011100", 211);
        addcode("01110011011101", 212);
        addcode("01110011011110", 213);
        addcode("01110011011111", 214);
        addcode("01110011100000", 215);
        addcode("01110011100001", 216);
        addcode("01110011100010", 217);
        addcode("01110011100011", 218);
        addcode("01110011100100", 219);
        addcode("01110011100101", 220);
        addcode("01110011100110", 221);
        addcode("01110011100111", 222);
        addcode("01110011101000", 223);
        addcode("01110011101001", 224);
        addcode("01110011101010", 225);
        addcode("01110011101011", 226);
        addcode("01110011101100", 227);
        addcode("01110011101101", 228);
        addcode("01110011101110", 229);
        addcode("01110011101111", 230);
        addcode("01110011110000", 231);
        addcode("01110011110001", 232);
        addcode("01110011110010", 233);
        addcode("01110011110011", 234);
        addcode("01110011110100", 235);
        addcode("01110011110101", 236);
        addcode("01110011110110", 237);
        addcode("01110011110111", 238);
        addcode("01110011111000", 240);
        addcode("01110011111001", 241);
        addcode("01110011111010", 242);
        addcode("01110011111011", 243);
        addcode("01110011111100", 244);
        addcode("01110011111101", 245);
        addcode("01110011111110", 246);
        addcode("01110011111111", 247);
        addcode("011101", 109);
        addcode("01111", 104);
        addcode("100000", 121);
        addcode("100001", 102);
        addcode("10001000", 84);
        addcode("1000100100", 39);
        addcode("1000100101", 59);
        addcode("100010011", 34);
        addcode("10001010", 69);
        addcode("100010110", 76);
        addcode("1000101110", 77);
        addcode("10001011110000", 248);
        addcode("10001011110001", 249);
        addcode("10001011110010", 250);
        addcode("10001011110011", 251);
        addcode("10001011110100", 252);
        addcode("10001011110101", 253);
        addcode("10001011110110", 254);
        addcode("10001011110111", 255);
        addcode("10001011111", 50);
        addcode("100011", 10);
        addcode("1001", 116);
        addcode("101", 32);
        addcode("110000", 13);
        addcode("1100010", 46);
        addcode("1100011", 44);
        addcode("11001", 115);
        addcode("1101", 101);
        addcode("111000000", 70);
        addcode("111000001", 78);
        addcode("11100001", 73);
        addcode("1110001000", 72);
        addcode("11100010010", 53);
        addcode("111000100110", 54);
        addcode("111000100111", 56);
        addcode("111000101", 82);
        addcode("11100011", 45);
        addcode("111001000", 79);
        addcode("11100100100", 48);
        addcode("11100100101", 113);
        addcode("1110010011", 66);
        addcode("111001010", 83);
        addcode("1110010110", 89);
        addcode("1110010111", 120);
        addcode("1110011", 98);
        addcode("11101000", 107);
        addcode("111010010000", 57);
        addcode("111010010001", 52);
        addcode("111010010010", 74);
        addcode("111010010011", 86);
        addcode("1110100101", 85);
        addcode("1110100110000", 33);
        addcode("11101001100010", 35);
        addcode("11101001100011", 37);
        addcode("11101001100100", 90);
        addcode("11101001100101", 187);
        addcode("11101001100110", 191);
        addcode("11101001100111", 239);
        addcode("11101001101", 40);
        addcode("11101001110", 41);
        addcode("111010011110", 55);
        addcode("111010011111", 63);
        addcode("11101010", 118);
        addcode("111010110", 106);
        addcode("111010111", 65);
        addcode("111011", 500);
        addcode("11110", 114);
        addcode("111110", 501);
        addcode("111111", 502);
    }

    private void addcode(String prefix, int code) {
        prefixTable.put(code, prefix);
        codeTable.put(prefix, code);
    }

    protected static Tree getInstance() {
        if (instance == null) {
            instance = new Tree();
        }
        return instance;
    }

    protected int getCode(String prefix) {
        if (! codeTable.containsKey(prefix)){
            return -1;
        }
        return(codeTable.get(prefix));
    }

    protected String getPrefix(int code){
        return(prefixTable.get(code));
    }
}
