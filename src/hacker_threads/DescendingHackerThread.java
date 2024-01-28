package hacker_threads;

public class DescendingHackerThread extends HackerThread{
    public DescendingHackerThread(Vault vault) {
        super(vault);
    }

    @Override
    public void run() {
        for (int i = MainRunner.MAX_PASSWORD; i>= 0; i--) {
            if (vault.isCorrectGuess(i)) {
                System.out.println(this.getName()+" guessed the password "+i);
                System.exit(0);
            }
        }
    }
}
