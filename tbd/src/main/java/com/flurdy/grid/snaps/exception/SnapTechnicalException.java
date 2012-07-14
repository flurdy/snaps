package com.flurdy.grid.snaps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SnapTechnicalException extends SnapException {


	public enum SnapTechnicalError {
		UNEXPECTED("An unexpected exception"),
		INVALID_INPUT("The input was not valid"),
		DATA_ERROR("Data in database was unexpected"),
		EMAIL("Sending email failed");

		private String description;

		private SnapTechnicalError(String description){
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	private SnapTechnicalError errorCode;


	public SnapTechnicalException(SnapTechnicalError errorCode, String message, Throwable cause){
		super( cause, message );
		this.errorCode = errorCode;
	}

	public SnapTechnicalException(SnapTechnicalError errorCode, Throwable cause){
		super( cause, errorCode.getDescription() );
		this.errorCode = errorCode;
	}

	public SnapTechnicalException(SnapTechnicalError errorCode, String message){
		super( message );
		this.errorCode = errorCode;
	}

	public SnapTechnicalException(Throwable cause){
		super( cause, SnapTechnicalError.UNEXPECTED.getDescription() );
		this.errorCode = SnapTechnicalError.UNEXPECTED;
	}



	public SnapTechnicalError getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(SnapTechnicalError errorCode) {
		this.errorCode = errorCode;
	}

	
}
