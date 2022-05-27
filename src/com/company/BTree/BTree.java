package com.company.BTree;

import com.company.BTreeNode.*;

import java.util.*;

public class BTree <K extends Comparable<K>, V> implements IBTree {
    private final int minimumDegree;
    private BTreeNode<K, V> root;

    /**
     * constructor of the B-tree
     * @param minimumDegree the minimum degree of the tree
     */
    public BTree(int minimumDegree) {
        this.minimumDegree = minimumDegree;
        this.root = new BTreeNode<>(minimumDegree, true);
    }

    /**
     * traverse the tree
     */
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
        if(search(key) != null) return;
        // If root is full
        if (root.getNumOfKeys() == 2 * minimumDegree - 1) {
            BTreeNode<K,V> r = root;
            BTreeNode<K,V> newRoot = new BTreeNode(minimumDegree, false);
            // change the root to the new root
            root = newRoot;
            // Make old root as child of new root
            List<BTreeNode<K, V>> children = new ArrayList<>();            
            children.add(r);
            newRoot.setChildren(children);
            // Split the old root and move 1 key to the new root
            split(newRoot, 0, r);         
            // insert the node at he new key   
            insertNonFull(newRoot, (K)key, (V)value);
        }
        else // If root is not full, insertNonFull at root
            insertNonFull(root, (K)key, (V)value);
    }

    /**
     * insert the (key, value) at nonfull node
     * @param node the node where we insert
     * @param key the key of the new inserted node
     * @param value the value of the new inserted node
     */
    private void insertNonFull(BTreeNode<K, V> node, K key, V value) {
        int i = node.getNumOfKeys() - 1;
        // If the node is leaf
        if (node.isLeaf()) {
            // Add the (key, value) at the end of the list
            node.getKeys().add(key); node.getValues().add(value);
            // sort the list
            while (i >= 0 && node.getKeys().get(i).compareTo(key) > 0) {
                node.getKeys().set(i + 1, node.getKeys().get(i));
                node.getValues().set(i + 1, node.getValues().get(i));
                i--;
            }
            // put the (key, value) at the correct location
            node.getKeys().set(i + 1, key);
            node.getValues().set(i + 1, value);
            // increment number of keys of the node
            node.setNumOfKeys(node.getNumOfKeys() + 1);
        }
        else { // If node is not leaf
            // find the child which will have the new (key, value)
            while (i >= 0 && node.getKeys().get(i).compareTo(key) > 0)
                i--;
            i++;
            BTreeNode<K, V> tmp = node.getChildren().get(i);
            // if the child is full
            if(tmp.getNumOfKeys() == 2 * minimumDegree - 1) {
                // split the child
                split(node, i, tmp);
                // which of the two children will have the new key
                if (node.getKeys().get(i).compareTo(key) < 0)
                    i++;
            }
            insertNonFull(node.getChildren().get(i), key, value);
        }
    }

    /**
     * 
     * @param parent the parent of the child to be split
     * @param position the index of the child tht will split
     * @param current the node to be split
     */
    private void split(BTreeNode<K, V> parent, int position, BTreeNode<K, V> current) {
        // the new right chlild to the node that will be split
        BTreeNode<K, V> rightChild = new BTreeNode(minimumDegree, current.isLeaf());
        rightChild.setNumOfKeys(minimumDegree - 1);
        // Copy the last (minimumDegree - 1) (key, value) of current node to the right child
        for (int j = 0; j < minimumDegree - 1; j++) {
            rightChild.getKeys().add(current.getKeys().get(j + minimumDegree));
            rightChild.getValues().add(current.getValues().get(j + minimumDegree));
        }

        // Copy the last minimumDegree children of current node to the right child
        // if the current node not leaf
        if (!current.isLeaf())
            for (int j = 0; j < minimumDegree; j++)
                rightChild.getChildren().add(current.getChildren().get(j + minimumDegree));

        // Decrement the number of keys of the current node to (minimumDegree - 1)
        current.setNumOfKeys(minimumDegree - 1);
        // Add the new right child to parent's children
        parent.getChildren().add(rightChild);
        // sort the children of the parent
        for(int j = parent.getNumOfKeys(); j >= position + 1; j--)
            parent.getChildren().set(j + 1, parent.getChildren().get(j));
        // place the new right child at the correct place
        parent.getChildren().set(position + 1, rightChild);
        // Move up the (key, value) in the middle of the current child to the parent's keys
        parent.getKeys().add((K)current.getKeys().get(minimumDegree - 1));
        parent.getValues().add((V)current.getValues().get(minimumDegree - 1));
        // sort the (key, value) of the parent
        for (int j = parent.getNumOfKeys() - 1; j >= position; j--) {
            parent.getKeys().set(j + 1, parent.getKeys().get(j));
            parent.getValues().set(j + 1, parent.getValues().get(j));
        }
        // place the new (key, value) in the correct place
        parent.getKeys().set(position, (K)current.getKeys().get(minimumDegree - 1));
        parent.getValues().set(position, (V)current.getValues().get(minimumDegree - 1));
        // increment umber of keys of the parent
        parent.setNumOfKeys(1 + parent.getNumOfKeys());
        // remove the last (minimumDegree) (key, value) from the current node
        for(int j = 0; j < minimumDegree; j++) {                            
            current.getKeys().remove(current.getKeys().size() - 1);
            current.getValues().remove(current.getValues().size() - 1);
        }
        // if current is not leaf,
        // remove the last (minimumDegree) children from the current node
        if(!current.isLeaf())
            for(int j = 0; j < minimumDegree; j++)
                current.getChildren().remove(current.getChildren().size() - 1);
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