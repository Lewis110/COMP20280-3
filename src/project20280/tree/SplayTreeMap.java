package src.project20280.tree;

import src.project20280.interfaces.Entry;
import src.project20280.interfaces.Position;

import java.util.Comparator;

public class SplayTreeMap<K, V> extends TreeMap<K, V> {

    /**
     * Constructs an empty map using the natural ordering of keys.
     */
    public SplayTreeMap() {
        super();
    }

    /**
     * Constructs an empty map using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the map
     */
    public SplayTreeMap(Comparator<K> comp) {
        super(comp);
    }

    /**
     * Utility used to rebalance after a map operation.
     */
    private void splay(Position<Entry<K, V>> p) {
        while (!isRoot(p)) {
            Position<Entry<K, V>> parent = parent(p);
            if (isRoot(parent)) {
                rotate(p);
            } else {
                Position<Entry<K, V>> grand = parent(parent);
                if ((p == left(parent)) == (parent == left(grand))) {
                    rotate(parent);
                    rotate(p);
                } else {
                    rotate(p);
                    rotate(p);
                }
            }
        }
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after a node access.
     *
     * @param p
     */
    //@Override
    protected void rebalanceAccess(Position<Entry<K, V>> p) {
        if (isExternal(p)) p = parent(p);
        if (p != null) splay(p);
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after an insertion.
     *
     * @param p
     */
    //@Override
    protected void rebalanceInsert(Position<Entry<K, V>> p) {
        splay(p);
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after a deletion.
     *
     * @param p
     */
    //@Override
    protected void rebalanceDelete(Position<Entry<K, V>> p) {
        if (p != null && !isRoot(p)) splay(parent(p));
    }

    public String toString() {
        return tree.toString();
    }

    public static void main(String[] args) {
        SplayTreeMap<Integer, Integer> treeMap = new SplayTreeMap<Integer, Integer>();

        Integer[] arr = new Integer[]{44, 17, 88, 8, 32, 65, 97, 28, 54, 82, 93, 21, 29, 76, 80};
        for (Integer i : arr)
            treeMap.put(i, i);
        System.out.println("treeMap " + treeMap);
    }
}
