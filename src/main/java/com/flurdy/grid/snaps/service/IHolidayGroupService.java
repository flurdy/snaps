package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import java.util.Set;

import com.flurdy.grid.snaps.domain.Traveller;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IHolidayGroupService {

	public Set<HolidayGroup> searchForHolidayGroups(String groupName);
	
//	@Secured("ROLE_USER") 
	@PreAuthorize("hasRole('ROLE_USER')")
	public void addHolidayGroup(HolidayGroup holidayGroup);

	public HolidayGroup findHolidayGroup(Long groupId);

	@PreAuthorize("hasRole('ROLE_USER')")
	public void addHolidayMember(HolidayGroup holidayGroup, Traveller traveller);
}
