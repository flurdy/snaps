package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;


@NamedQueries({
	@NamedQuery(name="photoAlbum.findById",
			query="select distinct pa from PhotoAlbum pa "
				+ "left join fetch pa.holidayGroup "
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

//	@Column(nullable = false)
	private String sharingProvider;

//	private String providerId;

	@ManyToOne(cascade={CascadeType.MERGE},fetch=FetchType.EAGER)
	private Traveller owner;

	@Transient
	private Set<String> thumbnails;

	public PhotoAlbum() { 	}
	
	public PhotoAlbum( PhotoSharingProvider sharingProvider, String url ){
		this.sharingProvider = sharingProvider.name();
		this.url = url;
	}

	private PhotoAlbum(Builder builder) {
		this.sharingProvider = builder.sharingProvider.name();
		this.url = builder.url;
		this.holidayGroup = builder.holidayGroup;
//		this.providerId = builder.providerId;
		this.owner = builder.owner;
	}


	public boolean isValid() {
		if( sharingProvider != null ){
			PhotoSharingProvider photoSharingProvider = PhotoSharingProvider.valueOf(sharingProvider);
			if( photoSharingProvider != null ){
				if( url != null && url.trim().length() > 0
						&& ( url.startsWith("http://") || url.startsWith("https://") )){
					return photoSharingProvider.validUrl(url);	
				}
			}
		}
		return false;
	}


	public static class Builder {
		private String url;
		private HolidayGroup holidayGroup;
		private PhotoSharingProvider sharingProvider;
		private Traveller owner;
//		private String providerId;
		public Builder(){
		}
//		public Builder providerId(String providerId){
//			this.providerId = providerId;
//			return this;
//		}
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
		public Builder owner(Traveller owner){
			this.owner = owner;
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

	public Traveller getOwner() {
		return owner;
	}

	public void setOwner(Traveller owner) {
		this.owner = owner;
	}

	public Set<String> getThumbnails() {
		if( thumbnails == null)
			thumbnails = new HashSet<String>();
		return thumbnails;
	}

	public void setThumbnails(Set<String> thumbnails) {
		this.thumbnails = thumbnails;
	}

//	public String getProviderId() {
//		return providerId;
//	}
//
//	public void setProviderId(String providerId) {
//		this.providerId = providerId;
//	}

	@Override
	public String toString() {
		return "Id: " + albumId
				+ " | provider: " + sharingProvider
				+ " | url: " + url;
	}







}
