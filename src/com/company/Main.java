package com.company;
import com.company.BTree.BTree;
import com.company.SearchEngine.SearchEngine;


public class Main {
    public static void main(String[] args) {
        BTree<String, String> t = new BTree<>(3);

        t.insert(15, "a");
        t.insert(40, "a");
        t.insert(100, "a");
        t.insert(25, "a");
        t.insert(200, "a");
        t.insert(657, "a");
        t.insert(120, "a");
        t.insert(65, "a");
        t.insert(333, "a");
        t.insert(444, "a");
        t.insert(37, "a");
        t.insert(191, "a");
        t.insert(222, "a");
        t.insert(777, "a");
        t.insert(300, "a");
        t.insert(420, "a");
        t.insert(313, "a");
        t.insert(305, "a");
        t.insert(229, "a");
        t.insert(280, "a");
        t.insert(800, "a");
        t.insert(900, "a");
        t.insert(950, "a");


        System.out.println("Traversal of the constructed tree is ");
        t.traverse();
        t.delete(40);
        System.out.println("---------------------------------------------------");
        System.out.println("Traversal of the constructed tree is ");
        t.traverse();
        t.delete(25);
        System.out.println("---------------------------------------------------");
        System.out.println("Traversal of the constructed tree is ");
        t.traverse();
    }
}