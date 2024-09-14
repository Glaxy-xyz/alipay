package com.business.apply_system.common.exception;

import lombok.ToString;

import java.util.Map;

@ToString
public class ServiceException extends RuntimeException {

    private int httpStatus = 400;
    private final String code;
    private String requestId;
    private Map<String, String> detail;

    public ServiceException(String code) {
       this.code = code;
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String code, String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public ServiceException(String code, String message, int httpStatus, Map<String, String> detail) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceException that = (ServiceException) o;

        if (httpStatus != that.httpStatus) {
            return false;
        }
        if (code != null ? !code.equals(that.code) : that.code != null) {
            return false;
        }
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) {
            return false;
        }
        return detail != null ? detail.equals(that.detail) : that.detail == null;

    }

    @Override
    public int hashCode() {
        int result = httpStatus;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        return result;
    }

    public String getErrorCode() {
        return code;
    }

    public String getErrorMessage() {
        return getMessage();
    }
}
