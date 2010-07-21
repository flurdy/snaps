/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web.holiday;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.web.AbstractGridController;
import java.util.Set;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/holiday")
//@SessionAttributes(types=HolidayGroup.class)
public class HolidayController extends AbstractGridController {


	@RequestMapping(value="",method=RequestMethod.GET)
	public ModelAndView findOrListHolidaysHandler(String groupName){
		log.debug("finding holiday group");
		log.debug("starting with: "+groupName);
		
		Set<HolidayGroup> holidayGroups = holidayGroupService.searchForHolidayGroups(groupName);

		if( holidayGroups.size()==1){
			HolidayGroup holidayGroup = holidayGroups.iterator().next();
			return showHolidayHandler(holidayGroup.getGroupId());
		}

		ModelAndView modelAndView = new ModelAndView("holiday/list");
		modelAndView.addObject("holidayGroups", holidayGroups);
		return returnTemplate(modelAndView);
	}


	@RequestMapping(value="",method=RequestMethod.POST)
	public String createHolidayHandler(
			@ModelAttribute("holidayGroup") HolidayGroup holidayGroup, BindingResult result){

		log.debug("posting new holiday group");
		log.debug("holiday group: "+holidayGroup);
		
		holidayGroupService.addHolidayGroup(holidayGroup);

		return "redirect:/holiday/"+holidayGroup.getGroupId();
	}


	@RequestMapping("/{groupId}")
	public ModelAndView showHolidayHandler(@PathVariable("groupId") long groupId){
		log.debug("read holiday group");

		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(groupId);
		final Traveller traveller = travellerService.findCurrentTraveller();

		log.debug("holiday group: "+holidayGroup);
		ModelAndView modelAndView = ( holidayGroup.isMember(traveller) )
			? new ModelAndView("holiday/readMember")
			: new ModelAndView("holiday/read");
		modelAndView.addObject("holidayGroup", holidayGroup);
		return returnTemplate(modelAndView);
	}


	@RequestMapping("/new.html")
	public ModelAndView showCreateHolidayHandler(HolidayGroup holidayGroup){
		log.debug("showing create holiday");
		return returnTemplate("holiday/create");
	}
	

}
