package project20280.tree;

import java.util.Random;

public class BenchmarkDelete {

    private static final Random rand = new Random(42);

    public static void main(String[] args) {

        int[] sizes = {1000, 5000, 10000, 20000, 50000};
        int runs = 5;

        System.out.println("n,treap_ns_per_delete,avl_ns_per_delete,treemap_ns_per_delete");

        for (int n : sizes) {

            long treapTotal = 0;
            long avlTotal = 0;
            long treeMapTotal = 0;

            for (int r = 0; r < runs; r++) {

                int[] data = generateData(n);

                // ---- TREAP ----
                TreapMap<Integer, Integer> treap = new TreapMap<>(42L + r);
                for (int x : data) treap.put(x, x);

                long start = System.nanoTime();
                for (int x : data) treap.remove(x);
                long treapTime = System.nanoTime() - start;
                treapTotal += treapTime;

                // ---- AVL ----
                AVLTreeMap<Integer, Integer> avl = new AVLTreeMap<>();
                for (int x : data) avl.put(x, x);

                start = System.nanoTime();
                for (int x : data) avl.remove(x);
                long avlTime = System.nanoTime() - start;
                avlTotal += avlTime;

                // ---- TREEMAP ----
                java.util.TreeMap<Integer, Integer> treeMap = new java.util.TreeMap<>();
                for (int x : data) treeMap.put(x, x);

                start = System.nanoTime();
                for (int x : data) treeMap.remove(x);
                long treeMapTime = System.nanoTime() - start;
                treeMapTotal += treeMapTime;
            }

            long treapAvg = treapTotal / runs;
            long avlAvg = avlTotal / runs;
            long treeMapAvg = treeMapTotal / runs;

            long treapPerDelete = treapAvg / n;
            long avlPerDelete = avlAvg / n;
            long treeMapPerDelete = treeMapAvg / n;

            System.out.println(
                    n + "," +
                            treapPerDelete + "," +
                            avlPerDelete + "," +
                            treeMapPerDelete
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
}