package com.ph1.topup.central.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class Exception extends RuntimeException
{
    public Exception(String s)
    {
        super(s);
    }
}
