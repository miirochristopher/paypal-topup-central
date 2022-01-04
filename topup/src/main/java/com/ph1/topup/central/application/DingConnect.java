package com.ph1.topup.central.application;

import com.ph1.topup.central.universals.Constants;
import com.ph1.topup.central.universals.DingAuth;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;

@RestController
@RequestMapping("/products/api/v1/ph1/topup/central")
public class DingConnect
{
	@Autowired
	private DingAuth dingAuth;

	RestTemplate restTemplate = new RestTemplate();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DingConnect.class);
	
	@ApiOperation(value = "View a list of available products from Ding", response = Serializable.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Ding products"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@RequestMapping(value = "/ding", produces="application/json", method = RequestMethod.GET)
	public Serializable getDingProducts() throws IOException
	{
		HttpHeaders headers = dingAuth.getHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response =
				restTemplate.exchange(Constants.DING_PRODUCTS_URI, HttpMethod.GET, entity, String.class);

		if(response.getStatusCode() == HttpStatus.OK)
		{
			return response.getBody();
		}
		else{
			return "error";
		}

	}

}



