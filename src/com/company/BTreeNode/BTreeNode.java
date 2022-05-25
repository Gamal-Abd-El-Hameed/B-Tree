package com.company.BTreeNode;

import java.util.*;

public class BTreeNode <K extends Comparable <K>, V> implements IBTreeNode {
    private int numOfKeys;
    private int minimumDegree;
    private boolean isLeaf;
    private List<K> keys;
    private List<V> values;
    private List<BTreeNode<K, V>> children;

    public BTreeNode(int degree, boolean leaf) {
        this.minimumDegree = degree;
        this.numOfKeys = 0;
        this.isLeaf = leaf;
        // new list of children
        this.children = new ArrayList<>(2 * degree);
        // new list of keys
        this.keys = new ArrayList<>(2 * degree - 1);
        // new list of values
        this.values = new ArrayList<>(2 * degree - 1);
    }

    public BTreeNode(int degree, boolean leaf, K key, V value) {
        this.minimumDegree = degree;
        this.isLeaf = leaf;
        // new list of children
        children = new ArrayList<>(2 * degree);
        // new list of keys
        keys = new ArrayList<>(2 * degree - 1);
        // add the key to the list
        keys.set(0, key);
        numOfKeys = 1; // Update number of keys
        // new list of values
        values = new ArrayList<>(2 * degree - 1);
        // add the value to the list
        values.set(0, value);
    }

    public void insertNonFull(K key, V value) {
        int i = numOfKeys - 1;
        // If this is a leaf node
        if (isLeaf) {
            // find the location of new key
            while (i >= 0 && keys.get(i).compareTo(key) > 0) {
                keys.set(i + 1, keys.get(i));
                values.set(i + 1, values.get(i));
                i--;
            }
            // Insert the new key at found location
            keys.set(i + 1, key);
            values.set(i + 1, value);
            numOfKeys++;
        }
        else { // If node is not leaf
            // find the child which is going to have the new key
            while (i >= 0 && keys.get(i).compareTo(key) > 0)
                i--;
            // if the child is full
            if(children.get(i + 1).numOfKeys == 2 * minimumDegree - 1){
                // If the child is full, split it
                splitChild(i+1, children.get(i + 1));

                // which of the two children will have the new key
                if (keys.get(i + 1).compareTo(key) < 0)
                    i++;
            }
            children.get(i + 1).insertNonFull(key, value);
        }
    }

    public void splitChild(int position, BTreeNode current) {
        BTreeNode newNode = new BTreeNode(current.minimumDegree, current.isLeaf());
        newNode.numOfKeys = minimumDegree - 1;

        // Copy the last (t - 1) keys of current node to the new node
        for (int j = 0; j < minimumDegree - 1; j++)
            newNode.getKeys().set(j, current.getKeys().get(j + minimumDegree));

        // Copy the last t children of current node to the new node
        if (!current.isLeaf())
            for (int j = 0; j < minimumDegree; j++)
                newNode.getChildren().set(j, current.getChildren().get(j + minimumDegree));

        // Reduce the number of keys in current node
        current.numOfKeys = minimumDegree - 1;

        // create space of new child
        for (int j = minimumDegree; j >= position + 1; j--)
            children.set(j + 1, children.get(j));

        // Link the new child to this node
        children.set(position + 1, newNode);

        // find the location of new key and move all greater keys one space ahead
        for (int j = minimumDegree - 1; j >= position; j--)
            keys.set(j + 1, keys.get(j));

        // Copy the middle key of y to this node
        keys.set(position, (K)current.getKeys().get(minimumDegree - 1));

        // Increment count of keys in this node
        numOfKeys++;
    }

    /**
     * traverse all nodes in a subtree rooted with this node in inorder traversal
     */
    public void traverse() {
        int i;
        // traverse through all keys and children except last child
        for (i = 0; i < numOfKeys; i++) {
            // If not leaf, traverse the subtree rooted ont its left first
            if (!isLeaf)
                children.get(i).traverse();
            // print the key
            System.out.println(" " + keys.get(i));
        }

        // Print the subtree rooted with last child
        if (!isLeaf)
            children.get(i).traverse();
    }

    @Override
    public int getNumOfKeys() {
        return numOfKeys;
    }

    @Override
    public void setNumOfKeys(int numOfKeys) {
        this.numOfKeys = numOfKeys;
    }

    @Override
    public boolean isLeaf() {
        return isLeaf;
    }

    @Override
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Override
    public List<K> getKeys() {
        return keys;
    }

    @Override
    public void setKeys(List keys) {
        this.keys = keys;
    }

//    @Override
//    public void setKeys(List<K> keys) {
//        this.keys = keys;
//    }

    @Override
    public List<V> getValues() {
        return values;
    }

    @Override
    public void setValues(List values) {
        this.values = values;
    }

//    @Override
//    public void setValues(List<V> values) {
//        this.values = values;
//    }

    @Override
    public List<BTreeNode<K, V>> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

//    @Override
//    public void setChildren(List<IBTreeNode<K, V>> children) {
//        this.children = children;
//    }
}
