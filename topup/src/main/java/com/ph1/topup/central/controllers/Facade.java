package com.ph1.topup.central.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Facade
{
	@GetMapping("/")
	public String getHomePage(Model model)
	{
		return "index";
	}

	@GetMapping("/error")
	public String getErrorPage(Model model)
	{
		return "error";
	}

	@GetMapping("/terms")
	public String getTermsPage(Model model)
	{
		return "terms";
	}

	@GetMapping("/payment")
	@PreAuthorize("hasRole('ROLE_CUSTOMER')")
	public String getPaymentPage(Model model)
	{
		return "payment";
	}

	@GetMapping("/success")
	public String getSuccessfulPaymentPage(Model model)
	{
		return "success";
	}

	@GetMapping("/cancel")
	public String getCancelPaymentPage(Model model)
	{
		return "cancel";
	}

}
