package org.example.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация, указывающая, что метод можно вызывать удалённо.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteExecutable {
}
