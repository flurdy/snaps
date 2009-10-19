/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.fotogator.domain;

import java.io.Serializable;

public class HolidayGroup implements Serializable{

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

