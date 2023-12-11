package org.example.sharing;

import java.util.Random;

public class Atomic {

    public static void main(String[] args) {

        Metrics m = new Metrics();

        BusinessLogic b = new BusinessLogic(m);
        MetricPrinter mp = new MetricPrinter(m);

        b.start();
        mp.start();


    }

    public static class MetricPrinter extends Thread{
        private Metrics metric;

        public MetricPrinter(Metrics metric){
            this.metric = metric;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double currentAverage = metric.getAverage();
                System.out.println("Average: " + currentAverage);
            }
        }

    }

    public static class BusinessLogic extends Thread {

        private Metrics metric;
        private Random random = new Random();

        public BusinessLogic(Metrics metric) {
            this.metric = metric;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long end = System.currentTimeMillis();

                metric.addSample(end - start);
            }
        }
    }

    private static class Metrics {
        private long count = 0;
        private volatile double average = 0.0;

        public synchronized void addSample(long sample) {
            double currSum = average*count  ;
            count++;
            average += (currSum + sample) / count;
        }

        public double getAverage() {
            return average;
        }
    }
}
