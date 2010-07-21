package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@NamedQueries({
	@NamedQuery(name="holidayGroup.findById",
			query="select distinct hg from HolidayGroup hg "
			+ "left join fetch hg.photoAlbums "
			+ "left join fetch hg.members "		
			+ "where hg.groupId = :groupId")
})
@Entity
public class HolidayGroup implements Serializable {

	@Id
	@GeneratedValue
	private Long groupId;              

	private String groupName;

	@OneToMany(mappedBy = "holidayGroup",cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	private Set<PhotoAlbum> photoAlbums;

	@OneToMany(mappedBy = "holidayGroup",cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	private Set<HolidayMember> members;


	public HolidayGroup(){
	}

	private HolidayGroup(Builder builder) {
		this.groupId = builder.groupId;
		this.groupName = builder.groupName;
	}


	public static class Builder {
		private Long groupId;
		private String groupName;
		public Builder(){
		}
        public Builder groupId(Long groupId){
            this.groupId = groupId;
            return this;
        }
		public Builder groupName(String groupName){
			this.groupName = groupName;
			return this;
		}
		public HolidayGroup build() {
			return new HolidayGroup(this);
		}
	}


	public boolean isMember(Traveller traveller){
		boolean travellerIsMember = false;
		if( members != null && !members.isEmpty()){
			for( HolidayMember holidayMember : members ){
				if( holidayMember.getTraveller().equals(traveller) ){
					return true;
				}
			}	
		}
		return travellerIsMember;
	}

	public HolidayGroup getBasicHolidayGroupClone(){
		return new HolidayGroup.Builder()
					.groupId(groupId.longValue())
					.groupName(""+groupName).build();
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

	public Set<HolidayMember> getMembers() {
		return members;
	}

	public void setMembers(Set<HolidayMember> members) {
		this.members = members;
	}

	@Override
	public boolean equals(Object compareObject) {
		if( compareObject != null && compareObject instanceof HolidayGroup){
			HolidayGroup compareGroup = (HolidayGroup) compareObject;
			if( (compareGroup.getGroupId() != null && groupId != null
						&& compareGroup.getGroupId().equals( groupId ))
					|| compareGroup.getGroupId() == null && groupId == null ){
				if( ( compareGroup.getGroupName() != null && groupName != null
							&& compareGroup.getGroupName().equals( groupName) )
						||  compareGroup.getGroupName() == null && groupName == null ){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Holiday Group: Id: "+ groupId 
				  + " | Name: " + groupName;
//				  + " | albums: " 
//				  + ((photoAlbums!=null) ? photoAlbums.size() : "null");
	}



}

