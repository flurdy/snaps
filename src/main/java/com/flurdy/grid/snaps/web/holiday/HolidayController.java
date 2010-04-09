/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web.holiday;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.web.AbstractGridController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
	public ModelAndView findOrListHolidaysHandler2(){
		log.debug("finding holiday group");
		return returnTemplate("holiday/list");
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
	public ModelAndView readHolidayHandler(@PathVariable("groupId") long groupId){
		log.debug("read holiday group");
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(groupId);
		log.debug("holiday group: "+holidayGroup);
		ModelAndView modelAndView = new ModelAndView("holiday/read");
		modelAndView.addObject("holidayGroup", holidayGroup);
		return returnTemplate(modelAndView);
	}


	@RequestMapping("/new.html")
	public ModelAndView showCreateHolidayHandler(HolidayGroup holidayGroup){
		log.debug("showing create holiday");
		return returnTemplate("holiday/create");
	}
	

}
