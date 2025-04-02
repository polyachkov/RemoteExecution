package org.example.common;

import java.io.Serializable;

/**
 * Перечисление команд, чтобы отличать UPLOAD_JAR и INVOKE_METHOD.
 */
public enum Commands implements Serializable {
    UPLOAD_JAR,
    INVOKE_METHOD
}
