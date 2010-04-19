/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web;

import com.flurdy.grid.snaps.service.IAdminService;
import com.flurdy.grid.snaps.service.IHolidayGroupService;
import com.flurdy.grid.snaps.service.IPhotoAlbumService;
import com.flurdy.grid.snaps.service.IPhotoSharingProviderService;
import com.flurdy.grid.snaps.service.ISecurityService;
import com.flurdy.grid.snaps.service.ITravellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractGridController {

	protected transient Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired protected IHolidayGroupService holidayGroupService;
	@Autowired protected ISecurityService securityService;
	@Autowired protected IAdminService adminService;
	@Autowired protected ITravellerService travellerService;
	@Autowired protected IPhotoAlbumService photoAlbumService;
	@Autowired protected IPhotoSharingProviderService sharingProviderService;

	protected ModelAndView returnTemplate(String view){
		ModelAndView modelAndView = new ModelAndView(view);
		modelAndView.getModel().put("analyticsId", "123" );
		log.debug("returning view: "+view);
		return modelAndView;
	}

	protected ModelAndView returnTemplate(ModelAndView modelAndView){
		modelAndView.getModel().put("analyticsId", "123" );
		log.debug("returning view: "+modelAndView.getViewName());
		return modelAndView;
	}

}