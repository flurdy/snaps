package com.flurdy.grid.snaps.web.admin;

import com.flurdy.grid.snaps.web.AbstractGridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController extends AbstractGridController {


	@RequestMapping("")
	public ModelAndView showAdminHandler() {

		return returnTemplate("admin/index");
	}

}
