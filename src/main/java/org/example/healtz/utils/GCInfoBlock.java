package org.example.healtz.utils;

import java.lang.management.MemoryUsage;

public class GCInfoBlock {
  public double getCompacted() {
    return 2.0 + 2.0 * Math.log( compacted );
  }

  public void setCompacted( double compacted ) {
    this.compacted = compacted;
  }

  public boolean isEmpty() {
    return empty;
  }

  public enum Payloads {
    OK( 85 ),
    MEDIUM_LOAD( 75 ),
    HIGH_LOAD( 45 ),
    SLOWDOWN( 9 );

    private int userLoadInPercents;

    Payloads( int userLoadInPercents ) {
      this.userLoadInPercents = userLoadInPercents;
    }

    static Payloads getByLoad( int currentUsagePercent ) {
      if ( currentUsagePercent <= 0 ) return SLOWDOWN;
      if ( currentUsagePercent >= 100 ) return OK;
      for ( Payloads payload : values() ) {
        if ( payload.userLoadInPercents > currentUsagePercent ) continue;
        return payload;
      }
      return OK;
    }
  }

  private static long maxDuration = 200L;

  private String gcName = "";
  private long time;
  private MemoryUsage memoryUsage;
  private long callNumber;
  private long duration;
  private Payloads gcState = Payloads.OK;
  private double compacted = 1.0;
  private GcType type = GcType.STW;
  private boolean empty = false;

  public void setEmpty( boolean empty ) {
    this.empty = empty;
  }

  enum GcType {
    CONCURRENT, STW
  }

  public GCInfoBlock() {
  }

  public GcType getType() {
    return type;
  }

  public void setType( GcType type ) {
    this.type = type;
  }

  public void setGCName( String gcName ) {
    this.gcName = gcName;
  }

  public void setTime( long time ) {
    this.time = time;
  }

  public void setMemoryUsage( MemoryUsage memoryUsage ) {
    this.memoryUsage = memoryUsage;
  }

  public String getGCName() {
    return gcName;
  }

  public long getTime() {
    return time;
  }

  public long getUsedMemory() {
    return memoryUsage.getUsed();
  }

  public long getFreeMemory() {
    return memoryUsage.getCommitted() - memoryUsage.getUsed();
  }

  public MemoryUsage getMemoryUsage() {
    return memoryUsage;
  }

  public String getTenuredGenString() {
    return memoryUsage.toString();
  }

  public long getMaxMemory() {
    return memoryUsage.getMax();
  }

  public void setCallNumber( long callNumber ) {
    this.callNumber = callNumber;
  }

  public long getCallNumber() {
    return callNumber;
  }

  public void setDuration( Long duration ) {
    this.duration = duration == null ? 0 : duration;
  }

  public long getDuration() {
    return duration;
  }

  public static long getMaxDuration() {
    return maxDuration;
  }

  public Payloads getGcState() {
    return gcState;
  }

  public void setGcState( Payloads gcState ) {
    this.gcState = gcState;
  }
}
