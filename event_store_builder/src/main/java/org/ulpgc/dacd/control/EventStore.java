package org.ulpgc.dacd.control;

import java.util.List;

public interface EventStore {
    void write(String event, String basePath);
}
