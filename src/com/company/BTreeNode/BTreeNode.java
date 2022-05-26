package com.company.BTreeNode;

import java.util.*;

public class BTreeNode <K extends Comparable <K>, V> implements IBTreeNode {
    private int numOfKeys;
    private final int minimumDegree;

//    public int getMinimumDegree() {
//        return minimumDegree;
//    }
//
//    public void setMinimumDegree(int minimumDegree) {
//        this.minimumDegree = minimumDegree;
//    }

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

//    public BTreeNode(int degree, boolean leaf, K key, V value) {
//        this.minimumDegree = degree;
//        this.isLeaf = leaf;
//        // new list of children
//        children = new ArrayList<>();
//        // new list of keys
//        keys = new ArrayList<>();
//        // add the key to the list
//        keys.add(key);
//        numOfKeys = 1; // Update number of keys
//        // new list of values
//        values = new ArrayList<>();
//        // add the value to the list
//        values.add(value);
//    }

//     public void splitChild(int position, BTreeNode current) {
//         BTreeNode newNode = new BTreeNode(current.minimumDegree, current.isLeaf());
//         newNode.numOfKeys = minimumDegree - 1;
//
//         // Copy the last (t - 1) keys of current node to the new node
//         for (int j = 0; j < minimumDegree - 1; j++) {
//             newNode.getKeys().add(current.getKeys().get(j + minimumDegree));
//             newNode.getValues().add(current.getValues().get(j + minimumDegree));
//         }
//
//
//         // Copy the last t children of current node to the new node
//         if (!current.isLeaf())
//             for (int j = 0; j < minimumDegree; j++)
//                 newNode.getChildren().add(current.getChildren().get(j + minimumDegree));
//
//         // Reduce the number of keys in current node
//         current.numOfKeys = minimumDegree - 1;
//
//         // create space of new child
//         children.add(null); // temporary addition
//         // System.out.println("children size = " + children.size() + "numOfKeys = " + numOfKeys);
//         for (int j = numOfKeys; j >= position + 1; j--)
//             children.set(j + 1, children.get(j));
//
//         // Link the new child to this node
//         children.set(position + 1, newNode);
//
//         // find the location of new key and move all greater keys one space ahead
//         keys.add(null); values.add(null);
//         for (int j = numOfKeys - 1; j >= position; j--) {
//             keys.set(j + 1, keys.get(j));
//             values.set(j + 1, values.get(j));
//         }
//
//         // Copy the middle key of y to this node
//         keys.set(position, (K)current.getKeys().get(minimumDegree - 1));
//         values.set(position, (V)current.getValues().get(minimumDegree - 1));
//
//         // Increment count of keys in this node
//         numOfKeys++;
//     }

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
            System.out.println(keys.get(i) + ", " + values.get(i) + ", #keys = " + numOfKeys
             + ", children:");
            for(int j = 0; j < children.size(); j++) {
                System.out.println("child " + j + " --> " + children.get(j).getKeys());
            }
            System.out.println("-------------------");
        }

        // Print the subtree rooted with last child
        if (!isLeaf)
            children.get(i).traverse();
    }

    public V searchInBTree(K key){
        BTreeNode<K,V> node = searchForTheNode(key);

        if(node == null) return null;

        for(int i=0; i < this.getNumOfKeys(); i++){
            if (key.compareTo(keys.get(i)) == 0)
                return this.getValues().get(i);
        }
        return null;
    }

    public BTreeNode<K,V> searchForTheNode(K key){
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

    public BTreeNode<K,V> parentOfNode (BTreeNode<K,V> node){
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

    public int posOfKeyInNode(BTreeNode<K,V> node,K key){
        int i=0;
        for (i = 0; i < numOfKeys; i++) {
            if (node.getKeys().get(i) == key) {
                break;
            }
        }
        return i;
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
                int pos = findPosInParentChildren(parentNode, currentNode);
                if(pos-1 >=0 && parentNode.getChildren().get(pos-1).numOfKeys >= currentNode.minimumDegree) {
                    neighbourNode = parentNode.getChildren().get(pos - 1);

                    K keyToMoveUp = neighbourNode.getKeys().get(neighbourNode.getKeys().size()-1);
                    V valueToMoveUp = neighbourNode.getValues().get(neighbourNode.getValues().size()-1);
                    K keyToMoveDown = parentNode.getKeys().get(pos - 1);
                    V valueToMoveDown = parentNode.getValues().get(pos - 1);

                    neighbourNode.getKeys().remove(neighbourNode.getKeys().size()-1);
                    neighbourNode.getValues().remove(neighbourNode.getValues().size()-1);
                    neighbourNode.numOfKeys--;
                    parentNode.getKeys().set(pos - 1, keyToMoveUp);
                    parentNode.getValues().set(pos - 1, valueToMoveUp);

                    currentNode.getKeys().set(keyPos, keyToMoveDown);
                    currentNode.getValues().set(keyPos, valueToMoveDown);
                }else if(pos <= parentNode.getChildren().size()-1 && parentNode.getChildren().get(pos+1).numOfKeys >= currentNode.minimumDegree){
                    neighbourNode = parentNode.getChildren().get(pos + 1);

                    K keyToMoveUp = neighbourNode.getKeys().get(0);
                    V valueToMoveUp = neighbourNode.getValues().get(0);
                    K keyToMoveDown = parentNode.getKeys().get(pos);
                    V valueToMoveDown = parentNode.getValues().get(pos);

                    neighbourNode.getKeys().remove(0);
                    neighbourNode.getValues().remove(0);
                    neighbourNode.numOfKeys--;
                    parentNode.getKeys().set(pos, keyToMoveUp);
                    parentNode.getValues().set(pos, valueToMoveUp);

                    currentNode.getKeys().set(keyPos, keyToMoveDown);
                    currentNode.getValues().set(keyPos, valueToMoveDown);
                }else{
                    currentNode.getKeys().remove(keyPos);
                    currentNode.getValues().remove(keyPos);
                    List<K> newKeys = new ArrayList<>();
                    List<V> newValues = new ArrayList<>();
                    currentNode.numOfKeys--;
                    if(pos - 1 >= 0){
                        K keyToMoveDown = parentNode.getKeys().get(pos - 1);
                        V valueToMoveDown = parentNode.getValues().get(pos - 1);

                        neighbourNode = parentNode.getChildren().get(pos - 1);
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
                        parentNode.getChildren().remove(pos-1);
                        parentNode.getKeys().remove(pos-1);
                        parentNode.getValues().remove(pos-1);
                        parentNode.numOfKeys--;
                    }else if (pos <= parentNode.getChildren().size()-1){
                        K keyToMoveDown = parentNode.getKeys().get(pos);
                        V valueToMoveDown = parentNode.getValues().get(pos);

                        neighbourNode = parentNode.getChildren().get(pos + 1);
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
                        parentNode.getChildren().remove(pos + 1);
                        parentNode.getKeys().remove(pos);
                        parentNode.getValues().remove(pos);
                        parentNode.numOfKeys--;
                    }
                }
            }
        }
    }

    public int findPosInParentChildren(BTreeNode parentNode, BTreeNode childNode){
        int i;
        for(i=0; i < parentNode.getChildren().size(); i++){
            if (parentNode.getChildren().get(i) == childNode)
                break;
        }
        return i;
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
