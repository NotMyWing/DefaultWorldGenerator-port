"""
A Python sample implementation of the Huff Struct parser

HuffStruct is a generic name for Huffman Structure

Here we have a static huffman tree, with 259 weighted codes
(0-255) - 8 bit charicter data
(500-502) - control codes for structured data

Using the embeded control codes the huffman tree decodes into
nodes containing either a array of child nodes, or an 8bit binary
string.

These can then be combined by programs for storing more complex
data as required.


== Huffman tree source ==

The huffman tree was sourced using the project Gutenberg copy of:
"ON THE DECAY OF THE ART OF LYING"
by Mark Twain

http://www.gutenberg.org/cache/epub/2572/pg2572.txt

Plus the addition of hardcoded weights for the 3 control codes

== The Python3 code used to generate the tree ==

import string

chars = [0]*256
with open("pg2572.txt","rb") as fp:
    while True:
        ch = fp.read(1)
        if(len(ch) == 0):
            break
        chars[ch[0]]+=1

nodes=[]
for i in range(256):
    nodes.append((chars[i],i))

#inject custom codes
nodes.append((800,500)) #start list
nodes.append((800,501)) #start string
nodes.append((800,502)) #end list

#calc huff tree
while(len(nodes) > 1):
    nodes.sort(key=lambda a:a[0])
    a = nodes.pop(0)
    b = nodes.pop(0)
    nodes.append((a[0] + b[0] + 2,(a[1],b[1])))

#print huff tree
def printnode(prefix,node):
    if(isinstance(node,tuple)):
        printnode(prefix + "0",node[0])
        printnode(prefix + "1",node[1])
    else:
        print("addcode( \"" + prefix + "\", " + str(node) + ");")

printnode("",nodes[0][1])

== Library ==

This is mostly a simple library to encode/decode the huffman structures in
python3  and is the reference implementation for versions in other languages

Here a node refers either to a binary string b'' or an array 0 or more nodes

storeb(data) takes a node/node tree and returns the huffman encoded string
loadb(data) takes the raw binary encoded data and returns the tree of nodes

if this module is run directly its of the form "huffstruct.py <filename>" and
will print the raw tree that the file contained.

"""
import io

CODE_START_ARRAY=500
CODE_START_STRING=501
CODE_END_ELEMENT=502

