package com.company.BTreeNode;

import java.util.*;

public class BTreeNode <K extends Comparable <K>, V> implements IBTreeNode {
    private int numOfKeys;
    private final int minimumDegree;
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

    /**
     * traverse all nodes in a subtree rooted with this node in inorder traversal
     */
    public void traverse() {

        System.out.println("Node\n\tDegree: "+this.minimumDegree+"\tLeaf: "+ this.isLeaf+"\tNumberOfKeys: "+ this.numOfKeys+"\n\tKeys: "+this.getKeys().toString()+"\n\tValues: "+this.getValues().toString()+"\tNumberOfChildren: "+this.getChildren().size());
        for(int i=0;i<this.getChildren().size();i++){
            System.out.println();
            this.getChildren().get(i).traverse();
        }
    }

    /**
     * search for a key in B-tree
     * @param key The key to search for
     * @return The value that are stored with the key if the key is in the B-tree
     */
    public V searchInBTree(K key){
        BTreeNode<K,V> node = searchForTheNode(key);

        if(node == null) return null;

        for(int i=0; i < node.getNumOfKeys(); i++){
            if (key.compareTo(node.getKeys().get(i)) == 0)
                return node.getValues().get(i);
        }
        return null;
    }

    /**
     * Function to search for the node that has a certain key
     * @param key The key that the node has
     * @return The node contains the key
     */
    private BTreeNode<K,V> searchForTheNode(K key) {
        int i;
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

    /**
     * Function to search for the parent of a given node
     * @param node The children node of the parent node to search for
     * @return The parent node of a given node
     */
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

    /**
     * Function to get the location of a key in a node
     * @param node The node that has the key
     * @param key The key in the node
     * @return The index of the key in the node
     */
    private int posOfKeyInNode(BTreeNode<K,V> node,K key){
        int i;
        for (i = 0; i < node.numOfKeys; i++) {
            if (node.getKeys().get(i) == key) {
                break;
            }
        }
        return i;
    }

    /**
     * Function to get the location a child node inside a parent node
     * @param parentNode Parent node that has the given child node
     * @param childNode Child node of the parent node
     * @return The index of child in the parent node
     */
    private int PosInParentChildren(BTreeNode<K,V> parentNode, BTreeNode<K,V> childNode){
        int i;
        for(i=0; i < parentNode.getChildren().size(); i++){
            if (parentNode.getChildren().get(i) == childNode)
                break;
        }
        return i;
    }

    /**
     * Function to get the predecessor node that has the predecessor key
     * @param node The left child node from which we search for the predecessor key to replace a key that will be deleted
     * @return The node that has the predecessor key
     */
    private BTreeNode<K,V> getPredecessorNode(BTreeNode<K,V> node){
        if(node.isLeaf) return node;
        else return getPredecessorNode(node.getChildren().get(node.getChildren().size()-1));
    }

    /**
     * Function to get the predecessor value from the predecessor node
     * @param node The predecessor node
     * @return The predecessor value
     */
    private V getPredecessorValue(BTreeNode<K,V> node){
        List<V> nodeValues = node.getValues();
        return nodeValues.get(nodeValues.size()-1);
    }

    /**
     * Function to get the predecessor key from the predecessor node
     * @param node The predecessor node
     * @return The predecessor key
     */
    private K getPredecessorKey(BTreeNode<K,V> node){
        List<K> nodeKeys = node.getKeys();
        return nodeKeys.get(nodeKeys.size()-1);
    }

    /**
     * Function to get the successor node that has the successor key
     * @param node The right child node from which we search for the successor key to replace a key that will be deleted
     * @return The node that has the successor key
     */
    private BTreeNode<K,V> getSuccessorNode(BTreeNode<K,V> node){
        if(node.isLeaf) return node;
        else return getPredecessorNode(node.getChildren().get(0));
    }

    /**
     * Function to get the successor value from the successor node
     * @param node The successor node
     * @return The successor value
     */
    private V getSuccessorValue(BTreeNode<K,V> node){
        List<V> nodeValues = node.getValues();
        return nodeValues.get(0);
    }

    /**
     * Function to get the successor key from the successor node
     * @param node The successor node
     * @return The successor key
     */
    private K getSuccessorKeys(BTreeNode<K,V> node){
        List<K> nodeKeys = node.getKeys();
        return nodeKeys.get(0);
    }

    /**
     * Function to check if a node has less than the minimum number of keys and fix the node
     * @param currentNode The node to be checked
     * @param keyPos The index of the last key that has been deleted
     */
    private void checkIfMin(BTreeNode<K,V> currentNode, int keyPos){
        BTreeNode<K,V> parentNode= this.parentOfNode(currentNode);
        BTreeNode<K,V> neighbourNode;
        BTreeNode<K,V> leftChild;
        BTreeNode<K,V> rightChild;

        // if the node is the root or the node has more or equal number of keys than the minimum number of keys in a node
        if(parentNode == null || currentNode.numOfKeys >= currentNode.minimumDegree - 1){
            return;
        // the node has keys less than the minimum number of keys in a node
        }else{
            int childPos = PosInParentChildren(parentNode, currentNode);

            // look for a left sibling and see if it can donate a key to the node through the parent node
            if(childPos - 1 >= 0 && parentNode.getChildren().get(childPos-1).numOfKeys - 1 >= currentNode.minimumDegree - 1) {
                neighbourNode = parentNode.getChildren().get(childPos - 1);
                V valueToMoveUp = neighbourNode.getValues().get(neighbourNode.getValues().size()-1);
                K keyToMoveUp = neighbourNode.getKeys().get(neighbourNode.getKeys().size()-1);
                V valueToMoveDown = parentNode.getValues().get(parentNode.getValues().size()-1);
                K keyToMoveDown = parentNode.getKeys().get(parentNode.getKeys().size()-1);

                BTreeNode<K,V> childNode = neighbourNode.getChildren().get(neighbourNode.getChildren().size()-1);
                neighbourNode.getChildren().remove(neighbourNode.getChildren().size()-1);

                currentNode.getKeys().add(0,keyToMoveDown);
                currentNode.getValues().add(0,valueToMoveDown);

                parentNode.getKeys().remove(parentNode.getKeys().size()-1);
                parentNode.getValues().remove(parentNode.getValues().size()-1);
                parentNode.getKeys().add(0,keyToMoveUp);
                parentNode.getValues().add(0,valueToMoveUp);

                neighbourNode.getValues().remove(neighbourNode.getValues().size()-1);
                neighbourNode.getKeys().remove(neighbourNode.getKeys().size()-1);
                neighbourNode.numOfKeys--;

                leftChild = currentNode.getChildren().get(keyPos);
                rightChild = currentNode.getChildren().get(keyPos+1);

                rightChild.getValues().addAll(0,leftChild.getValues());
                rightChild.getKeys().addAll(0,leftChild.getKeys());
                currentNode.getChildren().set(keyPos,childNode);

            // look for a right sibling and see if it can donate a key to the node through the parent node
            }else if(childPos + 1 < parentNode.getChildren().size() && parentNode.getChildren().get(childPos+1).numOfKeys - 1 >= currentNode.minimumDegree - 1){
                neighbourNode = parentNode.getChildren().get(childPos + 1);
                V valueToMoveUp = neighbourNode.getValues().get(0);
                K keyToMoveUp = neighbourNode.getKeys().get(0);
                V valueToMoveDown = parentNode.getValues().get(0);
                K keyToMoveDown = parentNode.getKeys().get(0);

                BTreeNode<K,V> childNode = neighbourNode.getChildren().get(0);
                neighbourNode.getChildren().remove(0);

                currentNode.getKeys().add(keyToMoveDown);
                currentNode.getValues().add(valueToMoveDown);

                parentNode.getKeys().remove(0);
                parentNode.getValues().remove(0);
                parentNode.getKeys().add(keyToMoveUp);
                parentNode.getValues().add(valueToMoveUp);

                neighbourNode.getValues().remove(0);
                neighbourNode.getKeys().remove(0);
                neighbourNode.numOfKeys--;

                leftChild = currentNode.getChildren().get(keyPos);
                rightChild = currentNode.getChildren().get(keyPos+1);

                leftChild.getValues().addAll(rightChild.getValues());
                leftChild.getKeys().addAll(rightChild.getKeys());
                currentNode.getChildren().set(keyPos+1,childNode);
            // if the left and right siblings has minimum number of keys
            }else{
                // check if there is a left sibling to merge it with the node in the parent node
                if(childPos - 1 >= 0){
                    BTreeNode<K,V> leftNode = parentNode.getChildren().get(childPos);
                    parentNode.getValues().addAll(childPos,currentNode.getValues());
                    parentNode.getValues().addAll(childPos - 1,leftNode.getValues());
                    parentNode.getKeys().addAll(childPos,currentNode.getKeys());
                    parentNode.getKeys().addAll(childPos - 1,leftNode.getKeys());

                    parentNode.getChildren().remove(childPos-1);
                    parentNode.getChildren().addAll(childPos-1, leftNode.getChildren());
                    if(currentNode.numOfKeys == 0) {
                        BTreeNode<K, V> rightLeaf = currentNode.getChildren().get(keyPos);
                        BTreeNode<K, V> leftLeaf = currentNode.getChildren().get(keyPos + 1);
                        currentNode.getChildren().remove(keyPos);
                        currentNode.getChildren().remove(keyPos + 1);
                        parentNode.getChildren().addAll(childPos, currentNode.getChildren());

                        rightLeaf.getKeys().addAll(leftLeaf.getKeys());
                        rightLeaf.getValues().addAll(leftLeaf.getValues());
                        rightLeaf.getChildren().addAll(leftLeaf.getChildren());

                        currentNode.setValues(rightLeaf.getValues());
                        currentNode.setKeys(rightLeaf.getKeys());
                        currentNode.setChildren(rightLeaf.getChildren());
                    }else{
                        parentNode.getChildren().remove(childPos);
                        parentNode.getChildren().addAll(childPos,currentNode.getChildren());
                    }

                // check if there is a right sibling to merge it with the node in the parent node
                }else if(childPos + 1 < parentNode.getChildren().size()){
                    BTreeNode<K,V> rightNode = parentNode.getChildren().get(childPos + 1);

                    parentNode.getValues().addAll(childPos + 1,rightNode.getValues());
                    parentNode.getValues().addAll(childPos,currentNode.getValues());

                    parentNode.getKeys().addAll(childPos + 1,rightNode.getKeys());
                    parentNode.getKeys().addAll(childPos,currentNode.getKeys());

                    parentNode.getChildren().remove(childPos+1);
                    parentNode.getChildren().addAll(childPos+1, rightNode.getChildren());
                    if(currentNode.numOfKeys == 0){
                        BTreeNode<K,V> rightLeaf = currentNode.getChildren().get(keyPos);
                        BTreeNode<K,V> leftLeaf = currentNode.getChildren().get(keyPos + 1);
                        currentNode.getChildren().remove(keyPos);
                        currentNode.getChildren().remove(keyPos+1);
                        parentNode.getChildren().addAll(childPos,currentNode.getChildren());

                        rightLeaf.getKeys().addAll(leftLeaf.getKeys());
                        rightLeaf.getValues().addAll(leftLeaf.getValues());
                        rightLeaf.getChildren().addAll(leftLeaf.getChildren());

                        currentNode.setValues(rightLeaf.getValues());
                        currentNode.setKeys(rightLeaf.getKeys());
                        currentNode.setChildren(rightLeaf.getChildren());
                    }else{
                        parentNode.getChildren().remove(childPos);
                        parentNode.getChildren().addAll(childPos,currentNode.getChildren());
                    }
                }
            }
        }
    }

    /**
     * Function to remove a key from a B-tree node
     * @param key The key to be deleted
     */
    public void remove(K key){
        BTreeNode<K,V> currentNode = searchForTheNode(key);
        if(currentNode == null) return;
        BTreeNode<K,V> parentNode = this.parentOfNode(currentNode);
        BTreeNode<K,V> neighbourNode;

        int keyPos = posOfKeyInNode(currentNode, key);

        if(currentNode.isLeaf) {    // if the key is in a leaf node
            // if removing the key won't violate the B-tree minimum number of key in a node or the node is the root then remove the key
            if (currentNode.numOfKeys - 1 >= currentNode.minimumDegree - 1 || parentNode == null) {
                currentNode.getKeys().remove(keyPos);
                currentNode.getValues().remove(keyPos);
                currentNode.numOfKeys--;
            }else{  // removing would violate the B-tree minimum number of key in a node
                int childPos = PosInParentChildren(parentNode, currentNode);
                // if the node has a left sibling which can donate a key to the node through the parent node then perform the donation
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

                // if the node has a right sibling which can donate a key to the node through the parent node then perform the donation
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

                }else{ // if the left and right siblings nodes have a minimum number of keys then merge the node with sibling through the parent node
                    currentNode.getKeys().remove(keyPos);
                    currentNode.getValues().remove(keyPos);
                    currentNode.numOfKeys--;

                    List<K> newKeys = new ArrayList<>();
                    List<V> newValues = new ArrayList<>();

                    if(childPos - 1 >= 0){ // look first if the node has a left sibling
                        K keyToMoveDown = parentNode.getKeys().get(childPos - 1);
                        V valueToMoveDown = parentNode.getValues().get(childPos - 1);

                        neighbourNode = parentNode.getChildren().get(childPos - 1);
                        newKeys.addAll(neighbourNode.getKeys());
                        newValues.addAll(neighbourNode.getValues());
                        newKeys.add(keyToMoveDown);
                        newValues.add(valueToMoveDown);
                        newKeys.addAll(currentNode.getKeys());
                        newValues.addAll(currentNode.getValues());

                        currentNode.setKeys(newKeys);
                        currentNode.setValues(newValues);
                        currentNode.numOfKeys = currentNode.getValues().size();

                        parentNode.getChildren().remove(childPos-1);
                        parentNode.getKeys().remove(childPos-1);
                        parentNode.getValues().remove(childPos-1);
                        parentNode.numOfKeys--;
                        checkIfMin(parentNode, keyPos); // check if the parent node has less than minimum number of keys in the node
                    }else if (childPos + 1 < parentNode.getChildren().size()){ // use the right sibling when there is no left sibling
                        K keyToMoveDown = parentNode.getKeys().get(childPos);
                        V valueToMoveDown = parentNode.getValues().get(childPos);
                        newKeys.addAll(currentNode.getKeys());
                        newValues.addAll(currentNode.getValues());
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
                        checkIfMin(parentNode, keyPos); // check if the parent node has less than minimum number of keys in the node
                    }
                }
            }
        }else{ // if the node was not a leaf node

            BTreeNode<K, V> predecessorNode = getPredecessorNode(currentNode.getChildren().get(keyPos));
            BTreeNode<K, V> successorNode = getSuccessorNode(currentNode.getChildren().get(keyPos+1));

            // if replacing the key to be deleted with the predecessor key won't make the predecessor node has less than the minimum number of key then make the replacement
            if(predecessorNode.numOfKeys - 1 >= predecessorNode.minimumDegree - 1){
                V newValue = getPredecessorValue(predecessorNode);
                K newKey = getPredecessorKey(predecessorNode);
                currentNode.getKeys().set(keyPos, newKey);
                currentNode.getValues().set(keyPos, newValue);
                predecessorNode.remove(newKey);

            // if replacing the key to be deleted with the successor key won't make the successor node has less than the minimum number of key then make the replacement
            }else if(successorNode.numOfKeys - 1 >= successorNode.minimumDegree - 1){
                V newValue = getSuccessorValue(successorNode);
                K newKey = getSuccessorKeys(successorNode);
                currentNode.getKeys().set(keyPos, newKey);
                currentNode.getValues().set(keyPos, newValue);
                successorNode.remove(newKey);

            // if replacing with predecessor or successor key will violate the min number of keys in the predecessor or successor node then replace with predecessor and fix the node
            }else{
                List<K> newKeys = new ArrayList<>();
                List<V> newValues = new ArrayList<>();
                // left and right child nodes of the key to be removed
                BTreeNode<K,V> leftChild = currentNode.getChildren().get(keyPos);
                BTreeNode<K,V> rightChild = currentNode.getChildren().get(keyPos + 1);

                // if the node (that has the key to be deleted) has the predecessor or successor node then merge them and remove the key from the node
                if(currentNode.getChildren().contains(predecessorNode) || currentNode.getChildren().contains(successorNode)){
                    newValues.addAll(leftChild.getValues());
                    newKeys.addAll(leftChild.getKeys());
                    newValues.addAll(rightChild.getValues());
                    newKeys.addAll(rightChild.getKeys());
                    rightChild.setValues(newValues);
                    rightChild.setKeys(newKeys);

                    currentNode.getChildren().remove(keyPos);
                    currentNode.getValues().remove(keyPos);
                    currentNode.getKeys().remove(keyPos);
                    currentNode.numOfKeys--;
                    checkIfMin(currentNode, keyPos); // check if the node has less than the minimum number of keys
                // make the replacement and delete the predecessor key from the predecessor node
                }else{
                    V newValue = getPredecessorValue(predecessorNode);
                    K newKey = getPredecessorKey(predecessorNode);
                    currentNode.getKeys().set(keyPos, newKey);
                    currentNode.getValues().set(keyPos, newValue);
                    predecessorNode.remove(newKey);
                }
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

    @Override
    public List<V> getValues() {
        return values;
    }

    @Override
    public void setValues(List values) {
        this.values = values;
    }

    @Override
    public List<BTreeNode<K, V>> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

}
