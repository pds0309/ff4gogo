package com.pds.web.utils;


import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Log4j2
public class ApiExceptionHandler{

    @ExceptionHandler(value = {UserRequestException.class})
    public ResponseEntity<Object> handleUserRequestException(UserRequestException e, HttpServletRequest request){
        basicLogs(request);
        simpleExceptionLog(e);
        return ResponseHandler.generateResponse(
                e.getMessage(), e.getCode(), HttpStatus.BAD_REQUEST, null, request.getRequestURI());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleMissParamException(MissingServletRequestParameterException e, HttpServletRequest request) {
        basicLogs(request);
        simpleExceptionLog(e);
        ErrorInfo paramError = ErrorInfo.PARAMETER_INVALID;
        return ResponseHandler.generateResponse(
                paramError.getErrorMsg(), paramError.getErrorCode(), HttpStatus.BAD_REQUEST, null, request.getRequestURI());
    }



    public void basicLogs(HttpServletRequest request){
        Map<String, String> map = new HashMap<>();
        map.put("from", request.getRemoteAddr());
        String queryString = request.getQueryString();
        if(request.getQueryString()!=null){
            map.put("QueryString",request.getQueryString());
        }
        map.put("path", queryString != null ? (request.getRequestURI() + "?" + queryString) : request.getRequestURI());
        log.info(map);
    }
    public void simpleExceptionLog(Exception e){
        log.info("{"+ e.getClass().getSimpleName()+"}  " + e.getMessage());
    }
}
