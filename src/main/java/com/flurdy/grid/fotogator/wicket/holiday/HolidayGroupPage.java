/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.fotogator.wicket.holiday;

import com.flurdy.grid.fotogator.domain.HolidayGroup;
import com.flurdy.grid.fotogator.wicket.*;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class HolidayGroupPage extends GridPage {

	private static final String CREATE_GROUP_FORM_ID = "createGroupForm";
	private static final String GROUP_NAME_ID = "groupName";

	public HolidayGroupPage() {
		super();
		addLayoutForCreateHoliday();
	}

	private void addLayoutForCreateHoliday() {
		addJibTitle("Create Holiday Group");
		IModel<HolidayGroup> groupModel = new CompoundPropertyModel<HolidayGroup>(new HolidayGroup());
		Form<HolidayGroup> createGroupForm = new Form<HolidayGroup>(CREATE_GROUP_FORM_ID,groupModel){
			@Override
			protected void onSubmit() {
				HolidayGroup holidayGroup = getModelObject();
				info("creating "+holidayGroup);

			}
		};
		add( createGroupForm );
		createGroupForm.add(new RequiredTextField<String>(GROUP_NAME_ID));
	}

	


}
