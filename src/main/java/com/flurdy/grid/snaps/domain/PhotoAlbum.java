package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class PhotoAlbum implements Serializable{

	@Id
	private Long albumId;

	private String url;

	@OneToMany
	private PhotoSharingProvider provider;

	public PhotoAlbum() { 	}
	
	public PhotoAlbum( PhotoSharingProvider provider, String url ){
		this.provider = provider;
		this.url = url;
	}

	private PhotoAlbum(Builder builder) {
		this.provider = builder.provider;
		this.url = builder.url;
	}

	public static class Builder {
		private String url;
		private PhotoSharingProvider provider;
		public Builder(){
		}
		public Builder url(String url){
			this.url = url;
			return this;
		}
		public Builder provider(PhotoSharingProvider provider){
			this.provider = provider;
			return this;
		}
		public PhotoAlbum build() {
			return new PhotoAlbum(this);
		}
	}


	
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
