package hacker_threads;

public abstract class HackerThread extends Thread {

    protected final Vault vault;

    public HackerThread(Vault vault) {
        this.vault = vault;
        this.setName(this.getClass().getSimpleName());
        setPriority(MAX_PRIORITY);
    }

    @Override
    public synchronized void start() {
        System.out.println("Starting the thread "+Thread.currentThread().getName());
        super.start();
    }
}
