package org.example;

public class NotNull {
    public int internalField;
    private String p1;
    private NotNull p2;

    public NotNull(){
    }

    public NotNull(String constructorParameter1, NotNull constructorParameter2) {
        this.p1 = constructorParameter1;
        this.p2 = constructorParameter2;
    }
}
