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

        System.out.println("Node\n\tDegree: "+this.minimumDegree+"\tLeaf: "+ this.isLeaf+"\tNumberOfKeys: "+ this.numOfKeys+"\n\tKeys: "+this.getKeys().toString()+"\n\tValues: "+this.getValues().toString()+"\tNumberOfChildren: "+this.getChildren().size());
        for(int i=0;i<this.getChildren().size();i++){
            System.out.println();
            this.getChildren().get(i).traverse();
//         int i;
//         // traverse through all keys and children except last child
//         for (i = 0; i < numOfKeys; i++) {
//             // If not leaf, traverse the subtree rooted ont its left first
//             if (!isLeaf)
//                 children.get(i).traverse();
//             // print the key
//             System.out.println(keys.get(i) + ", " + values.get(i) + ", #keys = " + numOfKeys
//              + ", children:");
//             for(int j = 0; j < children.size(); j++) {
//                 System.out.println("child " + j);
//                 System.out.println("keys:--> " + children.get(j).getKeys());
//                 System.out.println("values:--> " + children.get(j).getValues());
//             }
//             System.out.println("-------------------");

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
        for (i = 0; i < node.numOfKeys; i++) {
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
        if(node.isLeaf) return node;
        else return getPredecessorNode(node.getChildren().get(node.getChildren().size()-1));
    }

    private V getPredecessorValue(BTreeNode<K,V> node){
        List<V> nodeValues = node.getValues();
        return nodeValues.get(nodeValues.size()-1);
    }

    private K getPredecessorKey(BTreeNode<K,V> node){
        List<K> nodeKeys = node.getKeys();
        return nodeKeys.get(nodeKeys.size()-1);
    }

    private BTreeNode<K,V> getSuccessorNode(BTreeNode<K,V> node){
        if(node.isLeaf) return node;
        else return getPredecessorNode(node.getChildren().get(0));
    }

    private V getSuccessorValue(BTreeNode<K,V> node){
        List<V> nodeValues = node.getValues();
        return nodeValues.get(0);
    }

    private K getSuccessorKeys(BTreeNode<K,V> node){
        List<K> nodeKeys = node.getKeys();
        return nodeKeys.get(0);
    }

    private void checkIfMin(BTreeNode<K,V> currentNode, int keyPos){
        BTreeNode<K,V> parentNode= this.parentOfNode(currentNode);
        BTreeNode<K,V> neighbourNode;
        BTreeNode<K,V> leftChild;
        BTreeNode<K,V> rightChild;
        if(parentNode == null || currentNode.numOfKeys >= currentNode.minimumDegree - 1){
            return;
        }else{
            int childPos = PosInParentChildren(parentNode, currentNode);

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
            }else{
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

    public void remove(K key){
        BTreeNode<K,V> currentNode = searchForTheNode(key);
        if(currentNode == null) return;
        BTreeNode<K,V> parentNode = this.parentOfNode(currentNode);
        BTreeNode<K,V> neighbourNode;

        int keyPos = posOfKeyInNode(currentNode, key);

        if(currentNode.isLeaf) {
            if (currentNode.numOfKeys - 1 >= currentNode.minimumDegree - 1 || parentNode == null) {
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
                        newKeys.addAll(currentNode.getKeys());
                        newValues.addAll(currentNode.getValues());

                        currentNode.setKeys(newKeys);
                        currentNode.setValues(newValues);
                        currentNode.numOfKeys = currentNode.getValues().size();

                        parentNode.getChildren().remove(childPos-1);
                        parentNode.getKeys().remove(childPos-1);
                        parentNode.getValues().remove(childPos-1);
                        parentNode.numOfKeys--;
                        checkIfMin(parentNode, keyPos);
                    }else if (childPos + 1 < parentNode.getChildren().size()){
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
                        checkIfMin(parentNode, keyPos);
                    }
                }
            }
        }else{

            BTreeNode<K, V> predecessorNode = getPredecessorNode(currentNode.getChildren().get(keyPos));
            BTreeNode<K, V> successorNode = getSuccessorNode(currentNode.getChildren().get(keyPos+1));

            if(predecessorNode.numOfKeys - 1 >= predecessorNode.minimumDegree - 1){
                V newValue = getPredecessorValue(predecessorNode);
                K newKey = getPredecessorKey(predecessorNode);
                currentNode.getKeys().set(keyPos, newKey);
                currentNode.getValues().set(keyPos, newValue);
                predecessorNode.remove(newKey);
            }else if(successorNode.numOfKeys - 1 >= successorNode.minimumDegree - 1){
                V newValue = getSuccessorValue(successorNode);
                K newKey = getSuccessorKeys(successorNode);
                currentNode.getKeys().set(keyPos, newKey);
                currentNode.getValues().set(keyPos, newValue);
                successorNode.remove(newKey);
            }else{
                List<K> newKeys = new ArrayList<>();
                List<V> newValues = new ArrayList<>();
                BTreeNode<K,V> leftChild = currentNode.getChildren().get(keyPos);
                BTreeNode<K,V> rightChild = currentNode.getChildren().get(keyPos + 1);
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
                    checkIfMin(currentNode, keyPos);
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
