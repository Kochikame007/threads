package org.example.sharing;

import org.example.Main;

import javax.management.relation.RelationNotFoundException;
import java.lang.annotation.Target;
import java.util.Random;

public class IMain {

    public static void main(String[] args) {
        Intersection i = new Intersection();
        Thread ta = new Thread(new TrainA(i));
        Thread tb = new Thread(new TrainB(i));

        ta.start();
        tb.start();

    }

    public static class TrainB implements Runnable {
        private Intersection i;
        private Random r = new Random();

        public TrainB(Intersection i) {
            this.i = i;
        }

        @Override
        public void run() {
            while (true) {
                long sleepTime = r.nextLong(5);
                try {
                    Thread.sleep(sleepTime);
                    i.takeRoadB();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class TrainA implements Runnable {
        private Intersection i;
        private Random r = new Random();

        public TrainA(Intersection i) {
            this.i = i;
        }

        @Override
        public void run() {
            while (true) {
                long sleepTime = r.nextLong(5);
                try {
                    Thread.sleep(sleepTime);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.takeRoadA();
            }
        }
    }

    public static class Intersection {
        private Object roadA = new Object();
        private Object roadB = new Object();

        public void takeRoadA()  {
            synchronized (roadA) {
                System.out.println("Road A is locked by the " + Thread.currentThread().getName());
            }
                synchronized (roadB) {
                    System.out.println("Road B is locked by the " + Thread.currentThread().getName());
                }

        try {
            Thread.sleep(1);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        }


        public void takeRoadB() throws InterruptedException {
            synchronized (roadB) {
                System.out.println("Road B is locked by the " + Thread.currentThread().getName());
            }
                synchronized (roadA) {
                    System.out.println("Road A is locked by the " + Thread.currentThread().getName());
                }
            
            try {
                Thread.sleep(1);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }


    }
}
