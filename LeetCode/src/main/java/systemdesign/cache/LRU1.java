package systemdesign.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LRU1<K, V> {
    private final int MAX_CACHE_SIZE;
    private final float DEFAULT_LOAD_FACTORY = 0.75f;

    LinkedHashMap<K, V> map;

    public LRU1(int cacheSize) {
        MAX_CACHE_SIZE = cacheSize;
        int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTORY) + 1;
        map = new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTORY, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }

    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized void remove(K key) {
        map.remove(key);
    }

    public synchronized Set<Map.Entry<K, V>> getAll() {
        return map.entrySet();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            stringBuilder.append(String.format("%s: %s  ", entry.getKey(), entry.getValue()));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        LRU1<Integer, Integer> lru1 = new LRU1<>(5);
        lru1.put(1, 1);
        lru1.put(2, 2);
        lru1.put(3, 3);
        System.out.println(lru1);
        lru1.get(1);
        System.out.println(lru1);
        lru1.put(4, 4);
        lru1.put(5, 5);
        lru1.put(6, 6);
        System.out.println(lru1);
    }
}
