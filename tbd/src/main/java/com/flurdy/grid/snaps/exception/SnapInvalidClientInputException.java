package com.flurdy.grid.snaps.exception;


//@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SnapInvalidClientInputException extends SnapLogicalException {

	public enum InputError {
		PASSWORD_MISMATCH("Passwords does not match"),
		PASSWORD_LENGTH("Password not long enough"),
		URL("URL not valid"),
		USERNAME_TAKEN("Username already taken"),
		HOLIDAY("Holiday invalid"),
		UNSPECIFIED("Input invalid"),
		PHOTO_ALBUM("Photo album invalid");

		private String description;

		private InputError(String description){
			this.description = description;
		}

		public String getDescription(){
			return this.description;
		}
	}

	private InputError inputError;

	public SnapInvalidClientInputException(InputError snapInputError){
		super(SnapLogicalError.INVALID_INPUT,snapInputError.getDescription());
		this.inputError = snapInputError;
	}

	public SnapInvalidClientInputException(String message){
		super(SnapLogicalError.INVALID_INPUT, message);
		this.inputError = InputError.UNSPECIFIED;
	}

	public InputError getInputError(){
		return this.inputError;
	}

}

