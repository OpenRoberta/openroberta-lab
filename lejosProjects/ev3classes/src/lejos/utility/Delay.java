package lejos.utility;

/**
 * Simple collection of time delay routines that are non interruptable.
 * @author andy
 */
public class Delay {

    /**
     * Wait for the specified number of milliseconds.
     * Delays the current thread for the specified period of time. Can not
     * be interrupted (but it does preserve the interrupted state).
     * @param period time to wait in ms
     */
    public static void msDelay(long period)
    {
        if (period <= 0) return;
        long end = System.currentTimeMillis() + period;
        boolean interrupted = false;
        do {
            try {
                Thread.sleep(period);
            } catch (InterruptedException ie)
            {
                interrupted = true;
            }
            period = end - System.currentTimeMillis();
        } while (period > 0);
        if (interrupted)
            Thread.currentThread().interrupt();
    }

    /**
     * Wait for the specified number of microseconds.
     * Delays the current thread for the specified period of time. Can not
     * be interrupted.
     * @param period time to wait in us
     */
    public static void usDelay(long period)
    {
        long end = System.nanoTime() + period*1000;
        msDelay(period/1000);
        // To improve accuracy for small time periods we use a spin loop.
        // Note that we will still have jitter (due to the scheduler, but
        // this is probably better than nothing).
        while (System.nanoTime() < end)
        {
        	// just spin
        }
    }

    /**
     * Wait for the specified number of nanoseconds.
     * Delays the current thread for the specified period of time. Can not
     * be interrupted.
     * @param period time to wait in ns
     */
    public static void nsDelay(long period)
    {
        long end = System.nanoTime() + period;
        msDelay(period/1000000);
        // To improve accuracy for small time periods we use a spin loop.
        // Note that we will still have jitter (due to the scheduler, but
        // this is probably better than nothing).
        while (System.nanoTime() < end)
        {
        	// just spin
        }
    }

}
