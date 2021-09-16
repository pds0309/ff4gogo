package com.pds.web.exception;

public class UserRequestException extends RuntimeException{

    private static final long serialVersionUID = -8634700792767837033L;

    private final int code;

    public UserRequestException(String message , int status){
        super(message);
        this.code = status;
    }
    public UserRequestException(ErrorInfo errorInfo){
        super(errorInfo.getErrorMsg());
        this.code = errorInfo.getErrorCode();
    }

    public int getCode() {
        return code;
    }
}
