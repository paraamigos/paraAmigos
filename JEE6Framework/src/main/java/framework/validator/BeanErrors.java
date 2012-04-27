package framework.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Armazena todos os erros de valida��o 
 * de um bean
 * 
 * @author DBA Eng. de Sistemas
 *
 */
public class BeanErrors {

	private List<BeanError> errors = new ArrayList<BeanError>();
	
	
	/**
	 * Retorna se existem erros de valida��o
	 * 
	 * @return se existem erros de valida��o
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	/**
	 * Adiciona um erro de valida��o de bean
	 * 
	 * @param error erro de valida��o de bean
	 */
	public void addError(BeanError error) {
		errors.add(error);
	}
	
	
	/**
	 * Retorna lista de erros de valida��o de um bean
	 * 
	 * @return lista de erros de valida��o de um bean
	 */
	public List<BeanError> getErrors() {
		return errors;
	}
}
