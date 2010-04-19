package com.flurdy.grid.snaps.web.admin;

import com.flurdy.grid.snaps.dao.SecurityRepository;
import com.flurdy.grid.snaps.domain.SecurityAuthority;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.web.AbstractGridController;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/traveller")
public class TravellerAdminController extends AbstractGridController {



	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView listTravellersHandler() {
		
		List<Traveller> travellers = travellerService.findTravellers();

		ModelAndView modelAndView = new ModelAndView("admin/traveller/list");
		modelAndView.getModel().put("travellers", travellers);
		return returnTemplate(modelAndView);
	}



	@RequestMapping(value = "/{travellerId}/edit", method = RequestMethod.GET)
	public ModelAndView showTravellerEditFormHandler(@PathVariable("travellerId") long travellerId) {

		Traveller traveller = travellerService.findTraveller(travellerId);
		SecurityDetail securityDetail = securityService.findLogin(traveller.getSecurityDetail().getUsername());

		ModelAndView modelAndView = new ModelAndView("admin/traveller/read");
		modelAndView.getModel().put("traveller", traveller);
		modelAndView.getModel().put("securityDetail", securityDetail);
		return returnTemplate(modelAndView);
	}

	@RequestMapping(value = "/{travellerId}", method = RequestMethod.PUT)
	public String updateTravellerHandler(Traveller traveller) {

		adminService.updateTraveller(traveller);

		return "redirect:/admin";//traveller/"+traveller.getTravellerId();
	}

	@RequestMapping(value = "/security/{username}/password", method = RequestMethod.PUT)
	public String changeSecurityDetailPasswordHandler(
			@PathVariable("username") String username,
			String password, String confirmPassword) {

		if (password.equals(confirmPassword)) {

			securityService.changeSecurityDetailPassword(username, password);

			return "redirect:/admin";
		} else {
			log.debug("Password:[" + password + "] != [" + confirmPassword + "]");
			throw new IllegalArgumentException("Passwords do not match");
		}
	}

	@RequestMapping(value = "/security/{username}/enable", method = RequestMethod.PUT)
	public String enableOrDisableSecurityDetailHandler(
			@PathVariable("username") String username,
			boolean enable) {

		if (enable) {
			securityService.enableSecurityDetail(username);
		} else {
			securityService.disableSecurityDetail(username);
		}

		return "redirect:/admin";
	}

	@RequestMapping(value = "/security/{username}/authority/{authorityRole}", method = RequestMethod.DELETE)
	public String deleteAuthorityHandler(
			@PathVariable("username") String username,
			@PathVariable("authorityRole") String authorityRole) {
		if (username != null && username.length() > 0 && authorityRole != null && authorityRole.length() > 0) {

			log.debug("Role:"+authorityRole);
			securityService.removeAuthority(username, new SecurityAuthority(authorityRole));

			return "redirect:/admin/";
		} else {
			throw new IllegalArgumentException("User [" + username + "] or role [" + authorityRole + "] invalid");
		}
	}

	@RequestMapping(value = "/security/{username}/authority", method = RequestMethod.POST)
	public String addAuthorityHandler(
			@PathVariable("username") String username,
			String authorityRole) {

		if (username != null && username.length() > 0 && authorityRole != null && authorityRole.length() > 0) {

			log.debug("Role:"+authorityRole);

			securityService.addAuthority(username, new SecurityAuthority(authorityRole));

			return "redirect:/admin/";
		} else {
			throw new IllegalArgumentException("User [" + username + "] or role [" + authorityRole + "] invalid");
		}

	}


	@RequestMapping(value = "/{travellerId}", method = RequestMethod.DELETE)
	public String deleteTravellerHandler(@PathVariable("travellerId") long travellerId) {

//		Traveller traveller = travellerService.findTraveller(travellerId);
//		SecurityDetail securityDetail = securityService.findLogin(traveller.getSecurityDetail().getUsername());

		adminService.deleteTraveller(travellerId);

		return "redirect:/admin";//traveller/"+traveller.getTravellerId();
	}


	
}
