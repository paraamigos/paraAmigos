package framework.model.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import framework.annotation.SearchField;

/**
 * Classe utilit�ria para constru��o din�mica
 * de JPA-QL queries
 *  
 * @author Luiz Alberto
 *
 */
public class QueryBuilderUtil {

	private StringBuilder whereClause = new StringBuilder();
	private List<Object> parameters;
	private Object entity;
	
	
	/**
	 * Constroi dinamicamente a JPA-QL query
	 * 	  
	 * @return JPA-QL query
	 * @throws Exception em caso de erro
	 */
	public String buildWhereClause() throws Exception {
		boolean whereAppended = false;
		
		for (Field field : entity.getClass().getDeclaredFields()) {
			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof SearchField) {
					field.setAccessible(true);
					whereAppended = appendWhereClauseParams(whereAppended,field,annotation);
				}
			}
		}
		return whereClause.toString();
	}

	/**
	 * Constroi a clausula "order by" da query de consulta.
	 * 
	 * @return clausula "order by".  
	 */
	public String buildOrderByClause() {
		Map<Integer,String> order	= new TreeMap<Integer,String>();
		StringBuilder query 		= new StringBuilder();
		
		for (Field field : entity.getClass().getDeclaredFields()) {
			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof SearchField){
					SearchField pesquisar = (SearchField) annotation;
					//Verifica se � diferente de 0 se for diferente � pq tem ordem
					if (pesquisar.ordem() != 0) {
						boolean	isEmptyField = pesquisar.campo() == null || "".equals(pesquisar.campo()); 
						String 	campo 		 = isEmptyField ? field.getName() : pesquisar.campo();  
						
						order.put(pesquisar.ordem(), campo);
					}
				}
			}
		}
		Set<Entry<Integer,String>>entrySet	= order.entrySet();
		boolean 				  flag 		= false;
		
		for (Entry<Integer,String> entry : entrySet) {
			if (flag) {
				query.append(" , o.");
			} else {
				query.append(" order by o.");
				flag = true;
			}
			query.append(entry.getValue());
		}
		return query.toString();
	}
	
	/**
	 * Processa os appends da clausura where da query
	 * 
	 * @param whereAppended se clausula where foi apendada	
	 * @param field			campo do form obtido por reflection
	 * @param annotation	anota��o do campo do form
	 * @return se clausula where foi apendada
	 * @throws IllegalArgumentException em caso de erro
	 * @throws IllegalAccessException	em caso de erro
	 */
	private boolean appendWhereClauseParams(boolean whereAppended,Field field,Annotation annotation)
			throws IllegalArgumentException, IllegalAccessException {
		boolean wasWhereAppended	= whereAppended;
		Object fieldValue 			= field.get(entity);
		
		if (fieldValue != null && !"".equals(fieldValue.toString())) {
			SearchField searchField = (SearchField) annotation;
			
			//Verifica se esta passando pela primeira vez
			whereClause.append(wasWhereAppended ? SearchField.AND_CLAUSE : SearchField.WHERE_CLAUSE);
			wasWhereAppended = true;
			
			//Verifica se existe anotacao com o nome do campo, 
			//se nao existir utiliza o proprio nome do atributo como padrao
			//Verifica tambem se � uppercase
			boolean campoIsEmpty = searchField.campo() == null || "".equals(searchField.campo());
			String  campo 		 = campoIsEmpty ? field.getName() : searchField.campo();  

			whereClause.append(searchField.ignoreCase() ? "UPPER(o."+campo+") " : "o."+ campo + " ");
			
			String operator	= searchField.operador();
			boolean isLike 	= SearchField.LIKE.equals(operator) || SearchField.LIKE_ESQUERDA.equals(operator);
			fieldValue 		= isLike ? "%" + fieldValue : fieldValue;
			fieldValue 		= searchField.ignoreCase() ? fieldValue.toString().toUpperCase() : fieldValue;
			isLike 	   		= operator.equals(SearchField.LIKE) || operator.equals(SearchField.LIKE_DIREITA); 
			fieldValue 		= isLike ? fieldValue + "%" : fieldValue;
			
			boolean isOpNotNull 	= operator != null && !"".equals(operator);
			boolean isLikeLefRight	= SearchField.LIKE_DIREITA.equals(operator)||SearchField.LIKE_ESQUERDA.equals(operator);
			operator				= isLikeLefRight ? SearchField.LIKE : operator;
				
			whereClause.append(isOpNotNull ? operator : SearchField.IGUAL);
			whereClause.append(" ? ");
			
			parameters.add(fieldValue);
		}
		return wasWhereAppended;
	}
	
	
	/**
	 * Define o objeto a partir do qual ser� montada a query
	 * 
	 * @param entity objeto a partir do qual ser� montada a query
	 */
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	
	/**
	 * Define o atributo que cont�m os par�metros da query
	 * 
	 * @param parameters atributo que cont�m os par�metros da query
	 */
	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}
	
	
	
	
}
