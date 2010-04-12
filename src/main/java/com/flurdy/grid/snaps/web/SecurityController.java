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
public class SecurityController extends AbstractGridController {

    @RequestMapping("login.html")
	public ModelAndView indexHandler(){ log.debug("login2"); return returnTemplate("login"); }

}
