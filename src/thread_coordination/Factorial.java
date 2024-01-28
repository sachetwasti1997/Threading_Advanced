package thread_coordination;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Factorial {
    public static void main(String[] args) throws InterruptedException {
        List<Long> inputs = Arrays.asList(0L, 322L, 6545L, 35435L, 1000L, 10000000L);
        List<FactorialThread> list = new ArrayList<>();
        for (long i: inputs) {
            list.add(new FactorialThread(i));
        }
        for (Thread t: list) t.start();
        for (FactorialThread t: list) {
            t.join(4000);
            System.out.println("Started calculation of "+t.getInput()+" factorial time given to calculation 4 seconds!");
            if (!t.isFinished()) {
                t.interrupt();
            }
        }
        for (FactorialThread f: list) {
            if (f.isFinished()) {
                System.out.printf("Factorial of %d is %d%n", f.getInput(), f.result());
            }else {
                System.out.printf("Factorial of %d still in progress!%n", f.getInput());
            }
        }
    }

}

class FactorialThread extends Thread {
    private final long input;
    private BigInteger factResult = BigInteger.ONE;
    private boolean isFinished;

    public FactorialThread(long input) {
        this.input = input;
    }

    @Override
    public void run() {
        calcFactorial(input);
    }

    private void calcFactorial(long input) {
        BigInteger factResult = BigInteger.ONE;
        for (long i = input; i>0; i--) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            factResult = factResult.multiply(BigInteger.valueOf(i));
        }
        this.factResult = factResult;
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public BigInteger result() {
        return factResult;
    }

    public long getInput() {
        return input;
    }
}
