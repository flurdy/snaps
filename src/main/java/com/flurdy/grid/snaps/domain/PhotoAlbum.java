/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class PhotoAlbum {

	@Id
	private Long albumId;

	private String url;

	@OneToMany
	private PhotoSharingProvider provider;



	
	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	public PhotoSharingProvider getProvider() {
		return provider;
	}

	public void setProvider(PhotoSharingProvider provider) {
		this.provider = provider;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}





}
