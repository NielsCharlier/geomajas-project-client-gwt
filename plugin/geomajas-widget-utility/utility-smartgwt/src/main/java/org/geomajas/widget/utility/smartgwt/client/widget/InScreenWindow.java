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

package org.geomajas.widget.utility.smartgwt.client.widget;

import com.smartgwt.client.widgets.Window;
import org.geomajas.gwt.client.util.WidgetLayout;

/**
 * {@link Window} variant which tries to assure that the window remains within screen bounds.
 *
 * @author Joachim Van der Auwera
 */
public class InScreenWindow extends Window {

	@Override
	public void onDraw() {
		// try to force to be inside the screen
		WidgetLayout.keepWindowInScreen(this);
		super.onDraw();
	}

}