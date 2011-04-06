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

package org.geomajas.widget.advancedviews.client.widget;

import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.AbstractLayer;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.advancedviews.client.widget.LayerTreeWithLegend.LayerTreeLegendNode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.EventHandler;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

/**
 * The LayerTree shows a tree resembling the available layers for the map
 * Several actions can be executed on the layers (make them invisible, ...).
 * 
 * TODO This is a copy from LayerTree, should make original properties
 * protected, of add get/setters
 * FIXME The @since and @Api tags are no longer valid on this copy, see the above todo
 * 
 * @author Kristof Heirwegh
 * @author Frank Wynants
 * @author Pieter De Graef
 * @since 1.0.0 (1.6.0)
 */
@Api
public abstract class LayerTreeBase extends Canvas implements LeafClickHandler, FolderClickHandler,
		LayerSelectionHandler {

	protected static final String ICON_BASE = "[ISOMORPHIC]/geomajas/widget/layertree/";
	protected static final String ICON_HIDE = ICON_BASE + "layer-hide.png";
	protected static final String ICON_SHOW = ICON_BASE + "layer-show.png";
	protected static final String ICON_SHOW_LABELED = ICON_BASE + "layer-show-labeled.png";
	protected static final String ICON_SHOW_OUTOFRANGE = ICON_BASE + "layer-show-outofrange.png";
	protected static final String ICON_SHOW_OUTOFRANGE_LABELED = ICON_BASE + "layer-show-outofrange-labeled.png";

	protected static final String IMG_TAGNAME = "IMG";

	protected static final int LAYERTREEBUTTON_SIZE = 22;

	protected static final int DEFAULT_ICONSIZE = 18;

	protected final HTMLFlow htmlSelectedLayer = new HTMLFlow(I18nProvider.getLayerTree().activeLayer(
			I18nProvider.getLayerTree().none()));

	protected LayerTreeTreeNode selectedLayerTreeNode;

	protected TreeGrid treeGrid;

	protected RefreshableTree tree;

	protected MapModel mapModel;

	protected boolean initialized;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Initialize the LayerTree, using a MapWidget as base reference. It will
	 * display the map's layers, as configured in the XML configuration, and
	 * select/deselect the layer as the user clicks on them in the tree.
	 * 
	 * @param mapWidget
	 *            map widget this layer tree is connected to
	 * @since 1.0.0
	 */
	@Api
	public LayerTreeBase(final MapWidget mapWidget) {
		super();
		setHeight100();
		mapModel = mapWidget.getMapModel();
		htmlSelectedLayer.setWidth100();
		treeGrid = createTreeGrid();
		treeGrid.setSelectionType(SelectionStyle.SINGLE);

		// Wait for the MapModel to be loaded
		mapModel.addMapModelHandler(new MapModelHandler() {
			public void onMapModelChange(MapModelEvent event) {
				if (!initialized) {
					initialize();
				}
				initialized = true;
			}
		});
		mapModel.addLayerSelectionHandler(this);
	}

	// -------------------------------------------------------------------------
	// LayerSelectionHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * When a layer deselection event comes in, the LayerTree must also deselect
	 * the correct node in the tree, update the selected layer text, and update
	 * all buttons icons.
	 *
	 * @since 1.0.0
	 */
	@Api
	public void onDeselectLayer(LayerDeselectedEvent event) {
		ListGridRecord selected = treeGrid.getSelectedRecord();
		if (selected != null) {
			treeGrid.deselectRecord(selected);
		}
		selectedLayerTreeNode = null;
		htmlSelectedLayer.setContents(I18nProvider.getLayerTree().activeLayer(I18nProvider.getLayerTree().none()));
	}

	/**
	 * When a layer selection event comes in, the LayerTree must also select the
	 * correct node in the tree, update the selected layer text, and update all
	 * buttons icons.
	 *
	 * @since 1.0.0
	 */
	@Api
	public void onSelectLayer(LayerSelectedEvent event) {
		for (TreeNode node : tree.getAllNodes()) {
			if (node.getName().equals(event.getLayer().getLabel())) {
				selectedLayerTreeNode = (LayerTreeTreeNode) node;
				treeGrid.selectRecord(selectedLayerTreeNode);
				htmlSelectedLayer.setContents(I18nProvider.getLayerTree().activeLayer(
						selectedLayerTreeNode.getLayer().getLabel()));

				// Canvas[] toolStripMembers = toolStrip.getMembers();
				// updateButtonIconsAndStates(toolStripMembers);
			}
		}
	}

	// -------------------------------------------------------------------------
	// LeafClickHandler, FolderClickHandler, CellClickHandler
	// -------------------------------------------------------------------------

	/**
	 * When the user clicks on a folder nothing gets selected.
	 */
	public void onFolderClick(FolderClickEvent event) {
		try {
			Element e = EventHandler.getNativeMouseTarget();
			if (IMG_TAGNAME.equals(e.getTagName())) {
				onIconClick(event.getFolder());
			} else {
				mapModel.selectLayer(null);
			}
		} catch (Exception e) {
			GWT.log(e.getMessage());
			// some other unusable element
		}
	}

	/**
	 * When the user clicks on a leaf the headertext of the treetable is changed
	 * to the selected leaf and the toolbar buttons are updated to represent the
	 * correct state of the buttons.
	 */
	public void onLeafClick(LeafClickEvent event) {
		try {
			Element e = EventHandler.getNativeMouseTarget();
			if (IMG_TAGNAME.equals(e.getTagName())) {
				onIconClick(event.getLeaf());
			} else {
				LayerTreeTreeNode layerTreeNode = (LayerTreeTreeNode) event.getLeaf();
				if (null != selectedLayerTreeNode
						&& layerTreeNode.getLayer().getId().equals(selectedLayerTreeNode.getLayer().getId())) {
					mapModel.selectLayer(null);
				} else {
					mapModel.selectLayer(layerTreeNode.getLayer());
				}
			}
		} catch (Exception e) {
			GWT.log(e.getMessage());
			// some other unusable element
		}
	}

	protected void onIconClick(TreeNode node) {
		if (node != null && node instanceof LayerTreeTreeNode) {
			LayerTreeTreeNode n = (LayerTreeTreeNode) node;
			n.layer.setVisible(!n.layer.isVisible());
			n.updateIcon();
		}
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	/**
	 * Get the currently selected tree node.
	 *
	 * @return selected node
	 */
	public LayerTreeTreeNode getSelectedLayerTreeNode() {
		return selectedLayerTreeNode;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected void initialize() {
		buildTree();
		VLayout vLayout = new VLayout();
		vLayout.setSize("100%", "100%");
		htmlSelectedLayer.setBackgroundColor("#cccccc");
		htmlSelectedLayer.setAlign(Alignment.CENTER);
		vLayout.addMember(htmlSelectedLayer);
		vLayout.addMember(treeGrid);
		treeGrid.markForRedraw();
		LayerTreeBase.this.addChild(vLayout);
		LayerTreeBase.this.markForRedraw();
	}

	/**
	 * Builds up the tree showing the layers.
	 */
	protected void buildTree() {
		treeGrid.setWidth100();
		treeGrid.setHeight100();
		treeGrid.setShowHeader(false);
		treeGrid.setOverflow(Overflow.AUTO);
		tree = new RefreshableTree();
		final TreeNode nodeRoot = new TreeNode("ROOT");
		tree.setRoot(nodeRoot); // invisible ROOT node (ROOT node is required)

		ClientLayerTreeInfo layerTreeInfo = mapModel.getMapInfo().getLayerTree();
		if (layerTreeInfo != null) {
			ClientLayerTreeNodeInfo treeNode = layerTreeInfo.getTreeNode();
			processNode(treeNode, nodeRoot, false);
		}

		treeGrid.setData(tree);
		treeGrid.addLeafClickHandler(this);
		treeGrid.addFolderClickHandler(this);
		tree.openFolder(nodeRoot);
		syncNodeState(false);
	}

	/**
	 * Processes a treeNode (add it to the TreeGrid)
	 * 
	 * @param treeNode
	 *            The treeNode to process
	 * @param nodeRoot
	 *            The root node to which the treeNode has te be added
	 * @param tree
	 *            The tree to which the node has to be added
	 * @param mapModel
	 *            map model
	 * @param refresh
	 *            True if the tree is refreshed (causing it to keep its expanded
	 *            state)
	 */
	protected abstract void processNode(final ClientLayerTreeNodeInfo treeNode, final TreeNode nodeRoot,
			final boolean refresh);

	protected abstract void syncNodeState(boolean layersOnly);

	/**
	 * Creation of treegrid is decoupled to allow you to make a custom treegrid
	 * (SmartGWT uses some designpatterns which only give you the ability to
	 * customize certain aspects through subclassing)
	 *
	 * @return
	 */
	protected TreeGrid createTreeGrid() {
		return new TreeGrid();
	}

	/**
	 * A SmartGWT Tree with one extra method 'refresh'. This is needed to update
	 * icons on the fly in a tree
	 *
	 * @author Frank Wynants
	 */
	protected class RefreshableTree extends Tree {
		/**
		 * Refreshes the icons in the tree, this is done by closing and
		 * reopening all nodes A dirty solution but no other option was found at
		 * the time
		 */
		public void refreshIcons() {
			GWT.log("Refresh node(icon)s");

			// TODO this doesn't work, always returns all folders ???
			TreeNode[] openNodes = this.getOpenList(this.getRoot());

			this.closeAll();
			syncNodeState(true);

			// exclude layers, which are handled by syncNodeState()
			for (TreeNode openNode : openNodes) {
				if (!(openNode instanceof LayerTreeLegendNode) && !(openNode instanceof LayerTreeLegendNode)) {
					this.openFolder(openNode);
				}
			}
		}
	}

	/**
	 * A node inside the LayerTree.
	 *
	 * @author Frank Wynants
	 * @author Pieter De Graef
	 */
	public class LayerTreeTreeNode extends TreeNode {

		protected RefreshableTree tree;

		protected AbstractLayer<?> layer;

		/**
		 * Constructor creates a TreeNode with layer.getLabel as label.
		 *
		 * @param tree
		 *            tree for node
		 * @param layer
		 *            The layer object
		 */
		public LayerTreeTreeNode(RefreshableTree tree, Layer<?> layer) {
			super(layer.getLabel());
			this.layer = (AbstractLayer<?>) layer;
			this.tree = tree;
			updateIcon(false);
		}

		public void updateIcon() {
			updateIcon(true);
		}

		/**
		 * Causes the node to check its status (visible, showing labels, ...)
		 * and to update its icon to match its status.
		 */
		public void updateIcon(boolean refresh) {
			if (layer.isVisible()) {
				if (layer.isShowing()) {
					if (layer.isLabeled()) {
						setIcon(ICON_SHOW_LABELED);
					} else {
						setIcon(ICON_SHOW);
					}
				} else {
					if (layer.isLabeled()) {
						setIcon(ICON_SHOW_OUTOFRANGE_LABELED);
					} else {
						setIcon(ICON_SHOW_OUTOFRANGE);
					}
				}
			} else {
				setIcon(ICON_HIDE);
			}
			if (refresh) {
				tree.refreshIcons();
			}
		}

		public AbstractLayer<?> getLayer() {
			return layer;
		}
	}
}