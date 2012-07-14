package com.flurdy.grid.snaps.web;

import com.flurdy.grid.snaps.exception.*;
import com.flurdy.grid.snaps.service.IAdminService;
import com.flurdy.grid.snaps.service.IHolidayGroupService;
import com.flurdy.grid.snaps.service.IPhotoAlbumService;
import com.flurdy.grid.snaps.service.IPhotoSharingProviderService;
import com.flurdy.grid.snaps.service.ISecurityService;
import com.flurdy.grid.snaps.service.ITravellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

public abstract class AbstractGridController {

	protected transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired protected IHolidayGroupService holidayGroupService;
	@Autowired protected ISecurityService securityService;
	@Autowired protected IAdminService adminService;
	@Autowired protected ITravellerService travellerService;
	@Autowired protected IPhotoAlbumService photoAlbumService;
	@Autowired protected IPhotoSharingProviderService sharingProviderService;

	@Resource
	protected Map<String,String> configurationMap;

	protected ModelAndView returnTemplate(String view){
		ModelAndView modelAndView = new ModelAndView(view);
		modelAndView.getModel().put("analyticsId", configurationMap.get("analyticsId") );
		log.debug("returning view: "+view);
		return modelAndView;
	}

	protected ModelAndView returnTemplate(ModelAndView modelAndView){
		modelAndView.getModel().put("analyticsId", "123" );
		log.debug("returning view: "+modelAndView.getViewName());
		return modelAndView;
	}


	@ExceptionHandler(SnapAccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ModelAndView handleAccessDeniedException(SnapAccessDeniedException exception) {

		log.debug("error access denied",exception);

		ModelAndView modelAndView = new ModelAndView("error/accessDenied");
		modelAndView.addObject("exception", exception);

		return returnTemplate(modelAndView);
	}


//	@ExceptionHandler(Throwable.class)
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	public ModelAndView handleUncaughtException2(Throwable exception) {
//
//		log.error("An uncaught exception was thrown",exception);
//
//		ModelAndView modelAndView = new ModelAndView("error/unexpected");
//		modelAndView.addObject("exception", exception);
//		return returnTemplate(modelAndView);
//	}


	@ExceptionHandler(SnapLogicalException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView handleTechnicalException(SnapLogicalException exception) {

		log.debug("error/logical");

		ModelAndView modelAndView = new ModelAndView("error/logical");
		modelAndView.addObject("exception", exception);

		return returnTemplate(modelAndView);

	}



	@ExceptionHandler(SnapTechnicalException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleTechnicalException(SnapTechnicalException exception) {

		log.debug("error/technical",exception);

		ModelAndView modelAndView = ( exception.getErrorCode() == SnapTechnicalException.SnapTechnicalError.UNEXPECTED )
				? new ModelAndView("error/unexpected")
				: new ModelAndView("error/technical");
		modelAndView.addObject("exception", exception);

		return returnTemplate(modelAndView);

	}



	@ExceptionHandler(SnapNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView handleNotFoundException(SnapNotFoundException exception) {

		log.debug("error/technical");

		ModelAndView modelAndView = new ModelAndView("error/notFound");
		modelAndView.addObject("exception", exception);

		return returnTemplate(modelAndView);

	}


	@ExceptionHandler(SnapInvalidClientInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView handleInvalidInputException(SnapInvalidClientInputException exception) {

		log.debug("Invalid input by client: ",exception);


		ModelAndView modelAndView = new ModelAndView("error/logical");
		modelAndView.addObject("exception", exception);

		return returnTemplate(modelAndView);

	}


}
