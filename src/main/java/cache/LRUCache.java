package cache;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
        moveToHead(key);
        return cache.get(key);
    }

    @Override
    public void put(K newKey, V newValue) {
        if (newKey == null || newValue == null) {
            throw new NullPointerException("key or value == null");
        }
        V cachedObject = cache.get(newKey);
        if (cachedObject == null) {
            insertOrEvict(newKey, newValue);
        } else {
            rewriteObject(newKey, newValue);
        }
    }

    private void insertOrEvict(K newKey, V newValue) {
        if (currentSize == maxSize) {
            evict(newKey, newValue);
        } else {
            addNew(newKey, newValue);
        }
    }

    @Override
    public int size() {
        return currentSize;
    }

    private void addNew(K newKey, V newValue) {
        cache.put(newKey, newValue);
        queue.add(newKey);
        currentSize++;
    }

    private void moveToHead(K key) {
        if (cache.containsKey(key) && currentSize > 1) {
            queue.remove(key);
            queue.addFirst(key);
        }
    }

    private void evict(K newKey, V newValue) {
        K evictedKey = queue.removeLast();
        queue.addFirst(newKey);
        cache.remove(evictedKey);
        cache.put(newKey, newValue);
    }

    private void rewriteObject(K newKey, V newValue) {
        cache.put(newKey, newValue);
        moveToHead(newKey);
    }
}
