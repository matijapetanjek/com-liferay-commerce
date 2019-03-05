package com.liferay.commerce.batch.item.internal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liferay.commerce.openapi.core.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Date;

public class SkuDTO {

	@Nullable
	public BigDecimal getCost() {
		return _cost;
	}

	@Nullable
	public Double getDepth() {
		return _depth;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	@Nullable
	public Date getDisplayDate() {
		return _displayDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	@Nullable
	public Date getExpirationDate() {
		return _expirationDate;
	}

	public String getExternalReferenceCode() {
		return _externalReferenceCode;
	}

	@Nullable
	public String getGtin() {
		return _gtin;
	}

	@Nullable
	public Double getHeight() {
		return _height;
	}

	@Nullable
	public Long getId() {
		return _id;
	}

	@Nullable
	public String getManufacturerPartNumber() {
		return _manufacturerPartNumber;
	}

	@Nullable
	public BigDecimal getPrice() {
		return _price;
	}

	@Nullable
	public BigDecimal getPromoPrice() {
		return _promoPrice;
	}

	public String getSku() {
		return _sku;
	}

	@Nullable
	public Double getWeight() {
		return _weight;
	}

	@Nullable
	public Double getWidth() {
		return _width;
	}

	@Nullable
	public Boolean isNeverExpire() {
		return _neverExpire;
	}

	@Nullable
	public Boolean isPublished() {
		return _published;
	}

	@Nullable
	public Boolean isPurchasable() {
		return _purchasable;
	}

	public void setCost(BigDecimal cost) {
		_cost = cost;
	}

	public void setDepth(Double depth) {
		_depth = depth;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	public void setDisplayDate(Date displayDate) {
		_displayDate = displayDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	public void setExpirationDate(Date expirationDate) {
		_expirationDate = expirationDate;
	}

	public void setExternalReferenceCode(String externalReferenceCode) {
		_externalReferenceCode = externalReferenceCode;
	}

	public void setGtin(String gtin) {
		_gtin = gtin;
	}

	public void setHeight(Double height) {
		_height = height;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		_manufacturerPartNumber = manufacturerPartNumber;
	}

	public void setNeverExpire(Boolean neverExpire) {
		_neverExpire = neverExpire;
	}

	public void setPrice(BigDecimal price) {
		_price = price;
	}

	public void setPromoPrice(BigDecimal promoPrice) {
		_promoPrice = promoPrice;
	}

	public void setPublished(Boolean published) {
		_published = published;
	}

	public void setPurchasable(Boolean purchasable) {
		_purchasable = purchasable;
	}

	public void setSku(String sku) {
		_sku = sku;
	}

	public void setWeight(Double weight) {
		_weight = weight;
	}

	public void setWidth(Double width) {
		_width = width;
	}

	@Nullable
	private BigDecimal _cost;

	@Nullable
	private Double _depth;

	@Nullable
	private Date _displayDate;

	@Nullable
	private Date _expirationDate;

	private String _externalReferenceCode;

	@Nullable
	private String _gtin;

	@Nullable
	private Double _height;

	@Nullable
	private Long _id;

	@Nullable
	private String _manufacturerPartNumber;

	@Nullable
	private Boolean _neverExpire;

	@Nullable
	private BigDecimal _price;

	@Nullable
	private BigDecimal _promoPrice;

	@Nullable
	private Boolean _published;

	@Nullable
	private Boolean _purchasable;

	private String _sku;

	@Nullable
	private Double _weight;

	@Nullable
	private Double _width;

}