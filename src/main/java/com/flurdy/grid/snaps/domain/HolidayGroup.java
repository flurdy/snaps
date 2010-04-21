package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class HolidayGroup implements Serializable {

	@Id
	@GeneratedValue
	private Long groupId;

	private String groupName;

	@OneToMany(mappedBy = "holidayGroup",cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	private Set<PhotoAlbum> photoAlbums;

	public HolidayGroup(){
	}

	public HolidayGroup(String groupName){
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Set<PhotoAlbum> getPhotoAlbums() {
		return photoAlbums;
	}

	public void setPhotoAlbums(Set<PhotoAlbum> photoAlbums) {
		this.photoAlbums = photoAlbums;
	}

	@Override
	public String toString() {
		return "Holiday Group: Id: "+ groupId 
				  + " | Name: " + groupName;
//				  + " | albums: " 
//				  + ((photoAlbums!=null) ? photoAlbums.size() : "null");
	}


}

