package org.example.tasks;

import org.example.common.RemoteExecutable;

public class SingleElementTask {

    @RemoteExecutable
    public Integer applyOneElement(Integer x) {
        return x + 100;
    }

    @RemoteExecutable
    public String greet(String name) {
        return "Hello! " + name;
    }
}
