package com.ph1.topup.central.payments.repository;

import com.ph1.topup.central.payments.domain.PayPal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayPalRepository extends JpaRepository<PayPal, Long>
{
	PayPal findByPaymentId(String paymentId);
}
