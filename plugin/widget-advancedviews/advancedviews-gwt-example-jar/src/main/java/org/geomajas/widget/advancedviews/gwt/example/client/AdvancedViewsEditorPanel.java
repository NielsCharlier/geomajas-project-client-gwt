/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.advancedviews.gwt.example.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.widget.advancedviews.client.widget.ExpandingThemeWidget;
import org.geomajas.widget.advancedviews.client.widget.ThemeWidget;
import org.geomajas.widget.advancedviews.configuration.client.ThemesInfo;
import org.geomajas.widget.advancedviews.editor.client.ThemeConfigurationEditor;
import org.geomajas.widget.advancedviews.gwt.example.client.i18n.ApplicationMessages;

/**
 * Sample to demonstrate use of the advancedviews plug-in.
 *
 * @author Oliver May
 */
public class AdvancedViewsEditorPanel extends SamplePanel {

	public static final ApplicationMessages MESSAGES = GWT.create(ApplicationMessages.class);

	private ThemeWidget themes;

	private ExpandingThemeWidget exthemes;

	private ThemeConfigurationEditor editor;

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {
		public SamplePanel createPanel() {
			return new AdvancedViewsEditorPanel();
		}
	};

	public AdvancedViewsEditorPanel() {

	}


	public static final String TITLE = "Advancedviews plug-in editor";

	public Canvas getViewPanel() {
		VLayout vlayout = new VLayout();
		vlayout.setWidth100();
		vlayout.setHeight100();



		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(5);
		layout.setMargin(5);
		layout.setShowEdges(true);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget map = new MapWidget("mapMain", "advancedViewsApp");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		map.getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {
			@Override
			public void onMapModelChanged(MapModelChangedEvent event) {
				editor.setBaseGeodesk(createGeodeskDto(event.getMapModel()));
				editor.setWidgetConfiguration(event.getMapModel().getMapInfo());
				editor.setDisabled(false);
			}
		});

		layout.addMember(map);

		// ---------------------------------------------------------------------
		// Theme
		// ---------------------------------------------------------------------
		themes = new ThemeWidget(map);
		themes.setParentElement(map);
		themes.setSnapTo("BL");
		themes.setSnapOffsetTop(-50);
		themes.setSnapOffsetLeft(10);
		themes.setWidth(150);

		exthemes = new ExpandingThemeWidget(map);
		exthemes.setParentElement(map);
		exthemes.setSnapTo("BR");
		exthemes.setSnapOffsetTop(-50);
		exthemes.setSnapOffsetLeft(-20);
		exthemes.setWidth(160);


		HLayout editorLayout = new HLayout();

		editor = new ThemeConfigurationEditor();
		editor.setDisabled(true);

		editorLayout.addMember(editor.getCanvas());

		vlayout.addMember(layout);
		vlayout.addMember(editorLayout);

		return vlayout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.applicationTitle("");
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[]{
				"classpath:org/geomajas/widget/advancedviews/gwt/example/context/applicationContext.xml",
				"classpath:org/geomajas/widget/advancedviews/gwt/example/context/mapMain.xml",
				"classpath:org/geomajas/widget/advancedviews/gwt/example/context/themesInfo.xml"};
	}

	private BaseGeodeskDto createGeodeskDto(MapModel mapModel) {
		BlueprintDto blueprintDto = new BlueprintDto();
		UserApplicationInfo uai = new UserApplicationInfo();
		ClientApplicationInfo cai = new ClientApplicationInfo();
		cai.getMaps().add(mapModel.getMapInfo());
		blueprintDto.setUserApplicationInfo(uai);

		return blueprintDto;
	}
}
