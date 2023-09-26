package com.zerobase.mydividend.exception.impl;

import com.zerobase.mydividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistCompanyException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 회사입니다.";
    }
}
