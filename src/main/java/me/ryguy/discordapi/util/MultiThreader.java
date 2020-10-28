package me.ryguy.discordapi.util;

public interface MultiThreader {
    default void thread(String s, Runnable r) {
        new Thread(s) {
            @Override
            public void run() {
                try {
                    r.run();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    default void thread(Runnable r) {
        new Thread() {
            @Override
            public void run() {
                try {
                    r.run();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
