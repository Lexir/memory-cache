package cache;

import java.util.*;

public class LFUCache<K, V> implements Cache<K, V> {

    private Map<K, V> cache;
    private List<Pair<K, Integer>> frequency;
    private int maxSize;
    private int currentSize;

    public LFUCache(int maxSize) {
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.cache = new HashMap<>(maxSize);
        this.frequency = new ArrayList<>();
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        if (cache.containsKey(key)) {
            incrementFrequency(key);
            return cache.get(key);
        }
        return null;
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


    @Override
    public int size() {
        return cache.size();
    }

    private void incrementFrequency(K key) {
        for (Pair<K, Integer> pair : frequency) {
            if (pair.first == key) {
                pair.second++;
            }
        }
    }

    private void insert(K newKey, V newValue) {
        cache.put(newKey, newValue);
        frequency.add(new Pair<>(newKey, 1));
        currentSize++;
    }

    private void insertOrEvict(K newKey, V newValue) {
        if (currentSize == maxSize) {
            evict(newKey, newValue);
        } else {
            insert(newKey, newValue);
        }
    }

    private void evict(K newKey, V newValue) {
        Optional<Pair<K, Integer>> min = frequency
                .stream()
                .min(Comparator.comparingInt(pairPrev -> pairPrev.second));
        min.ifPresent(pair -> {
            cache.remove(pair.first);
            cache.put(newKey, newValue);
            pair.first = newKey;
            pair.second = 1;
        });
    }

    private void rewriteObject(K newKey, V newValue) {
        cache.put(newKey, newValue);
        frequency.stream()
                .filter((key) -> key == newKey)
                .findFirst()
                .ifPresent(pair -> pair.second++);
    }

    private static class Pair<S, T> {
        S first;
        T second;

        public Pair(S first, T second) {
            this.first = first;
            this.second = second;
        }
    }
}
