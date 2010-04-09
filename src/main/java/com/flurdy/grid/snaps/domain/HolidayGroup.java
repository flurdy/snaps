package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class HolidayGroup implements Serializable {

	@Id
	@GeneratedValue
	private Long groupId;

	private String groupName;

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

	@Override
	public String toString() {
		return "Holiday Group: Id: "+ groupId + " | Name: " + groupName;
	}


}

