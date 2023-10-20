package org.example.healtz.utils.types;

public class ZGC implements GarbageCollectors {
    @Override
    public boolean isConcurrentPhase( String cause, String name ) {
        return "No GC".equals( cause ) || "ZGC Cycles".equals( name );
    }
}
