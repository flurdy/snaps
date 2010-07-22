package com.flurdy.grid.snaps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SnapLogicalException extends SnapException {

	public enum SnapLogicalError {
		
		INVALID_INPUT("The input was not valid"),
		INVALID_STATE("The current state was not expected"),
		NOT_FOUND("The resource could not be found"),
		ACCESS_DENIED("Access denied for this resource");

		private String description;

		private SnapLogicalError(String description){
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	private SnapLogicalError errorCode;


	public SnapLogicalException(SnapLogicalError errorCode){
		this.errorCode = errorCode;
		this.message = errorCode.getDescription();
	}

	public SnapLogicalException(SnapLogicalError errorCode, String message){
		this.errorCode = errorCode;
		this.message = message;
	}



	public SnapLogicalError getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(SnapLogicalError errorCode) {
		this.errorCode = errorCode;
	}


}

