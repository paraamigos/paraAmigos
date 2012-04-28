package framework.application;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -506671777889984507L;
	
	private String messageKey;
	
	
	public ApplicationException() {
		super();
	}

	public ApplicationException(String messageKey) {
		super();
		
		this.messageKey = messageKey;
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	
	public String getMessageKey() {
		return messageKey;
	}
	
	
}
