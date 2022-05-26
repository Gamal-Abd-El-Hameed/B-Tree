package com.company;

import com.company.BTree.BTree;

public class Main {
    public static void main(String[] args) {
        BTree<Integer, String> t = new BTree(3);
        t.insert(10, "Mo");
        t.insert(20, "Fawzy");
        t.insert(5, "Abbas");
        t.insert(6, "Nour");
        t.insert(12, "Farrag");
        t.insert(30, "Gamal");
        t.insert(7, "FAANG");
        System.out.println("Traversal of the constructed tree is ");
        t.traverse();
    }
}