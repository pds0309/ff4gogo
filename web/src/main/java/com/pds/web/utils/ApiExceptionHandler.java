package com.pds.web.utils;


import com.pds.web.exception.ErrorInfo;
import com.pds.web.exception.UserRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @ExceptionHandler(value = {MissingServletRequestParameterException.class , HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleMissParamException(Exception e, HttpServletRequest request) {
        basicLogs(request);
        simpleExceptionLog(e);
        ErrorInfo paramError = ErrorInfo.PARAMETER_INVALID;
        return ResponseHandler.generateResponse(
                paramError.getErrorMsg(), paramError.getErrorCode(), HttpStatus.BAD_REQUEST, null, request.getRequestURI());
    }
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMissParamException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        basicLogs(request);
        simpleExceptionLog(e);
        ErrorInfo paramError = ErrorInfo.PARAMETER_INVALID;
        return ResponseHandler.generateResponse(
                paramError.getErrorMsg(), paramError.getErrorCode(), HttpStatus.BAD_REQUEST, null, request.getRequestURI());
    }

    //405
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ModelAndView handleNotAllowedMethodException(HttpRequestMethodNotSupportedException e,HttpServletRequest request){
        basicLogs(request);
        simpleExceptionLog(e);
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorcode",HttpStatus.METHOD_NOT_ALLOWED.value());
        mav.addObject("errorname",HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        mav.addObject("errormsg","["+ ErrorInfo.METHOD_INVALID.getErrorCode()+"] \n" + ErrorInfo.METHOD_INVALID.getErrorMsg() + e.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    public ResponseEntity<Object> handleHttpApiErrorException(HttpClientErrorException e, HttpServletRequest request) {
        basicLogs(request);
        simpleExceptionLog(e);
        ErrorInfo errorInfo;
        int code = e.getRawStatusCode();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if(code == 403){
            errorInfo = ErrorInfo.FF4_API_CANNOT_READ;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        else{
            errorInfo = ErrorInfo.PARAMETER_INVALID;
        }
        String message = e.getStatusText() + errorInfo.getErrorMsg();
        return ResponseHandler.generateResponse(
                message, errorInfo.getErrorCode(), status, null, request.getRequestURI());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIlligalArgException(IllegalArgumentException e , HttpServletRequest request){
        basicLogs(request);
        simpleExceptionLog(e);
        return ResponseHandler.generateResponse(
                e.getMessage(),ErrorInfo.METHOD_INVALID.getErrorCode(),HttpStatus.BAD_REQUEST,null, request.getRequestURI()
        );
    }

    //TODO 향후 해당 예외 발견될 경우 따로 예외 처리한다.
    @ExceptionHandler(value = {Exception.class})
    public ModelAndView handleAllException(final Exception e, HttpServletRequest request){
        basicLogs(request);
        log.info("===============exception detail============");
        log.info(e);
        log.info("===========================================");
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorcode",HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("errorname",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        mav.addObject("errormsg","["+ErrorInfo.SERVER_ERROR.getErrorCode()+"] \n"
         + ErrorInfo.SERVER_ERROR.getErrorMsg());
        mav.setViewName("error");
        return mav;
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
