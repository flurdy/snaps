/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web;

import com.flurdy.grid.snaps.domain.Traveller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class FrontController extends AbstractGridController {

    @RequestMapping("/index.html")
	public ModelAndView indexHandler(){
//	    if( travellerService.findCurrentTraveller() != null ){
//			return new ModelAndView("redirect:/holiday");
//	    } else {
	        return returnTemplate("index");
//	    }
    }

	@RequestMapping("/about.html")
	public ModelAndView aboutHandler(){
		return returnTemplate("about");
	}


}
