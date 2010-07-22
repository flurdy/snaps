/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web.holiday;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.HolidayMember;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapLogicalException.SnapLogicalError;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.web.AbstractGridController;

import java.util.HashSet;
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

		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(groupId);
		if( holidayGroup != null ){
			final Traveller currentTraveller = travellerService.findCurrentTraveller();

			ModelAndView modelAndView = ( holidayGroup.isMember(currentTraveller) )
				? new ModelAndView("holiday/readMember")
				: new ModelAndView("holiday/readVisitor");

			modelAndView.addObject("holidayGroup", holidayGroup);

			if ( holidayGroup.isMember(currentTraveller) ){

				Set<Traveller> pendingTravellers = new HashSet<Traveller>();
				for(HolidayMember member : holidayGroup.getMembers() ){
					if( !member.isApproved()){
						pendingTravellers.add(member.getTraveller());
					}
				}
				modelAndView.addObject("pendingTravellers", pendingTravellers);

				Set<Traveller> travellers = new HashSet<Traveller>();
				for(HolidayMember member : holidayGroup.getMembers() ){
					if( member.isApproved() && !member.getTraveller().equals(currentTraveller)){
						travellers.add(member.getTraveller());
					}
				}
				modelAndView.addObject("travellers", travellers);

			} else if ( holidayGroup.isPendingMember(currentTraveller) ){
				modelAndView.addObject("isPendingTraveller", Boolean.TRUE);
			}

			return returnTemplate(modelAndView);
			
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.HOLIDAY);
		}
	}


	@RequestMapping("/new.html")
	public ModelAndView showCreateHolidayHandler(HolidayGroup holidayGroup){
		log.debug("showing create holiday");
		return returnTemplate("holiday/create");
	}
	

}
