package hacker_threads;

public class PoliceThread extends Thread{
    @Override
    public void run() {
        System.out.println(this.getName()+" running, you have 10 seconds to be caught!");
        for (int i=0; i<10; i++) {
            try {
                Thread.sleep(1000);
                System.out.println(i+1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Caught the thief game over!");
        System.exit(0);
    }
}
