package framework.exception;

public class BusinessValidationException extends Exception {

	private static final long serialVersionUID = 8925380135828515222L;

	private String messageKey;


	/**
	 * Construtor com chave da mensagem de erro
	 * 
	 * @param messageKey chave da mensagem de erro
	 */
	public BusinessValidationException(String messageKey) {
		super();
		
		this.messageKey = messageKey;
	}
	
	/**
	 * Retorna a chave da mensagem de erro
	 * 
	 * @return chave da mensagem de erro
	 */
	public String getMessageKey() {
		return messageKey;
	}

}
