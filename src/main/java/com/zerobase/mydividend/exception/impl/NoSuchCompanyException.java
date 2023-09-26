package com.zerobase.mydividend.exception.impl;

import com.zerobase.mydividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoSuchCompanyException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 회사입니다.";
    }
}
