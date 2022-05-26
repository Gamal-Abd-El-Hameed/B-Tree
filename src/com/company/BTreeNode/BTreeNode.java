package com.company.BTreeNode;

import java.lang.reflect.Array;
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
        this.children = new ArrayList<>();
        // new list of keys
        this.keys = new ArrayList<>();
        // new list of values
        this.values = new ArrayList<>();
    }

    public BTreeNode(int degree, boolean leaf, K key, V value) {
        this.minimumDegree = degree;
        this.isLeaf = leaf;
        // new list of children
        children = new ArrayList<>();
        // new list of keys
        keys = new ArrayList<>();
        // add the key to the list
        keys.add(key);
        numOfKeys = 1; // Update number of keys
        // new list of values
        values = new ArrayList<>();
        // add the value to the list
        values.add(value);
    }

    public void insertNonFull(K key, V value) {
        int i = numOfKeys - 1;
        // If this is a leaf node
        if (isLeaf) {
            // temp addition to increase list size by one
            keys.add(key); values.add(value);
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
            if(children.get(i + 1).numOfKeys == 2 * minimumDegree - 1) {
                // If the child is full, split it
                splitChild(i + 1, children.get(i + 1));

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
        for (int j = 0; j < minimumDegree - 1; j++) {
            newNode.getKeys().add(current.getKeys().get(j + minimumDegree));
            newNode.getValues().add(current.getValues().get(j + minimumDegree));
        }
            

        // Copy the last t children of current node to the new node
        if (!current.isLeaf())
            for (int j = 0; j < minimumDegree; j++)
                newNode.getChildren().add(current.getChildren().get(j + minimumDegree));

        // Reduce the number of keys in current node
        current.numOfKeys = minimumDegree - 1;

        // create space of new child
        children.add(newNode); // temporary addition
        // System.out.println("children size = " + children.size() + "numOfKeys = " + numOfKeys);
        for (int j = numOfKeys; j >= position + 1; j--)
            children.set(j + 1, children.get(j));

        // Link the new child to this node
        children.set(position + 1, newNode);

        // find the location of new key and move all greater keys one space ahead
        keys.add(null); values.add(null);
        for (int j = numOfKeys - 1; j >= position; j--) {
            keys.set(j + 1, keys.get(j));
            values.set(j + 1, values.get(j));
        }            

        // Copy the middle key of y to this node
        keys.set(position, (K)current.getKeys().get(minimumDegree - 1));
        values.set(position, (V)current.getValues().get(minimumDegree - 1));

        // Increment count of keys in this node
        numOfKeys++;
    }

    /**
     * traverse all nodes in a subtree rooted with this node in inorder traversal
     */
    public void traverse() {
//        int i;
//        // traverse through all keys and children except last child
//        for (i = 0; i < numOfKeys; i++) {
//            // If not leaf, traverse the subtree rooted ont its left first
//            if (!isLeaf)
//                children.get(i).traverse();
//            // print the key
//            System.out.println(keys.get(i) + ", " + values.get(i));
//        }
//
//        // Print the subtree rooted with last child
//        if (!isLeaf)
//            children.get(i).traverse();
        System.out.println("Node\n\tNumber Of Keys: "+ this.numOfKeys +"\n\tkeys: " + this.getKeys().toString() + "\n\tValues: " + this.getValues() + "\n\tChildren Size: " + this.getChildren().size());
        for(int i=0;i<this.getChildren().size();i++){
            System.out.println("Child "+i);
            this.getChildren().get(i).traverse();
        }
    }

    public V searchInBTree(K key){
        BTreeNode<K,V> node = searchForTheNode(key);

        if(node == null) return null;

        for(int i=0; i < node.getNumOfKeys(); i++){
            if (key.compareTo(node.getKeys().get(i)) == 0)
                return node.getValues().get(i);
        }
        return null;
    }

    private BTreeNode<K,V> searchForTheNode(K key){
        int i=0;
        List<K> keys = this.getKeys();

        for(i=0; i < this.getNumOfKeys(); i++){
            if(key.compareTo(keys.get(i)) < 0)
                break;
            else if (key.compareTo(keys.get(i)) == 0)
                return this;
        }

        if ( this.isLeaf() ) return null;
        else return getChildren().get(i).searchForTheNode(key);
    }

    private BTreeNode<K,V> parentOfNode (BTreeNode<K,V> node){
        if(this.isLeaf)
            return null;

        List<BTreeNode<K,V>> children = this.getChildren();

        if(children.contains(node)) {
            return this;
        } else {
            for(BTreeNode<K,V> c: children){
                if(c.parentOfNode(node)!=null){
                    return c.parentOfNode(node);
                }
            }
        }
        return null;
    }

    private int posOfKeyInNode(BTreeNode<K,V> node,K key){
        int i=0;
        for (i = 0; i < numOfKeys; i++) {
            if (node.getKeys().get(i) == key) {
                break;
            }
        }
        return i;
    }

    private int PosInParentChildren(BTreeNode<K,V> parentNode, BTreeNode<K,V> childNode){
        int i;
        for(i=0; i < parentNode.getChildren().size(); i++){
            if (parentNode.getChildren().get(i) == childNode)
                break;
        }
        return i;
    }

    private BTreeNode<K,V> getPredecessorNode(BTreeNode<K,V> node){
        return node;
    }

    private V getPredecessorValue(BTreeNode<K,V> node){
        List<V> nodeValues = node.getValues();
        return nodeValues.get(nodeValues.size()-1);
    }

    private K getPredecessorKey(BTreeNode<K,V> node){
        List<K> nodeKeys = node.getKeys();
        return nodeKeys.get(nodeKeys.size()-1);
    }

    private void removePredecessor(BTreeNode<K,V> node){

    }

    private BTreeNode<K,V> getSuccessorNode(BTreeNode<K,V> node){
        return node;
    }

    private V getSuccessorValue(BTreeNode<K,V> node){
        List<V> nodeValues = node.getValues();
        return nodeValues.get(0);
    }

    private K getSuccessorKeys(BTreeNode<K,V> node){
        List<K> nodeKeys = node.getKeys();
        return nodeKeys.get(0);
    }

    private void removeSuccessor(BTreeNode<K,V> node){

    }

    public void remove(K key){
        BTreeNode<K,V> currentNode = searchForTheNode(key);
        BTreeNode<K,V> parentNode = this.parentOfNode(currentNode);
        BTreeNode<K,V> neighbourNode;

        int keyPos = posOfKeyInNode(currentNode, key);

        if(currentNode.isLeaf) {
            if (currentNode.numOfKeys - 1 >= currentNode.minimumDegree - 1) {
                currentNode.getKeys().remove(keyPos);
                currentNode.getValues().remove(keyPos);
                currentNode.numOfKeys--;
            }else{
                int childPos = PosInParentChildren(parentNode, currentNode);
                if(childPos - 1 >= 0 && parentNode.getChildren().get(childPos-1).numOfKeys - 1 >= currentNode.minimumDegree - 1) {
                    neighbourNode = parentNode.getChildren().get(childPos - 1);

                    K keyToMoveUp = neighbourNode.getKeys().get(neighbourNode.numOfKeys-1);
                    V valueToMoveUp = neighbourNode.getValues().get(neighbourNode.numOfKeys-1);
                    K keyToMoveDown = parentNode.getKeys().get(childPos - 1);
                    V valueToMoveDown = parentNode.getValues().get(childPos - 1);

                    neighbourNode.getKeys().remove(neighbourNode.numOfKeys-1);
                    neighbourNode.getValues().remove(neighbourNode.numOfKeys-1);
                    neighbourNode.numOfKeys--;

                    parentNode.getKeys().set(childPos - 1, keyToMoveUp);
                    parentNode.getValues().set(childPos - 1, valueToMoveUp);

                    currentNode.getKeys().set(keyPos, keyToMoveDown);
                    currentNode.getValues().set(keyPos, valueToMoveDown);
                }else if(childPos + 1 < parentNode.getChildren().size() && parentNode.getChildren().get(childPos+1).numOfKeys - 1 >= currentNode.minimumDegree - 1){
                    neighbourNode = parentNode.getChildren().get(childPos + 1);

                    K keyToMoveUp = neighbourNode.getKeys().get(0);
                    V valueToMoveUp = neighbourNode.getValues().get(0);
                    K keyToMoveDown = parentNode.getKeys().get(childPos);
                    V valueToMoveDown = parentNode.getValues().get(childPos);

                    neighbourNode.getKeys().remove(0);
                    neighbourNode.getValues().remove(0);
                    neighbourNode.numOfKeys--;

                    parentNode.getKeys().set(childPos, keyToMoveUp);
                    parentNode.getValues().set(childPos, valueToMoveUp);

                    currentNode.getKeys().set(keyPos, keyToMoveDown);
                    currentNode.getValues().set(keyPos, valueToMoveDown);
                }else{
                    currentNode.getKeys().remove(keyPos);
                    currentNode.getValues().remove(keyPos);
                    currentNode.numOfKeys--;

                    List<K> newKeys = new ArrayList<>();
                    List<V> newValues = new ArrayList<>();

                    if(childPos - 1 >= 0){
                        K keyToMoveDown = parentNode.getKeys().get(childPos - 1);
                        V valueToMoveDown = parentNode.getValues().get(childPos - 1);

                        neighbourNode = parentNode.getChildren().get(childPos - 1);
                        newKeys.addAll(neighbourNode.getKeys());
                        newValues.addAll(neighbourNode.getValues());
                        newKeys.add(keyToMoveDown);
                        newValues.add(valueToMoveDown);
                        for(int i=0; i<currentNode.numOfKeys;i++){
                            newKeys.add(currentNode.getKeys().get(i));
                            newValues.add(currentNode.getValues().get(i));
                        }
                        currentNode.setKeys(newKeys);
                        currentNode.setValues(newValues);
                        currentNode.numOfKeys = currentNode.getValues().size();

                        parentNode.getChildren().remove(childPos-1);
                        parentNode.getKeys().remove(childPos-1);
                        parentNode.getValues().remove(childPos-1);
                        parentNode.numOfKeys--;
                    }else if (childPos + 1 < parentNode.getChildren().size()){
                        K keyToMoveDown = parentNode.getKeys().get(childPos);
                        V valueToMoveDown = parentNode.getValues().get(childPos);

                        for(int i=0; i<currentNode.numOfKeys;i++){
                            newKeys.add(currentNode.getKeys().get(i));
                            newValues.add(currentNode.getValues().get(i));
                        }
                        newKeys.add(keyToMoveDown);
                        newValues.add(valueToMoveDown);
                        neighbourNode = parentNode.getChildren().get(childPos + 1);
                        newKeys.addAll(neighbourNode.getKeys());
                        newValues.addAll(neighbourNode.getValues());
                        currentNode.setKeys(newKeys);
                        currentNode.setValues(newValues);
                        currentNode.numOfKeys = currentNode.getValues().size();

                        parentNode.getChildren().remove(childPos + 1);
                        parentNode.getKeys().remove(childPos);
                        parentNode.getValues().remove(childPos);
                        parentNode.numOfKeys--;
                    }
                }
            }
        }else{
            if(currentNode.getChildren().get(keyPos).numOfKeys - 1 >= currentNode.minimumDegree - 1){
                BTreeNode<K,V> predecessorNode = getPredecessorNode(currentNode);
                V newValue = getPredecessorValue(predecessorNode);
                K newKey = getPredecessorKey(predecessorNode);

                removePredecessor(predecessorNode);

                currentNode.getValues().set(keyPos, newValue);
                currentNode.getKeys().set(keyPos, newKey);
            }else if(keyPos + 1 < currentNode.getChildren().size() && currentNode.getChildren().get(keyPos+1).numOfKeys - 1 >= currentNode.minimumDegree - 1 ){
                BTreeNode<K,V> successorNode = getSuccessorNode(currentNode);
                V newValue = getSuccessorValue(successorNode);
                K newKey = getSuccessorKeys(successorNode);

                removePredecessor(successorNode);

                currentNode.getValues().set(keyPos, newValue);
                currentNode.getKeys().set(keyPos, newKey);
            }

        }
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
