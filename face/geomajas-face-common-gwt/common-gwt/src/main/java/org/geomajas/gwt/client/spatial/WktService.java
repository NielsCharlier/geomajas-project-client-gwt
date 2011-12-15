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

package org.geomajas.gwt.client.spatial;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * General service for Well-Known-Text to and from geometry conversion.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class WktService {

	private static final String ERR_MSG = "Error while parsing WKT: ";

	private WktService() {
		// Final class should have a private no-argument constructor.
	}

	/**
	 * Parses a given WKT string into a geometry object.
	 * 
	 * @param wkt
	 *            The Well Known Text to parse.
	 * @return Returns The resulting geometry from parsing the WKT.
	 * @throws WktException
	 *             In case something went wrong while parsing. The WKT format must be respected.
	 */
	public static Geometry toGeometry(String wkt) throws WktException {
		if (wkt != null) {
			String type = typeWktToGeom(wkt.substring(0, wkt.indexOf(" ")).trim());
			if (type == null) {
				throw new WktException(ERR_MSG + "type of geometry (" + type + ") not supported");
			}
			if (wkt.indexOf("EMPTY") >= 0) {
				return new Geometry(type, 0, 0);
			}
			Geometry geometry = new Geometry(type, 0, 0);
			String result = format(wkt.substring(wkt.indexOf("(")), geometry);
			if (result.length() != 0) {
				throw new WktException(ERR_MSG + "unexpected ending \"" + result + "\"");
			}
			return geometry;
		}
		throw new WktException(ERR_MSG + "illegal argument; no WKT");
	}

	private static String format(String wkt, Geometry geometry) throws WktException {
		String childType = getChildType(geometry.getGeometryType());
		String scopeWkt = wkt.substring(1);
		if (scopeWkt.startsWith("(")) {
			// Type has sub-geometries, so there must be a child type:
			if (childType == null) {
				throw new WktException(ERR_MSG + "no child geometry type could be found for type "
						+ geometry.getGeometryType());
			}

			List<Geometry> geometries = new ArrayList<Geometry>();
			while (scopeWkt.startsWith("(")) {
				Geometry childGeometry = new Geometry(childType, 0, 0);
				geometries.add(childGeometry);
				scopeWkt = format(scopeWkt, childGeometry);
				if (scopeWkt.startsWith(",")) {
					scopeWkt = scopeWkt.substring(1).trim();
				}
			}
			geometry.setGeometries(geometries.toArray(new Geometry[geometries.size()]));
			scopeWkt = scopeWkt.substring(1);
		} else {
			// Type has no sub-geometries, but should contain coordinates directly:
			if (childType != null) {
				// No Polygons, MultiPoints, MultiLineStrings or MultiPolygons:
				throw new WktException(ERR_MSG + "Geometry of type \"" + geometry.getGeometryType()
						+ "\" has no direct coordinates.");
			}
			// Format coordinates:
			String[] coordStrings = scopeWkt.substring(0, scopeWkt.indexOf(")")).split(",");
			if (Geometry.POINT.equals(geometry.getGeometryType()) && coordStrings.length > 1) {
				throw new WktException(ERR_MSG + "a point can have only one coordinate");
			}

			Coordinate[] coordinates = new Coordinate[coordStrings.length];
			for (int i = 0; i < coordStrings.length; i++) {
				String token = coordStrings[i].trim();
				String[] values = token.split(" ");
				if (values.length != 2) {
					throw new WktException(ERR_MSG + "only 2D coordinates are supported");
				}
				try {
					coordinates[i] = new Coordinate(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
				} catch (Exception e) {
					throw new WktException(ERR_MSG + "could not parse X and Y values (" + token + ")");
				}
			}
			geometry.setCoordinates(coordinates);
			scopeWkt = scopeWkt.substring(scopeWkt.indexOf(")") + 1);
		}
		return scopeWkt;
	}

	private static String typeWktToGeom(String wktType) {
		if (Geometry.POINT.toUpperCase().equals(wktType)) {
			return Geometry.POINT;
		} else if (Geometry.LINE_STRING.toUpperCase().equals(wktType)) {
			return Geometry.LINE_STRING;
		} else if (Geometry.POLYGON.toUpperCase().equals(wktType)) {
			return Geometry.POLYGON;
		} else if (Geometry.MULTI_POINT.toUpperCase().equals(wktType)) {
			return Geometry.MULTI_POINT;
		} else if (Geometry.MULTI_LINE_STRING.toUpperCase().equals(wktType)) {
			return Geometry.MULTI_LINE_STRING;
		} else if (Geometry.MULTI_POLYGON.toUpperCase().equals(wktType)) {
			return Geometry.MULTI_POLYGON;
		}
		return null;
	}

	private static String getChildType(String parentType) {
		if (Geometry.POINT.equals(parentType) || Geometry.LINE_STRING.equals(parentType)
				|| Geometry.LINEAR_RING.equals(parentType)) {
			return null;
		} else if (Geometry.POLYGON.equals(parentType)) {
			return Geometry.LINEAR_RING;
		} else if (Geometry.MULTI_POINT.equals(parentType)) {
			return Geometry.POINT;
		} else if (Geometry.MULTI_LINE_STRING.equals(parentType)) {
			return Geometry.LINE_STRING;
		} else if (Geometry.MULTI_POLYGON.equals(parentType)) {
			return Geometry.POLYGON;
		}
		return null;
	}

	/**
	 * Format a given geometry to Well Known Text.
	 * 
	 * @param geometry
	 *            The geometry to format.
	 * @return Returns the WKT string.
	 * @throws WktException
	 *             In case something went wrong while formatting.
	 */
	public static String toWkt(Geometry geometry) throws WktException {
		if (Geometry.POINT.equals(geometry.getGeometryType())) {
			return toWktPoint(geometry);
		} else if (Geometry.LINE_STRING.equals(geometry.getGeometryType())
				|| Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
			return toWktLineString(geometry);
		} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
			return toWktPolygon(geometry);
		} else if (Geometry.MULTI_POINT.equals(geometry.getGeometryType())) {
			return toWktMultiPoint(geometry);
		} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
			return toWktMultiLineString(geometry);
		} else if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
			return toWktMultiPolygon(geometry);
		}
		return "";
	}

	// ------------------------------------------------------------------------
	// Private methods for WKT formatting:
	// ------------------------------------------------------------------------

	private static boolean isEmpty(Geometry geometry) {
		return (geometry.getCoordinates() == null || geometry.getCoordinates().length == 0)
				&& (geometry.getGeometries() == null || geometry.getGeometries().length == 0);
	}

	private static String toWktPoint(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "POINT EMPTY";
		}
		return "POINT (" + geometry.getCoordinates()[0].getX() + " " + geometry.getCoordinates()[0].getY() + ")";
	}

	private static String toWktLineString(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "LINESTRING EMPTY";
		}
		String wkt = "LINESTRING (";
		for (int i = 0; i < geometry.getCoordinates().length; i++) {
			if (i > 0) {
				wkt += ", ";
			}
			wkt += geometry.getCoordinates()[i].getX() + " " + geometry.getCoordinates()[i].getY();
		}
		return wkt + ")";
	}

	private static String toWktPolygon(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "POLYGON EMPTY";
		}
		String wkt = "POLYGON (";
		if (geometry.getGeometries() != null) {
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				String ringWkt = toWktLineString(geometry.getGeometries()[i]);
				if (i > 0) {
					wkt += ",";
				}
				wkt += ringWkt.substring(ringWkt.indexOf("("));
			}
		}
		return wkt + ")";
	}

	private static String toWktMultiPoint(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "MULTIPOINT EMPTY";
		}
		String wkt = "MULTIPOINT (";
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			String pointWkt = toWktPoint(geometry.getGeometries()[i]);
			if (i > 0) {
				wkt += ",";
			}
			wkt += pointWkt.substring(pointWkt.indexOf("("));
		}
		return wkt + ")";
	}

	private static String toWktMultiLineString(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "MULTILINESTRING EMPTY";
		}
		String wkt = "MULTILINESTRING (";
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			String lineWkt = toWktLineString(geometry.getGeometries()[i]);
			if (i > 0) {
				wkt += ",";
			}
			wkt += lineWkt.substring(lineWkt.indexOf("("));
		}
		return wkt + ")";
	}

	private static String toWktMultiPolygon(Geometry geometry) {
		if (isEmpty(geometry)) {
			return "MULTIPOLYGON EMPTY";
		}
		String wkt = "MULTIPOLYGON (";
		for (int i = 0; i < geometry.getGeometries().length; i++) {
			String polygonWkt = toWktPolygon(geometry.getGeometries()[i]);
			if (i > 0) {
				wkt += ",";
			}
			wkt += polygonWkt.substring(polygonWkt.indexOf("("));
		}
		return wkt + ")";
	}
}