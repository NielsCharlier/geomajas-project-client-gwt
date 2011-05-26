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

package org.geomajas.plugin.printing.component.impl;

import org.geomajas.global.Api;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;

/**
 * Basic container component for printing. Handles the size calculation, layout and rendering of its children.
 * Variant of {@link PrintComponentImpl} which uses better class name.
 *
 * @author Jan De Moerloose
 * @since 2.1.0
 *
 * @param <T> DTO object class
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class AbstractPrintComponent<T extends PrintComponentInfo> extends PrintComponentImpl<T> {

	public AbstractPrintComponent() {
		super();
	}

	public AbstractPrintComponent(String id) {
		super(id);
	}

	public AbstractPrintComponent(String id, LayoutConstraint constraint) {
		super(id, constraint);
	}

}
