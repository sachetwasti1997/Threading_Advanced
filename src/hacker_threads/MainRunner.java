package hacker_threads;

import java.util.ArrayList;
import java.util.Random;

public class MainRunner {
    public static int MAX_PASSWORD = 9999;

    public static void main(String[] args) {
        var threads = new ArrayList<Thread>();
        Vault vault = new Vault(new Random().nextInt(0, 9999));
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceThread());
        for (Thread th: threads) {
            th.start();
        }
    }
}
