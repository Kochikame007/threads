package org.example.sharing;

public class ResourceSharing {

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter counter = new InventoryCounter();
        IncrementingThread i = new IncrementingThread(counter);
        DecrementingThread d = new DecrementingThread(counter);

        i.start();
        d.start();
        i.join();
        d.join();

        System.out.println("We have "+ counter.getItems());
    }

    private static class IncrementingThread extends Thread {

        private InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter){
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for(int i = 0; i <1000;i++){
                inventoryCounter.increment();
            }
        }
    }

    private static class DecrementingThread extends Thread {

        private InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter){
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for(int i = 0; i <1000;i++){
                inventoryCounter.decrement();
            }
        }
    }
    private static class InventoryCounter{
        private int items=0;
        Object lock = new Object();

        public  void increment(){
            synchronized(this.lock) {
                items++;
            }
        }

        public void decrement(){
            synchronized(this.lock) {
                items--;
            }
        }

        public int getItems(){
            return items;
        }
    }
}
