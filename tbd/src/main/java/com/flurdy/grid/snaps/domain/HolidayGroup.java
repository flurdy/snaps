package com.flurdy.grid.snaps.domain;

import com.flurdy.grid.snaps.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@NamedQueries({
	@NamedQuery(name="holidayGroup.findById",
			query="select distinct hg from HolidayGroup hg "
			+ "left join fetch hg.photoAlbums "
			+ "left join fetch hg.members "
			+ "where hg.groupId = :groupId"),
	@NamedQuery(name="holidayGroup.findBasicById",
			query="select distinct hg from HolidayGroup hg "
			+ "where hg.groupId = :groupId"),
	@NamedQuery(name="holidayGroup.searchByName",
			query="select distinct hg from HolidayGroup hg " +
				"where hg.groupName like :groupName " +
				"order by hg.groupName" ),
	@NamedQuery(name="holidayGroup.findAll",
			query="select distinct hg from HolidayGroup hg " +
				"order by hg.groupName" )
})
@Entity
public class HolidayGroup implements Serializable {

	protected static final Logger log = LoggerFactory.getLogger(HolidayGroup.class);

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

	public HolidayGroup(String groupName){
		this.groupName = groupName;
	}

	private HolidayGroup(Builder builder) {
		this.groupId = builder.groupId;
		this.groupName = builder.groupName;
		this.members = builder.members;
	}


	public static class Builder {
		private Long groupId;
		private String groupName;
		private Set<HolidayMember> members;
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
		public Builder members(Set<HolidayMember> members){
			this.members = members;
			return this;
		}
		public HolidayGroup build() {
			return new HolidayGroup(this);
		}
	}


	public HolidayGroup getBasicHolidayGroupClone(){
		Set<HolidayMember> members = new HashSet<HolidayMember>();
		if( this.members != null)
			members.addAll(this.members);
		return new HolidayGroup.Builder()
					.groupId(groupId)
					.groupName(""+groupName)
					.members(members)
					.build();
	}

	public boolean isMember(Traveller traveller){
		if( traveller != null ){
			if( members != null && !members.isEmpty()){
				for( HolidayMember holidayMember : members ){					
					if( holidayMember.getTraveller()!=null && holidayMember.getTraveller().equals(traveller) ){
						return holidayMember.isApproved();
					} else {
						log.debug("Traveller is not a member: "+traveller);
						log.debug("member: "+holidayMember);
					}
				}
			}
		}
		return false;
	}

	public void addMember(Traveller traveller) {
		if( members == null )
			members = new HashSet<HolidayMember>();
		if( isPendingMember(traveller)){
			approveMember(traveller);
		} else if( isMember(traveller)){
			return;			
		} else {
			members.add(new HolidayMember.Builder()
				.holidayGroup(this)
				.traveller(traveller)
				.approved(true)
				.build());
		}
	}

	public boolean isPendingMember(Traveller traveller) {
		if( members != null && !members.isEmpty()){
			for( HolidayMember holidayMember : members ){
				if( holidayMember.getTraveller().equals(traveller) ){
					return !holidayMember.isApproved();
				}
			}
		}
		return false;
	}


	public void approveMember(Traveller traveller) {
		for( HolidayMember holidayMember : members ){
			if( holidayMember.getTraveller().equals(traveller) ){
				holidayMember.setApproved(true);
			}
		}
	}

	public void addPendingMember(Traveller pendingTraveller) {
		if( members == null )
			members = new HashSet<HolidayMember>();
		members.add(new HolidayMember.Builder()
				.holidayGroup(this)
				.traveller(pendingTraveller)
				.approved(false)
				.build());
	}


	public boolean isValid() {
		if( this.groupName != null  ){
			if(this.groupName.length() > 3 ){
				if(this.groupName.matches("^\\p{L}[\\p{L} ]+\\p{L}$") ){
					return true;
				}
			}
		}
		return false;
	}

	

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		if(groupName != null)
			groupName = groupName.trim();
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

