package com.business.apply_system.common.exception.handler;

import com.business.apply_system.common.exception.ServiceException;

import com.business.apply_system.common.result.RestResult;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import java.util.stream.Collectors;


@Slf4j
@Order(80)
@RestControllerAdvice
public class ExceptionHandle {

    private static final String INVALID_REQUEST = "InvalidRequest";

    @Resource
    private ErrorProperties gjErrorProperties;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     ServiceException ex) {
        log.warn("[exception:ServiceException] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(ex.getErrorCode(), ex.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletException.class)
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     ServletException ex) {
        log.warn("[exception:ServletException] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse("400", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     NoHandlerFoundException ex) {
        log.warn("[exception:NoHandlerFoundException] handled:" + request.getMethod() + " " + request.getRequestURI()
                , ex);
        return buildServiceExceptionResponse("404", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("[exception:UnknownException] " + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse("Exception", "Internal Server Error");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response, BadSqlGrammarException ex) {
        log.error("[exception:BadSQL] " + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse("BadSQLException", ex.getSQLException().getMessage());
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     HttpRequestMethodNotSupportedException ex) {
        log.warn("[exception:HttpRequestMethodNotSupportedException] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST,
                String.format("%s %s not support", request.getMethod(), request.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     IllegalArgumentException ex) {
        log.warn("[exception:IllegalArgument] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse("IllegalArgument", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     MethodArgumentNotValidException ex) {
        log.warn("[exception:MethodArgumentNotValidException] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        String msg = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return buildServiceExceptionResponse(INVALID_REQUEST, msg);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     BindException ex) {
        log.warn("[exception:BindException] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);

        String msg = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return buildServiceExceptionResponse(INVALID_REQUEST, msg);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     MethodArgumentTypeMismatchException ex) {
        log.warn("[exception:MethodArgumentTypeMismatchException] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST, "request parameter is error");
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     UnsatisfiedServletRequestParameterException ex) {
        log.warn("[exception:UnsatisfiedServletRequestParameterException] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     HttpMediaTypeNotAcceptableException ex) {
        log.warn("[exception:Exception] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     ConstraintViolationException ex) {
        log.warn("[exception:Exception] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST,
                ex.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     HttpMediaTypeNotSupportedException ex) {
        log.warn("[exception:Exception] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST,
                ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     HttpMessageNotReadableException ex) {
        log.warn("[exception:Exception] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST, getExceptionMessage(ex));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     MissingServletRequestParameterException ex) {
        log.warn("[exception:Exception] handled:" + request.getMethod() + " " + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST,
                ex.getMessage());
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handle(HttpServletRequest request, HttpServletResponse response,
                                     IllegalStateException ex) {
        log.warn("[exception:IllegalStateException] handled: " + request.getMethod() + " "
                + request.getRequestURI(), ex);
        return buildServiceExceptionResponse(INVALID_REQUEST,
                "Ambiguous handler methods mapped for the request");
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResult<String> handleTypeMismatchException(HttpServletRequest request, HttpServletResponse response,
                                                          TypeMismatchException ex) {
        log.warn("[exception:TypeMismatchException] handled: " + request.getMethod() + " " + request.getRequestURI(),
                ex);
        // get the detailed exception message
        Throwable throwable = ex;
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return buildServiceExceptionResponse(INVALID_REQUEST, getExceptionMessage(throwable));
    }

    private RestResult<String> buildServiceExceptionResponse(String code, String message) {
        RestResult<String> response = new RestResult<>();
        response.setCode(code);
        String msg = gjErrorProperties.getMsg(code);
        if (StringUtils.isNotBlank(msg) && StringUtils.isNotBlank(message)) {
            String[] split = message.split(",");
            int index = 0;
            while (msg.contains("{}") && index < split.length) {
                msg = msg.replaceFirst("\\{\\}", split[index++]);
            }
            response.setMessage(msg);
        } else {
            if (StringUtils.isNotBlank(message)) {
                msg = message;
            }
            response.setMessage(msg);
        }
        return response;
    }


    private String getExceptionMessage(Throwable ex) {
        if (ex == null || StringUtils.isEmpty(ex.getMessage())) {
            return "";
        }
        // get the first part of exception detail
        int endOfLine = ex.getMessage().indexOf('\n');
        int endOfSemicolon = ex.getMessage().indexOf(':');
        int end;
        if (endOfLine < 0 && endOfSemicolon < 0) {
            end = ex.getMessage().length();
        } else if (endOfLine > 0 && endOfSemicolon > 0) {
            end = Math.min(endOfLine, endOfSemicolon);
        } else if (endOfLine < 0) {
            end = endOfSemicolon;
        } else {
            end = endOfLine;
        }
        return ex.getMessage().substring(0, end);
    }

}
