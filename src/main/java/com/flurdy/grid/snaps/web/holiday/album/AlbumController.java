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
		return returnTemplate("holiday/album/new");
	}



	@RequestMapping(value="",method=RequestMethod.POST)
	public String addAlbumHandler(
			@PathVariable("holidayGroupId") long holidayGroupId,
			String providerName,
			String url ){

		log.debug("adding album");

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayGroupId);
		if( holidayGroup != null ){
			PhotoSharingProvider provider = sharingProviderService.findProvider(providerName);
			if( provider != null ){
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
