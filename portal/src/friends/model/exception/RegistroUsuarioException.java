package friends.model.exception;

public class RegistroUsuarioException extends Exception {

	private static final long serialVersionUID = 2052329915150182954L;
	

	public RegistroUsuarioException() {
		super();
	}

	public RegistroUsuarioException(String message) {
		super(message);
	}

	public RegistroUsuarioException(Throwable cause) {
		super(cause);
	}

	public RegistroUsuarioException(String message, Throwable cause) {
		super(message, cause);
	}

}
