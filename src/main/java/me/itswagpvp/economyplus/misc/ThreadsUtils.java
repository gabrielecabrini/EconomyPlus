package me.itswagpvp.economyplus.misc;

import java.util.Set;

public class ThreadsUtils {

    // Stops the plugin's created threads
    public static void stopAllThreads() {
        try {
            Set<Thread> threads = Thread.getAllStackTraces().keySet();
            for (Thread t : threads) {
                if (t.getName().endsWith("-economyplus")) {
                    t.stop();
                }
            }
        } catch (Exception ignored) {}

    }

    // Stop a single thread
    public static void stopThread(String threadName) {
        try {
            Set<Thread> threads = Thread.getAllStackTraces().keySet();
            for (Thread t : threads) {
                if (t.getName().equals(threadName)) {
                    t.stop();
                }
            }
        } catch (Exception ignored) {}
    }
}
