package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import java.util.HashSet;
import java.util.Set;

import com.flurdy.grid.snaps.domain.Traveller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class HolidayGroupService extends AbstractService implements IHolidayGroupService {

	@Autowired
	private ITravellerService travellerService;

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
		final Traveller traveller = travellerService.findCurrentTraveller();
		log.info("Adding group:" + holidayGroup
				+ " by traveller:" + traveller);
		holidayGroupRepository.addHolidayGroup( holidayGroup );
		holidayGroup = holidayGroupRepository.findHolidayGroup( holidayGroup.getGroupId() );
		holidayGroup.addMember(traveller);
		holidayGroupRepository.updateHolidayGroup( holidayGroup );
	}

	@Override
	public HolidayGroup findHolidayGroup(Long groupId) {
		final HolidayGroup holidayGroup = holidayGroupRepository.findHolidayGroup( groupId );
		if( holidayGroup != null ){
			final Traveller traveller = travellerService.findCurrentTraveller();

			if( holidayGroup.isMember(traveller)){
				log.debug("Traveller is a member of this group");
				return holidayGroup;
			} else {
				log.debug("Traveller is NOT a member of this group");
				return holidayGroup.getBasicHolidayGroupClone();
			}
		} else {
			log.debug("Holiday not found");			
			return null;
		}
	}

	@Override
	public void addHolidayMember(HolidayGroup holidayGroup, Traveller traveller) {
		if( holidayGroup != null && holidayGroup.getGroupId() > 0 ){
			holidayGroup = holidayGroupRepository.findHolidayGroup(holidayGroup.getGroupId());
			traveller = travellerService.findCurrentTraveller();

			holidayGroup.addMember(traveller);

			holidayGroupRepository.updateHolidayGroup(holidayGroup);

		} else {
			throw new RuntimeException("Not a valid holiday");
		}
	}

	private Set<HolidayGroup> findHolidayGroups(String groupName) {
		if(groupName == null)
			groupName = "";
		return new HashSet<HolidayGroup>(holidayGroupRepository.findHolidayGroups( groupName ));
	}


}
