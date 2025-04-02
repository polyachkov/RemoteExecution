package org.example.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Запрос на вызов: className, methodName, args
 */
public class RemoteRequest implements Serializable {
    private final String className;
    private final String methodName;
    private final Object[] args;

    public RemoteRequest(String className, String methodName, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "RemoteRequest{" +
            "className='" + className + '\'' +
            ", methodName='" + methodName + '\'' +
            ", args=" + Arrays.toString(args) +
            '}';
    }
}
