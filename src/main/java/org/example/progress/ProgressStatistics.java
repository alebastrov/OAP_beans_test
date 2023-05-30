package org.example.progress;

public class ProgressStatistics {
    private Long momentOfFirstTick = null;
    private Long momentOfCurrentTick = null;
    private double current;
    private final double max;
    private double avgSpeed;

    public ProgressStatistics( double max ) {
        this.max = max;
    }

    void tick( double current ) {
        if ( this.current == current || max == 0.0 ) return;
        if ( momentOfFirstTick == null ) {
            momentOfFirstTick = System.currentTimeMillis();
        }
        momentOfCurrentTick = System.currentTimeMillis();
        this.current = current;
    }

    public double speed() {
        if ( momentOfFirstTick == null || momentOfCurrentTick == null ) return 0.0;
        double ticksPerSecond = 1_000.0 * current / ( 1 + momentOfCurrentTick - momentOfFirstTick ); // tick/sec
        if ( avgSpeed == 0.0 ) {
            avgSpeed = ticksPerSecond;
        } else {
            avgSpeed = ( 2.0 * avgSpeed + ticksPerSecond ) / 3.0;
        }
        return avgSpeed != 0.0 ? avgSpeed : ticksPerSecond;
    }

    public double percent() {
        return 100.0 * current / max;
    }

    public long elapsedMilliseconds() {
        return System.currentTimeMillis() - momentOfFirstTick;
    }

    public long remainingSeconds() {
        double ticksLeft = max - current;
        long secondsToGo = (long) (1.0 * ticksLeft / avgSpeed);
        return secondsToGo;
    }

}
