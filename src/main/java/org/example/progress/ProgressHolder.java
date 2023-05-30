package org.example.progress;

import lombok.Data;


@Data
public class ProgressHolder implements Progressor {
    private String id;

    private double start;
    private double current;
    private double end;
    private boolean frozen;
    private String message;
    private ProgressStatistics statistics = null;

    public void tick() {
        tick( 1.0 );
    }
    public void tick( double increment ) {
        if ( !frozen && end != 0.0 && statistics == null ) {
            statistics = new ProgressStatistics( end );
        }
        current += increment;
        if ( increment < 0 && current <= start ) frozen = true;
        if ( increment > 0 && current >= end ) frozen = true;
        if ( statistics!= null && !frozen ){
            statistics.tick( current );
        }
        if ( statistics != null && !frozen && ((long)current) % 500 == 0) {
            System.err.println( "Speed: " + statistics.speed() +
                    ", " + statistics.percent() + " %" +
                    ", remain: " + statistics.remainingSeconds() + " sec");
        }
    }



}
