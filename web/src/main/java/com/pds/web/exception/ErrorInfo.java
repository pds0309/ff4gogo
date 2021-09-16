package com.pds.web.exception;


import lombok.Getter;

@Getter
public enum ErrorInfo {
    //BAD_REQUEST
    USER_DATA_NOT_EXIST(-101, "존재하지 않는 유저입니다."),
    USER_BAD_ACCESS(-102 , "잘못된 방식의 접근입니다. 정상적인 방법으로 조회해주세요"),
    PARAMETER_INVALID(-103, "잘못된 입력값이 식별되었습니다.");


    private final int errorCode;
    private final String errorMsg;

    private ErrorInfo(int code , String msg){
        this.errorCode = code;
        this.errorMsg = msg;
    }
}
