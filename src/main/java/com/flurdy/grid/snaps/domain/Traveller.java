package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@NamedQueries({
    @NamedQuery(name = "traveller.findAll",
		query = "select trav from Traveller trav " +
		"left join fetch trav.securityDetail " +
		"order by trav.fullname"),
    @NamedQuery(name = "traveller.findById",
		query = "select distinct trav from Traveller trav " +
		"left join fetch trav.securityDetail " +
		"where trav.travellerId = :travellerId"),
    @NamedQuery(name = "traveller.findByUsername",
		query = "select distinct trav from Traveller trav " +
		"left join fetch trav.securityDetail " +
		"where trav.securityDetail.username = :username")
//		query = "from Traveller")traveller.findById
})
 @Entity
public class Traveller implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long travellerId;

	@Column(nullable=false)
	private String fullname;

	@Column(nullable=false)
	private String email;

	@OneToOne
	private SecurityDetail securityDetail;

	@OneToMany(mappedBy = "owner",cascade={CascadeType.ALL},fetch=FetchType.LAZY)
		private Set<PhotoAlbum> photoAlbums;

	@OneToMany(mappedBy = "traveller",cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	private Set<HolidayMember> holidayMemberships;


	public Traveller(){
	}

	public Traveller(SecurityDetail securityDetail){
		this.securityDetail = securityDetail;
	}

	private Traveller(Builder builder) {
		this.travellerId = builder.travellerId;
		this.securityDetail = new SecurityDetail.Builder()
						.username(builder.username)
						.password(builder.password)
						.enabled(builder.enabled)
						.authorities(builder.authorities).build();
		this.fullname = builder.fullname;
		this.email = builder.email;
	}


	public static class Builder {
        private Long travellerId;
		private String username;
		private String password;
		private Set<SecurityAuthority> authorities;
		private String fullname;
		private String email;
		private boolean enabled = true;
		public Builder(){
		}
        public Builder travellerId(Long travellerId){
            this.travellerId = travellerId;
            return this;
        }
		public Builder username(String username){
			this.username = username;
			return this;
		}
		public Builder password(String password){
			this.password = password;
			return this;
		}
		public Builder enabled(boolean enabled){
			this.enabled = enabled;
			return this;
		}
		public Builder authorities(Set<SecurityAuthority> authorities){
			this.authorities = authorities;
			return this;
		}
		public Builder fullname(String fullname){
			this.fullname = fullname;
			return this;
		}
		public Builder email(String email){
			this.email = email;
			return this;
		}
		public Traveller build() {
			return new Traveller(this);
		}
	}

	@Override
	public boolean equals(Object object) {
		
		if( object != null && object instanceof Traveller){
			Traveller traveller = (Traveller) object;

			if( travellerId.equals(traveller.getTravellerId())
					&& fullname.equals(traveller.getFullname())
					&& email.equals(traveller.getEmail()) ){
				if( (securityDetail != null && traveller.getSecurityDetail() != null
						&& traveller.getSecurityDetail().equals(this.securityDetail))
						|| (securityDetail == null && traveller.getSecurityDetail() == null )	){
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + (this.travellerId != null ? this.travellerId.hashCode() : 0);
		hash = 11 * hash + (this.fullname != null ? this.fullname.hashCode() : 0);
		hash = 11 * hash + (this.email != null ? this.email.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "Fullname: " + fullname
			+ "| Email: " + email
			+ "| " + securityDetail;
	}






	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public SecurityDetail getSecurityDetail() {
		return securityDetail;
	}

	public void setSecurityDetail(SecurityDetail securityDetail) {
		this.securityDetail = securityDetail;
	}

	public Long getTravellerId() {
		return travellerId;
	}

	public void setTravellerId(Long travellerId) {
		this.travellerId = travellerId;
	}

	public Set<PhotoAlbum> getPhotoAlbums() {
		return photoAlbums;
	}

	public void setPhotoAlbums(Set<PhotoAlbum> photoAlbums) {
		this.photoAlbums = photoAlbums;
	}

	public Set<HolidayMember> getHolidayMemberships() {
		return holidayMemberships;
	}

	public void setHolidayMemberships(Set<HolidayMember> holidayMemberships) {
		this.holidayMemberships = holidayMemberships;
	}
}
