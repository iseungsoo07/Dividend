package com.zerobase.mydividend.exception.impl;

import com.zerobase.mydividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class TickerEmptyException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "ticker가 없습니다.";
    }
}
