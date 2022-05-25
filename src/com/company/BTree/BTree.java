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
        if(isExist((K)key, (V)value)) return;
        // if tree is empty
        if (root == null)
            root = new BTreeNode(minimumDegree, true, key, value);

        else { // If tree is not empty
            // If root is full
            if (root.getNumOfKeys() == 2 * minimumDegree - 1) {
                BTreeNode newRoot = new BTreeNode(minimumDegree, false, key, value);
                // new list of children
                List<BTreeNode<K, V>> children = new ArrayList<>(2 * minimumDegree);
                // Make old root as child of new root
                children.set(0, root);
                // set the keys of the root
                newRoot.setChildren(children);
                // Split the old root and move 1 key to the new root
                newRoot.splitChild(0, root);
                // New root has two children now. Decide which of the
                // two children is going to have new key
                int i = 0;
                if (newRoot.getKeys().get(0).compareTo(key) < 0)
                    i++;
                newRoot.getChildren().get(i).insertNonFull((K)key, (V)value);
                // Change root
                root = newRoot;
            }
            else // If root is not full, call insertNonFull for root
                root.insertNonFull((K)key, (V)value);
        }
    }

    // Same as search but return boolean
    private boolean isExist(K key, V value) {
        return false;
    }

    @Override
    // public V search(K key);
    public Object search(Comparable key) {
        return null;
    }

    @Override
    // public boolean delete(K key);
    public boolean delete(Comparable key) {
        return false;
    }
}
