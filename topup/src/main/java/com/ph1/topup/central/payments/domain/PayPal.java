package com.ph1.topup.central.payments.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="payments")
@ApiModel(description = "All details about the PayPal Payments. ")
public class PayPal
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@ApiModelProperty(hidden = true)
	private Long id;

	@Column(name = "state")
	@ApiModelProperty(hidden = true)
	private String  state;

	@NotBlank(message ="Email Address is required!")
	@Column(name = "email")
	@ApiModelProperty(notes = "The payer's email address")
	private String  email;
	
	@Column(name = "phone")
	@ApiModelProperty(notes = "The payer's phone number")
	private String  phone;

	@Column(name = "paid_at")
	@ApiModelProperty(hidden = true)
	private String  times;

	@DecimalMin(value = "1.0", inclusive = true)
	@Column(name = "amount")
	@ApiModelProperty(notes = "The payable amount in USD")
	private double  amount;
	
	public static final String PAYMENT_METHOD    = "paypal";

	public static final String PAYMENT_INTENT    = "sale";

	public static final String PAYMENT_CURRENCY  = "USD";

	@Column(name = "country")
	@ApiModelProperty(notes = "The payer's country/origin")
	private String  country;

	@Column(name = "payment_id")
	@ApiModelProperty(hidden = true)
	private String  paymentId;

	@Column(name = "description")
	@ApiModelProperty(notes = "The payment description/details")
	private String  description;
	
}
