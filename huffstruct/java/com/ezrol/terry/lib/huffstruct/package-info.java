/**
 * Huffstruct
 *
 * A huffman Tree based structure library allowing the storage of binary data in a loadable structure
 *
 * The format allows for 2 data types
 * 1) Binary Strings (byte array of arbitrary length)
 * 2) Lists (containing zero or more binary strings or sub lists)
 *
 * The developer the developer can of course determine additional properties of the data, such as conversions
 * to numberic, and boolean values, or nesting lists for an associative array type value.
 *
 * The goal of the library is to obfuscate any textual/binary data for saving preference to encourage use of the proper
 * editor since the data based on a huffman Tree input bytes are not byte aligned in the output. further tags to breakup
 * the arrays/strings (metadata) are in additional codes outside the 256 for byte data.
 *
 * A static hard codded huffman tree is used for this format its highly encouraged that if you use a custom tree
 * to change the package name to ensure no conflict with code using this static tree, since resulting data will not be
 * interchangeable.
 *
 * Simple usage:
 *
 * Deserialization:
 * Huffstruct.loadData(bytes[])
 *   This static method will take a byte array and extract a StructNode tree from it
 *
 * Serialization:
 * Huffstruct.dumpData(StructNode)
 *   This static method will take the root node of a StructNode tree and form the binary array from it
 *
 * Overriding StructNode is possible, (as long as your nodes only return either a byte array or a list of nodes not
 * both when requested) however realize the deserializer will not use your custom classes, thus you will need
 * to walk over the tree converting it on load.
 *
 * Created by ezterry on 7/1/17.
 *
 * The MIT License Copyright
 * (c) 2017 EzTerry (Terrence Ezrol)
 *
 * Permission is hereby
 * granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions: The above copyright notice and this
 * permission notice shall be included in all copies or substantial portions of
 * the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.ezrol.terry.lib.huffstruct;