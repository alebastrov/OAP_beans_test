package org.example.progress;

public interface Progressor {
    double getStart();
    double getEnd();
    double getCurrent();
    String getMessage();
    String getId();
}
