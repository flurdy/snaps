package com.flurdy.grid.fotogator.wicket.holiday;

import com.flurdy.grid.fotogator.domain.HolidayGroup;
import com.flurdy.grid.fotogator.wicket.*;
import com.flurdy.grid.fotogator.wicket.html.MenuLink;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class HolidayGroupPage extends GridPage {

	private static final String CREATE_GROUP_FORM_ID = "createGroupForm";
	private static final String EDIT_GROUP_FORM_ID = "editGroupForm";
	private static final String REMOVE_GROUP_FORM_ID = "removeGroupForm";
	private static final String GROUP_NAME_ID = "groupName";
	private static final String FRAGMENT_PLACEHOLDER_ID = "groupFragment";

	public HolidayGroupPage() {
		super();
		addLayoutForCreateHoliday();
	}

	public HolidayGroupPage(HolidayGroupModel holidayGroupModel) {
		super();
		addLayoutForViewHoliday(holidayGroupModel);
	}

	public HolidayGroupPage(Set<HolidayGroup> existingHolidayGroups) {
		super();
		addLayoutForSelectingHoliday(existingHolidayGroups);
	}

	private void addLayoutForCreateHoliday() {
		addJibTitle("Create Holiday Group");

		Fragment groupFragment = new CreateHolidayGroupFragment();
		addOrReplace(groupFragment);

		addEmptyHatch();
	}

	private void addLayoutForEditHoliday(final HolidayGroupModel holidayGroupModel) {
		final HolidayGroup holidayGroup = holidayGroupModel.getObject();
		log.info("Editing " + holidayGroup);
//		info("Editing " + holidayGroup);
		addJibTitle("Edit Holiday Group");

//		remove(FRAGMENT_PLACEHOLDER_ID);

		Fragment groupFragment = new EditHolidayGroupFragment(holidayGroup);
		replace(groupFragment);


		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add(new MenuLink("View") {

			@Override
			public void onClick() {
				log.info("Clicked on view:" + holidayGroup);
//					HolidayGroupModel holidayGroupModel  = new HolidayGroupModel(holidayGroup);
				addLayoutForViewHoliday(holidayGroupModel);
			}
		});
		hatchLinks.add(new MenuLink("remove") {

			@Override
			public void onClick() {
				log.info("Clicked on remove:" + holidayGroup);
				addLayoutForRemoveHoliday(holidayGroupModel);
			}
		});
		addHatchMenu(hatchLinks);

	}

	private void addLayoutForRemoveHoliday(final HolidayGroupModel holidayGroupModel) {
		final HolidayGroup holidayGroup = holidayGroupModel.getObject();
		log.info("Removing " + holidayGroup);
//		info("Removing " + holidayGroup);
		addJibTitle("Remove Holiday Group?");

//		remove(FRAGMENT_PLACEHOLDER_ID);

		Fragment groupFragment = new RemoveHolidayGroupFragment(holidayGroup);
		replace(groupFragment);

		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add(new MenuLink("View") {

			@Override
			public void onClick() {
				log.info("Clicked on view:" + holidayGroup);
//					HolidayGroupModel holidayGroupModel  = new HolidayGroupModel(holidayGroup);
				addLayoutForViewHoliday(holidayGroupModel);
			}
		});
		addHatchMenu(hatchLinks);

	}

	private void addLayoutForViewHoliday(final HolidayGroupModel holidayGroupModel) {//final HolidayGroup holidayGroup) {
		final HolidayGroup holidayGroup = holidayGroupModel.getObject();
		log.info("Viewing " + holidayGroupModel);
//		info("Viewing " + holidayGroup);
		addJibTitle("Holiday Group");
//		remove(FRAGMENT_PLACEHOLDER_ID);
		Fragment groupFragment = new ViewHolidayGroupFragment(holidayGroup);
		addOrReplace(groupFragment);


		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add(new MenuLink("edit") {

			@Override
			public void onClick() {
				log.info("Clicked on edit:" + holidayGroup);
				addLayoutForEditHoliday(holidayGroupModel);
			}
		});
		addHatchMenu(hatchLinks);

	}

	private void addLayoutForDeletedHoliday() {
		log.info("Viewing no group");
//		info("Viewing no group");
		addJibTitle("Holiday Group deleted");
//		remove(FRAGMENT_PLACEHOLDER_ID);
		Fragment groupFragment = new DeletedHolidayGroupFragment();
		replace(groupFragment);

		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add(new MenuLink("create") {

			@Override
			public void onClick() {
				log.info("Clicked on create:");
				addLayoutForCreateHoliday();
			}
		});
		addHatchMenu(hatchLinks);

	}

	private void addLayoutForSelectingHoliday(Set<HolidayGroup> existingHolidayGroups) {
		log.info("selecting holidays");
		info("selecting holidays");

		addJibTitle("Find Holiday Group");

		Fragment groupFragment = new SelectHolidayGroupFragment(existingHolidayGroups);
		addOrReplace(groupFragment);


		List<MenuLink> hatchLinks = new ArrayList<MenuLink>();
		hatchLinks.add(new MenuLink("create") {

			@Override
			public void onClick() {
				log.info("Clicked on create:");
				addLayoutForCreateHoliday();
			}
		});
		addHatchMenu(hatchLinks);

	}

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

					try {

						holidayGroupService.addHolidayGroup(holidayGroup);

					} catch (Exception ex) {
						log.warn("add holiday failed", ex);
						error(ex);
					}
					HolidayGroupModel holidayGroupModel = new HolidayGroupModel(holidayGroup);
					addLayoutForViewHoliday(holidayGroupModel);
				}
			};
			add(createGroupForm);
			createGroupForm.add(new RequiredTextField<String>(GROUP_NAME_ID));
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
					HolidayGroupModel holidayGroupModel = new HolidayGroupModel(holidayGroup);
					addLayoutForViewHoliday(holidayGroupModel);
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

	public class DeletedHolidayGroupFragment extends Fragment {

		private static final String FRAGMENT_ID = "deletedGroupFragment";

		public DeletedHolidayGroupFragment() {
			super(FRAGMENT_PLACEHOLDER_ID, FRAGMENT_ID, HolidayGroupPage.this);
		}
	}

	public class ViewHolidayGroupFragment extends Fragment {

		private static final String FRAGMENT_ID = "viewGroupFragment";
		private static final String GROUP_NAME_ID = "groupName";

		public ViewHolidayGroupFragment(HolidayGroup holidayGroup) {
			super(FRAGMENT_PLACEHOLDER_ID, FRAGMENT_ID, HolidayGroupPage.this);
			IModel<HolidayGroup> groupModel = new CompoundPropertyModel<HolidayGroup>(holidayGroup);
			setDefaultModel(groupModel);
			add(new Label(GROUP_NAME_ID));
		}
	}

	public class SelectHolidayGroupFragment extends Fragment {

		private static final String FRAGMENT_ID = "selectGroupFragment";
		private static final String ROW_ID = "groupRow";
		private static final String LINK1_ID = "groupLink1";
		private static final String LINK2_ID = "groupLink2";
		private static final String NAME_ID = "groupName";

		public SelectHolidayGroupFragment(Collection<HolidayGroup> holidayGroups) {
			super(FRAGMENT_PLACEHOLDER_ID, FRAGMENT_ID, HolidayGroupPage.this);
			RepeatingView groupRows = new RepeatingView(ROW_ID);
			add(groupRows);
			for (final HolidayGroup holidayGroup : holidayGroups) {
				final HolidayGroupModel holidayGroupModel = new HolidayGroupModel(holidayGroup);
				WebMarkupContainer row = new WebMarkupContainer(groupRows.newChildId());
				groupRows.add(row);
				Link<String> link1 = new Link<String>(LINK1_ID){
					@Override
					public void onClick(){
						addLayoutForViewHoliday(holidayGroupModel);
					}
				};
				link1.add( new Label(NAME_ID, holidayGroup.getGroupName()));
				Link<String> link2 = new Link<String>(LINK2_ID){
					@Override
					public void onClick(){
						addLayoutForViewHoliday(holidayGroupModel);
					}
				};
				row.add(link1);
				row.add(link2);
			}

		}
	}
}

