package org.example.tasks;

import org.example.common.RemoteExecutable;

public class FibonacciTask {
    @RemoteExecutable
    public long compute(int n) {
        if (n <= 1) return n;
        return compute(n - 1) + compute(n - 2);
    }
}
