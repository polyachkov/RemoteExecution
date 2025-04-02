package org.example.tasks;

import org.example.common.RemoteExecutable;

public class MultiArgsTask {

    @RemoteExecutable
    public int sumThreeNumbers(int x, int y, int z) {
        return x + y + z;
    }

    @RemoteExecutable
    public String concatTwoStrings(String a, String b) {
        return a + "-" + b;
    }
}
