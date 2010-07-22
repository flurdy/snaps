package com.flurdy.grid.snaps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.FORBIDDEN)
public class SnapAccessDeniedException extends SnapLogicalException {

	public enum SnapAccessError {

		NOT_MEMBER("Traveller was not a member of this holiday");

		private String description;

		private SnapAccessError(String description){
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	private SnapAccessError accessError;

	public SnapAccessDeniedException(String message) {
		super(SnapLogicalError.ACCESS_DENIED, message);
	}

	public SnapAccessDeniedException(SnapAccessError  accessError) {
		super(SnapLogicalError.ACCESS_DENIED, accessError.getDescription());
		this.accessError = accessError;
	}

}
