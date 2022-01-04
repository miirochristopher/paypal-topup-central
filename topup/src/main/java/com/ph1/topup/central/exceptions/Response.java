package com.ph1.topup.central.exceptions;

public class Response
{
    private String filed;

    public Response(String filed)
    {
        this.filed = filed;
    }

    public String getFiled()
    {
        return filed;
    }

    public void setFiled(String filed)
    {
        this.filed = filed;
    }
    
}
