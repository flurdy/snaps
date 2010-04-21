package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



@NamedQueries({
	@NamedQuery(name="photoAlbum.findById",
			query="select distinct pa from PhotoAlbum pa "
			+ "where pa.albumId = :albumId")
})
@Entity
public class PhotoAlbum implements Serializable{

	@Id
	@GeneratedValue
	private Long albumId;

	private String url;

	@ManyToOne(cascade={CascadeType.MERGE},fetch=FetchType.EAGER)
	private HolidayGroup holidayGroup;

	private String sharingProvider;

	public PhotoAlbum() { 	}
	
	public PhotoAlbum( PhotoSharingProvider sharingProvider, String url ){
		this.sharingProvider = sharingProvider.name();
		this.url = url;
	}

	private PhotoAlbum(Builder builder) {
		this.sharingProvider = builder.sharingProvider.name();
		this.url = builder.url;
		this.holidayGroup = builder.holidayGroup;
	}

	public static class Builder {
		private String url;
		private HolidayGroup holidayGroup;
		private PhotoSharingProvider sharingProvider;
		public Builder(){
		}
		public Builder url(String url){
			this.url = url;
			return this;
		}
		public Builder holidayGroup(HolidayGroup holidayGroup){
			this.holidayGroup = holidayGroup;
			return this;
		}
		public Builder sharingProvider(PhotoSharingProvider sharingProvider){
			this.sharingProvider = sharingProvider;
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

	public PhotoSharingProvider getSharingProvider() {
		return PhotoSharingProvider.valueOf(sharingProvider);
	}

	public void setSharingProvider(PhotoSharingProvider sharingProvider) {
		this.sharingProvider = sharingProvider.name();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HolidayGroup getHolidayGroup() {
		return holidayGroup;
	}

	public void setHolidayGroup(HolidayGroup holidayGroup) {
		this.holidayGroup = holidayGroup;
	}





}
