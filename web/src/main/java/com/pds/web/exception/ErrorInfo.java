package com.pds.web.exception;


import lombok.Getter;

@Getter
public enum ErrorInfo {
    //BAD_REQUEST
    USER_DATA_NOT_EXIST(-101, "존재하지 않는 유저입니다."),
    USER_BAD_ACCESS(-102 , "잘못된 방식의 접근입니다. 정상적인 방법으로 조회해주세요"),
    PARAMETER_INVALID(-103, "잘못된 입력값이 식별되었습니다."),
    METHOD_INVALID(-104,"잘못된 방법의 요청입니다."),
        //종종 몰수경기 등으로 인한 매치 데이터 누락, 경기 정보 없음 및 유저 ID가 존재하지 않는 오류 발생함.
    FF4_API_ERROR(-301 , "FION4 Open-API 메타 데이터가 비정상적입니다. 해당 데이터를 수집하지 않았습니다."),

    //INTERNAL_SERVER_ERROR - 나오면 안된다 어지간하면
    SERVER_ERROR(-999,"내부 오류입니다. Open-API 통신이 원활하지 않거나 피파온라인이 점검중일 수 있습니다.");

    private final int errorCode;
    private final String errorMsg;

    private ErrorInfo(int code , String msg){
        this.errorCode = code;
        this.errorMsg = msg;
    }
}
