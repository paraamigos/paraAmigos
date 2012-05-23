package framework.model.service;

/**
 * Esta classe contem parametros para queries customizadas 
 * a serem utilizadas em classes de servi�o do framework
 * ou da aplica��o
 * 
 * @author Luiz Alberto
 *
 */
public class QueryParameters extends SearchParameters {

	private Object[] parameters;
	private String countQuery;
	private String query;
	
	
	/**
	 * Retorna os parametros da query
	 * 
	 * @return os parametros da query
	 */
	public Object[] getParameters() {
		return parameters;
	}
	
	/**
	 * Define os parametros da query 
	 * 
	 * @param parameters os parametros da query
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Contem a query que retorna o total de registros
	 * 
	 * @return query que retorna o total de registros
	 */
	public String getCountQuery() {
		return countQuery;
	}
	
	/**
	 * Define a query que retorna o total de registros
	 * 
	 * @param countQuery query que retorna o total de registros
	 */
	public void setCountQuery(String countQuery) {
		this.countQuery = countQuery;
	}
	
	/**
	 * Retorna a query que retorna as entidades
	 * 
	 * @return a query que retorna as entidades
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Define a query que retorna as entidades
	 * 
	 * @param query a query que retorna as entidades
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Retorna tipo de parametriza��o de query
	 * 
	 * @return tipo de parametriza��o
	 */
	@Override
	public boolean isQueryByExample() {
		return false;
	}
	
	
	
	
}
