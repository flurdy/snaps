/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flurdy.grid.fotogator.wicket.holiday;

import com.flurdy.grid.fotogator.domain.HolidayGroup;
import com.flurdy.grid.fotogator.wicket.*;
import com.flurdy.grid.fotogator.wicket.html.MenuLink;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class HolidayGroupPage extends GridPage {

	private static final String CREATE_GROUP_FORM_ID = "createGroupForm";
	private static final String EDIT_GROUP_FORM_ID = "editGroupForm";
	private static final String REMOVE_GROUP_FORM_ID = "removeGroupForm";
	private static final String GROUP_NAME_ID = "groupName";
	private static final String FRAGMENT_PLACEHOLDER_ID = "groupFragment";

	public class CreateHolidayGroupFragment extends Fragment {
		private static final String FRAGMENT_ID = "createGroupFragment";
		public CreateHolidayGroupFragment() {
			super(FRAGMENT_PLACEHOLDER_ID, FRAGMENT_ID, HolidayGroupPage.this);
			IModel<HolidayGroup> groupModel = new CompoundPropertyModel<HolidayGroup>(new HolidayGroup());
			Form<HolidayGroup> createGroupForm = new Form<HolidayGroup>(CREATE_GROUP_FORM_ID, groupModel) {
				@Override
				protected void onSubmit() {
					HolidayGroup holidayGroup = getModelObject();
					info("creating " + holidayGroup);
					addLayoutForViewHoliday(holidayGroup);
				}
			};
			add(createGroupForm);
			createGroupForm.add(new RequiredTextField<String>(GROUP_NAME_ID));
		}
	}

	public class DeletedHolidayGroupFragment extends Fragment {
		private static final String FRAGMENT_ID = "deletedGroupFragment";
		public DeletedHolidayGroupFragment() {
			super(FRAGMENT_PLACEHOLDER_ID, FRAGMENT_ID, HolidayGroupPage.this);
		}
	}

	public class EditHolidayGroupFragment extends Fragment {
		private static final String FRAGMENT_ID = "editGroupFragment";
		public EditHolidayGroupFragment(HolidayGroup holidayGroup) {
			super(FRAGMENT_PLACEHOLDER_ID, FRAGMENT_ID, HolidayGroupPage.this);
			IModel<HolidayGroup> groupModel = new CompoundPropertyModel<HolidayGroup>(holidayGroup);
			Form<HolidayGroup> groupForm = new Form<HolidayGroup>(EDIT_GROUP_FORM_ID, groupModel) {
				@Override
				protected void onSubmit() {
					HolidayGroup holidayGroup = getModelObject();
					info("updating " + holidayGroup);
//					this.setVisible(false);
					addLayoutForViewHoliday(holidayGroup);
				}
			};
			add(groupForm);
			groupForm.add(new RequiredTextField<String>(GROUP_NAME_ID));
		}
	}
	
	public class RemoveHolidayGroupFragment extends Fragment {
		private static final String FRAGMENT_ID = "removeGroupFragment";
		public RemoveHolidayGroupFragment(HolidayGroup holidayGroup) {
			super(FRAGMENT_PLACEHOLDER_ID, FRAGMENT_ID, HolidayGroupPage.this);
			IModel<HolidayGroup> groupModel = new CompoundPropertyModel<HolidayGroup>(holidayGroup);
			Form<HolidayGroup> groupForm = new Form<HolidayGroup>(REMOVE_GROUP_FORM_ID, groupModel) {
				@Override
				protected void onSubmit() {
					HolidayGroup holidayGroup = getModelObject();
					info("deleting " + holidayGroup);
					addLayoutForDeletedHoliday();
				}
			};
			add(groupForm);
			groupForm.add(new Label(GROUP_NAME_ID));
		}
	}
	
	public class ViewHolidayGroupFragment extends Fragment {
		private static final String FRAGMENT_ID = "viewGroupFragment";
		private static final String GROUP_NAME_ID = "groupName";
		public ViewHolidayGroupFragment(HolidayGroup holidayGroup) {
			super(FRAGMENT_PLACEHOLDER_ID, FRAGMENT_ID, HolidayGroupPage.this);
			IModel<HolidayGroup> groupModel = new CompoundPropertyModel<HolidayGroup>(holidayGroup);
			setDefaultModel(groupModel);
			add( new Label(GROUP_NAME_ID));
		}
	}

	public HolidayGroupPage() {
		super();
		addLayoutForCreateHoliday();
	}

	private void addLayoutForCreateHoliday() {
		addJibTitle("Create Holiday Group");

		Fragment groupFragment = new CreateHolidayGroupFragment();
		addOrReplace( groupFragment );
	}

	private void addLayoutForEditHoliday(final HolidayGroup holidayGroup) {
		log.info("Editing " + holidayGroup);
		info("Editing " + holidayGroup);
		addJibTitle("Edit Holiday Group");

//		remove(FRAGMENT_PLACEHOLDER_ID);

		Fragment groupFragment = new EditHolidayGroupFragment(holidayGroup);
		replace( groupFragment );


		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add( new MenuLink("View") {
				@Override
				public void onClick() {
					log.info("Clicked on view:" + holidayGroup);
					addLayoutForViewHoliday( holidayGroup );
				}
			}
		);
		hatchLinks.add( new MenuLink("remove") {
				@Override
				public void onClick() {
					log.info("Clicked on remove:" + holidayGroup);
					addLayoutForRemoveHoliday( holidayGroup );
				}
			}
		);
		addHatchMenu(hatchLinks);

	}

	private void addLayoutForRemoveHoliday(final HolidayGroup holidayGroup) {
		log.info("Removing " + holidayGroup);
		info("Removing " + holidayGroup);
		addJibTitle("Remove Holiday Group?");

//		remove(FRAGMENT_PLACEHOLDER_ID);

		Fragment groupFragment = new RemoveHolidayGroupFragment(holidayGroup);
		replace( groupFragment );

		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add( new MenuLink("View") {
				@Override
				public void onClick() {
					log.info("Clicked on view:" + holidayGroup);
					addLayoutForViewHoliday( holidayGroup );
				}
			}
		);
		addHatchMenu(hatchLinks);

	}

	private void addLayoutForViewHoliday(final HolidayGroup holidayGroup) {
		log.info("Viewing " + holidayGroup);
		info("Viewing " + holidayGroup);
		addJibTitle("Holiday Group");
//		remove(FRAGMENT_PLACEHOLDER_ID);
		Fragment groupFragment = new ViewHolidayGroupFragment(holidayGroup);
		replace( groupFragment );


		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add( new MenuLink("edit") {
				@Override
				public void onClick() {
					log.info("Clicked on edit:" + holidayGroup);
					addLayoutForEditHoliday( holidayGroup );
				}
			}
		);
		addHatchMenu(hatchLinks);

	}

	private void addLayoutForDeletedHoliday() {
		log.info("Viewing no group" );
		info("Viewing no group");
		addJibTitle("Holiday Group deleted");
//		remove(FRAGMENT_PLACEHOLDER_ID);
		Fragment groupFragment = new DeletedHolidayGroupFragment();
		replace( groupFragment );

		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add( new MenuLink("create") {
				@Override
				public void onClick() {
					log.info("Clicked on create:" );
//					remove(FRAGMENT_PLACEHOLDER_ID);
					addLayoutForCreateHoliday( );
				}
			}
		);
		addHatchMenu(hatchLinks);

	}

}

