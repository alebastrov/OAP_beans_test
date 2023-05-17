package org.example.linking;

import lombok.ToString;

@ToString
public class MessageProducer {
    public Ace toBeResolved;

    public MessageProducer(Ace toBeResolved) {
        this.toBeResolved = toBeResolved;
    }
}
