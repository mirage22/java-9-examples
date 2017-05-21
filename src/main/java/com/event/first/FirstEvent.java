package com.event.first;

import java.util.Date;

/**
 * @author Miro Wengner (@miragemiko)
 */
public class FirstEvent {

    private String source;
    private Date date;
    private String message;

    FirstEvent() {
    }

    String getSource() {
        return source;
    }

    void setSource(String source) {
        this.source = source;
    }

    Date getDate() {
        return date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FirstEvent{" +
                "source='" + source + '\'' +
                ", date=" + date +
                ", message='" + message + '\'' +
                '}';
    }
}
