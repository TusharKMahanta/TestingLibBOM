package com.hh.testingframwork.kafka.container;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class KafkaMessageContainer<V> {
    private List<V> messages;

    public KafkaMessageContainer() {
        messages = new ArrayList<>();
    }

    public void add(V element) {
        messages.add(element);
    }

    public List<V> filter(Predicate<V> predicate) {
        return messages.stream().filter(predicate).toList();
    }
}