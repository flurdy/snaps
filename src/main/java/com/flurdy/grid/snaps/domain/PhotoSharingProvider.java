/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@NamedQueries({
//    @NamedQuery(name = "sharingProvider.findAll",
//		query = "select prov from PhotoSharingProvider prov order by prov.name"),
//    @NamedQuery(name = "sharingProvider.findByName",
//		query = "select distinct prov from PhotoSharingProvider prov " +
//		"where prov.name = :providerName")
//})
//@Entity
public enum PhotoSharingProvider  {
	PICASA("Picasa Webalbum"), FLICKR("Flickr"), CUSTOM("Custom album");

	private String title;

	PhotoSharingProvider(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public boolean validUrl(String url){
		switch(this){
			case PICASA:
				return url.matches("^http://picasaweb.google.com/\\w+/\\w+.?$");
			case FLICKR:
				return url.matches("^http://www.flickr.com/photos/\\w+/sets/\\d+/?$");
			case CUSTOM:
				return true;
			default:
				return false;
		}
	}
	private static final Pattern PICASA_ID= Pattern.compile("^http://picasaweb.google.com/(\\w+/\\w+).?$");
	private static final Pattern FLICKR_ID = Pattern.compile("^http://www.flickr.com/(photos/\\w+/sets/)(\\d+)/$");
	public String parseProviderId(String url){
		Matcher matcher = null;
		switch(this){
			case PICASA:
				matcher = PICASA_ID.matcher(url);
				matcher.find();
				return matcher.group(1);
			case FLICKR:
				matcher = FLICKR_ID.matcher(url);
				matcher.find();
				return matcher.group(2);
			case CUSTOM:
			default:
				return null;
		}
	}


}
