package org.example.interrupt;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        Thread t = new Thread (new BlockingTask());
        t.start();
        t.interrupt();
//            Thread t = new Thread (new LongComputation(new BigInteger("2") , new BigInteger("10")));
//
//            t.start();
//            t.interrupt();
    }

    private static class LongComputation implements Runnable {
        private BigInteger power;
        private BigInteger base;

        public LongComputation(BigInteger base, BigInteger power) {

        this.power =power;
        this.base =base;
    }

        @Override
        public void run() {
                System.out.println(base +" "+ power +" :"+ pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power){
            BigInteger result = BigInteger.ONE;

            for(BigInteger i =BigInteger.ZERO;i.compareTo(power)!=0 ;i = i.add(BigInteger.ONE)){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("thread interrupted");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                System.out.println("executed successfully");
            } catch (InterruptedException e) {
//                System.out.println("Exiting Blocking thread");
            }
        }
    }
}
