package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flurdy.grid.snaps.domain.HolidayMember;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapAccessDeniedException;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapLogicalException.SnapLogicalError;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException.SnapTechnicalError;
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

		assert traveller != null;

		log.info("Adding group:" + holidayGroup
				+ " by traveller:" + traveller);

		if( holidayGroup != null && holidayGroup.isValid() ){
						
			long holidayId = holidayGroupRepository.addHolidayGroup( holidayGroup );
			holidayGroup = holidayGroupRepository.findHolidayGroup( holidayId );
			holidayGroup.addMember(traveller);
			holidayGroupRepository.updateHolidayGroup( holidayGroup );

		} else {
			throw new SnapTechnicalException(SnapTechnicalError.INVALID_INPUT,"Not a valid holiday");
		}
	}

	@Override
	public HolidayGroup findHolidayGroup(long groupId) {

		assert groupId > 0;

		final HolidayGroup holidayGroup = holidayGroupRepository.findHolidayGroup( groupId );
		if( holidayGroup != null ){
			final Traveller traveller = travellerService.findCurrentTraveller();

			if( traveller != null ){

				if( holidayGroup.isMember(traveller)){
					return holidayGroup;
				} else {
					return holidayGroup.getBasicHolidayGroupClone();
				}
			} else {
				throw new SnapTechnicalException(SnapTechnicalError.UNEXPECTED,"Current traveller unavailable");
			}
		} else {
			log.debug("Holiday not found: " + groupId);			
			return null;
		}
	}

	@Override
	public void addHolidayMember(HolidayGroup holidayGroup, Traveller traveller) {
		if( holidayGroup != null && holidayGroup.getGroupId() > 0 ){

			final Traveller currentTraveller = travellerService.findCurrentTraveller();

			if( holidayGroup.isMember(currentTraveller)){

				if( holidayGroup.isPendingMember(traveller)){
					holidayGroup.approveMember(traveller);
				} else {
					holidayGroup.addMember(traveller);
				}

				holidayGroupRepository.updateHolidayGroup(holidayGroup);

			} else {
				log.info("not member");
				throw new SnapAccessDeniedException(SnapAccessDeniedException.SnapAccessError.NOT_MEMBER);
			}
		} else {
			throw new SnapTechnicalException(SnapTechnicalError.INVALID_INPUT,"Not a valid holiday");
		}
	}

	@Override
	public void addTravellerAsPendingMember(HolidayGroup holidayGroup, Traveller pendingTraveller) {
		if( holidayGroup != null && holidayGroup.getGroupId() > 0 ){

			if( !holidayGroup.isMember(pendingTraveller)
					&& !holidayGroup.isPendingMember(pendingTraveller) ){

				log.info("Adding traveller [" + pendingTraveller.getSecurityDetail().getUsername()
						+ "] as pending member of [" + holidayGroup.getGroupId() + "]");

				holidayGroup.addPendingMember(pendingTraveller);

				holidayGroupRepository.updateHolidayGroup(holidayGroup);


			} else {
				throw new SnapLogicalException(SnapLogicalError.INVALID_STATE, "Traveller is already a pending/member of this group");
			}
		} else {
			throw new SnapTechnicalException(SnapTechnicalError.INVALID_INPUT,"Not a valid holiday");
		}
	}

	@Override
	public List<HolidayGroup> findAllHolidays() {
		return new ArrayList(holidayGroupRepository.findAllHolidayGroups());				
	}

	private Set<HolidayGroup> findHolidayGroups(String groupName) {
		if(groupName == null)
			groupName = "";
		return new HashSet<HolidayGroup>(holidayGroupRepository.findHolidayGroups( groupName ));
	}


}
