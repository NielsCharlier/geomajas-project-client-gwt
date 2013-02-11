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
package org.geomajas.widget.advancedviews.editor.client;

import org.geomajas.plugin.deskmanager.client.gwt.common.WidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.common.WidgetEditorFactory;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.widget.layer.configuration.client.ClientLayerTreeInfo;

import com.google.gwt.core.client.GWT;


/**
 * EditorFactory for the layertree configuration.
 * 
 * @author Oliver May
 *
 */
public class LayerTreeEditorFactory implements WidgetEditorFactory {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	@Override
	public WidgetEditor createEditor() {
		return new LayerTreeEditor();
	}

	@Override
	public String getKey() {
		return ClientLayerTreeInfo.IDENTIFIER;
	}

	@Override
	public String getName() {
		return MESSAGES.blueprintDetailTabLayerTree();
	}

}