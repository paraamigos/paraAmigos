package framework.model.service;

import framework.application.ApplicationException;

/**
 * Classe para tratamento e lan�amento de exce��es
 * ocorridas em um CRUDService.
 * 
 * @author Luiz Alberto
 *
 */
public class ApplicationServiceException extends ApplicationException {

	private static final long serialVersionUID = -516704164148779358L;
	
	

	/**
	 * Inicializa a classe com a mensagem da exce��o ocorrida
	 * 
	 * @param message mensagem da exce��o ocorrida
	 */
	public ApplicationServiceException(String messageKey) {
		super(messageKey);
	}

	/**
	 * Inicializa a classe com a exce��o ocorrida
	 * 
	 * @param cause exce��o ocorrida
	 */
	public ApplicationServiceException(Throwable cause) {
		super(cause);
	}


}
