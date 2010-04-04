package com.flurdy.grid.fotogator.wicket;

import com.flurdy.grid.fotogator.wicket.holiday.HolidayGroupPage;
import com.flurdy.grid.fotogator.wicket.holiday.HolidayGroupModel;
import com.flurdy.grid.fotogator.domain.HolidayGroup;
import java.util.Set;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Homepage
 */
public class HomePage extends GridPage {

	private static final long serialVersionUID = 1L;
	private static final String FIND_GROUP_FORM_ID = "findGroupForm";
	private static final String NEW_GROUP_FORM_ID = "newGroupForm";
	private static final String FIND_GROUP_FORM_GROUP_ID = "groupName";

	public HomePage() {
		super();
		addLayout();
	}

	public HomePage(final PageParameters parameters) {
		super(parameters);
		addLayout();
	}

	private void addLayout() {

		IModel<HolidayGroup> groupModel = new CompoundPropertyModel<HolidayGroup>(new HolidayGroup());

		Form<HolidayGroup> findGroupForm = new StatelessForm<HolidayGroup>(FIND_GROUP_FORM_ID, groupModel) {

			@Override
			protected void onSubmit() {
				HolidayGroup holidayGroup = getModelObject();
				log.info( "Looking for groups with [" + holidayGroup.getGroupName() + "]");
				Set<HolidayGroup> existingHolidayGroups = holidayGroupService.searchForHolidayGroups(holidayGroup.getGroupName());
				if( existingHolidayGroups != null && existingHolidayGroups.size() > 0 ){
					if( existingHolidayGroups.size() == 1 ){
						HolidayGroupModel holidayGroupModel  = new HolidayGroupModel(existingHolidayGroups.iterator().next());
						setResponsePage(new HolidayGroupPage( holidayGroupModel ));
					} else {
						log.info("Several holiday groups matched.");

						setResponsePage(new HolidayGroupPage( existingHolidayGroups ));

					}
				} else {
					warn("Holiday group could not be found");
				}
			}
		};
		add(findGroupForm);
		findGroupForm.add(new TextField<String>(FIND_GROUP_FORM_GROUP_ID));

		Form<Void> newGroupForm = new StatelessForm<Void>(NEW_GROUP_FORM_ID) {

			@Override
			protected void onSubmit() {
				setResponsePage(new HolidayGroupPage());
			}
		};
		add(newGroupForm);

	}

}
