package ua.kpi.facadeservice.model;

import java.util.UUID;

public class Message {
    public UUID id;
    public String text;

    public Message(UUID id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
