package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class HolidayGroupService implements IHolidayGroupService {

	protected transient final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IHolidayGroupRepository holidayGroupRepository;

	@Override
	public Set<HolidayGroup> searchForHolidayGroups(String groupName) {
		assert groupName != null ;
		assert groupName.length() > 0;
		try {
			Long groupId = Long.parseLong(groupName);
			Set<HolidayGroup> holidayGroups = new HashSet<HolidayGroup>();
			holidayGroups.add(findHolidayGroup(groupId));
		return holidayGroups;
		} catch (NumberFormatException exception){
			return findHolidayGroups(groupName);
		}		
	}

	@Override
	public void addHolidayGroup(HolidayGroup holidayGroup) {
		log.debug("Adding group");		
		holidayGroupRepository.addHolidayGroup( holidayGroup );
	}

	@Override
	public HolidayGroup findHolidayGroup(Long groupId) {
		log.debug("finding group");
		return holidayGroupRepository.findHolidayGroup( groupId );
	}

	private Set<HolidayGroup> findHolidayGroups(String groupName) {
		if(groupName == null)
			groupName = "";
		return new HashSet<HolidayGroup>(holidayGroupRepository.findHolidayGroups( groupName ));
	}


}
