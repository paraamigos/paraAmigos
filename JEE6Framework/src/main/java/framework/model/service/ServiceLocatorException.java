package framework.model.service;

/**
 * Classe de exce��o para erros em Locator
 * @author  Luiz Alberto
 */
public class ServiceLocatorException extends RuntimeException {

	private static final long serialVersionUID = 8875196236017511511L;

	/**
     * Inicializa a classe com a mensagem de erro
     * 
     * @param message mensagem de erro
     */
    public ServiceLocatorException(String message) {
        super(message);
    }
    
    /**
     * Inicializa a classe com a mensagem de erro
     * 
     * @param exception erro ocorrido
     */
    public ServiceLocatorException(Exception exception) {
        super(exception);
    }
    
    
}