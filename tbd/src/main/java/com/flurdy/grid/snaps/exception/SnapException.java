package com.flurdy.grid.snaps.exception;

public abstract class SnapException extends RuntimeException {

	protected Throwable cause;

	protected String message;

	public SnapException(){}

	public SnapException(String message){
		this.message = message;
	}

	public SnapException(Throwable cause){
		this.cause = cause;
	}

	public SnapException(Throwable cause, String message){
		this.cause = cause;
		this.message = message;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

