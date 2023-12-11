package org.example.hacker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Demo {

    public static final int MAX_PASSWORD = 9999;

    public static void main(String[] args) {
        Random r = new Random();
        Vault v = new Vault(r.nextInt(MAX_PASSWORD));
        List<Thread > l = new ArrayList<Thread>();
        AscendingThread a = new AscendingThread(v);
        DescendingThread d = new DescendingThread(v);
        PoliceThread p = new PoliceThread();
        l.add(a);
        l.add(d);
        l.add(p);

        for(Thread t : l){
            t.start();
        }
    }

    private static class Vault {
        private int password;

        public Vault(int password) {
            this.password = password;
        }

        public boolean correctPassword(int guess) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {

            }
            return this.password == guess;
        }
    }

    private static abstract class HackerThread extends Thread {
        protected Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(MAX_PRIORITY);
        }

        @Override
        public synchronized void start() {
            System.out.println("starting thread " + this.getName());
            super.start();

        }
    }

    private static class AscendingThread extends HackerThread {

        public AscendingThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = 0; guess < MAX_PASSWORD; guess++) {
                if (vault.correctPassword(guess)) {
                    System.out.println(this.getName() + " has guessed password");
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingThread extends HackerThread {

        public DescendingThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = MAX_PASSWORD; guess > 0; guess--) {
                if (vault.correctPassword(guess)) {
                    System.out.println(this.getName() + " has guessed password");
                    System.exit(0);
                }
            }

        }
    }

    private static class PoliceThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                System.out.println(i);
            }
            System.out.println("game over ");
            System.exit(0);
        }
    }
}
