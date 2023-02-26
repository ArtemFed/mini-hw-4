package org.example;

// Мини-дз по многопоточности:
//  Дано два потока — производитель и потребитель.
//  Производитель генерирует некоторые данные (числа). Потребитель «потребляет» их.
//  Два потока разделяют общий буфер данных, размер которого ограничен.
//  Если буфер пуст, потребитель должен ждать, пока там появятся данные.
//  Если буфер заполнен полностью, производитель должен ждать,
//  пока потребитель заберёт данные, и место освободится.

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    static final int bufferSize = 5;
    static final int seed = 42;

    public static void main(String[] args) {
        ArrayBlockingQueue<Integer> sharedBuffer = new ArrayBlockingQueue<>(bufferSize);

        Random rnd = new Random(seed);

        Runnable producer = () -> {
            for (int i = 0; i < 2*bufferSize; i++) {
                try {
                    int ranVal = rnd.nextInt(0, 100);
                    System.out.println(EnvTheme.getColorString("Producing: " + ranVal, "red"));
                    sharedBuffer.put(ranVal);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumer = () -> {
            while (true) {
                try {
                    System.out.println(EnvTheme.getColorString("Consuming: " + sharedBuffer.take(), "cyan"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }
}