package com.ph1.topup.central.application;

import com.ph1.topup.central.payments.domain.PayPal;
import com.ph1.topup.central.payments.services.LedgerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/payments/api/v1/ph1/topup/central")
public class PhenOnePayments
{
	@Autowired
	private LedgerService ledgerService;
	
	@ApiOperation(value = "View a list of approved PayPal payments", response = PayPal.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved PayPal payments"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@GetMapping("/ledger")
	@Produces(MediaType.APPLICATION_JSON)
	public  ResponseEntity<?> getAllPayments()
	{
			Iterable<PayPal> payments  =   ledgerService.findAll();

			return new ResponseEntity<>(payments, HttpStatus.OK);
	}

	@ApiOperation(value = "Filter approved PayPal payments by paymentId", response = PayPal.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved PayPal payments"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@GetMapping("/ledger/{paymentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public  ResponseEntity<?> getByPaymentId(@PathVariable String paymentId)
	{
			PayPal payment = ledgerService.findByPaymentId(paymentId);

			return new ResponseEntity<>(payment, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete approved PayPal payment by Email", response = PayPal.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved PayPal payment"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@DeleteMapping("/ledger/{paymentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	public ResponseEntity<?> deleteByPaymentId(@PathVariable String paymentId)
	{
		ledgerService.deleteByPaymentId(paymentId);

		return new ResponseEntity<String>("The payment with Payment Id " + paymentId + " was deleted Successfully!", HttpStatus.OK);
	}
	
}
