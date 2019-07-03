package com.au.exception;

/**
 * @author:artificialunintelligent
 * @Date:2019-07-03
 * @Time:19:12
 */
public class ParamException extends BaseException {

    private static final long serialVersionUID = -5613453279126040154L;

    private static final String MESSAGE = "parameters and types need to be consistent";

    public ParamException() {
        super(MESSAGE);
    }

    public ParamException(String message, Throwable cause) {
        super(MESSAGE, cause);
    }
}
