/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.loketten;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * 
 * @author Oliver May
 *
 */
public class Geodesks extends VLayout implements EditSessionHandler {

	private IButton buttonNew;

	private GeodeskGrid grid;

	private GeodeskDetail detail;

	private static final int MARGIN = 20;

	public Geodesks() {
		super(MARGIN);

		detail = new GeodeskDetail();

		VLayout topContainer = new VLayout(5);
		topContainer.setShowResizeBar(true);

		// topContainer.setMinHeight(150);
		topContainer.setHeight("*");
		topContainer.setLayoutBottomMargin(5);

		buttonNew = new IButton("Nieuw Geodesk");
		buttonNew.setIcon(WidgetLayout.iconAdd);
		buttonNew.setAutoFit(true);
		buttonNew.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Window w = new ChooseBlueprintWindow(new DataCallback<String>() {

					public void execute(String result) {
						if (result != null && !"".equals(result)) {
							CommService.createNewGeodesk(result);
						}
					}
				});
				w.show();
			}
		});

		grid = new GeodeskGrid();
		grid.addSelectionChangedHandler(detail);

		topContainer.addMember(buttonNew);
		topContainer.addMember(grid);

		// ----------------------------------------------------------

		detail.setHeight(350);
		detail.setMinHeight(350);
		detail.setLayoutTopMargin(5);

		addMember(topContainer);
		addMember(detail);

		Whiteboard.registerHandler(this);
	}

	public void destroy() {
		Whiteboard.unregisterHandler(this);
		super.destroy();
	}

	// -- EditSessionHandler--------------------------------------------------------

	public void onEditSessionChange(EditSessionEvent ese) {
		if (ese.isParentOfRequestee(detail)) {
			grid.setDisabled(ese.isSessionStart());
			buttonNew.setDisabled(ese.isSessionStart());
			// detail handles event itself so no need to do it here
		}
	}
}