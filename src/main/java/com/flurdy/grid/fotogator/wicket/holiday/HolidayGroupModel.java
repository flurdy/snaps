/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.fotogator.wicket.holiday;

import com.flurdy.grid.fotogator.domain.HolidayGroup;
import com.flurdy.grid.fotogator.service.IHolidayGroupService;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HolidayGroupModel extends LoadableDetachableModel<HolidayGroup> {

	private transient final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@SpringBean
	private IHolidayGroupService holidayGroupService;

	private Long groupId;

	public HolidayGroupModel(Long groupId) {
		super();
		this.groupId = groupId;
		InjectorHolder.getInjector().inject(this);
	}

	public HolidayGroupModel(HolidayGroup holidayGroup) {
		super(holidayGroup);
		assert holidayGroup.getGroupId() != null;
		this.groupId = holidayGroup.getGroupId();
		InjectorHolder.getInjector().inject(this);
	}


	@Override
	protected HolidayGroup load() {
		if(groupId!=null && groupId>0)
			return holidayGroupService.findHolidayGroup(groupId);
		else
			return new HolidayGroup();
	}

}
