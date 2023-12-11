package org.example.join;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<Long> li = Arrays.asList(0l, 3435L, 35435L, 2324L, 4645L, 23L);

        List<FactorialThread> ft = new ArrayList<FactorialThread>();
        for (Long l : li) {
            ft.add(new FactorialThread(l));
        }

        for (Thread t : ft) {
            t.start();
        }

        for (Thread t : ft) {
            t.join();
        }

        for (int i = 0; i < li.size(); i++) {
            FactorialThread f = ft.get(i);
            if (f.isFinished()) {
                System.out.println("result " + li.get(i) + " " + f.getResult());
            }else{
                System.out.println("result still in progress " + li.get(i) );
            }
        }
    }

    public static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result;
        private boolean isFinished;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial(long inputNumber) {
            BigInteger tempresult = BigInteger.ONE;
            for (long i = inputNumber; i > 0; i--) {
                tempresult = tempresult.multiply(new BigInteger(Long.toString(i)));
            }

            return tempresult;
        }
    }
}
