package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {

//        MyThread t = new MyThread();
//        t.start();
//
//        Thread r = new Thread(new MyTask());
//        r.start();
//        for(int i =0;i<100;i++){
//            System.out.print("M");
//        }

        Thread t = new Thread (() -> {
            System.out.println("runnable");
            System.out.println("start");
            throw new RuntimeException("internal error");
        });

        t.start();

        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("uncaught exception");
            }
        });





    }
}