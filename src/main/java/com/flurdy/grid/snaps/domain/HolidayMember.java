package com.flurdy.grid.snaps.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class HolidayMember implements Serializable {

	@Id
	@GeneratedValue
	private Long memberId;

	@ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
	private HolidayGroup holidayGroup;

	@ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
	private Traveller traveller;


	public HolidayMember() {
	}

	
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public HolidayGroup getHolidayGroup() {
		return holidayGroup;
	}

	public void setHolidayGroup(HolidayGroup holidayGroup) {
		this.holidayGroup = holidayGroup;
	}

	public Traveller getTraveller() {
		return traveller;
	}

	public void setTraveller(Traveller traveller) {
		this.traveller = traveller;
	}
}
