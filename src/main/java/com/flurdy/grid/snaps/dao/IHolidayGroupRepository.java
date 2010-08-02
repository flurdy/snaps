package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import java.util.Collection;
import java.util.List;

public interface IHolidayGroupRepository {

	public Long addHolidayGroup(HolidayGroup holidayGroup);

	public HolidayGroup findHolidayGroup(Long groupId);
	public Collection<HolidayGroup> findHolidayGroups(String groupName);

	public void updateHolidayGroup(HolidayGroup holidayGroup);

	public Collection<HolidayGroup> findAllHolidayGroups();

	public void deleteHolidayGroup(HolidayGroup holidayGroup);
}
