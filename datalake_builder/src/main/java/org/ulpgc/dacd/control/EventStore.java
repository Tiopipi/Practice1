package org.ulpgc.dacd.control;


public interface EventStore {
    void write(String event, String basePath, String rootDirectory);
}