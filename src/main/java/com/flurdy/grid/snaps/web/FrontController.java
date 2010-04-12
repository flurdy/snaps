/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class FrontController extends AbstractGridController {

    @RequestMapping("/index.html")
	public ModelAndView indexHandler(){ log.debug("index"); return returnTemplate("index"); }

	@RequestMapping("/about.html")
	public ModelAndView aboutHandler(){ log.debug("about"); return returnTemplate("about"); }


}
