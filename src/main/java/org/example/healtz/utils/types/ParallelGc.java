package org.example.healtz.utils.types;

//-XX:+UseParallelGC
public class ParallelGc implements GarbageCollectors {
    @Override
    public boolean isConcurrentPhase( String cause, String name ) {
        return false;
    }
}
