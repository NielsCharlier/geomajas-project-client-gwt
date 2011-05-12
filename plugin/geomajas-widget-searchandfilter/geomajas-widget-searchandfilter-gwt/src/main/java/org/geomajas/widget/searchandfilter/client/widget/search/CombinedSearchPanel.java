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
package org.geomajas.widget.searchandfilter.client.widget.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SaveRequestEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SaveRequestHandler;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class CombinedSearchPanel extends SearchPanel {

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private static final String ADD_IMAGE = "[SKIN]/actions/add.png";

	private DynamicForm form;
	private RadioGroupItem type;
	private ListGrid searchItems;
	private SelectItem selectSearch;

	public CombinedSearchPanel(final MapWidget mapWidget) {
		super(mapWidget);
		VLayout layout = new VLayout(10);
		layout.setWidth100();
		layout.setHeight100();

		// -- type --
		form = new DynamicForm();
		form.setWidth100();
		form.setAlign(Alignment.LEFT);
		type = new RadioGroupItem();
		type.setTitle(messages.combinedSearchWidgetType());
		type.setValueMap(messages.combinedSearchWidgetOr(), messages.combinedSearchWidgetAnd());
		type.setValue(messages.combinedSearchWidgetAnd());
		type.setVertical(false);
		form.setFields(type);
		layout.addMember(form);

		// -- grid --
		VLayout wrapper = new VLayout(10);
		searchItems = new ListGrid();
		searchItems.setWidth100();
		searchItems.setHeight(1);
		searchItems.setShowAllRecords(true);
		searchItems.setBodyOverflow(Overflow.VISIBLE);
		searchItems.setOverflow(Overflow.VISIBLE);
		searchItems.setLeaveScrollbarGap(false);
		searchItems.setShowHeader(false);
		searchItems.setShowRecordComponents(true);
		searchItems.setShowRecordComponentsByCell(true);
		ListGridField nameField = new ListGridField(SearchListRecord.NAME_FIELD);
		nameField.setType(ListGridFieldType.TEXT);
		ListGridField removeField = new ListGridField(SearchListRecord.REMOVE_FIELD);
		removeField.setType(ListGridFieldType.IMAGE);
		removeField.setShowHover(true);
		removeField.setWidth(24);
		removeField.setHoverCustomizer(new HoverCustomizer() {
			public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
				return messages.combinedSearchWidgetRemove();
			}
		});
		removeField.setWidth(20);
		removeField.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				searchItems.removeData(event.getRecord());
			}
		});
		searchItems.setFields(nameField, removeField);

		HLayout addBar = new HLayout(0);
		addBar.setAlign(Alignment.LEFT);
		addBar.setHeight(20);
		Img addImg = new Img(ADD_IMAGE, 16, 16);
		addImg.setTooltip(messages.combinedSearchWidgetAdd());
		DynamicForm searchForm = new DynamicForm();
		selectSearch = new SelectItem();
		selectSearch.setTitle(addImg.getInnerHTML());
		selectSearch.setWidth(200);
		selectSearch.setTooltip(messages.combinedSearchWidgetSelectSearch());
		selectSearch.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				if (event.getValue() != null) {
					final SearchWidget sw = SearchWidgetRegistry.getSearchWidgetInstance((String) event.getValue());
					sw.showForSave(new SaveRequestHandler() {
						public void onSaveRequested(SaveRequestEvent event) {
							searchItems.addData(new SearchListRecord(sw.getName(), event.getCriterion()));
						}
					});
				}
				event.cancel();
			}
		});
		searchForm.setFields(selectSearch);
		addBar.addMember(searchForm);

		wrapper.setOverflow(Overflow.AUTO);
		wrapper.addMember(searchItems);
		wrapper.addMember(addBar);
		layout.addMember(wrapper);

		// ----------------------------------------------------------

		setWidth(350);
		setHeight(250);
		addChild(layout);
	}

	@Override
	public boolean validate() {
		if (searchItems.getRecords().length > 0) {
			return true;
		} else {
			SC.say(messages.combinedSearchWidgetNoValue());
			return false;
		}
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		List<Criterion> criteria = new ArrayList<Criterion>();
		for (ListGridRecord record : searchItems.getRecords()) {
			criteria.add(((SearchListRecord) record).getCritter());
		}
		if (messages.combinedSearchWidgetAnd().equals(type.getValue())) {
			AndCriterion crit = new AndCriterion();
			crit.setCriteria(criteria);
			return crit;
		} else {
			OrCriterion crit = new OrCriterion();
			crit.setCriteria(criteria);
			return crit;
		}
	}
	
	@Override
	public VectorLayer getFeatureSearchVectorLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		form.reset();
		searchItems.getDataAsRecordList().removeList(searchItems.getRecords());
	}

	/**
	 * Which searches can you use to combine?
	 * <p>
	 * Use this method if you do not use the SearchWidgetRegistry or want to
	 * limit the number of widgets.
	 *
	 * @param searchWidgets
	 */
	public void initializeList(Collection<SearchWidget> searchWidgets) {
		LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
		for (SearchWidget searchWidget : searchWidgets) {
			values.put(searchWidget.getSearchWidgetId(), searchWidget.getName());
		}
		selectSearch.setValueMap(values);
	}

	/**
	 * Which searches can you use to combine?
	 * <p>
	 * Use this method if you use the SearchWidgetRegistry and want to use all
	 * searches in it (do not forget to add them to the registry first.
	 *
	 * @param searchWidgets
	 */
	public void initializeListUseAll() {
		selectSearch.setValueMap(SearchWidgetRegistry.getSearchWidgetMapping());
	}

	@Override
	public void initialize(Criterion featureSearch) {
		List<Criterion> critters;
		if (featureSearch instanceof AndCriterion) {
			critters = ((AndCriterion) featureSearch).getCriteria();
			type.setValue(messages.combinedSearchWidgetAnd());
		} else if (featureSearch instanceof OrCriterion) {
			critters = ((OrCriterion) featureSearch).getCriteria();
			type.setValue(messages.combinedSearchWidgetOr());
		} else {
			return;
		} // not supported

		if (critters != null && critters.size() > 0) {
			for (Criterion cr : critters) {
				searchItems.addData(new SearchListRecord(SearchWidgetRegistry.getI18nTypeName(cr), cr));
			}
		}
	}

	public static boolean canHandle(Criterion criterion) {
		if (criterion == null) {
			return false;
		}
		return (criterion instanceof OrCriterion || criterion instanceof AndCriterion);
	}

	// ----------------------------------------------------------

	/**
	 * Used by Grid.
	 */
	private static class SearchListRecord extends ListGridRecord {

		public static final String NAME_FIELD = "nameField";
		public static final String REMOVE_FIELD = "removeField";

		// private String name;
		private Criterion critter;

		public SearchListRecord(String name, Criterion criterion) {
			setSearchWidget(name, criterion);
		}

		public void setSearchWidget(String name, Criterion criterion) {
			this.critter = criterion;
			setAttribute(NAME_FIELD, name);
			setAttribute(REMOVE_FIELD, "[SKIN]/actions/remove.png");
		}

		public Criterion getCritter() {
			return critter;
		}
	}
}
