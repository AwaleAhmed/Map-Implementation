package edu.sdsu.cs.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;

public class BalancedMap<K extends Comparable<K>, V> implements IMap<K, V> {
    //Awale Ahmed
    TreeMap<K, V> map = new TreeMap<K, V>();

    public BalancedMap() {

    }

    public class MapNode {
        MapEntry contents;

        public int compareTo(MapNode mapNode) {

            return contents.compareTo(mapNode.contents);
        }
    }

    public class MapEntry implements Comparable<MapEntry> {
        K key;

        public int compareTo(MapEntry mapEntry) {

            return key.compareTo(mapEntry.key);
        }
    }

    //problem is here
    public BalancedMap(IMap<K, V> original) {
        map = new TreeMap<>();
        for (K key : original.keyset()) {
            V value = original.getValue(key);
            map.put(key, value);
        }
    }

    public boolean contains(K key) {

        return map.containsKey(key);
    }


    public boolean add(K key, V value) {

        return map.put(key, value) == null;
    }


    public V delete(K key) {
        if (map.remove(key) == null) ;
        return null;
    }

    public V getValue(K key) {
        //Key not found or empty map
        if (isEmpty() || map.get(key) == null)
            return null;
        else
            return map.get(key);
    }

    public K getKey(V value) {

        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (((Comparable<V>) entry.getValue()).equals(value))
                return entry.getKey();
        }
        return null;
    }

    public Iterable<K> getKeys(V value) {
        List<K> keys = new ArrayList<K>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (((Comparable<V>) entry.getValue()).equals(value))
                keys.add(entry.getKey());
        }
        return keys;

    }

    public int size() {

        return map.size();
    }

    public boolean isEmpty() {

        return size() == 0;
    }

    public void clear() {

        map.clear();
    }

    public Iterable<K> keyset() {
        return map.keySet();
    }

    public Iterable<V> values() {
        return map.values();
    }

}
