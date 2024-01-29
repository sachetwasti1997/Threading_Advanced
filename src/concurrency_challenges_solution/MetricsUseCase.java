package concurrency_challenges_solution;

import java.util.Random;

public class MetricsUseCase {

    public static void main(String[] args) {
        Metrics metrics = new Metrics();
        Thread thread1 = new Thread(new BusinessLogic(metrics));
        Thread thread2 = new Thread(new BusinessLogic(metrics));
        Thread metricsPrinter = new Thread(new MetricsPrinter(metrics));
        thread1.start();
        thread2.start();
        metricsPrinter.start();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread1.interrupt();
        thread2.interrupt();
        metricsPrinter.interrupt();
    }

    private static class MetricsPrinter implements Runnable {
        private final Metrics metrics;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Current average is "+metrics.getAverage());
            }
        }
    }

    private static class BusinessLogic implements Runnable {

        private final Metrics metrics;
        private final Random random = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long end = System.currentTimeMillis();
                metrics.calculateAverage(end-start);
            }
        }
    }

    private static class Metrics {
        private long count = 0;
        private volatile double average = 0.0;
        public synchronized void calculateAverage(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum+sample) / count;
        }
        public double getAverage() {
            return average;
        }
    }

}
