package com.company;
import com.company.BTree.BTree;
import com.company.SearchEngine.SearchEngine;


public class Main {
    public static void main(String[] args) {
        BTree<String, String> t = new BTree<>(2);

//        t.insert(1, "a");
//        t.insert(11, "k");
//        t.insert(8, "h");
//        t.insert(14, "n");
//        t.insert(4, "d");
//
//        t.insert(6, "f");
//        t.insert(4, "d");
//        t.insert(26, "z");
//        System.out.println("Traversal of the constructed tree is ");
//        t.traverse();
//        System.out.println(t.search(4));
//        System.out.println(t.delete(8));
//        System.out.println("\n\n\nTraversal of the constructed tree is ");
//        t.traverse();
        SearchEngine engine = new SearchEngine()


//        t.insert(7, "g");
//        t.insert(3, "c");
//        t.insert(2, "b");
//        t.insert(25, "y");
//        t.insert(9, "i");
//        t.insert(5, "e");
//
//        t.delete(14);

//        t.insert(6, "f");
//        t.insert(26, "z");
//        t.insert(7, "g");
//        t.insert(3, "c");
//        t.insert(2, "b");
//        t.insert(25, "y");
//        t.insert(9, "i");
//        t.insert(5, "e");
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

/**
 * https://teams.microsoft.com/_#/pre-join-calling/19:meeting_NGU1OGFmYTEtZTIwOC00MDljLTljZTQtZjJiZDNiZTk2YTli@thread.v2
 */