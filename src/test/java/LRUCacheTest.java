import cache.LRUCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {
    final int MAX_SIZE = 4;
    LRUCache<Integer, Integer> cache = new LRUCache<>(MAX_SIZE);

    @Test
    void addNewItem() {
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.put(4, 4);
        assertEquals(MAX_SIZE, cache.size());
    }

    @Test
    void addOverCapacity() {
        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.put(4, 4);
        cache.put(5, 5);
        Integer actual = cache.get(1);
        Integer actual2 = cache.get(2);
        Integer actual3 = cache.get(3);
        Integer actual4 = cache.get(4); // evicted element
        Integer actual5 = cache.get(5);
        assertEquals(MAX_SIZE, cache.size());
        assertEquals(actual, 1);
        assertEquals(actual2, 2);
        assertEquals(actual3, 3);
        assertEquals(actual5, 5);
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

}
