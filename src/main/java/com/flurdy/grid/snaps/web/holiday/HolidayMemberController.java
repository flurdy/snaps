package com.flurdy.grid.snaps.web.holiday;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.Traveller;
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
	public String createHolidayHandler( @PathVariable("groupId") long groupId ){

		log.debug("requesting to join holiday group");

		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(groupId);
		final Traveller traveller = travellerService.findCurrentTraveller();

		log.debug("holiday group: "+holidayGroup);
		log.debug("traveller: "+traveller);


			holidayGroupService.addHolidayMember(holidayGroup,traveller);
			return "redirect:/holiday/"+holidayGroup.getGroupId();
		
	}


}
