package project20280.tree;

import java.util.*;
import java.util.function.IntFunction;

/**
 * Search Benchmark
 * Compares Treap, AVLTreeMap, and java.util.TreeMap for:
 *   - Successful search
 *   - Unsuccessful search
 * across different input sizes and patterns.
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
            int x = rng.nextInt(n);
            int y = rng.nextInt(n);
            Integer tmp = a[x];
            a[x] = a[y];
            a[y] = tmp;
        }
        return a;
    }

    static long medianNs(Runnable task) {
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
        boolean get(int key);
        void clear();
        String name();
    }

    static MapWrapper javaTreeMap() {
        return new MapWrapper() {
            private java.util.TreeMap<Integer, Integer> m = new java.util.TreeMap<>();

            @Override
            public void put(int k) {
                m.put(k, k);
            }

            @Override
            public boolean get(int k) {
                return m.containsKey(k);
            }

            @Override
            public void clear() {
                m = new java.util.TreeMap<>();
            }

            @Override
            public String name() {
                return "java.util.TreeMap";
            }
        };
    }

    static MapWrapper avlTreeMap() {
        return new MapWrapper() {
            private AVLTreeMap<Integer, Integer> m = new AVLTreeMap<>();

            @Override
            public void put(int k) {
                m.put(k, k);
            }

            @Override
            public boolean get(int k) {
                return m.get(k) != null;
            }

            @Override
            public void clear() {
                m = new AVLTreeMap<>();
            }

            @Override
            public String name() {
                return "AVLTreeMap";
            }
        };
    }

    static MapWrapper treap() {
        return new MapWrapper() {
            private TreapMap<Integer, Integer> m = new TreapMap<>(42L);

            @Override
            public void put(int k) {
                m.put(k, k);
            }

            @Override
            public boolean get(int k) {
                return m.get(k) != null;
            }

            @Override
            public void clear() {
                m = new TreapMap<>(42L);
            }

            @Override
            public String name() {
                return "TreapMap";
            }
        };
    }

    record Result(String structure, String pattern, int n, double successMs, double failMs) {}

    static Result measure(MapWrapper map, String pattern, Integer[] data) {
        int n = data.length;
        int missStart = n * 20 + 1;

        // build once
        map.clear();
        for (int k : data) map.put(k);

        long successNs = medianNs(() -> {
            for (int k : data) map.get(k);
        });

        long failNs = medianNs(() -> {
            for (int i = 0; i < n; i++) map.get(missStart + i);
        });

        double successMs = (double) successNs / NANOS_PER_MS;
        double failMs = (double) failNs / NANOS_PER_MS;

        return new Result(map.name(), pattern, n, successMs, failMs);
    }

    public static void main(String[] args) {
        List<MapWrapper> structures = List.of(
                javaTreeMap(),
                avlTreeMap(),
                treap()
        );

        Map<String, IntFunction<Integer[]>> patterns = new LinkedHashMap<>();
        patterns.put("random", BenchmarkSearch::random);
        patterns.put("ascending", BenchmarkSearch::ascending);
        patterns.put("descending", BenchmarkSearch::descending);
        patterns.put("partiallySorted", BenchmarkSearch::partiallySorted);

        List<Result> results = new ArrayList<>();

        for (var entry : patterns.entrySet()) {
            String patternName = entry.getKey();

            for (int n : SIZES) {
                Integer[] data = entry.getValue().apply(n);

                for (MapWrapper map : structures) {
                    Result r = measure(map, patternName, data);
                    results.add(r);

                    System.out.printf(
                            "%-20s | %-15s | n=%-6d | hit=%.3f ms | miss=%.3f ms%n",
                            r.structure(), r.pattern(), r.n(), r.successMs(), r.failMs()
                    );
                }
            }
        }

        System.out.println("\nCSV OUTPUT");
        System.out.println("structure,pattern,n,successMs,failMs");
        for (Result r : results) {
            System.out.printf(
                    "%s,%s,%d,%.4f,%.4f%n",
                    r.structure(), r.pattern(), r.n(), r.successMs(), r.failMs()
            );
        }
    }
}
