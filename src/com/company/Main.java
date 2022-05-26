package com.company;

import com.company.BTree.BTree;

public class Main {
    public static void main(String[] args) {
        BTree<Integer, String> t = new BTree<>(3);
//        t.insert(10, "Mo");
//        t.insert(20, "Fawzy");
//        t.insert(5, "Abbas");
//        t.insert(6, "Nour");
//        t.insert(12, "Farrag");
//        t.insert(30, "Gamal");
//        t.insert(7, "FAANG");
//        t.insert(15, "Mo");
//        t.insert(29, "Fawzy");
//        t.insert(1, "Abbas");
//        t.insert(666, "Nour");
//        t.insert(120, "Farrag");
//        t.insert(300, "Gamal");
//        t.insert(77, "FAANG");
        t.insert(1, "a");
        t.insert(11, "k");
        t.insert(8, "h");
        t.insert(14, "n");
        t.insert(4, "d");

        t.insert(6, "f");
        t.insert(26, "z");
        t.insert(7, "g");
        t.insert(3, "c");
        t.insert(2, "b");
        t.insert(25, "y");
        t.insert(9, "i");
        t.insert(5, "e");


        t.insert(21, "u");
        t.insert(12, "l");
        t.insert(18, "r");
        t.insert(24, "x");
        t.insert(10, "j");


        t.insert(15, "o");
        t.insert(16, "p");
        t.insert(17, "q");
        t.insert(23, "w");

        t.insert(19, "s");
        t.insert(13, "m");
        t.insert(20, "t");
        t.insert(22, "v");

        System.out.println("Traversal of the constructed tree is ");
        t.traverse();
    }
}