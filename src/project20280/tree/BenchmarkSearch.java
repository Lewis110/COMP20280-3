package project20280.tree;

import java.util.*;

/**
 * Search Benchmark
 * Compares Treap, AVLTreeMap, and java.util.TreeMap for:
 *   - Successful search
 *   - Unsuccessful search
 * across different input sizes and patterns.
 *
 * HOW TO USE:
 *   1. Drop this file into the same package as your Treap and AVLTreeMap.
 *   2. Replace the stub calls below with your actual put()/get() methods.
 *   3. Run main() and copy the printed CSV into the chart widget.
 */

public class BenchmarkSearch {
    static final int[] SIZES = {1000, 5000, 10000, 20000, 50000};
    static final int WARMUP_REPS = 3;
    static final int MEASURE_REPS = 5;
    static final long NANOS_PER_MS = 1_000_000L;

    static Integer[] random(int n) {
        Random rng = new Random(42);
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) a[i] = rng.nextInt(n * 10);
        return a;
    }

    static Integer[] ascending(int n) {
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }

    static Integer[] descending(int n) {
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        return a;
    }

    static Integer[] partiallySorted(int n) {
        Integer[] a = ascending(n);
        Random rng = new Random(42);
        // shuffle 10% of elements
        int swaps = n / 10;
        for (int i = 0; i < swaps; i++) {
            int x = rng.nextInt(n), y = rng.nextInt(n);
            Integer tmp = a[x]; a[x] = a[y]; a[y] = tmp;
        }
        return a;
    }

    static long medianNs(Runnable task) {
        // warmup
        for (int i = 0; i < WARMUP_REPS; i++) task.run();
        long[] times = new long[MEASURE_REPS];
        for (int i = 0; i < MEASURE_REPS; i++) {
            long t0 = System.nanoTime();
            task.run();
            times[i] = System.nanoTime() - t0;
        }
        Arrays.sort(times);
        return times[MEASURE_REPS / 2];
    }

    interface MapWrapper {
        void put(int key);
        boolean get(int key);   // returns true if found
        void clear();
        String name();
    }

    static MapWrapper javaTreeMap() {
        TreeMap<Integer, Integer> m = new TreeMap<>();
        return new MapWrapper() {
            public void put(int k)      { m.put(k, k); }
            public boolean get(int k)   { return m.checkKey(k); }
            public String name()        { return "java.util.TreeMap"; }
        };
    }

    static MapWrapper avlTreeMap() {
        // TODO
        TreeMap<Integer, Integer> stub = new TreeMap<>();
        return new MapWrapper() {
            public void put(int k)      { stub.put(k, k); /* replace */ }
            public boolean get(int k)   { return stub.checkKey(k); /* replace */ }
            public String name()        { return "AVLTreeMap"; }
        };
    }

    static MapWrapper treap() {
        // TODO
        TreeMap<Integer, Integer> stub = new TreeMap<>();
        return new MapWrapper() {
            public void put(int k)      { stub.put(k, k); /* replace */ }
            public boolean get(int k)   { return stub.checkKey(k); /* replace */ }
            public String name()        { return "Treap"; }
        };
    }

    record Result(String structure, String pattern, int n,
                  double successMs, double failMs) {}

    static Result measure(MapWrapper map, String pattern, Integer[] data) {
        int n = data.length;
        Random rng = new Random(99);

        // Build a set of keys that are definitely NOT in the map
        // (values outside the inserted range)
        int missBound = n * 10 + 1;

        long successNs = medianNs(() -> {
            map.clear();
            for (int k : data) map.put(k);
            // search for every key that was inserted
            for (int k : data) map.get(k);
        });

        long failNs = medianNs(() -> {
            map.clear();
            for (int k : data) map.put(k);
            // search for keys guaranteed absent
            for (int i = 0; i < n; i++) map.get(missBound + i);
        });

        double successMs = (double) successNs / NANOS_PER_MS;
        double failMs    = (double) failNs    / NANOS_PER_MS;

        return new Result(map.name(), pattern, n, successMs, failMs);
    }

    public static void main(String[] args) {
        List<MapWrapper> structures = List.of(javaTreeMap(), avlTreeMap(), treap());
        Map<String, java.util.function.IntFunction<Integer[]>> patterns = new LinkedHashMap<>();
        patterns.put("random",          BenchmarkSearch::random);
        patterns.put("ascending",       BenchmarkSearch::ascending);
        patterns.put("descending",      BenchmarkSearch::descending);
        patterns.put("partiallySorted", BenchmarkSearch::partiallySorted);

        List<Result> results = new ArrayList<>();

        for (var entry : patterns.entrySet()) {
            String patternName = entry.getKey();
            for (int n : SIZES) {
                Integer[] data = entry.getValue().apply(n);
                for (MapWrapper map : structures) {
                    Result r = measure(map, patternName, data);
                    results.add(r);
                    System.out.printf("%-22s | %-15s | n=%-6d | hit=%.3f ms | miss=%.3f ms%n",
                            r.structure(), r.pattern(), r.n(), r.successMs(), r.failMs());
                }
            }
        }

        System.out.println("\n--- CSV OUTPUT ---");
        System.out.println("structure,pattern,n,successMs,failMs");
        for (Result r : results) {
            System.out.printf("%s,%s,%d,%.4f,%.4f%n",
                    r.structure(), r.pattern(), r.n(), r.successMs(), r.failMs());
        }
    }
}
