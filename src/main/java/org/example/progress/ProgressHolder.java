package org.example.progress;

import lombok.Data;

import java.util.UUID;

@Data
public class ProgressHolder implements Progressor {
    private String id;

    private double start;
    private double current;
    private double end;
    private boolean frozen;
    private String message;

    public void tick() {
        tick( 1.0 );
    }
    public void tick( double increment ) {
        current += increment;
        if ( increment < 0 && current <= start ) frozen = true;
        if ( increment > 0 && current >= end ) frozen = true;
    }



}
