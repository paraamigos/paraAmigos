package framework.presentation.bean;

/**
 * Classe para tratamento e lan�amento de exce��es
 * ocorridas em um BaseCRUDBean.
 * 
 * @author Luiz Alberto
 *
 */
public class BeanException extends RuntimeException {

	private static final long serialVersionUID = 6381134277004004104L;
	
	

	/**
	 * Inicializa a classe com a mensagem da exce��o ocorrida
	 * 
	 * @param message mensagem da exce��o ocorrida
	 */
	public BeanException(String message) {
		super(message);
	}

	/**
	 * Inicializa a classe com a exce��o ocorrida
	 * 
	 * @param cause exce��o ocorrida
	 */
	public BeanException(Throwable cause) {
		super(cause);
	}


}
