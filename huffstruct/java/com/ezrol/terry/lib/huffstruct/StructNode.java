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
@SuppressWarnings("unused")
abstract public class StructNode {
    abstract public List<StructNode> getArray();
    abstract public byte[] getBinaryString();

    public static StructNode newArray(){
        return(new ListNode(new ArrayList<>()));
    }

    public static StructNode newArray(List<StructNode> lst){
        return(new ListNode(lst));
    }

    public static StructNode newBinaryString(byte[] s){
        return(new StringNode(s));
    }

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
