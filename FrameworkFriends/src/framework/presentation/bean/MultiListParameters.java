package framework.presentation.bean;

import java.util.ArrayList;
import java.util.List;

import framework.model.service.SearchParameters;

/**
 * Esta classe guarda os par�metros de uma consulta 
 * para uma tela multi-lista  
 * 
 * @author Luiz Alberto
 * 
 */
@SuppressWarnings("rawtypes")
public class MultiListParameters {

	private List resultList = new ArrayList<Object>();
	private SearchParameters queryParams;
	private int currentPage;
	private int count;
	
	
	/**
	 * Incrementa a pagina corrente 
	 * 
	 */
	public void incrementCurrentPage() {
		currentPage++;
		queryParams.setOffset(String.valueOf(currentPage - 1));
	}

	/**
	 * Decrementa a pagina corrente 
	 * 
	 */
	public void decrementCurrentPage() {
		currentPage--;
		queryParams.setOffset(String.valueOf(currentPage - 1));
	}
	
	/**
	 * Retorna a p�gina corrente da barra de pagina��o
	 * 
	 * @return p�gina corrente
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	/**
	 * Define a p�gina corrente da barra de pagina��o
	 * 
	 * @param currentPage a p�gina corrente da barra de pagina��o
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * Retorna o total de registros da consulta
	 * 
	 * @return total de registros
	 */
	public int getCount() {
		return count;
	}
	/**
	 * Define o total de registros da consulta
	 * 
	 * @param count total de registros
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * Retorna lista de resultados
	 * 
	 * @return lista de resultados
	 */
	public List getResultList() {
		return resultList;
	}
	/**
	 * Define a lista de resultados
	 * 
	 * @param resultList lista de resultados
	 */
	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
	/**
	 * Retorna os par�metros da query
	 * 
	 * @return par�metros da query
	 */
	public SearchParameters getQueryParams() {
		return queryParams;
	}
	/**
	 * Define os par�metros da query
	 * 
	 * @param queryParams par�metros da query
	 */
	public void setQueryParams(SearchParameters queryParams) {
		this.queryParams = queryParams;
	}

	
	
	
}
