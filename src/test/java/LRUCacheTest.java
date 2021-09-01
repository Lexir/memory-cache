import cache.LRUCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {
    final int MAX_SIZE = 4;
    LRUCache<Integer, Integer> cache = new LRUCache<>(MAX_SIZE);

    @Test
    void addNewItem() {
        cache.put(1, 1);
        assertEquals(1, cache.size());
    }

    @Test
    void evictItem() {
        for (int i = 1; i <= 5; i++) {
            cache.put(i, i);
        }
        Integer actual1 = cache.get(1);
        Integer actual2 = cache.get(2);
        Integer actual3 = cache.get(3);
        Integer actual4 = cache.get(4); // evicted element
        Integer actual5 = cache.get(5);
        assertEquals(MAX_SIZE, cache.size());
        assertEquals(1, actual1);
        assertEquals(2, actual2);
        assertEquals(3, actual3);
        assertEquals(5, actual5);
        assertNull(actual4);
    }

    @Test
    void evictItemAndReorderingElement() {
        for (int i = 1; i <= 4; i++) {
            cache.put(i, i);
        }

        cache.get(4);
        cache.put(5, 5);
        Integer actual3 = cache.get(3);   // evicted element
        assertEquals(MAX_SIZE, cache.size());
        assertNull(actual3);
    }

    @Test
    void addTwoElementOverCapacity() {
        for (int i = 1; i <= 6; i++) {
            cache.put(i, i);
        }

        Integer actual1 = cache.get(1);
        Integer actual2 = cache.get(2);
        Integer actual3 = cache.get(3); // evicted element
        Integer actual4 = cache.get(4); // evicted element
        Integer actual5 = cache.get(5);
        Integer actual6 = cache.get(6);

        assertEquals(MAX_SIZE, cache.size());
        assertEquals(1, actual1);
        assertEquals(2, actual2);
        assertEquals(5, actual5);
        assertEquals(6, actual6);
        assertNull(actual3);
        assertNull(actual4);
    }


    @Test
    void addTwinKey() {
        cache.put(1, 1);
        cache.put(1, 2);
        Integer actual = cache.get(1);
        assertEquals(2, actual);
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
    void getByNullKey() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                cache.get(null));
        assertEquals("key == null", exception.getMessage());
    }

    @Test
    void getByNotExistingKey() {
        Integer actual = cache.get(10);
        assertNull(actual);
    }

    @Test
    void getElementNotFullCache() {
        for (int i = 1; i <= 3; i++) {
            cache.put(i, i);
        }

        Integer actual4 = cache.get(4);
        assertEquals(3, cache.size());
        assertNull(actual4);
    }

}
