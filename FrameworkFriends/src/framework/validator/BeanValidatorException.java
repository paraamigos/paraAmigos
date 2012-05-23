package framework.validator;

/**
 * Exce&ccedil;&atilde;o para valida&ccedil;&atilde;o de Beans 
 * 
 * @author DBA Eng. de Sistemas
 * @since 03/09/2011
 */
public class BeanValidatorException extends RuntimeException {

	private static final long serialVersionUID = -6971858063800282630L;

	private BeanErrors errors;
	
	private String labelFieldKey;
	private String messageKey;
	
	
	
	/**
	 * Construtor default
	 */
	public BeanValidatorException() {
		super();
	}

	
	/**
	 * Construtor com erros de valida��o de um bean
	 * 
	 * @param errors erros de valida��o de um bean
	 */
	public BeanValidatorException(BeanErrors errors) {
		super();
		this.errors = errors;
	}


	/**
	 * Construtor com chave da mensagem de erro
	 * 
	 * @param messageKey chave da mensagem de erro
	 */
	public BeanValidatorException(String messageKey) {
		super();
		
		this.messageKey = messageKey;
	}


	/**
	 * Construtor com chave da mensagem de erro
	 * 
	 * @param messageKey 	chave da mensagem de erro
	 * @param labelFieldKey chave do label ou nome do campo
	 */
	public BeanValidatorException(String messageKey, String labelFieldKey) {
		super();
		
		this.labelFieldKey	= labelFieldKey;
		this.messageKey 	= messageKey;
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

	/**
	 * Retorna os erros de valida��o de um bean
	 * 
	 * @return os erros de valida��o de um bean
	 */
	public BeanErrors getErrors() {
		return errors;
	}
	
	
}





