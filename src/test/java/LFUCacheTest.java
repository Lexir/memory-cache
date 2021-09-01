import cache.Cache;
import cache.LFUCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LFUCacheTest {
    final int MAX_SIZE = 4;
    Cache<Integer, Integer> cache = new LFUCache<>(MAX_SIZE);

    @Test
    void addOneItem() {
        cache.put(1, 1);
        Integer actual = cache.get(1);
        assertEquals(1, cache.size());
        assertEquals(1, actual);
    }

    @Test
    void addOverCapacityItem() {
        for (int i = 1; i <= 5; i++) {
            cache.put(i, i);
        }
        Integer actual1 = cache.get(1);
        Integer actual2 = cache.get(2);
        Integer actual3 = cache.get(3);
        Integer actual4 = cache.get(4);
        Integer actual5 = cache.get(5);

        assertNull(actual1);
        assertEquals(2, actual2);
        assertEquals(3, actual3);
        assertEquals(4, actual4);
        assertEquals(5, actual5);
        assertEquals(MAX_SIZE, cache.size());
    }

    @Test
    void evictLessFrequencyItem() {
        for (int i = 1; i < 5; i++) {
            cache.put(i, i);
        }
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(4);

        cache.get(3); // increment frequency
        cache.get(3); // increment frequency

        cache.put(5, 5); //evict first less frequency element

        Integer actual1 = cache.get(1);
        Integer actual2 = cache.get(2);
        Integer actual3 = cache.get(3);
        Integer actual4 = cache.get(4);
        Integer actual5 = cache.get(5);

        assertNull(actual1);
        assertEquals(2, actual2);
        assertEquals(3, actual3);
        assertEquals(4, actual4);
        assertEquals(5, actual5);
        assertEquals(MAX_SIZE, cache.size());
    }

    @Test
    void evictSameFrequencyItems() {
        for (int i = 1; i < 5; i++) {
            cache.put(i, i);
        }
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(4);

        cache.put(5, 5); //evict first less frequency element

        Integer actual1 = cache.get(1);
        Integer actual2 = cache.get(2);
        Integer actual3 = cache.get(3);
        Integer actual4 = cache.get(4);
        Integer actual5 = cache.get(5);

        assertNull(actual1);
        assertEquals(2, actual2);
        assertEquals(3, actual3);
        assertEquals(4, actual4);
        assertEquals(5, actual5);
        assertEquals(MAX_SIZE, cache.size());
    }

    @Test
    void getNullKey() {

        Exception exception = assertThrows(NullPointerException.class, () ->
                cache.get(null));
        assertEquals("key == null", exception.getMessage());
    }

    @Test
    void addNullKey() {

        Exception exception = assertThrows(NullPointerException.class, () ->
                cache.put(null, 1));
        assertEquals("key or value == null", exception.getMessage());
    }

    @Test
    void addNullValue() {

        Exception exception = assertThrows(NullPointerException.class, () ->
                cache.put(1, null));
        assertEquals("key or value == null", exception.getMessage());
    }

    @Test
    void getNotExistingElementByKey() {
        cache.put(1, 1);
        cache.put(2, 2);
        Integer actual = cache.get(9999999);
        assertNull(actual);
    }

    @Test
    void rewriteElement() {
        cache.put(1, 1);
        cache.put(2, 1);

        cache.get(1);
        cache.get(1);

        cache.put(1, 2);
        Integer actual = cache.get(1);
        assertEquals(2, actual);
    }
}
