package com.flurdy.grid.snaps.web.holiday;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.web.AbstractGridController;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/holiday/{groupId}/member")
public class HolidayMemberController extends AbstractGridController {

	@RequestMapping(value="",method= RequestMethod.POST)
	public String joinHolidayHandler( @PathVariable("groupId") long groupId ){

		log.debug("requesting to join holiday group");

		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(groupId);
		final Traveller traveller = travellerService.findCurrentTraveller();

		if( holidayGroup != null && traveller != null ){

			log.debug("holiday group: "+holidayGroup);
			log.debug("traveller: "+traveller);

			holidayGroupService.addTravellerAsPendingMember(holidayGroup,traveller);
			return "redirect:/holiday/"+holidayGroup.getGroupId();

		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.HOLIDAY);
		}
	}


	@RequestMapping(value="/{travellerId}/approve",method= RequestMethod.POST)
	public String approveTravellerToJoinHolidayHandler(
			@PathVariable("groupId") long groupId,
			@PathVariable("travellerId") long travellerId ){

		log.debug("approving request to join holiday group");

		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(groupId);
		
		if( holidayGroup != null ){

			final Traveller traveller = travellerService.findTraveller(travellerId);

			if( traveller != null ){

				log.debug("holiday group: "+holidayGroup);
				log.debug("traveller: "+traveller);

				holidayGroupService.addHolidayMember(holidayGroup,traveller);
				return "redirect:/holiday/"+holidayGroup.getGroupId();

			} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.TRAVELLER);
			}
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.HOLIDAY);
		}
	}


}
