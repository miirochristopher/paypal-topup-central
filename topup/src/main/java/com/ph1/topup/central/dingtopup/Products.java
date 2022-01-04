package com.ph1.topup.central.dingtopup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "All details about the Ding Products. ")
public class Products implements Serializable
{
	@ApiModelProperty(notes = "The product's email provider code")
	public String ProviderCode;

	@ApiModelProperty(notes = "The product's sku code")
	public String SkuCode;

	@ApiModelProperty(notes = "The product's localization key")
	public String LocalizationKey;

	@ApiModelProperty(notes = "The product's minimum valuation")
	public Map<String, Minimum> Minimum;

	@ApiModelProperty(notes = "The product's maximum valuation")
	public Map<String, Maximum> Maximum;

	public Products() {
	}

	public Products(String providerCode, String skuCode, String localizationKey, Map<String, Minimum> minimum, Map<String, Maximum> maximum)
	{
		ProviderCode = providerCode;
		SkuCode = skuCode;
		LocalizationKey = localizationKey;
		Minimum = minimum;
		Maximum = maximum;
	}

	public String getProviderCode()
	{
		return ProviderCode;
	}

	public void setProviderCode(String providerCode)
	{
		ProviderCode = providerCode;
	}

	public String getSkuCode()
	{
		return SkuCode;
	}

	public void setSkuCode(String skuCode)
	{
		SkuCode = skuCode;
	}

	public String getLocalizationKey()
	{
		return LocalizationKey;
	}

	public void setLocalizationKey(String localizationKey)
	{
		LocalizationKey = localizationKey;
	}

	public Map<String, Minimum> getMinimum()
	{
		return Minimum;
	}

	public void setMinimum(Map<String, Minimum> minimum)
	{
		Minimum = minimum;
	}

	public Map<String, Maximum> getMaximum()
	{
		return Maximum;
	}

	public void setMaximum(Map<String, Maximum> maximum)
	{
		Maximum = maximum;
	}

	@Override
	public String toString()
	{
		return "Products{" +
				"ProviderCode='" + ProviderCode + '\'' +
				", SkuCode='" + SkuCode + '\'' +
				", LocalizationKey='" + LocalizationKey + '\'' +
				", Minimum=" + Minimum +
				", Maximum=" + Maximum +
				'}';
	}

}
