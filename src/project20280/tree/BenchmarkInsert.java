package project20280.tree;

import java.util.Random;

public class BenchmarkInsert {

    private static final Random rand = new Random(42);

    public static void main(String[] args) {

        int[] sizes = {1000, 5000, 10000, 20000, 50000};
        int runs = 5;

        System.out.println("n,treap_ns_per_insert,avl_ns_per_insert,treemap_ns_per_insert");

        for (int n : sizes) {
            long treapTotal = 0;
            long avlTotal = 0;
            long treeMapTotal = 0;

            for (int r = 0; r < runs; r++) {
                int[] data = generateData(n);

                // Warmup
                warmup(data);

                // Treap
                TreapMap<Integer, Integer> treap = new TreapMap<>(42L + r);
                long start = System.nanoTime();
                for (int x : data) {
                    treap.put(x, x);
                }
                long treapTime = System.nanoTime() - start;
                treapTotal += treapTime;

                // AVL
                AVLTreeMap<Integer, Integer> avl = new AVLTreeMap<>();
                start = System.nanoTime();
                for (int x : data) {
                    avl.put(x, x);
                }
                long avlTime = System.nanoTime() - start;
                avlTotal += avlTime;

                // Java TreeMap
                java.util.TreeMap<Integer, Integer> treeMap = new java.util.TreeMap<>();
                start = System.nanoTime();
                for (int x : data) {
                    treeMap.put(x, x);
                }
                long treeMapTime = System.nanoTime() - start;
                treeMapTotal += treeMapTime;
            }

            long treapAvg = treapTotal / runs;
            long avlAvg = avlTotal / runs;
            long treeMapAvg = treeMapTotal / runs;

            long treapPerInsert = treapAvg / n;
            long avlPerInsert = avlAvg / n;
            long treeMapPerInsert = treeMapAvg / n;

            System.out.println(
                    n + "," +
                            treapPerInsert + "," +
                            avlPerInsert + "," +
                            treeMapPerInsert
            );
        }
    }

    private static int[] generateData(int n) {
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = rand.nextInt(10_000_000);
        }
        return data;
    }

    private static void warmup(int[] data) {
        TreapMap<Integer, Integer> treap = new TreapMap<>(1L);
        AVLTreeMap<Integer, Integer> avl = new AVLTreeMap<>();
        java.util.TreeMap<Integer, Integer> treeMap = new java.util.TreeMap<>();

        for (int x : data) {
            treap.put(x, x);
            avl.put(x, x);
            treeMap.put(x, x);
        }
    }
}