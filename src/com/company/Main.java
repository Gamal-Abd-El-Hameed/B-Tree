package com.company;
import com.company.BTree.BTree;
import com.company.SearchEngine.SearchEngine;


public class Main {
    public static void main(String[] args) {
        BTree<String, String> t = new BTree<>(3);

        t.insert(15, "abbas");
        t.insert(40, "varo");
        t.insert(100, "alo");
        t.insert(25, "nour");
        t.insert(200, "akram");
        t.insert(657, "ahmad");
        t.insert(120, "darsh");
        t.insert(65, "sasa");
        t.insert(333, "ally");
        t.insert(444, "kareem");
        t.insert(37, "zezo");
        t.insert(191, "maged");
        t.insert(222, "random");
        t.insert(777, "mo salah");
        t.insert(300, "ahly");

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