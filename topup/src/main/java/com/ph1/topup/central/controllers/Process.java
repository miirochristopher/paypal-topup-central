package com.ph1.topup.central.controllers;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import com.ph1.topup.central.payments.domain.PayPal;
import com.ph1.topup.central.payments.services.LedgerService;
import com.ph1.topup.central.payments.services.PayPalService;
import com.ph1.topup.central.universals.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Process
{
	@Autowired
	private PayPalService payPalService;

	@Autowired
	private LedgerService ledgerService;

	private static final Logger LOGGER     = LoggerFactory.getLogger(Process.class);

	private List<Transaction> transactions;

	@RequestMapping(value = Constants.PAYPAL_ABANDON_URL, method = RequestMethod.GET)
	public String cancelPay()
	{
		return "redirect:/cancel";
	}
	
	@RequestMapping(value = Constants.PAYPAL_SUCCESS_URL, method = RequestMethod.GET)
	public String successPay(
			@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId,
			@Valid @ModelAttribute("transaction") PayPal transaction,
			BindingResult result,
			RedirectAttributes redirectAttributes) throws ParseException {
		try
		{
			Payment payment = payPalService.executePayment(paymentId, payerId);
			
			LOGGER.info(payment.toJSON());

			if (payment.getState().equals("approved"))
			{
				transaction.setPaymentId(payment.getId());
				transaction.setState(payment.getState());
				transaction.setTimes(payment.getCreateTime());
				transaction.setEmail(payment.getPayer().getPayerInfo().getEmail());
				transaction.setPhone(payment.getPayer().getPayerInfo().getPhone());
				transaction.setCountry(payment.getPayer().getPayerInfo().getCountryCode());
				transaction.setAmount(convertAmountToDouble(getTransactionAmount(payment)));
				transaction.setDescription(getTransactionDescription(payment));
				
				ledgerService.approvedPayment(transaction);

				return "redirect:/success";
				
			}

		}
		catch (PayPalRESTException e)
		{
			LOGGER.info(e.getMessage());
		}

		return "redirect:/payment";
	}

	private String getTransactionAmount(Payment payment)
			throws ParseException
	{
		List<Transaction> transactions = new ArrayList<>(payment.getTransactions());

		String amount = null;

		for (Transaction completed : transactions)
		{
			amount = completed.getAmount().getTotal();
		}

		return amount;
	}

	private String getTransactionCurrency(Payment payment)
			throws ParseException
	{
		List<Transaction> transactions = new ArrayList<>(payment.getTransactions());

		String currency = null;

		for (Transaction completed : transactions)
		{
			currency = completed.getAmount().getCurrency();
		}

		return currency;
	}

	private String getTransactionDescription(Payment payment)
	{
		List<Transaction> transactions = new ArrayList<>(payment.getTransactions());

		String description = null;

		for (Transaction completed : transactions)
		{
			description = completed.getDescription();
		}

		return description;
	}

	private double convertAmountToDouble(String amount) throws ParseException
	{
		NumberFormat parseAmountDouble = NumberFormat.getInstance();
		return parseAmountDouble.parse(amount).doubleValue();
	}
	
}
