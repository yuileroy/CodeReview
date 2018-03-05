package concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimpleConcurrentMap<K, V> implements Map<K, V> {
    ConcurrentHashMap<K, V> c;
    final ReadWriteLock lock = new ReentrantReadWriteLock();
    final Lock r = lock.readLock();
    final Lock w = lock.writeLock();
    final Map<K, V> map;

    public SimpleConcurrentMap() {
        this.map = new HashMap<K, V>(1 << 18);
    }

    public SimpleConcurrentMap(Map<K, V> map) {
        if (map == null)
            throw new NullPointerException();
        this.map = map;
    }

    public void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }

    public boolean containsKey(Object key) {
        r.lock();
        try {
            return map.containsKey(key);
        } finally {
            r.unlock();
        }
    }

    public boolean containsValue(Object value) {
        r.lock();
        try {
            return map.containsValue(value);
        } finally {
            r.unlock();
        }
    }

    /**
     * The set is NOT backed by the map, implemented as HashSet.
     */
    public Set<Map.Entry<K, V>> entrySet() {
        r.lock();
        try {
            return new HashSet<Map.Entry<K, V>>(map.entrySet());
        } finally {
            r.unlock();
        }
    }

    public V get(Object key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    public boolean isEmpty() {
        r.lock();
        try {
            return map.isEmpty();
        } finally {
            r.unlock();
        }
    }

    /**
     * The set is NOT backed by the map, implemented as HashSet.
     */
    public Set<K> keySet() {
        r.lock();
        try {
            return new HashSet<K>(map.keySet());
        } finally {
            r.unlock();
        }
    }

    public V put(K key, V value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        w.lock();
        try {
            map.putAll(m);
        } finally {
            w.unlock();
        }
    }

    public V remove(Object key) {
        w.lock();
        try {
            return map.remove(key);
        } finally {
            w.unlock();
        }
    }

    public int size() {
        r.lock();
        try {
            return map.size();
        } finally {
            r.unlock();
        }
    }

    /**
     * The collection is NOT backed by the map, implemented as ArrayList.
     */
    public Collection<V> values() {
        r.lock();
        try {
            return new ArrayList<V>(map.values());
        } finally {
            r.unlock();
        }
    }

}