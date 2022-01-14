package com.yomahub.liteflow.exception;


public class EmptyConditionValueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** 异常信息 */
    private String message;

    public EmptyConditionValueException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
