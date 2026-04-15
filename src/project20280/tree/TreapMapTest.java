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
}
