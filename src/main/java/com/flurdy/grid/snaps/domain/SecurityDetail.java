package com.flurdy.grid.snaps.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@NamedQueries({
	@NamedQuery(name="securitydetail.findSecurityDetailByUsername",
			query="select distinct sd from SecurityDetail sd " +
				"left join fetch sd.authorities " +
				"where sd.username = :username "),
	@NamedQuery(name="securitydetail.findSecurityDetailByEmail",
			query="select sd from SecurityDetail sd, Traveller trav " +
				"left join fetch sd.authorities " +
				"where trav.securityDetail = sd " +
				"and trav.email = :email ")
})
@Entity
public class SecurityDetail implements Serializable {

	public enum AuthorityRole{
		ROLE_ADMIN,
		ROLE_SUPER,
		ROLE_MONITOR,
		ROLE_USER
	}

	@Id
	private String username;

	@Column(nullable=false)
	private String password;

	private boolean enabled = true;
//	private boolean locked;
//	private boolean expired;

	@ElementCollection
	@CollectionTable(
		name="Authority",
		joinColumns=@JoinColumn(name="username")
	)
	private Set<String> authorities;

	public SecurityDetail() {	}
	
	public SecurityDetail(String username, String password) {
		this.username = username;
		this.password = password;
	}

	private SecurityDetail(Builder builder) {
		this.username = builder.username;
		this.password = builder.password;
		this.enabled = builder.enabled;
		this.authorities = builder.authorities;
	}

	public void addAuthority(AuthorityRole authority) {
		if( authorities == null ){
			authorities = new HashSet<String>();
		}
		if( !hasAuthority(authority)){
			authorities.add(authority.toString());
		}
	}

	public void removeAuthority(AuthorityRole authorityRole) {
		if( authorities != null && !authorities.isEmpty() && hasAuthority(authorityRole) ){
			for(String authority : authorities){
				if( authority.equals(authorityRole.name())){
					authorities.remove(authority);
					return;
				}	
			}
		}
	}

	public boolean hasAuthority(AuthorityRole authorityRole) {
		if( authorities != null && !authorities.isEmpty()){
			for(String authority : authorities){
				if( authority.equals(authorityRole.name())){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValid() {
		return username != null && username.trim().length() > 3
				&& password != null && password.trim().length() > 3;
	}


	public static AuthorityRole findRole(String role){
		for (AuthorityRole possibleRole : AuthorityRole.values() ) {
			if( possibleRole.toString().equalsIgnoreCase(role) ){
				return possibleRole;
			}
		}
		return null;
	}

	public static class Builder {
		private String username;
		private String password;
		private boolean enabled;
		private Set<String> authorities;
		public Builder(){
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
		public Builder authorities(Set<String> authorities){
			this.authorities = authorities;
			return this;
		}
		public SecurityDetail build() {
			return new SecurityDetail(this);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SecurityDetail other = (SecurityDetail) obj;
		if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
			return false;
		}
		if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
			return false;
		}
		if ( this.enabled != other.enabled ) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + (this.username != null ? this.username.hashCode() : 0);
		hash = 59 * hash + (this.password != null ? this.password.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "Username: "+username;
	}


	

	public Set<AuthorityRole> getAuthorities() {
		Set<AuthorityRole> authorityRoles = new HashSet<AuthorityRole>();
		for( String authority : authorities ){
			authorityRoles.add(findRole(authority));
		}
		return authorityRoles;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	
}
