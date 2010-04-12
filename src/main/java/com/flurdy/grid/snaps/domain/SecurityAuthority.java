package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@NamedQueries({
	@NamedQuery(name="authority.findAuthorityByRole",
			query="select distinct aut from SecurityAuthority aut " +
			"where aut.authorityRole = :authorityRole "	)
})
@Entity
public class SecurityAuthority implements Serializable {

	public enum AuthorityRole{
		ROLE_ADMIN,
		ROLE_SUPER,
		ROLE_MONITOR,
		ROLE_USER
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name="AUTHORITY_ID" )
	private Long securityAuthorityId;

	@Column( name="AUTHORITY_ROLE", unique=true )
	private String authorityRole;

	
	public SecurityAuthority(){

	}

	public SecurityAuthority(String role){

	}

	public SecurityAuthority(AuthorityRole role){
		this.authorityRole = role.toString();
	}

	public AuthorityRole getAuthorityRole() {
		for (AuthorityRole possibleRole : AuthorityRole.values() ) {
				 if( possibleRole.toString().equalsIgnoreCase(this.authorityRole) )
							return possibleRole;
		}
		return null;
	}

	public void setAuthorityRole(AuthorityRole authorityRole) {
		this.authorityRole = authorityRole.toString();
	}

	@Override
	public String toString() {
		return "Role: " + authorityRole;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SecurityAuthority other = (SecurityAuthority) obj;
		if ((this.authorityRole == null) ? (other.authorityRole != null) : !this.authorityRole.equals(other.authorityRole)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.securityAuthorityId != null ? this.securityAuthorityId.hashCode() : 0);
		hash = 97 * hash + (this.authorityRole != null ? this.authorityRole.hashCode() : 0);
		return hash;
	}




}
