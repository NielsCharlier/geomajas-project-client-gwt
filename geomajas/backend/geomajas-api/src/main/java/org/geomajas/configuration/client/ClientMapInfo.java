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
package org.geomajas.configuration.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Bbox;

/**
 * Map configuration.
 * 
 * @author Joachim Van der Auwera
 */
public class ClientMapInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	private String id;

	private String backgroundColor;

	private FeatureStyleInfo lineSelectStyle;

	private FeatureStyleInfo pointSelectStyle;

	private FeatureStyleInfo polygonSelectStyle;

	@NotNull
	private String crs;

	private int precision = 2;

	private boolean scaleBarEnabled;

	private boolean panButtonsEnabled;

	private UnitType displayUnitType = UnitType.METRIC;

	@NotNull
	private float maximumScale;

	@NotNull
	private Bbox initialBounds;

	private List<Double> resolutions;

	private boolean resolutionsRelative;

	private List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();

	private ClientLayerTreeInfo layerTree;

	private double unitLength = 1.0;

	private double pixelLength;

	private ClientToolbarInfo toolbar;

	private Bbox maxBounds = Bbox.ALL;

	/**
	 * Get the id of this map. This id is unique within the application.
	 * 
	 * @return the unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id of this map (auto-set by spring). The id must not be globally unique, but must be unique within the
	 * application.
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the background color of the map, expressed as a hexadecimal string (eg '#FFFFFF').
	 * 
	 * @return the hex string of the background color
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set the background color of this map, expressed as a hexadecimal string (eg '#FFFFFF').
	 * 
	 * @param backgroundColor
	 *            the hex string of the background color
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Get the line selection style for this map.
	 * 
	 * @return the line selection style
	 */
	public FeatureStyleInfo getLineSelectStyle() {
		return lineSelectStyle;
	}

	/**
	 * Set the line selection style for this map.
	 * 
	 * @param lineSelectStyle
	 *            line selection style
	 */
	public void setLineSelectStyle(FeatureStyleInfo lineSelectStyle) {
		this.lineSelectStyle = lineSelectStyle;
	}

	/**
	 * Get the point selection style for this map.
	 * 
	 * @return the point selection style
	 */
	public FeatureStyleInfo getPointSelectStyle() {
		return pointSelectStyle;
	}

	/**
	 * Set the point selection style for this map.
	 * 
	 * @param pointSelectStyle
	 *            point selection style
	 */
	public void setPointSelectStyle(FeatureStyleInfo pointSelectStyle) {
		this.pointSelectStyle = pointSelectStyle;
	}

	/**
	 * Get the polygon selection style for this map.
	 * 
	 * @return the polygon selection style
	 */
	public FeatureStyleInfo getPolygonSelectStyle() {
		return polygonSelectStyle;
	}

	/**
	 * Set the polygon selection style for this map.
	 * 
	 * @param polygonSelectStyle
	 *            polygon selection style
	 */
	public void setPolygonSelectStyle(FeatureStyleInfo polygonSelectStyle) {
		this.polygonSelectStyle = polygonSelectStyle;
	}

	/**
	 * Get the coordinate reference system of this map (SRS notation).
	 * 
	 * @return the CRS (SRS notation)
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference system of this map (SRS notation).
	 * 
	 * @param crs
	 *            the CRS (SRS notation)
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the precision of this map (number of decimals).
	 * 
	 * @return the precision of this map (number of decimals)
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Set the precision of this map (number of decimals).
	 * 
	 * @param precision
	 *            the precision of this map (number of decimals)
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * Should this map show a scalebar ?
	 * 
	 * @return true if shown
	 */
	public boolean isScaleBarEnabled() {
		return scaleBarEnabled;
	}

	/**
	 * Enable the scalebar for this map.
	 * 
	 * @param scaleBarEnabled
	 *            true if enabled (shown)
	 */
	public void setScaleBarEnabled(boolean scaleBarEnabled) {
		this.scaleBarEnabled = scaleBarEnabled;
	}

	/**
	 * Should this map show pan buttons ?
	 * 
	 * @return true if shown
	 */
	public boolean isPanButtonsEnabled() {
		return panButtonsEnabled;
	}

	/**
	 * Enable the pan buttons for this map.
	 * 
	 * @param panButtonsEnabled
	 *            true if enabled (shown)
	 */
	public void setPanButtonsEnabled(boolean panButtonsEnabled) {
		this.panButtonsEnabled = panButtonsEnabled;
	}

	/**
	 * Returns the display unit type of this map. This unit type will be used in eg the scalebar.
	 * 
	 * @return the display unit type
	 */
	public UnitType getDisplayUnitType() {
		return displayUnitType;
	}

	/**
	 * Set the display unit type of this map.
	 * 
	 * @param displayUnitType
	 *            unit type of this map
	 */
	public void setDisplayUnitType(UnitType displayUnitType) {
		this.displayUnitType = displayUnitType;
	}

	/**
	 * Returns the maximum scale (maximum zoom in) of this map. The minimum scale is indirectly determined from the
	 * maximum bounds.
	 * 
	 * @return the maximum scale (pixels/unit)
	 */
	public float getMaximumScale() {
		return maximumScale;
	}

	/**
	 * Set maximum scale (maximum zoom in) of this map.
	 * 
	 * @param maximumScale
	 *            the maximum scale (pixels/unit)
	 */
	public void setMaximumScale(float maximumScale) {
		this.maximumScale = maximumScale;
	}

	/**
	 * Returns the startup bounds/extent of this map. Warning: the map's view will be fitted to the initial bounds, but
	 * the view's aspect ratio will not be affected !
	 * 
	 * @return the initial bounds
	 */
	public Bbox getInitialBounds() {
		return initialBounds;
	}

	/**
	 * Set the startup bounds/extent of this map. Warning: the map's view will be fitted to the initial bounds, but the
	 * view's aspect ratio will not be affected !
	 * 
	 * @param initialBounds
	 */
	public void setInitialBounds(Bbox initialBounds) {
		this.initialBounds = initialBounds;
	}

	/**
	 * Returns the list of resolutions (inverse scale values) allowed by this map. This determines the predefined scale
	 * levels at which this map will be shown. If this list is non-empty, the map will not adjust to arbitrary scale
	 * levels but will instead snap to one of the scale levels defined in this list when zooming.
	 * 
	 * @return a list of resolutions (unit/pixel or pure number if relative)
	 */
	public List<Double> getResolutions() {
		if (null == resolutions) {
			resolutions = new ArrayList<Double>();
		}
		return resolutions;
	}

	/**
	 * Sets the list of resolutions (inverse scale values) allowed by this map. This determines the predefined scale
	 * levels at which this map will be shown. If this list is non-empty, the map will not adjust to arbitrary scale
	 * levels but will instead snap to one of the scale levels defined in this list when zooming.
	 * 
	 * @param resolutions
	 *            a list of resolutions (unit/pixel or pure number if relative)
	 */
	public void setResolutions(List<Double> resolutions) {
		this.resolutions = resolutions;
	}

	/**
	 * Are the resolutions relative ? If true, the resolutions are expressed as pure numbers and denote the ratio of the
	 * map unit and 1 m on the screen (as computed from the screen dpi).
	 * 
	 * @return true if relative
	 */
	public boolean isResolutionsRelative() {
		return resolutionsRelative;
	}

	/**
	 * If true, the resolutions are expressed as pure numbers and denote the ratio of the map unit and 1 m on the screen
	 * (as computed from the screen dpi).
	 * 
	 * @param resolutionsRelative
	 *            true if relative
	 */
	public void setResolutionsRelative(boolean resolutionsRelative) {
		this.resolutionsRelative = resolutionsRelative;
	}

	/**
	 * Get the layer tree that belongs with this map.
	 * 
	 * @return the layer tree
	 */
	public ClientLayerTreeInfo getLayerTree() {
		return layerTree;
	}

	/**
	 * Set layer tree that belongs with this map.
	 * 
	 * @param layerTree
	 *            the layer tree
	 */
	public void setLayerTree(ClientLayerTreeInfo layerTree) {
		this.layerTree = layerTree;
	}

	/**
	 * Get the tool bar that belongs with this map.
	 * 
	 * @return the tool bar
	 */
	public ClientToolbarInfo getToolbar() {
		return toolbar;
	}

	/**
	 * Set tool bar that belongs with this map.
	 * 
	 * @param toolbar
	 *            the layer tree
	 */
	public void setToolbar(ClientToolbarInfo toolbar) {
		this.toolbar = toolbar;
	}

	/**
	 * Get the list of layers of this map. The order is the drawing order.
	 * 
	 * @return the layers
	 */
	public List<ClientLayerInfo> getLayers() {
		return layers;
	}

	/**
	 * Set the list of layers of this map. The order is the drawing order.
	 * 
	 * @param layers
	 *            the layers
	 */
	public void setLayers(List<ClientLayerInfo> layers) {
		this.layers = layers;
	}

	/**
	 * Get the unit length of this map in actual meters. This is an approximate value in the horizontal direction and in
	 * the initial center of the map.
	 * 
	 * @return unit length in m
	 */
	public double getUnitLength() {
		return unitLength;
	}

	/**
	 * Set the unit length of the map (auto-set by Spring).
	 * 
	 * @param unitLength unit length in m
	 */
	public void setUnitLength(double unitLength) {
		this.unitLength = unitLength;
	}

	/**
	 *  Get the length in meters of a pixel on the map.
	 * @return
	 */
	public double getPixelLength() {
		return pixelLength;
	}

	/**
	 *  Set the unit length in meters of a pixel the map (auto-set by Spring).
	 *  
	 * @param pixelLength length of pixel in m
	 */
	public void setPixelLength(double pixelLength) {
		this.pixelLength = pixelLength;
	}

	/**
	 * Returns the maximum bounds/extent of this map.
	 * 
	 * @return the maximum bounds
	 */
	public Bbox getMaxBounds() {
		return maxBounds;
	}

	/**
	 * Sets the maximum bounds/extent of this map.
	 * 
	 * @param maxBounds the maximum bounds
	 */
	public void setMaxBounds(Bbox maxBounds) {
		this.maxBounds = maxBounds;
	}

}
