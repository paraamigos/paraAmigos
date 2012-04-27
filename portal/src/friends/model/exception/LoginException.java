package friends.model.exception;

public class LoginException extends Exception {

	private static final long serialVersionUID = 2561240552315207269L;

	
	public LoginException() {
		super();
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

	
	
}
