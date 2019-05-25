package edu.sdsu.cs.datastructures;
import java.util.*;

public class UnbalancedMap<K extends Comparable<K>, V> implements IMap<K, V> {
   //Awale Ahmed
    private Node<K, V> root, parent;
    private int currentSize, modificationCounter;
    private K k;
    private boolean wasLeft;
    private Comparator<? super K> comparator;

    public UnbalancedMap() {
        root = parent = null;
        currentSize = modificationCounter = 0;
        comparator = null;
    }

    public UnbalancedMap(IMap<K, V> original) {
        if (original == null) {
            root = new Node();
            currentSize = 0;
            comparator = null;
        } else {
            root = ((UnbalancedMap) original).getRoot();
            comparator = ((UnbalancedMap) original).getComporator();
            currentSize = (original).size();
        }
    }

    private Comparator<? super K> getComporator() {
        return comparator;
    }

    private Node getRoot() {
        return root;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> leftChild, rightChild;

        public Node(K k, V v) {
            key = k;
            value = v;
            leftChild = rightChild = null;
        }

        public Node() {
            key = null;
            value = null;
            leftChild = rightChild = null;
        }

    }


    @Override
    public boolean contains(K key) {
        if (findValue(key, root) == null)
            return false;
        return true;
    }

    @Override
    public boolean add(K key, V value) {
        if (isFull()) return false;
        if (contains(key)) return false;
        if (root == null)
            root = new Node<K, V>(key, value);
        else
            insert(key, value, root, null, false);
        currentSize++;
        modificationCounter++;
        return true;
    }

    @Override
    public V delete(K key) {
        V v = getValue(key);
        if (!contains(key)) return null;
        if (currentSize == 1 && ((Comparable<K>) key).compareTo(root.key) == 0)
            root = null;
        else {
            Node<K, V> node = find(key, root);
            delete(node, parent);
        }
        currentSize--;
        return v;
    }

    @Override
    public V getValue(K key) {
        return findValue(key, root);
    }

    @Override
    public K getKey(V value) {
        k = null;
        findKey(root, value);
        return k;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        List<K> keys = new ArrayList<K>();
        List<K> listkeys = listKeys();


        for (K mykey : listkeys) {
            keys.add(getKey(value));

        }

        return keys;

    }

    @Override
    public int size() {

        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        if (currentSize == 0) return true;
        return false;
    }

    @Override
    public void clear() {
        root = null;
        currentSize = 0;
        modificationCounter = 0;
    }

    @Override
    public Iterable<K> keyset() {
        List toReturn = new LinkedList();

        return toReturn;
    }

    private List<K> listKeys() {
        List toReturn = new LinkedList();
        Node<K, V> curr = root;

        if (curr != null) {
            currentSize++;

            while (curr.rightChild != null || curr.leftChild != null) {
                curr = curr.rightChild;
                toReturn.add(curr.key);

                if (curr.leftChild != null) {
                    curr = curr.leftChild;
                }

            }


        }


        return toReturn;
    }

    @Override
    public Iterable<V> values() {
        List toReturn = new LinkedList();

        return toReturn;
    }

    private void insert(K key, V value, Node<K, V> n, Node<K, V> parent,
                        boolean wasLeft) {
        if (n == null) {
            if (wasLeft) parent.leftChild = new Node<K, V>(key, value);
            else parent.rightChild = new Node<K, V>(key, value);
        } else if (((Comparable<K>) key).compareTo((K) n.key) < 0)
            insert(key, value, n.leftChild, n, true);
        else
            insert(key, value, n.rightChild, n, false);
    }

    private void delete(Node<K, V> n, Node<K, V> parent) {
        if (n.leftChild == null && n.rightChild == null) {
            if (wasLeft) parent.leftChild = null;
            else parent.rightChild = null;
        } else if (n.leftChild == null || n.rightChild == null) {
            if (n == root) {
                if (n.leftChild == null) {
                    root = n.rightChild;
                    n.rightChild = null;
                } else {
                    root = n.leftChild;
                    n.leftChild = null;
                }
            } else if (n.leftChild == null && wasLeft) {
                parent.leftChild = n.rightChild;
                n.rightChild = null;
            } else if (n.leftChild == null && !wasLeft) {
                parent.rightChild = n.rightChild;
                n.rightChild = null;
            } else if (n.rightChild == null && wasLeft) {
                parent.leftChild = n.leftChild;
                n.leftChild = null;
            } else {
                parent.rightChild = n.leftChild;
                n.leftChild = null;
            }
        } else {
            Node<K, V> rightNode, leftNode, successor;
            leftNode = n.leftChild;
            rightNode = successor = n.rightChild;
            if (successor.leftChild == null) {
                n.key = successor.key;
                n.value = successor.value;
                n.rightChild = successor.rightChild;
            } else {
                while (successor.leftChild != null) {
                    rightNode = successor;
                    successor = successor.leftChild;
                }
                if (successor.rightChild == null) {
                    n.key = successor.key;
                    n.value = successor.value;
                    rightNode.leftChild = null;
                } else {
                    n.key = successor.key;
                    n.value = successor.value;
                    rightNode.leftChild = successor.rightChild;
                }
            }
        }

    }

    private Node<K, V> find(K key, Node<K, V> n) {
        if (n == null) return null;
        if (((Comparable<K>) key).compareTo(n.key) < 0) {
            wasLeft = true;
            parent = n;
            return find(key, n.leftChild);
        } else if (((Comparable<K>) key).compareTo(n.key) > 0) {
            wasLeft = false;
            parent = n;
            return find(key, n.rightChild);
        } else
            return n;
    }

    private V findValue(K key, Node<K, V> n) {
        if (n == null) return null;
        if (((Comparable<K>) key).compareTo(n.key) < 0)
            return findValue(key, n.leftChild);
        else if (((Comparable<K>) key).compareTo(n.key) > 0)
            return findValue(key, n.rightChild);
        else
            return (V) n.value;
    }

    private void findKey(Node<K, V> n, V value) {
        if (n == null) return;
        if (((Comparable<V>) value).compareTo(n.value) == 0) {
            k = n.key;
            return;
        }
        findKey(n.leftChild, value);
        findKey(n.rightChild, value);
    }

    private boolean isFull() {
        return false;
    }

}
