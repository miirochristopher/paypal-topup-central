package com.ph1.topup.central.universals;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class DingAuth
{
	public HttpHeaders getHttpHeaders()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "api_key");
		headers.set("api_key","GsVbMNtuF6D5emwNz7MI8J");
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}
