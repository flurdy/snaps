package com.flurdy.grid.snaps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class SnapNotFoundException extends SnapLogicalException {
	
	public enum SnapResourceNotFound {
		HOLIDAY("Holiday not found"),
		PHOTO_ALBUM("Photo album not found"),
		TRAVELLER("Traveller not found"),
		SHARING_PROVIDER("Sharing provider not found"),
		SECURITY_DETAILS("Security details not found");

		private String description;

		private SnapResourceNotFound(String description){
			this.description = description;
		}

		public String getDescription(){
			return this.description;
		}
	}

	private SnapResourceNotFound resourceNotFound;

	public SnapNotFoundException(String message) {
		super(SnapLogicalError.NOT_FOUND, message);
	}

	public SnapNotFoundException(SnapResourceNotFound resourceNotFound) {
		super(SnapLogicalError.NOT_FOUND, resourceNotFound.getDescription());
		this.resourceNotFound = resourceNotFound;
	}

	public SnapResourceNotFound getResourceNotFound(){
		return this.resourceNotFound;
	}

}
