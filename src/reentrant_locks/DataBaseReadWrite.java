package reentrant_locks;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBaseReadWrite {

    public static final int HIGHEST_PRICE = 1000;

    public static void main(String[] args) throws InterruptedException {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();
        Random random = new Random();
        for (int i=0; i<100000; i++) {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }
        Thread writer = new Thread(() -> {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
            inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThread = 7;
        List<Thread> readers = new ArrayList<>();
        for (int i=0; i<numberOfReaderThread; i++) {
            Thread reader = new Thread(() -> {
               for (int j=0; j < 100000; j++) {
                   int upperBound = random.nextInt(HIGHEST_PRICE);
                   int lowerbound = upperBound > 0 ? random.nextInt(upperBound) :0;
                   inventoryDatabase.getNumberOfItemsInPriceRange(lowerbound, upperBound);
               }
            });
            reader.setDaemon(true);
            readers.add(reader);
        }

        long startTime = System.currentTimeMillis();
        for (Thread reader: readers) {
            reader.start();
        }
        for (Thread reader: readers) {
            reader.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("Time taken by readers: %d ms%n", (endTime-startTime));
    }

    public static class InventoryDatabase{
        private final TreeMap<Integer, Integer> priceToCountDatabase = new TreeMap<>();
        private final Lock lock = new ReentrantLock();
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int higherBound) {
//            lock.lock();
            readLock.lock();
            try {
                Integer fromKey = priceToCountDatabase.ceilingKey(lowerBound);
                Integer toKey = priceToCountDatabase.floorKey(higherBound);
                int sum = 0;
                if (fromKey == null || toKey == null) {
                    return sum;
                }
                NavigableMap<Integer, Integer> demo = priceToCountDatabase.subMap(fromKey, true, toKey, true);
                for (Integer value : demo.values()) {
                    sum += value;
                }
                return sum;
            }finally {
//                lock.unlock();
                readLock.unlock();
            }
        }

        public void addItem(int price) {
//            lock.lock();
            writeLock.lock();
            try {
                priceToCountDatabase.merge(price, 1, Integer::sum);
            }finally {
//                lock.unlock();
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
//            lock.lock();
            writeLock.lock();
            try {
                Integer count = priceToCountDatabase.get(price);
                if (count == null || count == 1) {
                    priceToCountDatabase.remove(price);
                } else {
                    priceToCountDatabase.put(price, count - 1);
                }
            }finally {
//                lock.unlock();
                writeLock.unlock();
            }
        }
    }

}
