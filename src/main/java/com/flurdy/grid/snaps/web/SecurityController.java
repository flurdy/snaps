/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web;

import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapInvalidClientInputException;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
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
			throw new SnapInvalidClientInputException(SnapInvalidClientInputException.InputError.PASSWORD_MISMATCH);
		
	}


    @RequestMapping("/admin/aristocracy/enforce")
	public String enforceAristocracyHandler(){

		log.info("Checking aristocracy!");

		securityService.enforceAristocracy();

		return "redirect:/";
	}


    @RequestMapping(value="/password/reset",method=RequestMethod.GET)
	public ModelAndView showResetPasswordFormHandler(){

		return returnTemplate("password/reset/form");
	}


    @RequestMapping(value="/password/confirmed",method=RequestMethod.GET)
	public ModelAndView showResetPasswordResultHandler(){

		return returnTemplate("password/reset/confirm");
	}


    @RequestMapping(value="/passwordy/reset",method=RequestMethod.POST)
	public String resetPasswordHandler(String usernameOrEmail){

		log.debug("reseting password");

	    if( usernameOrEmail!=null ){

		    try{
		        securityService.resetPassword(usernameOrEmail);
		    } catch(SnapNotFoundException exception){
				log.info("Reset asked for user not on system:" + usernameOrEmail);    
		    }
		    
			return "redirect:/password/confirmed";

	    } else
			throw new SnapLogicalException(SnapLogicalException.SnapLogicalError.INVALID_INPUT);

	}

	
}
