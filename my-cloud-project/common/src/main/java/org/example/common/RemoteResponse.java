package org.example.common;

import java.io.Serializable;

/**
 * Ответ от сервера:
 * success/fail, result, errorMsg
 */
public class RemoteResponse implements Serializable {
    private final boolean success;
    private final Object result;
    private final String errorMessage;

    public RemoteResponse(boolean success, Object result, String errorMessage) {
        this.success = success;
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
