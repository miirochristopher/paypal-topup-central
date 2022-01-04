package com.ph1.topup.central.payments.services;

import com.ph1.topup.central.exceptions.Exception;
import com.ph1.topup.central.payments.domain.PayPal;
import com.ph1.topup.central.payments.repository.PayPalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LedgerService
{
	@Autowired
	private PayPalRepository payPalRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(LedgerService.class);

	@Transactional
	public Iterable<PayPal> findAll()
	{
		return payPalRepository.findAll();
	}
	
	@Transactional
	public PayPal findByPaymentId(String paymentId)
	{
		PayPal payment = payPalRepository.findByPaymentId(paymentId);

		if (payment == null)
		{
			throw new Exception("Could not find payment using Payment Id " + paymentId.toString() + "." + " Contact the support team!");
		}
		return payment;
	}

	@Transactional
	public void approvedPayment(PayPal approved)
	{

		try {

			payPalRepository.save(approved);
			LOGGER.info(":::::Transferred Successfully!:::::");
			LOGGER.info(approved.toString());
		}
		catch (Exception e)
		{
			LOGGER.info(e.getMessage());
			throw new Exception(":::::Could not verify payment:::::" + approved.getId().toString() + " contact the support team!");
		}

	}

	@Transactional
	public void deleteByPaymentId(String paymentId)
	{
		PayPal payment = payPalRepository.findByPaymentId(paymentId);

		if(payment == null)
		{
			throw new Exception("Could not find payment with ID " + paymentId.toString() + "." + " Contact the support team!");
		}
		payPalRepository.delete(payment);
	}

}
