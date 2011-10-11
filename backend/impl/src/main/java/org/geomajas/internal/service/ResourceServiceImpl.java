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
package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link ResourceService}.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class ResourceServiceImpl implements ResourceService {

	private static final String CLASSPATH = "classpath:";

	@Autowired
	private ApplicationContext applicationContext;

	private List<String> rootPaths = new ArrayList<String>();

	public Resource find(String location) throws GeomajasException {
		Resource resource = applicationContext.getResource(location);
		if (resource.exists()) {
			return resource;
		} else {
			String cpResource = null;
			if (location.startsWith("/")) {
				cpResource = CLASSPATH + location.substring(0);
			} else {
				cpResource = CLASSPATH + location;
			}
			resource = applicationContext.getResource(cpResource);
			if (resource.exists()) {
				return resource;
			} else {
				for (String root : rootPaths) {
					if (root.endsWith("/")) {
						resource = applicationContext.getResource(root + location);
					} else {
						resource = applicationContext.getResource(root + "/" + location);
					}
					if (resource.exists()) {
						return resource;
					}
				}
				throw new GeomajasException(ExceptionCode.RESOURCE_NOT_FOUND, location);
			}
		}
	}

	public List<String> getRootPaths() {
		return rootPaths;
	}

	public void setRootPaths(List<String> rootPaths) {
		this.rootPaths = rootPaths;
	}

}