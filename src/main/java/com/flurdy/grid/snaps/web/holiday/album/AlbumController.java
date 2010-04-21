package com.flurdy.grid.snaps.web.holiday.album;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import com.flurdy.grid.snaps.web.AbstractGridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	public ModelAndView showAlbumHandler(
			  @PathVariable("holidayGroupId") long holidayGroupId,
			  @PathVariable("albumId") long albumId
	){
		log.debug("showing album");
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayGroupId);
		log.debug("holiday group: "+holidayGroup);
		PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(albumId);
		log.debug("album: "+photoAlbum);
		ModelAndView modelAndView = new ModelAndView("holiday/album/read");
		modelAndView.addObject("holidayGroup", holidayGroup);
		modelAndView.addObject("photoAlbum", photoAlbum);
		return returnTemplate(modelAndView);
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
					throw new IllegalArgumentException("Invalid sharing provider",exception);
				}
				if( url != null && url.trim().length()>0){

					PhotoAlbum album = photoAlbumService.addAlbum(holidayGroup, provider, url);

					return "redirect:/holiday/"+ holidayGroupId +"/album/" + album.getAlbumId();

				} else
					throw new IllegalArgumentException("Invalid album url");
			} else 
				throw new IllegalArgumentException("Invalid sharing provider");
		} else
			throw new IllegalArgumentException("Invalid holiday group");
	}
	

	
}

