package project20280.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreapMapTest {

    private static final long SEED = 42L;

    private TreapMap<Integer, String> treapWithSeed() {
        return new TreapMap<>(SEED);
    }

    private void insertAll(TreapMap<Integer, String> map, Integer[] keys) {
        for (Integer k : keys) {
            map.put(k, Integer.toString(k));
        }
    }

    private String keysInOrder(TreapMap<Integer, String> map) {
        Iterator<Integer> it = map.keySet().iterator();
        List<Integer> list = new ArrayList<>();
        it.forEachRemaining(list::add);
        return list.toString();
    }

    @Test
    void testInsertEmpty() {
        TreapMap<Integer, String> map = treapWithSeed();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    void testInsertSingle() {
        TreapMap<Integer, String> map = treapWithSeed();
        assertNull(map.put(10, "ten"));
        assertEquals(1, map.size());
        assertEquals("ten", map.get(10));
    }

    @Test
    void testInsertBatch() {
        TreapMap<Integer, String> map = treapWithSeed();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        insertAll(map, arr);

        assertEquals(12, map.size());
        assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", keysInOrder(map));

        for (Integer k : arr) {
            assertEquals(Integer.toString(k), map.get(k));
        }
    }

    @Test
    void testInsertDuplicateKey() {
        TreapMap<Integer, String> map = treapWithSeed();
        assertNull(map.put(5, "five"));
        assertEquals("five", map.put(5, "FIVE"));
        assertEquals(1, map.size());
        assertEquals("FIVE", map.get(5));
    }

    @Test
    void testInsertSortedAscending() {
        TreapMap<Integer, String> map = treapWithSeed();
        for (int i = 1; i <= 20; i++) {
            map.put(i, Integer.toString(i));
        }
        assertEquals(20, map.size());
        for (int i = 1; i <= 20; i++) {
            assertEquals(Integer.toString(i), map.get(i));
        }
    }

    @Test
    void testInsertSortedDescending() {
        TreapMap<Integer, String> map = treapWithSeed();
        for (int i = 20; i >= 1; i--) {
            map.put(i, Integer.toString(i));
        }
        assertEquals(20, map.size());
        for (int i = 1; i <= 20; i++) {
            assertEquals(Integer.toString(i), map.get(i));
        }
    }

    @Test
    void testInsertNullLookup() {
        TreapMap<Integer, String> map = treapWithSeed();
        insertAll(map, new Integer[]{3, 7, 1});
        assertNull(map.get(99));
        assertNull(map.get(-1));
    }

    @Test
    void testDeleteMissingKey() {
        TreapMap<Integer, String> map = treapWithSeed();
        insertAll(map, new Integer[]{10, 20, 30});
        assertNull(map.remove(99));
        assertEquals(3, map.size());
    }

    @Test
    void testDeleteSingle() {
        TreapMap<Integer, String> map = treapWithSeed();
        map.put(5, "five");
        assertEquals("five", map.remove(5));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get(5));
    }

    @Test
    void testDeleteFromBatch() {
        TreapMap<Integer, String> map = treapWithSeed();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        insertAll(map, arr);

        assertEquals("26", map.remove(26));
        assertEquals(11, map.size());
        assertNull(map.get(26));
        assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 33, 35]", keysInOrder(map));
    }

    @Test
    void testDeleteMultiple() {
        TreapMap<Integer, String> map = treapWithSeed();
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        insertAll(map, arr);

        assertEquals("15", map.remove(15));
        assertEquals("35", map.remove(35));
        assertEquals("1", map.remove(1));
        assertEquals(9, map.size());
        assertEquals("[2, 4, 5, 12, 21, 23, 24, 26, 33]", keysInOrder(map));
    }

    @Test
    void testDeleteDrainAll() {
        TreapMap<Integer, String> map = treapWithSeed();
        Integer[] arr = new Integer[]{8, 3, 14, 1, 6, 10, 20};
        insertAll(map, arr);

        for (Integer k : arr) {
            assertNotNull(map.remove(k));
        }
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    void testDeleteThenReinsert() {
        TreapMap<Integer, String> map = treapWithSeed();
        insertAll(map, new Integer[]{10, 20, 30});

        assertEquals("20", map.remove(20));
        assertEquals(2, map.size());
        assertNull(map.get(20));

        map.put(20, "twenty");
        assertEquals(3, map.size());
        assertEquals("twenty", map.get(20));
        assertEquals("[10, 20, 30]", keysInOrder(map));
    }

    @Test
    void testDeleteAlreadyRemoved() {
        TreapMap<Integer, String> map = treapWithSeed();
        insertAll(map, new Integer[]{5, 10, 15});
        assertEquals("10", map.remove(10));
        assertNull(map.remove(10));
        assertEquals(2, map.size());
    }
}
