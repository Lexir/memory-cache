package cache;

import java.util.*;

public class LRUCache<K, V> implements Cache<K, V> {

    private Map<K, V> cache;
    private Deque<K> queue;
    private int maxSize;
    private int currentSize;

    public LRUCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }
        this.maxSize = maxSize;
        this.currentSize = 0;
        queue = new LinkedList<>();
        this.cache = new HashMap<>(maxSize);
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        queue.remove(key);
        queue.addFirst(key);  // move element to head
        cache.toString();
        return cache.get(key);
    }

    @Override
    public void put(K newKey, V newValue) {
        if (newKey == null || newValue == null) {
            throw new NullPointerException("key or value == null");
        }
        V cachedObject = cache.get(newKey);
        if (cachedObject == null) {
            if (currentSize == maxSize) {
                K evictedKey = queue.removeLast();
                cache.remove(evictedKey);
                queue.add(newKey);
                cache.put(newKey, newValue);
            } else {
                cache.put(newKey, newValue);
                queue.add(newKey);
                currentSize++;
            }
        } else {
            queue.add(newKey);
            cache.put(newKey, newValue);
        }
    }

    @Override
    public int size() {
        return currentSize;
    }
}
