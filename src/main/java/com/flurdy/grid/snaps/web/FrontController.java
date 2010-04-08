/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value="/")
public class FrontController {

	protected transient Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/index.html")
	public String indexHandler(){ return "index"; }

    @RequestMapping("/front.html")
	public String frontHandler(){ return "index"; }

}
