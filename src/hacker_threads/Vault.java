package hacker_threads;

public class Vault {

    private final int password;

    public Vault(int password) {
        this.password = password;
    }

    public boolean isCorrectGuess(int password){
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return password == this.password;
    }
}
