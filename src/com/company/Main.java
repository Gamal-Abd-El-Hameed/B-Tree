package com.company;
import com.company.BTree.BTree;
public class Main {
    public static void main(String[] args) {
        BTree<Integer, String> t = new BTree<>(3);
//        t.insert(18,"b");
//        t.insert(19,"a");
//
//        t.insert(100,"b");
//        t.insert(110,"a");
//        t.insert(111,"b");
//        t.insert(112,"c");
//        t.insert(113,"a");
//        t.insert(0,"a");
//        t.insert(1,"b");
//        t.insert(2,"c");
//
//        t.insert(9,"c");
//        t.insert(10,"a");
//        t.insert(30,"b");
//        t.insert(35,"c");
//        t.insert(40,"a");
//        t.insert(50,"b");
//        t.insert(74,"c");
//        t.insert(99,"a");
//        t.insert(11,"b");
//        t.insert(12,"c");
//        t.insert(13,"a");
//        t.insert(3,"a");
//        t.insert(4,"b");
//        t.insert(5,"c");
//        t.insert(7,"a");
//        t.insert(8,"b");
//        t.insert(15,"b");
//        t.insert(16,"c");
//        t.insert(17,"a");




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
//
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