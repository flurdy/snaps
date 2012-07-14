package com.flurdy.grid.snaps.web;

import com.flurdy.grid.snaps.exception.SnapAccessDeniedException;
import com.flurdy.grid.snaps.exception.SnapInvalidClientInputException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapLogicalException.SnapLogicalError;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException.SnapTechnicalError;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@Controller
//@RequestMapping("/")
public class ErrorController extends AbstractGridController {

	@RequestMapping("/error/serverError.html")
	public ModelAndView handleServerError() {

		log.debug("error/serverError");

		ModelAndView modelAndView = new ModelAndView("error/unexpected");

		return returnTemplate(modelAndView);

	}

	@RequestMapping("/error/accessDenied.html")
	public ModelAndView handleAccessDenied() {

		log.debug("error/accessDenied");

		ModelAndView modelAndView = new ModelAndView("error/accessDenied");

		return returnTemplate(modelAndView);
	}

	@RequestMapping("/error/notFound.html")
	public ModelAndView handleNotFound() {

		log.debug("error/notFound");

		ModelAndView modelAndView = new ModelAndView("error/notFound");

		return returnTemplate(modelAndView);
	}


}

