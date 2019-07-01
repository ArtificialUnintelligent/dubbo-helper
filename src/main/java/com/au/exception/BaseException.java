package com.au.exception;

/**
 * @author:artificialunintelligent
 * @Date:2019-06-26
 * @Time:15:03
 */
public class BaseException extends Exception {

    private static final long serialVersionUID = 1653952641406154595L;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
