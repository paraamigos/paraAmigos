package friends.model.exception;

public class LoginException extends Exception {

	private static final long serialVersionUID = 2561240552315207269L;

	private String messageKey;
	
	
	public LoginException() {
		super();
	}

	public LoginException(String message, boolean isKey) {
		super(message);
		
		if (isKey) {
			messageKey = message;
		}
	}
	
	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}


	
	
	
}
