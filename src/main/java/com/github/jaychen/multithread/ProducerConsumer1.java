package com.github.jaychen.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Object.wait/notify
public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        Producer producer = new Producer(list);
        Consumer consumer = new Consumer(list);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    private static class Producer extends Thread {
        private final List<Integer> list;

        Producer(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {

                synchronized (list) {
                    while (!list.isEmpty()) {
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int randomValue = new Random().nextInt();
                    System.out.println("Producing " + randomValue);

                    list.add(randomValue);
                    list.notify();
                }

            }

        }
    }

    private static class Consumer extends Thread {
        private final List<Integer> list;

        Consumer(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (list) {
                    while (list.isEmpty()) {
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("Consuming " + list.get(0));
                    list.remove(0);
                    list.notify();
                }
            }
        }
    }
}
