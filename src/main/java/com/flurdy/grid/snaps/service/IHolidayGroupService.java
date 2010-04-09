package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import java.util.Set;

public interface IHolidayGroupService {

	public Set<HolidayGroup> searchForHolidayGroups(String groupName);

	public void addHolidayGroup(HolidayGroup holidayGroup);

	public HolidayGroup findHolidayGroup(Long groupId);

}