class huffmansearch:
    def __init__(self):
        self._prefixToCode = {}
        self._codeToPrefix = {}
        
        self.addcode( "00000", 99);
        self.addcode( "00001", 117);
        self.addcode( "0001", 97);
        self.addcode( "0010", 110);
        self.addcode( "0011", 105);
        self.addcode( "01000", 100);
        self.addcode( "010010", 119);
        self.addcode( "010011000", 67);
        self.addcode( "010011001", 95);
        self.addcode( "0100110100", 58);
        self.addcode( "0100110101", 47);
        self.addcode( "01001101100", 51);
        self.addcode( "010011011010", 91);
        self.addcode( "010011011011", 93);
        self.addcode( "0100110111", 87);
        self.addcode( "01001110", 80);
        self.addcode( "01001111", 71);
        self.addcode( "01010", 108);
        self.addcode( "010110", 103);
        self.addcode( "010111", 112);
        self.addcode( "0110", 111);
        self.addcode( "011100000", 49);
        self.addcode( "011100001", 68);
        self.addcode( "0111000100", 42);
        self.addcode( "011100010100", 75);
        self.addcode( "011100010101", 122);
        self.addcode( "0111000101100", 36);
        self.addcode( "0111000101101", 64);
        self.addcode( "0111000101110", 81);
        self.addcode( "0111000101111", 88);
        self.addcode( "01110001100000", 0);
        self.addcode( "01110001100001", 1);
        self.addcode( "01110001100010", 2);
        self.addcode( "01110001100011", 3);
        self.addcode( "01110001100100", 4);
        self.addcode( "01110001100101", 5);
        self.addcode( "01110001100110", 6);
        self.addcode( "01110001100111", 7);
        self.addcode( "01110001101000", 8);
        self.addcode( "01110001101001", 9);
        self.addcode( "01110001101010", 11);
        self.addcode( "01110001101011", 12);
        self.addcode( "01110001101100", 14);
        self.addcode( "01110001101101", 15);
        self.addcode( "01110001101110", 16);
        self.addcode( "01110001101111", 17);
        self.addcode( "01110001110000", 18);
        self.addcode( "01110001110001", 19);
        self.addcode( "01110001110010", 20);
        self.addcode( "01110001110011", 21);
        self.addcode( "01110001110100", 22);
        self.addcode( "01110001110101", 23);
        self.addcode( "01110001110110", 24);
        self.addcode( "01110001110111", 25);
        self.addcode( "01110001111000", 26);
        self.addcode( "01110001111001", 27);
        self.addcode( "01110001111010", 28);
        self.addcode( "01110001111011", 29);
        self.addcode( "01110001111100", 30);
        self.addcode( "01110001111101", 31);
        self.addcode( "01110001111110", 38);
        self.addcode( "01110001111111", 43);
        self.addcode( "01110010000000", 60);
        self.addcode( "01110010000001", 61);
        self.addcode( "01110010000010", 62);
        self.addcode( "01110010000011", 92);
        self.addcode( "01110010000100", 94);
        self.addcode( "01110010000101", 96);
        self.addcode( "01110010000110", 123);
        self.addcode( "01110010000111", 124);
        self.addcode( "01110010001000", 125);
        self.addcode( "01110010001001", 126);
        self.addcode( "01110010001010", 127);
        self.addcode( "01110010001011", 128);
        self.addcode( "01110010001100", 129);
        self.addcode( "01110010001101", 130);
        self.addcode( "01110010001110", 131);
        self.addcode( "01110010001111", 132);
        self.addcode( "01110010010000", 133);
        self.addcode( "01110010010001", 134);
        self.addcode( "01110010010010", 135);
        self.addcode( "01110010010011", 136);
        self.addcode( "01110010010100", 137);
        self.addcode( "01110010010101", 138);
        self.addcode( "01110010010110", 139);
        self.addcode( "01110010010111", 140);
        self.addcode( "01110010011000", 141);
        self.addcode( "01110010011001", 142);
        self.addcode( "01110010011010", 143);
        self.addcode( "01110010011011", 144);
        self.addcode( "01110010011100", 145);
        self.addcode( "01110010011101", 146);
        self.addcode( "01110010011110", 147);
        self.addcode( "01110010011111", 148);
        self.addcode( "01110010100000", 149);
        self.addcode( "01110010100001", 150);
        self.addcode( "01110010100010", 151);
        self.addcode( "01110010100011", 152);
        self.addcode( "01110010100100", 153);
        self.addcode( "01110010100101", 154);
        self.addcode( "01110010100110", 155);
        self.addcode( "01110010100111", 156);
        self.addcode( "01110010101000", 157);
        self.addcode( "01110010101001", 158);
        self.addcode( "01110010101010", 159);
        self.addcode( "01110010101011", 160);
        self.addcode( "01110010101100", 161);
        self.addcode( "01110010101101", 162);
        self.addcode( "01110010101110", 163);
        self.addcode( "01110010101111", 164);
        self.addcode( "01110010110000", 165);
        self.addcode( "01110010110001", 166);
        self.addcode( "01110010110010", 167);
        self.addcode( "01110010110011", 168);
        self.addcode( "01110010110100", 169);
        self.addcode( "01110010110101", 170);
        self.addcode( "01110010110110", 171);
        self.addcode( "01110010110111", 172);
        self.addcode( "01110010111000", 173);
        self.addcode( "01110010111001", 174);
        self.addcode( "01110010111010", 175);
        self.addcode( "01110010111011", 176);
        self.addcode( "01110010111100", 177);
        self.addcode( "01110010111101", 178);
        self.addcode( "01110010111110", 179);
        self.addcode( "01110010111111", 180);
        self.addcode( "01110011000000", 181);
        self.addcode( "01110011000001", 182);
        self.addcode( "01110011000010", 183);
        self.addcode( "01110011000011", 184);
        self.addcode( "01110011000100", 185);
        self.addcode( "01110011000101", 186);
        self.addcode( "01110011000110", 188);
        self.addcode( "01110011000111", 189);
        self.addcode( "01110011001000", 190);
        self.addcode( "01110011001001", 192);
        self.addcode( "01110011001010", 193);
        self.addcode( "01110011001011", 194);
        self.addcode( "01110011001100", 195);
        self.addcode( "01110011001101", 196);
        self.addcode( "01110011001110", 197);
        self.addcode( "01110011001111", 198);
        self.addcode( "01110011010000", 199);
        self.addcode( "01110011010001", 200);
        self.addcode( "01110011010010", 201);
        self.addcode( "01110011010011", 202);
        self.addcode( "01110011010100", 203);
        self.addcode( "01110011010101", 204);
        self.addcode( "01110011010110", 205);
        self.addcode( "01110011010111", 206);
        self.addcode( "01110011011000", 207);
        self.addcode( "01110011011001", 208);
        self.addcode( "01110011011010", 209);
        self.addcode( "01110011011011", 210);
        self.addcode( "01110011011100", 211);
        self.addcode( "01110011011101", 212);
        self.addcode( "01110011011110", 213);
        self.addcode( "01110011011111", 214);
        self.addcode( "01110011100000", 215);
        self.addcode( "01110011100001", 216);
        self.addcode( "01110011100010", 217);
        self.addcode( "01110011100011", 218);
        self.addcode( "01110011100100", 219);
        self.addcode( "01110011100101", 220);
        self.addcode( "01110011100110", 221);
        self.addcode( "01110011100111", 222);
        self.addcode( "01110011101000", 223);
        self.addcode( "01110011101001", 224);
        self.addcode( "01110011101010", 225);
        self.addcode( "01110011101011", 226);
        self.addcode( "01110011101100", 227);
        self.addcode( "01110011101101", 228);
        self.addcode( "01110011101110", 229);
        self.addcode( "01110011101111", 230);
        self.addcode( "01110011110000", 231);
        self.addcode( "01110011110001", 232);
        self.addcode( "01110011110010", 233);
        self.addcode( "01110011110011", 234);
        self.addcode( "01110011110100", 235);
        self.addcode( "01110011110101", 236);
        self.addcode( "01110011110110", 237);
        self.addcode( "01110011110111", 238);
        self.addcode( "01110011111000", 240);
        self.addcode( "01110011111001", 241);
        self.addcode( "01110011111010", 242);
        self.addcode( "01110011111011", 243);
        self.addcode( "01110011111100", 244);
        self.addcode( "01110011111101", 245);
        self.addcode( "01110011111110", 246);
        self.addcode( "01110011111111", 247);
        self.addcode( "011101", 109);
        self.addcode( "01111", 104);
        self.addcode( "100000", 121);
        self.addcode( "100001", 102);
        self.addcode( "10001000", 84);
        self.addcode( "1000100100", 39);
        self.addcode( "1000100101", 59);
        self.addcode( "100010011", 34);
        self.addcode( "10001010", 69);
        self.addcode( "100010110", 76);
        self.addcode( "1000101110", 77);
        self.addcode( "10001011110000", 248);
        self.addcode( "10001011110001", 249);
        self.addcode( "10001011110010", 250);
        self.addcode( "10001011110011", 251);
        self.addcode( "10001011110100", 252);
        self.addcode( "10001011110101", 253);
        self.addcode( "10001011110110", 254);
        self.addcode( "10001011110111", 255);
        self.addcode( "10001011111", 50);
        self.addcode( "100011", 10);
        self.addcode( "1001", 116);
        self.addcode( "101", 32);
        self.addcode( "110000", 13);
        self.addcode( "1100010", 46);
        self.addcode( "1100011", 44);
        self.addcode( "11001", 115);
        self.addcode( "1101", 101);
        self.addcode( "111000000", 70);
        self.addcode( "111000001", 78);
        self.addcode( "11100001", 73);
        self.addcode( "1110001000", 72);
        self.addcode( "11100010010", 53);
        self.addcode( "111000100110", 54);
        self.addcode( "111000100111", 56);
        self.addcode( "111000101", 82);
        self.addcode( "11100011", 45);
        self.addcode( "111001000", 79);
        self.addcode( "11100100100", 48);
        self.addcode( "11100100101", 113);
        self.addcode( "1110010011", 66);
        self.addcode( "111001010", 83);
        self.addcode( "1110010110", 89);
        self.addcode( "1110010111", 120);
        self.addcode( "1110011", 98);
        self.addcode( "11101000", 107);
        self.addcode( "111010010000", 57);
        self.addcode( "111010010001", 52);
        self.addcode( "111010010010", 74);
        self.addcode( "111010010011", 86);
        self.addcode( "1110100101", 85);
        self.addcode( "1110100110000", 33);
        self.addcode( "11101001100010", 35);
        self.addcode( "11101001100011", 37);
        self.addcode( "11101001100100", 90);
        self.addcode( "11101001100101", 187);
        self.addcode( "11101001100110", 191);
        self.addcode( "11101001100111", 239);
        self.addcode( "11101001101", 40);
        self.addcode( "11101001110", 41);
        self.addcode( "111010011110", 55);
        self.addcode( "111010011111", 63);
        self.addcode( "11101010", 118);
        self.addcode( "111010110", 106);
        self.addcode( "111010111", 65);
        self.addcode( "111011", 500);
        self.addcode( "11110", 114);
        self.addcode( "111110", 501);
        self.addcode( "111111", 502);
        
    def addcode(self,prefix,code):
        self._prefixToCode[prefix]=code
        self._codeToPrefix[code]=prefix
        
    def getCode(self,prefix):
        if(prefix not in self._prefixToCode):
            return None
        return(self._prefixToCode[prefix])
        
    def getPrefix(self,code):
        return(self._codeToPrefix[code])

