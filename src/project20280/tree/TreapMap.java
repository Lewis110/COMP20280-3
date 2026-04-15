package project20280.tree;

import project20280.interfaces.Entry;
import project20280.interfaces.Position;

import java.util.Comparator;
import java.util.Random;

public class TreapMap<K, V> extends TreeMap<K, V> {

    private Random rand;

    public TreapMap() {
        super();
        rand = new Random();
    }

    public TreapMap(long seed) {
        super();
        rand = new Random(seed);
    }

    public TreapMap(Comparator<K> comp) {
        super(comp);
        rand = new Random();
    }
    public TreapMap(Comparator<K> comp, long seed) {
        super(comp);
        rand = new Random(seed);
    }

    private int priority(Position<Entry<K, V>> p) {
        if(p == null) return Integer.MAX_VALUE;
        return tree.getAux(p);
    }

    @Override
    protected void rebalanceInsert(Position<Entry<K, V>> p) {
        if (isExternal(p)) return;
        int prio = rand.nextInt();
        tree.setAux(p, prio);

        while (!isRoot(p)) {
            Position<Entry<K, V>> par = parent(p);
            if (priority(par) <= priority(p)) break;
            rotate(p);
        }
    }

    @Override
    protected void rebalanceDelete(Position<Entry<K,V>> p) {
        // p is the sibling of the removed leaf, now an internal node (or external sentinel)
        // nothing needed for treap delete since TreeMap already removed the node
        // and we don't need to bubble down
    }

    public String toString() {
        return tree.toString();
    }

    public String toBinaryTreeString() {
        BinaryTreePrinter<Entry<K, V>> btp = new BinaryTreePrinter<>(this.tree);
        return btp.print();
    }

    public static void main(String[] args) {
        TreapMap<Integer, Integer> treap = new TreapMap<>(42L);
        Integer[] arr = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        for (Integer i : arr) {
            treap.put(i, i);
        }
        System.out.println(treap.toBinaryTreeString());
        System.out.println("keys: " + treap.keySet());
        System.out.println("size: " + treap.size());

        treap.remove(15);
        System.out.println("after remove(15):");
        System.out.println(treap.toBinaryTreeString());
        System.out.println("keys: " + treap.keySet());
    }
}
