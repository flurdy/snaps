package com.flurdy.grid.snaps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SnapInvalidClientInputException extends SnapLogicalException {

	public enum SnapInputError {
		PASSWORD_MISMATCH("Passwords does not match"),
		PASSWORD_LENGTH("Password not long enough"),
		URL("URL not valid"),
		USERNAME_TAKEN("Username already taken");

		private String description;

		private SnapInputError(String description){
			this.description = description;
		}

		public String getDescription(){
			return this.description;
		}
	}

	private SnapInputError snapInputError;

	public SnapInvalidClientInputException(SnapInputError snapInputError){
		super(SnapLogicalError.INVALID_INPUT,snapInputError.getDescription());
		this.snapInputError = snapInputError;
	}

	public SnapInputError getSnapInputError(){
		return this.snapInputError;
	}

}

