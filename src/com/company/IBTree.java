package com.company;

public interface IBTree<K extends Comparable<K>, V> {
    /**
     * Return the minimum degree o f the given Btree .
     * The minimum degree of the Btree is sent as a parameter to
     * the constructor .
     * @return
     */
    public int getMinimumDegree();

    /**
     * Return the root o f the given Btree .
     * @return
     */
    public IBTreeNode<K, V> getRoot();

    /**
     * Insert the given key in the Btree . If the key is already
     * in the Btree, ignore the call of this method .
     * @param key
     * @param value
     */
    public void insert(K key, V value);

    /**
     * Search f o r the given key in the BTree .
     * @param key
     * @return
     */
    public V search (K key ) ;

    /**
     * Delete the node with the given key from the Btree .
     * Return true in case o f s u c c e ss and f a l s e o the rw is e .
     * @param key
     * @return
     */
    public boolean delete (K key ) ;
}