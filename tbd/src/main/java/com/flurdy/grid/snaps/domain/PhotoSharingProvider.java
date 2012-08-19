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
	PICASA("Picasa Web Album"), FLICKR("Flickr"), CUSTOM("Custom album");

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
				return (url.matches("^http://picasaweb.google.com/\\w+/\\w+.?$") || url.matches("^http://picasaweb.google.com/data/feed/api/user/\\w+/albumid/\\w+.?$"));
			case FLICKR:
				return url.matches("^http://www.flickr.com/photos/\\w+/sets/\\d+/?$");
			case CUSTOM:
				return true;
			default:
				return false;
		}
	}

	private static final Pattern FLICKR_ID = Pattern.compile("^http://www.flickr.com/(photos/\\w+/sets/)(\\d+)/$");

	public String parseFlickrSetId(String url){
		Matcher matcher = null;
		switch(this){
			case FLICKR:
				matcher = FLICKR_ID.matcher(url);
				matcher.find();
				return matcher.group(2);
			case CUSTOM:
			case PICASA:
			default:
				return null;
		}
	}


	private static final Pattern PICASAURL_ID= Pattern.compile("^http://picasaweb.google.com/(\\w+)/(\\w+).?$");
	private static final Pattern PICASARSS_ID= Pattern.compile("^http://picasaweb.google.com/data/feed/api/user/(\\w+)/albumid/(\\w+).?$");

	public String parsePicasaUsername(String url) {
		Matcher matcher = PICASAURL_ID.matcher(url);
		if( matcher.find() ){
			return matcher.group(1);
		}
		return null;
	}

	public String parsePicasaAlbumname(String url) {
		Matcher matcher = PICASAURL_ID.matcher(url);
		if( matcher.find() ){
			return matcher.group(2);
		}
		return null;
	}
}