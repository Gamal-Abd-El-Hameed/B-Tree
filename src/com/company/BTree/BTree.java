package com.company.BTree;

import com.company.BTreeNode.*;

import java.util.*;

public class BTree <K extends Comparable<K>, V> implements IBTree {
    private final int minimumDegree;
    private BTreeNode<K, V> root;

    public BTree(int minimumDegree) {
        this.minimumDegree = minimumDegree;
        this.root = new BTreeNode<>(minimumDegree, true);
    }

    public void traverse() {
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
    public void insert(Comparable key, Object value) {
        // if the node already exists
//        if(search(key) != null) return;
        // If root is full
        if (root.getNumOfKeys() == 2 * minimumDegree - 1) {
            BTreeNode<K,V> r = root;
            BTreeNode<K,V> newRoot = new BTreeNode(minimumDegree, false);
            root = newRoot;
            // new list of children
            List<BTreeNode<K, V>> children = new ArrayList<>();
            // Make old root as child of new root
            children.add(r);
            // set the keys of the root
            newRoot.setChildren(children);
            // Split the old root and move 1 key to the new root
            split(newRoot, 0, r);
            // New root has two children now. Decide which of the
            // two children is going to have new key
            insertNonFull(newRoot, (K)key, (V)value);
        }
        else // If root is not full, call insertNonFull for root
            insertNonFull(root, (K)key, (V)value);
    }

    private void insertNonFull(BTreeNode<K, V> node, K key, V value) {
        int i = node.getNumOfKeys() - 1;
        // If this is a leaf node
        if (node.isLeaf()) {
            // temp addition to increase list size by one
            node.getKeys().add(null); node.getValues().add(null);
            // find the location of new key
            while (i >= 0 && node.getKeys().get(i).compareTo(key) > 0) {
                node.getKeys().set(i + 1, node.getKeys().get(i));
                node.getValues().set(i + 1, node.getValues().get(i));
                i--;
            }
            // Insert the new key at found location
            node.getKeys().set(i + 1, key);
            node.getValues().set(i + 1, value);
            node.setNumOfKeys(node.getNumOfKeys() + 1);
        }
        else { // If node is not leaf
            // find the child which is going to have the new key
            while (i >= 0 && node.getKeys().get(i).compareTo(key) > 0)
                i--;
            i++;
            BTreeNode<K, V> tmp = node.getChildren().get(i);
            // if the child is full
            if(tmp.getNumOfKeys() == 2 * minimumDegree - 1) {
                // If the child is full, split it
                split(node, i, tmp);

                // which of the two children will have the new key
                if (node.getKeys().get(i).compareTo(key) < 0)
                    i++;
            }
            insertNonFull(node.getChildren().get(i), key, value);
        }
    }

    private void split(BTreeNode<K, V> parent, int position, BTreeNode<K, V> current) {
        BTreeNode<K, V> newNode = new BTreeNode(minimumDegree, current.isLeaf());
        newNode.setNumOfKeys(minimumDegree - 1);
        // Copy the last (t - 1) keys of current node to the new node
        for (int j = 0; j < minimumDegree - 1; j++) {
            newNode.getKeys().add(current.getKeys().get(j + minimumDegree));
            newNode.getValues().add(current.getValues().get(j + minimumDegree));
        }

        // Copy the last t children of current node to the new node
        if (!current.isLeaf())
            for (int j = 0; j < minimumDegree; j++)
                newNode.getChildren().add(current.getChildren().get(j + minimumDegree));

        current.setNumOfKeys(minimumDegree - 1);
        parent.getChildren().add(null);
        for(int j = parent.getNumOfKeys(); j >= position + 1; j--)
            parent.getChildren().set(j + 1, parent.getChildren().get(j));
        parent.getChildren().set(position + 1, newNode);
        parent.getKeys().add(null);
        parent.getValues().add(null);
        for (int j = parent.getNumOfKeys() - 1; j >= position; j--) {
            parent.getKeys().set(j + 1, parent.getKeys().get(j));
            parent.getValues().set(j + 1, parent.getValues().get(j));
        }
        parent.getKeys().set(position, (K)current.getKeys().get(minimumDegree - 1));
        parent.getValues().set(position, (V)current.getValues().get(minimumDegree - 1));
        parent.setNumOfKeys(1 + parent.getNumOfKeys());
        for(int j = 0; j < minimumDegree; j++)
            current.getKeys().remove(current.getKeys().size() - 1);
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
