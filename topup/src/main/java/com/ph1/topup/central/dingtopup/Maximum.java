package com.ph1.topup.central.dingtopup;

public class Maximum
{
	 public Double CustomerFee;
	 public Double DistributorFee;
	 public Double ReceiveValue;
	 public String ReceiveCurrencyIso;
	 public Double ReceiveValueExcludingTax;
	 public Double TaxRate;
	 public String TaxName;
	 public Double TaxCalculation;
	 public Double SendValue;
	 public String SendCurrencyIso;

	public Maximum() {
	}

	public Maximum(Double customerFee, Double distributorFee, Double receiveValue, String receiveCurrencyIso,
			Double receiveValueExcludingTax, Double taxRate, String taxName, Double taxCalculation, Double sendValue,
			String sendCurrencyIso) {
		CustomerFee = customerFee;
		DistributorFee = distributorFee;
		ReceiveValue = receiveValue;
		ReceiveCurrencyIso = receiveCurrencyIso;
		ReceiveValueExcludingTax = receiveValueExcludingTax;
		TaxRate = taxRate;
		TaxName = taxName;
		TaxCalculation = taxCalculation;
		SendValue = sendValue;
		SendCurrencyIso = sendCurrencyIso;
	}

	public Double getCustomerFee()
	{
		return CustomerFee;
	}

	public void setCustomerFee(Double customerFee)
	{
		CustomerFee = customerFee;
	}

	public Double getDistributorFee()
	{
		return DistributorFee;
	}

	public void setDistributorFee(Double distributorFee)
	{
		DistributorFee = distributorFee;
	}

	public Double getReceiveValue()
	{
		return ReceiveValue;
	}

	public void setReceiveValue(Double receiveValue)
	{
		ReceiveValue = receiveValue;
	}

	public String getReceiveCurrencyIso()
	{
		return ReceiveCurrencyIso;
	}

	public void setReceiveCurrencyIso(String receiveCurrencyIso)
	{
		ReceiveCurrencyIso = receiveCurrencyIso;
	}

	public Double getReceiveValueExcludingTax()
	{
		return ReceiveValueExcludingTax;
	}

	public void setReceiveValueExcludingTax(Double receiveValueExcludingTax)
	{
		ReceiveValueExcludingTax = receiveValueExcludingTax;
	}

	public Double getTaxRate()
	{
		return TaxRate;
	}

	public void setTaxRate(Double taxRate)
	{
		TaxRate = taxRate;
	}

	public String getTaxName()
	{
		return TaxName;
	}

	public void setTaxName(String taxName)
	{
		TaxName = taxName;
	}

	public Double getTaxCalculation()
	{
		return TaxCalculation;
	}

	public void setTaxCalculation(Double taxCalculation)
	{
		TaxCalculation = taxCalculation;
	}

	public Double getSendValue()
	{
		return SendValue;
	}

	public void setSendValue(Double sendValue)
	{
		SendValue = sendValue;
	}

	public String getSendCurrencyIso()
	{
		return SendCurrencyIso;
	}

	public void setSendCurrencyIso(String sendCurrencyIso)
	{
		SendCurrencyIso = sendCurrencyIso;
	}

	@Override
	public String toString()
	{
		return "Maximum{" +
				"CustomerFee=" + CustomerFee +
				", DistributorFee=" + DistributorFee +
				", ReceiveValue=" + ReceiveValue +
				", ReceiveCurrencyIso='" + ReceiveCurrencyIso + '\'' +
				", ReceiveValueExcludingTax=" + ReceiveValueExcludingTax +
				", TaxRate=" + TaxRate +
				", TaxName='" + TaxName + '\'' +
				", TaxCalculation=" + TaxCalculation +
				", SendValue=" + SendValue +
				", SendCurrencyIso='" + SendCurrencyIso + '\'' +
				'}';
	}
	
}


