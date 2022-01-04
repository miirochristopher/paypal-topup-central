package com.ph1.topup.central.application;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.ph1.topup.central.payments.domain.PayPal;
import com.ph1.topup.central.payments.services.LedgerService;
import com.ph1.topup.central.payments.services.PayPalService;
import com.ph1.topup.central.universals.Constants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.UnknownHostException;

@Controller
public class PayPalPayments
{
	@Autowired
	private PayPalService payPalService;
	
	public final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private LedgerService ledgerService;

	@ApiOperation(value = "Initiate PayPal payment", response = String.class)
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public   String payment(
			 @Valid @ModelAttribute("transaction") PayPal transaction,
			 BindingResult result,
			 RedirectAttributes redirectAttributes,
			 HttpServletRequest request
	) throws UnknownHostException
	{
		String abandonUrl   =
				 request.getScheme()
						.concat("://")
						.concat(request.getLocalName())
					    .concat(":")
					    .concat(String.valueOf(request.getLocalPort()))
					    .concat("/")
					    .concat(Constants.PAYPAL_ABANDON_URL);
		String successUrl   =
				 request.getScheme()
						.concat("://")
						.concat(request.getLocalName())
					    .concat(":")
					    .concat(String.valueOf(request.getLocalPort()))
					    .concat("/")
					    .concat(Constants.PAYPAL_SUCCESS_URL);
		
		try
		{
			Payment payment = payPalService.createPayment
							(
							  transaction.getEmail(),
							  transaction.getPhone(),
							  transaction.getAmount(),
									PayPal.PAYMENT_METHOD,
									PayPal.PAYMENT_INTENT,
									PayPal.PAYMENT_CURRENCY,
							  transaction.getDescription(),
									abandonUrl,
									successUrl
							);

			redirectAttributes.addFlashAttribute("transaction", transaction);

			for(Links link:payment.getLinks())
			{
				if(link.getRel().equals("approval_url"))
				{
					return "redirect:"+ link.getHref();
				}
			}

		}
		catch (PayPalRESTException e)
		{
			LOGGER.info(e.getMessage());
		}

		return "redirect:/";

	}
	
}
