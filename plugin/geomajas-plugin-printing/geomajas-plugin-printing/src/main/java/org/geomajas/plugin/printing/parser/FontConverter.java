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
package org.geomajas.plugin.printing.parser;

import java.awt.Font;
import java.util.StringTokenizer;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Adapter for converting a font to XML and back.
 * 
 * @author Jan De Moerloose
 */
public class FontConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(Font.class);
	}

	@Override
	public Object fromString(String str) {
		StringTokenizer st = new StringTokenizer(str, ",");
		if (st.countTokens() < 2) {
			throw new IllegalArgumentException("Not enough tokens (<3) in font " + str);
		}
		int count = st.countTokens();

		String name = st.nextToken();
		int styleIndex = Font.PLAIN;
		if (count > 2) {
			String style = st.nextToken();
			if (style.equalsIgnoreCase("bold")) {
				styleIndex = Font.BOLD;
			} else if (style.equalsIgnoreCase("italic")) {
				styleIndex = Font.ITALIC;
			}
		}
		int size = Integer.parseInt(st.nextToken());
		return new Font(name, styleIndex, size);
	}

	@Override
	public String toString(Object obj) {
		Font font = (Font) obj;
		if (obj == null) {
			return null;
		}
		String style = null;
		if (font.getStyle() == Font.BOLD) {
			style = "bold";
		} else if (font.getStyle() == Font.ITALIC) {
			style = "italic";
		}
		return font.getName() + (style == null ? "" : "," + style) + "," + font.getSize();
	}

}