table = huffmansearch()
        
class bitstream:
    """takes a binary file like object (BytesIO or binary file) and
       provides a bit stream
       
       Note mixing read and write is not supported
    """
    def __init__(self,fobj):
        self._fp=fobj
        self.curbyte=None
        
    def readbit(self):
        if(self.curbyte is None):
            self.curbit=0
            self.curbyte = self._fp.read(1)[0]
        bit=(self.curbyte << self.curbit & 0xFF )>>7
        self.curbit += 1
        if(self.curbit>=8):
            self.curbyte=None
           
        if(bit == 1):
            return("1")
        else:
            return("0")
            
    def writebit(self,bit):
        """add a bit to the stream"""
        if(self.curbyte is None):
            self.curbyte = 0
            self.curbit=0
        if(bit == "1"):
            self.curbyte = self.curbyte | (1 << (7-self.curbit))
        self.curbit += 1
        if(self.curbit >= 8):
            self._fp.write(bytes([self.curbyte]))
            self.curbyte=None
    
    def flush(self):
        """write last byte even if not full"""
        if(self.curbyte is not None):
            self._fp.write(bytes([self.curbyte]))
            self.curbyte=None
        
    def readcode(self):
        """reads the next prefix from the bitstream"""
        prefix=""
        while(True):
            prefix+=self.readbit()
            code=table.getCode(prefix)
            if(code is not None):
                return(code)
            if(len(prefix) > 16):
                raise(KeyError("Prefix not found, invalid bit stream"))

    def writecode(self,code):
        """reads the next prefix from the bitstream"""
        prefix=table.getPrefix(code)
        print("add prefix: " + prefix)
        for bit in prefix:
            self.writebit(bit)
                
        
