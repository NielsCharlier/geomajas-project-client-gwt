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
package org.geomajas.internal.configuration;

import java.beans.PropertyEditorSupport;

import org.geomajas.configuration.client.ScaleInfo;

/**
 * Custom bean editor for ScaleInfo class. Supports 1 : x and x : y notation.
 * 
 * @author Jan De Moerloose
 * @see ScaleInfo
 * 
 */
public class ScaleInfoEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		// Prepare the string; remove all spaces and all comma's:
		text = text.replaceAll(" ", "");
		text = text.replaceAll(",", "");

		int pos = text.indexOf(':');
		if (pos > 0) {
			try {
				double numerator = Double.parseDouble(text.substring(0, pos));
				double denominator = Double.parseDouble(text.substring(pos + 1));
				double scale = 0;
				if (denominator != 0) {
					scale = numerator / denominator;
				}
				setValue(new ScaleInfo(scale));
			} catch (Exception e) {
				throw new IllegalArgumentException("Resolution " + text
						+ " could not be parsed. The following format was expected:" + " (x : y).");
			}
		} else {
			try {
				// Not recommended....
				setValue(new ScaleInfo(Double.parseDouble(text)));
			} catch (Exception e) {
				throw new IllegalArgumentException("Resolution " + text
						+ " could not be parsed. The following format was expected:" + " (x : y).");
			}
		}
	}

}
