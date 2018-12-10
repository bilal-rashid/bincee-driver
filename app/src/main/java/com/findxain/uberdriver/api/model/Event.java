package com.findxain.uberdriver.api.model;

public class Event<T> {
    public boolean isHandled = false;
    private T data;

    public Event(T data) {
        this.data = data;
    }

    public T getData() {
        if (!isHandled) {
            isHandled = true;
            return data;
        } else return null;

    }
}
