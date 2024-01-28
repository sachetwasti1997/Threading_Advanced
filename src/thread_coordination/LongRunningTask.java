package thread_coordination;

import java.math.BigInteger;

public class LongRunningTask {
    public static void main(String[] args) {
        var powThread = new Thread(new PowerCalculate(200000, 100000));
        powThread.start();
        try {
            Thread.sleep(3000);
            powThread.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class PowerCalculate implements Runnable{
    private BigInteger result = BigInteger.ONE;
    private final int base;
    private final int power;

    public PowerCalculate(int base, int power) {
        this.base = base;
        this.power = power;
    }

    @Override
    public void run() {
        System.out.printf("The result of %d^%d is %d",base, power, power(base, power));
    }

    public BigInteger power(int base, int power) {
        for (int i=0; i<power; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return BigInteger.ZERO;
            }
            result = result.multiply(BigInteger.valueOf(base));
        }
        return result;
    }
}
