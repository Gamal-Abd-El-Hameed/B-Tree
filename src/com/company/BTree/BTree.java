package com.company.BTree;

import com.company.BTreeNode.*;

import java.util.*;

public class BTree <K extends Comparable<K>, V> implements IBTree {
    private int minimumDegree;
    private BTreeNode<K, V> root;

    public BTree(int minimumDegree) {
        this.minimumDegree = minimumDegree;
        this.root = null;
    }

    public void traverse() {
        if (root != null)
            root.traverse();
    }

    @Override
    public int getMinimumDegree() {
        return minimumDegree;
    }

    @Override
    public IBTreeNode<K, V> getRoot() {
        return root;
    }

    @Override
//    public void insert(K key, V value);
    public void insert(Comparable key, Object value) {
        // if the node already exists
//        if(search(key) != null) return;
        // if tree is empty
        if (root == null)
            root = new BTreeNode(minimumDegree, true, key, value);

        else { // If tree is not empty
            // If root is full
            if (root.getNumOfKeys() == 2 * minimumDegree - 1) {
                BTreeNode<K,V> newRoot = new BTreeNode(minimumDegree, false);
                // new list of children
                List<BTreeNode<K, V>> children = new ArrayList<>();
                // Make old root as child of new root
                children.add(root);
                // set the keys of the root
                newRoot.setChildren(children);
                // Split the old root and move 1 key to the new root
                newRoot.splitChild(0, root);
                // New root has two children now. Decide which of the
                // two children is going to have new key
                int i = 0;
                if (newRoot.getKeys().get(0).compareTo((K) key) < 0)
                    i++;
                newRoot.getChildren().get(i).insertNonFull((K)key, (V)value);
                // Change root
                root = newRoot;
            }
            else // If root is not full, call insertNonFull for root
                root.insertNonFull((K)key, (V)value);
        }
    }

    @Override
    // public V search(K key);
    public Object search(Comparable key) { return root.searchInBTree((K) key); }

    @Override
    // public boolean delete(K key);
    public boolean delete(Comparable key) {
        if (root.searchInBTree((K) key) == null) return false;
        root.remove((K)key);
        return true;
    }
}
