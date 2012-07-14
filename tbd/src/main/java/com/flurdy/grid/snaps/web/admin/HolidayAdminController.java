package com.flurdy.grid.snaps.web.admin;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapInvalidClientInputException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.web.AbstractGridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping("/admin/holiday")
public class HolidayAdminController extends AbstractGridController {




		@RequestMapping(value = "", method = RequestMethod.GET)
		public ModelAndView listHolidaysHandler() {

			List<HolidayGroup> holidays = holidayGroupService.findAllHolidays();

			log.debug("holidays:"+holidays.size());

			ModelAndView modelAndView = new ModelAndView("admin/holiday/list");
			modelAndView.getModel().put("holidayGroups", holidays);
			return returnTemplate(modelAndView);
		}




	@RequestMapping(value = "/{holidayId}/edit", method = RequestMethod.GET)
	public ModelAndView showHolidayEditFormHandler(@PathVariable("holidayId") long holidayId) {

		if( holidayId > 0 ){

			HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayId);

			if( holidayGroup != null ){
				ModelAndView modelAndView = new ModelAndView("admin/holiday/read");
				modelAndView.getModel().put("holidayGroup", holidayGroup);
				return returnTemplate(modelAndView);
			} else {
				throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.HOLIDAY);
			}
		} else {
			throw new SnapInvalidClientInputException(SnapInvalidClientInputException.InputError.HOLIDAY);
		}

	}



	@RequestMapping(value = "/{holidayId}", method = RequestMethod.PUT)
	public String updateHolidayHandler(HolidayGroup holidayGroup) {

		if( holidayGroup != null && holidayGroup.isValid() ){
			
			adminService.updateHolidayGroup(holidayGroup);

			return "redirect:/admin/holiday";
		} else {
			throw new SnapInvalidClientInputException(SnapInvalidClientInputException.InputError.HOLIDAY);
		}
	}




	@RequestMapping(value = "/{holidayId}", method = RequestMethod.DELETE)
	public String deleteHolidayHandler(@PathVariable("holidayId") long holidayId) {
		if( holidayId > 0 ){
			adminService.deleteHolidayGroup(holidayId);

			return "redirect:/admin/holiday";
		} else {
			throw new SnapInvalidClientInputException(SnapInvalidClientInputException.InputError.HOLIDAY);
		}
	}




	@RequestMapping(value = "/{holidayId}/album/{albumId}", method = RequestMethod.DELETE)
	public String removePhotoAlbumHandler(@PathVariable("holidayId") long holidayId, @PathVariable("albumId") long albumId) {

		if( holidayId > 0 ){
			if( albumId > 0 ){

				adminService.deletePhotoAlbum(holidayId,albumId);

				return "redirect:/admin/holiday/" + holidayId + "/edit";
			} else {
				throw new SnapInvalidClientInputException(SnapInvalidClientInputException.InputError.PHOTO_ALBUM);
			}
		} else {
			throw new SnapInvalidClientInputException(SnapInvalidClientInputException.InputError.HOLIDAY);
		}
	}



}
