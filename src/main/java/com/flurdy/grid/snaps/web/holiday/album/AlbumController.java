package com.flurdy.grid.snaps.web.holiday.album;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import com.flurdy.grid.snaps.exception.SnapInvalidClientInputException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import com.flurdy.grid.snaps.web.AbstractGridController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/holiday/{holidayGroupId}/album")
public class AlbumController extends AbstractGridController {



	@RequestMapping(value="/new",method=RequestMethod.GET)
	public ModelAndView showNewAlbumHandler(@PathVariable("holidayGroupId") long holidayGroupId){
		log.debug("showing create album");
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayGroupId); 
		log.debug("holiday group: "+holidayGroup);
		ModelAndView modelAndView = new ModelAndView("holiday/album/new");
		modelAndView.addObject("holidayGroup", holidayGroup);
		return returnTemplate(modelAndView);
	}



	@RequestMapping(value="/{albumId}",method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.GONE)
	public String showAlbumHandler(
			  @PathVariable("holidayGroupId") long holidayGroupId,
			  @PathVariable("albumId") long albumId
	){
		log.debug("showing album");
		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayGroupId);
		if( holidayGroup != null ){
			log.debug("holiday group: "+holidayGroup);
			final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(albumId,holidayGroupId);
			if( photoAlbum != null ){
				log.debug("album: "+photoAlbum);
//
//				ModelAndView modelAndView = new ModelAndView("holiday/album/read");
//				modelAndView.addObject("holidayGroup", holidayGroup);
//				modelAndView.addObject("photoAlbum", photoAlbum);
//				return returnTemplate(modelAndView);

				return "redirect:/holiday/"+ holidayGroupId;

			} else {
				throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.PHOTO_ALBUM);
			}
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.HOLIDAY);
		}
	}
 
 

	@RequestMapping(value="",method=RequestMethod.POST)
	public String addAlbumHandler(
			@PathVariable("holidayGroupId") long holidayGroupId,
			String providerName,
			String url ){

		log.debug("adding album");

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayGroupId);
		if( holidayGroup != null ){
			if( providerName != null && providerName.trim().length()>0){
				PhotoSharingProvider provider = null;
				try{
					provider = PhotoSharingProvider.valueOf(providerName);
				} catch (IllegalArgumentException exception){
					throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.SHARING_PROVIDER);
				}
				if( url != null && url.trim().length()>0){

					PhotoAlbum album = photoAlbumService.addAlbum(holidayGroup, provider, url);

					return "redirect:/holiday/"+ holidayGroupId; // +"/album/" + album.getAlbumId();

				} else
					throw new SnapInvalidClientInputException(SnapInvalidClientInputException.InputError.URL);
			} else
				throw new SnapTechnicalException( SnapTechnicalException.SnapTechnicalError.INVALID_INPUT , "Invalid sharing provider");
		} else
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.HOLIDAY);
	}
	

	
}