def loadb(b):
    """load in from the byte array"""
    stream = bitstream(io.BytesIO(b))
    output = []
    stack = [output,]
    instring = False
    current = []
    
    while(True):
        token = stream.readcode()
        #print("token = " + str(token))
        if(instring):
            if(token == CODE_END_ELEMENT):
                #print("end of string")
                instring=False
                stack[0].append(bytes(current))
                current=[]
            elif(token in (CODE_START_STRING, CODE_START_ARRAY)):
                raise(ValueError("Unexpected code in middle of string"))
            else:
                current.append(token)
                
        elif(token == CODE_START_ARRAY):
            #print("start of array")
            stack.insert(0,[])
        elif(token == CODE_START_STRING):
            #print("start string")
            instring=True
        elif(token == CODE_END_ELEMENT):
            #print("end of array")
            if(len(stack)<2):
                raise(ValueError("Unexpected end of element code"))
            stack[1].append(stack[0])
            stack.pop(0)
        if(not instring and len(stack) == 1):
            break
        
    return(output[0])

def storeb(data):
    raw = io.BytesIO()
    stream = bitstream(raw)
    
    stack=[data]
    
    while(len(stack) != 0):
        if(stack[0] is None):
            #print("end element")
            stream.writecode(CODE_END_ELEMENT)
            stack.pop(0)
        elif(isinstance(stack[0],bytes)):
            #print("start string")
            stream.writecode(CODE_START_STRING)
            for ch in stack[0]:
                #print("char in string")
                stream.writecode(ch)
            #print("end string")
            stream.writecode(CODE_END_ELEMENT)
            stack.pop(0)
        else:
            #print("start array")
            stream.writecode(CODE_START_ARRAY)
            ar = stack.pop(0)
            stack.insert(0,None)
            ar = ar.copy()
            ar.reverse()
            for e in ar:
                stack.insert(0,e)
    stream.flush()
    raw.seek(0)
    return(raw.read())
    
if(__name__ == "__main__"):
    import sys
    import pprint
    if(len(sys.argv) > 1):
        print("Parsing " + sys.argv[1])
        pprint.pprint(loadb(open(sys.argv[1],"rb").read()))
    else:
        print("Please run with a filename to parse:")
        print(sys.argv[0] + " <filename>")
