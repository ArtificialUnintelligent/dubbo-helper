package com.au.exception;

/**
 * @author:artificialunintelligent
 * @Date:2019-06-26
 * @Time:15:04
 */
public class InitializationFailedException extends BaseException {

    private static final long serialVersionUID = -5613453279126040163L;

    private static final String MESSAGE = "initialization failed error, dubbo-config is null";

    public InitializationFailedException() {
        super(MESSAGE);
    }

    public InitializationFailedException(String message, Throwable cause) {
        super(MESSAGE, cause);
    }
}
