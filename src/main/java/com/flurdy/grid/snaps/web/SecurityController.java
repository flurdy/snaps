/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web;

import com.flurdy.grid.snaps.domain.Traveller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class SecurityController extends AbstractGridController {


	@RequestMapping("/join.html")
	public ModelAndView showJoinHandler(){ return returnTemplate("join"); }

	@RequestMapping(value="registration", method=RequestMethod.GET)
	public ModelAndView showRegisteredHandler(){ return returnTemplate("registered"); }
	
    @RequestMapping("login.html")
	public ModelAndView showLoginHandler(){ return returnTemplate("login"); }

    @RequestMapping(value="registration",method=RequestMethod.POST)
	public String registerNewTravellerHandler(
			String username, 		String fullname,
			String email,			String password,
			String confirmPassword
	){
		log.debug("register");

		if(password.equals(confirmPassword)){

			Traveller traveller = new Traveller.Builder()
					.username(username)
					.fullname(fullname)
					.password(password)
					.email(email).build();

			securityService.registerTraveller(traveller);
		return "redirect:/registration";
		} else
			throw new IllegalStateException("Passwords do not match");
		
	}

}
