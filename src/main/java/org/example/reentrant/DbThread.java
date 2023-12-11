package org.example.reentrant;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DbThread {

    public static final int HIGHEST_PRICE = 1000;

    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        InventoryDb idb = new InventoryDb();

        for (int i = 0; i < 100000; i++) {
            idb.addItem(i);
        }

        Thread writer = new Thread(() -> {
            while (true) {
                idb.addItem(rand.nextInt(HIGHEST_PRICE));
                idb.removeItem(rand.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        writer.setDaemon(true);
        writer.start();

        int numberOfThreads = 7;
        List<Thread> readers = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            Thread reader = new Thread(() -> {
                for (int x = 0; x < 100000; x++) {
                    int upperBound = rand.nextInt(HIGHEST_PRICE);
                    int lowerBound = upperBound > 0 ? rand.nextInt(upperBound) : upperBound;
                    idb.getNumberOfItemsInPriceRange(lowerBound, upperBound);
                }
            });
            reader.setDaemon(true);
            readers.add(reader);
        }
        long start = System.currentTimeMillis();

        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread reader : readers) {
            reader.join();
        }

        long end = System.currentTimeMillis();

        System.out.println(String.format("Total time taken  %d ms" , end - start));
    }

    public static class InventoryDb {

        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<Integer, Integer>();
        private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock write = readWriteLock.writeLock();
        Lock read = readWriteLock.readLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {

            read.lock();

            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.ceilingKey(upperBound);

                if (fromKey == null || toKey == 0) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrice = priceToCountMap.subMap(fromKey, true, toKey, true);
                int sum = 0;

                for (int i : rangeOfPrice.values()) {
                    sum += i;
                }
                return sum;
            } finally {
                read.unlock();
            }
        }

        public void addItem(int price) {
            write.lock();
            try {
                Integer p = priceToCountMap.get(price);
                if (p == null) {
                    priceToCountMap.put(price, 1);
                } else {
                    priceToCountMap.put(price, p + 1);
                }
            } finally {
                write.unlock();
            }
        }

        public void removeItem(int price) {
            write.lock();
            try {
                Integer p = priceToCountMap.get(price);
                if (p == null || p == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, p - 1);
                }
            } finally {
                write.unlock();
            }
        }
    }
}
