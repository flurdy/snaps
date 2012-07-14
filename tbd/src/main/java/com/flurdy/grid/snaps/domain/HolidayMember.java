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

	private boolean approved = false;

	public HolidayMember() {
	}


	private HolidayMember(Builder builder) {
		this.traveller = builder.traveller;
		this.holidayGroup = builder.holidayGroup;
		this.approved = builder.approved;
	}


	public static class Builder {
		private HolidayGroup holidayGroup;
		private Traveller traveller;
		private boolean approved;
		public Builder(){
		}
        public Builder holidayGroup(HolidayGroup holidayGroup){
            this.holidayGroup = holidayGroup;
            return this;
        }
		public Builder traveller(Traveller traveller){
			this.traveller = traveller;
			return this;
		}
		public Builder approved(boolean approved){
			this.approved = approved;
			return this;
		}
		public HolidayMember build() {
			return new HolidayMember(this);
		}
	}

	@Override
	public String toString() {
		return "HolidayGroup: " + holidayGroup.getGroupId()
				+ " | Traveller: " + traveller.getTravellerId()
				+ " approved: " + approved;
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

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
}
