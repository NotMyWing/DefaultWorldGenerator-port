package com.ezrol.terry.lib.huffstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A representation of a node in the huffstruct
 * Can either be a list or a binary string
 *
 * If you are extending this class remember you may return either a array or a binary string, not both
 * the other must return null
 *
 * Created by ezterry on 7/2/17.
 */

@SuppressWarnings("WeakerAccess")
abstract public class StructNode {
    /**
     * get any child elements (note one of getArray() and getBinaryString() must always be null, and the other must
     * always return non-null
     *
     * @return if this is an array type node, the array of child nodes, otherwise null
     */
    abstract public List<StructNode> getArray();

    /**
     * get the binary string (note one of getArray() and getBinaryString() must always be null, and the other must
     * always return non-null
     *
     * @return if this is the bytes of the string/data node
     */
    abstract public byte[] getBinaryString();

    /**
     * Create a new array node
     *
     * @return a StructNode object representing an array
     */
    public static StructNode newArray(){
        return(new ListNode(new ArrayList<>()));
    }

    /**
     * Create a new array node (using the provided array of sub nodes)
     *
     * @param lst the list of sub nodes to include in this new node
     * @return a StructNode object representing an array
     */
    public static StructNode newArray(List<StructNode> lst){
        return(new ListNode(lst));
    }

    /**
     * Create a new binary string node
     *
     * @param s the value of the binary string
     * @return a StructNode object representing the byte array
     */
    public static StructNode newBinaryString(byte[] s){
        return(new StringNode(s));
    }

    /**
     * The basic implementation of a List Node
     */
    private static class ListNode extends StructNode{
        private final List nodeList;

        private ListNode(List<StructNode> lst){
            nodeList = lst;
        }

        @Override
        public List<StructNode> getArray() {
            return nodeList;
        }

        @Override
        public byte[] getBinaryString() {
            return null;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "nodeList=" + nodeList +
                    '}';
        }
    }

    /**
     * The basic implementation of a binary string node
     */
    private static class StringNode extends StructNode{
        private final byte[] nodeData;

        private StringNode(byte[] dat){
            nodeData=dat;
        }

        @Override
        public List<StructNode> getArray() {
            return null;
        }

        @Override
        public byte[] getBinaryString() {
            return nodeData;
        }

        @Override
        public String toString() {
            return "StringNode{" +
                    "nodeData=" + Arrays.toString(nodeData) +
                    '}';
        }
    }
}
