/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Java bean for many-to-one attribute.
 * 
 * @author Jan De Moerloose
 */
public class ManyToOneAttributeBean {

	private Long id;

	private Boolean booleanAttr;

	private String currencyAttr;

	private Date dateAttr;

	private Double doubleAttr;

	private Float floatAttr;

	private String imageUrlAttr;

	private Integer integerAttr;

	private Long longAttr;

	private Short shortAttr;

	private String stringAttr;

	private String urlAttr;

	public static List<ManyToOneAttributeBean> manyToOneValues() {
		List<ManyToOneAttributeBean> values = new ArrayList<ManyToOneAttributeBean>();

		ManyToOneAttributeBean bean1 = new ManyToOneAttributeBean();
		bean1.setId(1L);
		bean1.setStringAttr("ManyToOne - 1");
		bean1.setBooleanAttr(true);
		values.add(bean1);

		ManyToOneAttributeBean bean2 = new ManyToOneAttributeBean();
		bean2.setId(2L);
		bean2.setStringAttr("ManyToOne - 2");
		bean2.setBooleanAttr(true);
		values.add(bean2);

		return values;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getBooleanAttr() {
		return booleanAttr;
	}

	public void setBooleanAttr(Boolean booleanAttr) {
		this.booleanAttr = booleanAttr;
	}

	public String getCurrencyAttr() {
		return currencyAttr;
	}

	public void setCurrencyAttr(String currencyAttr) {
		this.currencyAttr = currencyAttr;
	}

	public Date getDateAttr() {
		return dateAttr;
	}

	public void setDateAttr(Date dateAttr) {
		this.dateAttr = dateAttr;
	}

	public Double getDoubleAttr() {
		return doubleAttr;
	}

	public void setDoubleAttr(Double doubleAttr) {
		this.doubleAttr = doubleAttr;
	}

	public Float getFloatAttr() {
		return floatAttr;
	}

	public void setFloatAttr(Float floatAttr) {
		this.floatAttr = floatAttr;
	}

	public String getImageUrlAttr() {
		return imageUrlAttr;
	}

	public void setImageUrlAttr(String imageUrlAttr) {
		this.imageUrlAttr = imageUrlAttr;
	}

	public Integer getIntegerAttr() {
		return integerAttr;
	}

	public void setIntegerAttr(Integer integerAttr) {
		this.integerAttr = integerAttr;
	}

	public Long getLongAttr() {
		return longAttr;
	}

	public void setLongAttr(Long longAttr) {
		this.longAttr = longAttr;
	}

	public Short getShortAttr() {
		return shortAttr;
	}

	public void setShortAttr(Short shortAttr) {
		this.shortAttr = shortAttr;
	}

	public String getStringAttr() {
		return stringAttr;
	}

	public void setStringAttr(String stringAttr) {
		this.stringAttr = stringAttr;
	}

	public String getUrlAttr() {
		return urlAttr;
	}

	public void setUrlAttr(String urlAttr) {
		this.urlAttr = urlAttr;
	}

}
