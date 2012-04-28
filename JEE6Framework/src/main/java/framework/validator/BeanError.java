package framework.validator;

/**
 * Armazena um erro de valida��o 
 * de um bean
 * 
 * @author DBA Eng. de Sistemas
 *
 */
public class BeanError {

	private String labelFieldKey;
	private String messageKey;

	
	

	/**
	 * Construtor com chave da mensagem de erro
	 * 
	 * @param messageKey chave da mensagem de erro
	 */
	public BeanError(String messageKey) {
		super();
		this.messageKey = messageKey;
	}

	/**
	 * Construtor com chave da mensagem de erro 
	 * e chave do label do campo 
	 * 
	 * @param labelFieldKey chave do label ou nome do campo
	 * @param messageKey 	chave da mensagem de erro
	 */
	public BeanError(String labelFieldKey, String messageKey) {
		super();
		this.labelFieldKey = labelFieldKey;
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


	/**
	 * Retorna a chave do label ou nome do campo
	 * 
	 * @return chave do label ou nome do campo
	 */
	public String getLabelFieldKey() {
		return labelFieldKey;
	}

	
}
