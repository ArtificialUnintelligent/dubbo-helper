package com.au.exception;

/**
 * @author:artificialunintelligent
 * @Date:2019-07-01
 * @Time:15:02
 */
public class GetGenericServiceFailedException extends BaseException{
    private static final long serialVersionUID = -5613453279126040163L;

    private static final String MESSAGE = "get generic service failed error, service not exists";

    public GetGenericServiceFailedException() {
        super(MESSAGE);
    }

    public GetGenericServiceFailedException(String message, Throwable cause) {
        super(MESSAGE, cause);
    }
}
