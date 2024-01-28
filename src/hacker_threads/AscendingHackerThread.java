package hacker_threads;

public class AscendingHackerThread extends HackerThread{
    public AscendingHackerThread(Vault vault) {
        super(vault);
    }

    @Override
    public void run() {
        for (int i=0; i<=MainRunner.MAX_PASSWORD; i++) {
            if (vault.isCorrectGuess(i)) {
                System.out.println(this.getName()+" guessed the password "
                +i);
                System.exit(0);
            }
        }
    }
}
