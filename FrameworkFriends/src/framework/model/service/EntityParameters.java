package framework.model.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe contem parametros de para queries customizadas 
 * a serem utilizadas em classes de servi�o do framework ou da aplica��o. 
 * O estilo de consulta � similar ao QBE do hibernate (Query by Example)
 * 
 * @author Luiz Alberto
 *
 */
public class EntityParameters extends SearchParameters {

	private List<String> orderList = new ArrayList<String>();
	private Object entity;
	
	
	
	/**
	 * Retorna a lista de atributos do Entity Bean para orden��o
	 * 
	 * @return lista de atributos do Entity Bean para orden��o
	 */
	public List<String> getOrderList() {
		return orderList;
	}
	/**
	 * Define a lista de atributos do Entity Bean para orden��o
	 * 
	 * @param orderList lista de atributos do Entity Bean para orden��o
	 */
	public void setOrderList(List<String> orderList) {
		this.orderList = orderList;
	}
	/**
	 * Retorna o Entity Bean que serve de crit�rio para busca
	 * 
	 * @return Entity Bean
	 */
	public Object getEntity() {
		return entity;
	}
	/**
	 * Define o Entity Bean que serve de crit�rio para busca 
	 * 
	 * @param entity Entity Bean que serve de crit�rio para busca
	 */
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	
	/**
	 * Retorna tipo de parametriza��o de query
	 * 
	 * @return tipo de parametriza��o
	 */
	@Override
	public boolean isQueryByExample() {
		return true;
	}
	
	
}
