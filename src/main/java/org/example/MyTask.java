package org.example;

public class MyTask implements Runnable{

    @Override
    public void run() {
        for(int i =0;i<100;i++){
            System.out.print("R");
        }
    }
}
