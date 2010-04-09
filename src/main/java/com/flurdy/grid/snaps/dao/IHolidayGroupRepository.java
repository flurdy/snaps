package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import java.util.Collection;

public interface IHolidayGroupRepository {

	public void addHolidayGroup(HolidayGroup holidayGroup);

	public HolidayGroup findHolidayGroup(Long groupId);
	public Collection<HolidayGroup> findHolidayGroups(String groupName);

}
