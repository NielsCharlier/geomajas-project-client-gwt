/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.configuration.FontStyleInfo;

/**
 * DTO object for ScaleBarComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.ScaleBarComponent
 *
 */
public class ScaleBarComponentInfo extends PrintComponentInfo implements Serializable {

	/**
	 * The unit (meter, mile, degree)
	 */
	private String unit = "units";

	/**
	 * The number of tics for the scalebar
	 */
	private int ticNumber;

	/**
	 * The label font
	 */
	private FontStyleInfo font;

	public ScaleBarComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.LEFT);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.BOTTOM);
		getLayoutConstraint().setMarginX(20);
		getLayoutConstraint().setMarginY(20);
		getLayoutConstraint().setWidth(200);
		font = new FontStyleInfo();
		font.setFamily("Dialog");
		font.setStyle("Plain");
		font.setSize(10);
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getTicNumber() {
		return ticNumber;
	}

	public void setTicNumber(int ticNumber) {
		this.ticNumber = ticNumber;
	}

	public FontStyleInfo getFont() {
		return font;
	}

	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

}
