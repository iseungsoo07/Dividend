package com.zerobase.mydividend.exception.impl;

import com.zerobase.mydividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UnexpectedMonthException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "1월 ~ 12월 사이의 값이 아닙니다.";
    }
}
