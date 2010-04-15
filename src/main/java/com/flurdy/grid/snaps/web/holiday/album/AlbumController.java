package com.flurdy.grid.snaps.web.holiday.album;

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
	

}
