package framework.model.service;


import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import framework.annotation.SearchBean;
import framework.reflection.BeansUtil;

/**
 * Esta classe auxilia na implementa��o dos metodos b�sicos de CRUD,
 * para Application Services com suporte a seam. 
 * Deve ser estendida pelos Services da aplica��o 
 * 
 * @author Luiz Alberto
 *
 */
public class BaseApplicationService implements Serializable {

	private static final long serialVersionUID 				= 258966534209860744L;
	public static final String SEARCH_SELECT_COUNT_CLAUSE	= "select count(o) ";
	public static final String SEARCH_SELECT_CLAUSE 		= "select o ";
	public static final String PARAMETERS					= "PARAMETERS";
	public static final String QUERY						= "QUERY";
	private static final int   FIRST 						= 0;
	
	@PersistenceContext
	protected EntityManager entityManager;

	private QueryParameters queryParams;
	private Object entity;
	private int count;
	
	
	/**
	 * Atualiza um Entity Bean no banco
	 * 
	 * @param entity Entity Bean
	 */
	public void update(Object entity) {
		try {
			entityManager.merge(entity);
			entityManager.flush();
			
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	/**
	 * Deleta uma entidade da base de dados
	 * 
	 * @param entity Entidade a ser deletada
	 */
	public void remove(Object entity) {
		try {
			entityManager.remove(entity);
			entityManager.flush();
			
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	/**
	 * Persiste um Entity Bean no banco
	 * 
	 * @param entity Entity Bean
	 */
	public void save(Object entity) {
		try {
			entityManager.persist(entity);
			entityManager.flush();
			
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}

	/**
	 * M�todo utilizado para pesquisas atrav�s de CRUDBean
	 * 
	 * @param 	<T>		tipo parametrizado
	 * @param	entity	Entity Bean	 
	 * @param	offset	p�gina do banco
	 * @return			lista contendo todos os registros da entidade
	 */
	public <T> List<T> search(Object entity, String offset) {
		setEntity(entity);	
		
		return search(offset, null);
	}

	/**
	 * M�todo utilizado para pesquisas ordenadas atrav�s de CRUDBean
	 * 
	 * @param <T>				tipo parametrizado
	 * @param entity			Entity Bean
	 * @param offset			p�gina do banco
	 * @param orderByProperty	propriedade do Entity Bean pela qual ser� ordenada a query	
	 * @return	lista contendo todos os registros da entidade
	 */
	public <T> List<T> search(Object entity, String offset, String orderByProperty) {
		setEntity(entity);	
		
		return search(offset, orderByProperty);
	}
	
	/**
	 * M�todo utilizado para pesquisas atrav�s de CRUDBean
	 *  
	 * @param 	<T> 			tipo parametrizado 
	 * @param	paramOffSet		p�gina do banco
	 * @param 	orderByProperty propriedade do Entity Bean pela qual ser� ordenada a query
	 * @return	lista contendo todos os registros da entidade
	 */
	public <T> List<T> search(String paramOffSet, String orderByProperty) {
		Map<Object,Object>query	= getSearchQuery(orderByProperty);
		Object[] params 		= (Object[])query.get(PARAMETERS);
		String jpaql			= (String) query.get(QUERY);
		boolean paging			= isPaging();
		List<T> resultList		= null;
		
		if (paging) {
			int	pageSize 	= getPagingSize();
			int offset 		= 1;
			
			if (paramOffSet != null) {
				offset 	= Integer.parseInt(paramOffSet) + 1;
			} else {
				Map<Object,Object>queryCount = getSearchCountQuery();
				Object[]	paramsCount		 = (Object[])queryCount.get(PARAMETERS);
				String 		sqlCount 		 = (String)queryCount.get(QUERY);
				List<Object>result			 = findByQuery(sqlCount, paramsCount);
				count 						 = Integer.parseInt(result.get(FIRST).toString());
			}
			int startIndex	= ((offset - 1) * pageSize) + 1;
			int endIndex	= ((offset - 1) * pageSize) + pageSize;
			resultList 		= findByQuery(jpaql, params, startIndex, endIndex);
			
		} else {
			resultList = findByQuery(jpaql, params);	
		}
		return resultList;
	}

	/**
	 * M�todo utilizado para pesquisas customizadas
	 * 
	 * @param 	<T> tipo parametrizado
	 * @param 	queryParams par�metros da query
	 * @return	lista contendo todos os registros da entidade
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> search(SearchParameters queryParams) {
		return	(List<T>) (
					queryParams.isQueryByExample() ? 
					searchByExample((EntityParameters)queryParams) :
					search((QueryParameters)queryParams)
				);
	}
	/**
	 * M�todo utilizado para pesquisas customizadas
	 * 
	 * @param 	<T> tipo parametrizado
	 * @param 	queryParams par�metros da query
	 * @return	lista contendo todos os registros da entidade
	 */
	public <T> List<T> searchByExample(EntityParameters queryParams) {
		boolean paging			= queryParams.isPaging();
		String paramOffSet		= queryParams.getOffset();
		Object entity			= queryParams.getEntity();
		List<String>orderList	= queryParams.getOrderList();
		List<T> resultList		= null;
		
		if (paging) {
			int	pageSize 	= queryParams.getPagingSize();
			int offset 		= 1;
			
			if (paramOffSet != null) {
				offset 	= Integer.parseInt(paramOffSet) + 1;
			} else {
				count = countByExample(entity);
			}
			int startIndex	= ((offset - 1) * pageSize) + 1;
			int endIndex	= ((offset - 1) * pageSize) + pageSize;
			resultList 		= findByExample(entity, orderList, startIndex, endIndex);
			
		} else {			
			resultList = findByExample(entity, orderList);	
		}
		return resultList;
	}
	
	/**
	 * M�todo utilizado para pesquisas customizadas
	 * 
	 * @param 	<T> tipo parametrizado
	 * @param 	queryParams par�metros da query
	 * @return	lista contendo todos os registros da entidade
	 */
	public <T> List<T> search(QueryParameters queryParams) {
		this.queryParams		= queryParams;
		Object[] params 		= queryParams.getParameters();
		String jpaql			= queryParams.getQuery();
		boolean paging			= queryParams.isPaging();
		String paramOffSet		= queryParams.getOffset();
		List<T> resultList		= null;
		
		if (paging) {
			int	pageSize 	= queryParams.getPagingSize();
			int offset 		= 1;
			
			if (paramOffSet != null) {
				offset 	= Integer.parseInt(paramOffSet) + 1;
			} else {
				Object[]	paramsCount		 = queryParams.getParameters();
				String 		sqlCount 		 = queryParams.getCountQuery();
				List<Object>result			 = findByQuery(sqlCount, paramsCount);
				count 						 = Integer.parseInt(result.get(FIRST).toString());
			}
			int startIndex	= ((offset - 1) * pageSize) + 1;
			int endIndex	= ((offset - 1) * pageSize) + pageSize;
			resultList 		= findByQuery(jpaql, params, startIndex, endIndex);
			
		} else {
			resultList = findByQuery(jpaql, params);	
		}
		return resultList;
	}

//	/**
//	 * M�todo utilizado para pesquisas customizadas e multi-listas
//	 * 
//	 * @param queryParams par�metros da query
//	 */
//	public void searchMultiList(EntityParameters queryParams) {
//		multiListParams 		= new MultiListParameters();
//		List<String>orderList	= queryParams.getOrderList();
//		String paramOffSet		= queryParams.getOffset();
//		boolean paging			= queryParams.isPaging();
//		Object entity			= queryParams.getEntity();
//		List resultList			= null;
//		
//		if (paging) {
//			int	pageSize 	= queryParams.getPagingSize();
//			int offset 		= 1;
//			
//			if (paramOffSet != null) {
//				offset 	= Integer.parseInt(paramOffSet) + 1;
//			} else {
//				count = countByExample(entity);
//			}
//			int startIndex	= ((offset - 1) * pageSize) + 1;
//			int endIndex	= ((offset - 1) * pageSize) + pageSize;
//			resultList 		= findByExample(entity, orderList, startIndex, endIndex);
//			
//		} else {
//			resultList = findByExample(entity, orderList);	
//		}
//		multiListParams.setQueryParams(queryParams);
//		multiListParams.setResultList(resultList);
//	}
	
	/**
	 * Realiza consulta atrav�s de QBE (Query By Example)
	 * 
	 * @param <T>			tipo parametrizado
	 * @param entity		Entity Bean de exemplo com par�metros para query
	 * @param orderList		lista de atributos pelos quais ser� ordenada a query
	 * @param startIndex	�ndice inicial da pagina��o
	 * @param endIndex		�ndice final da pagina��o
	 * @return				lista contendo o resultado da query
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByExample(Object entity,List<String>orderList,int startIndex,int endIndex) {
		Criteria criterio	= createCriteria(entity.getClass());
		Example example 	= createExample(entity);
    	List result 		= null;
		
		criterio.add(example);
    	
        if (startIndex < 1) {
            startIndex = 1;
        }
        if ((endIndex - startIndex) < 0) {
            result = new ArrayList();
        } else {
        	criterio.setMaxResults(endIndex - startIndex + 1);
        	criterio.setFirstResult(startIndex - 1);
        	
        	if (orderList != null) {
        		for (String propertyName : orderList) {
        			criterio.addOrder(Order.asc(propertyName));
        		}
    		}
        	result = criterio.list();
        }
		return result;
	}

	/**
	 * Realiza consulta atrav�s de QBE (Query By Example)
	 * 
	 * @param <T>			tipo parametrizado
	 * @param entity		Entity Bean de exemplo com par�metros para query
	 * @param orderList		lista de atributos pelos quais ser� ordenada a query
	 * @return				lista contendo o resultado da query
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByExample(Object entity, List<String>orderList) {
		Criteria criterio	= createCriteria(entity.getClass());
		Example example 	= createExample(entity);
		
		criterio.add(example);

    	if (orderList != null) {
    		for (String propertyName : orderList) {
    			criterio.addOrder(Order.asc(propertyName));
    		}
		}
		return criterio.list();
	}
	
	/**
	 * Obtem o count atraves de QBE
	 * 
	 * @param 	entity Entity Bean
	 * @return	total de registros da query
	 */
	protected int countByExample(Object entity) {
		Criteria criterio	= createCriteria(entity.getClass());
		Example example 	= createExample(entity);
		
		criterio.setProjection(
			Projections.projectionList().add(
				Projections.count(getEntityIdPropertyName(entity))
			)
		);
		criterio.add(example);
		
		List result = criterio.list();
		
		return 	!result.isEmpty() ? 
				Integer.parseInt(result.get(FIRST).toString()) :
				0;	
	}
	
	/**
	 * Retorna example a partir de um Entity Bean
	 * 
	 * @param	entity Entity Bean
	 * @return	example a partir de um Entity Bean
	 */
	private Example createExample(Object entity) {
		Example	example	= Example.create(entity);
		example.ignoreCase();
		example.enableLike(MatchMode.ANYWHERE);
		
		return example;
	}

//	/**
//	 * M�todo utilizado para pesquisas customizadas e multi-listas
//	 * 
//	 * @param queryParams par�metros da query
//	 */
//	public void searchMultiList(QueryParameters queryParams) {
//		multiListParams 	= new MultiListParameters();
//		this.queryParams	= queryParams;
//		Object[] params 	= queryParams.getParameters();
//		String jpaql		= queryParams.getQuery();
//		boolean paging		= queryParams.isPaging();
//		String paramOffSet	= queryParams.getOffset();
//		List resultList		= null;
//		
//		if (paging) {
//			int	pageSize 	= queryParams.getPagingSize();
//			int offset 		= 1;
//			
//			if (paramOffSet != null) {
//				offset 	= Integer.parseInt(paramOffSet) + 1;
//			} else {
//				Object[]	paramsCount		 = queryParams.getParameters();
//				String 		sqlCount 		 = queryParams.getCountQuery();
//				List<Object>result			 = findByQuery(sqlCount, paramsCount);
//				count 						 = Integer.parseInt(result.get(FIRST).toString());
//			}
//			int startIndex	= ((offset - 1) * pageSize) + 1;
//			int endIndex	= ((offset - 1) * pageSize) + pageSize;
//			resultList 		= findByQuery(jpaql, params, startIndex, endIndex);
//			
//		} else {
//			resultList = findByQuery(jpaql, params);	
//		}
//		multiListParams.setQueryParams(queryParams);
//		multiListParams.setResultList(resultList);
//	}
	
	/**
	 * Recupera a query de consulta com base nas anota��es de pesquisa.
	 * 
	 * @param orderByProperty propriedade do Entity Bean pela qual ser� ordenada a query
	 * @return SQL de pesquisa com os filtros apropriados.  
	 */
	public Map<Object,Object> getSearchQuery(String orderByProperty) {
		try {
			Map<Object,Object> searchQuery	= new HashMap<Object, Object>(); 
			List<Object>	parameters		= new ArrayList<Object>();
			StringBuilder 	query 			= new StringBuilder();
			
			query.append(SEARCH_SELECT_CLAUSE);
			query.append(getSearchQueryFromClause());
			query.append(getSearchQueryWhereClause(parameters));
			query.append(getSearchQueryOrderByClause(orderByProperty));
			
			searchQuery.put(PARAMETERS, parameters.toArray());
			searchQuery.put(QUERY, query.toString());

			return searchQuery;
			
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}

	/**
	 * Recupera a query para recuperar o total de registros com base nas anota��es de pesquisa.
	 * 
	 * @return SQL com os filtros apropriados.  
	 */
	public Map<Object,Object> getSearchCountQuery(){
		try {
			Map<Object,Object> searchQuery	= new HashMap<Object, Object>(); 
			List<Object>	parameters		= new ArrayList<Object>();
			StringBuilder 	query 			= new StringBuilder();
			
			query.append(SEARCH_SELECT_COUNT_CLAUSE);
			query.append(getSearchQueryFromClause());
			query.append(getSearchQueryWhereClause(parameters));

			searchQuery.put(PARAMETERS, parameters.toArray());
			searchQuery.put(QUERY, query.toString());
			
			return searchQuery;
			
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	/**
	 * Recupera a clausula "from" da query de consulta.
	 * 
	 * @return Clausula "from".  
	 */
	private String getSearchQueryFromClause() {
		return  "from " + getEntityName() + " o ";
	}

	/**
	 * Recupera o nome da entidade principal do bean.
	 * 
	 * @return Nome da entidade principal do bean definida na anota��o da classe.  
	 */
	public String getEntityName() {
		return entity.getClass().getSimpleName();
	}
	
	/**
	 * Recupera a clausula "where" da query de consulta.
	 * 
	 * @param 	parameters guarda os parametros para a query
	 * @return	clausula "where".  
	 */
	public String getSearchQueryWhereClause(List<Object> parameters) {
		QueryBuilderUtil queryBuilder = new QueryBuilderUtil();
		queryBuilder.setEntity(entity);
		queryBuilder.setParameters(parameters);

		try {
			return queryBuilder.buildWhereClause();
			
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	/**
	 * Recupera a clausula "order by" da query de consulta.
	 * 
	 * @param orderByProperty propriedade do Entity Bean pela qual ser� ordenada a query
	 * @return clausula "order by".  
	 */
	private String getSearchQueryOrderByClause(String orderByProperty) {
		String orderBy = "";

		if (orderByProperty != null) {
			orderBy = " order by o." + orderByProperty; 
		} else {
			QueryBuilderUtil queryBuilder = new QueryBuilderUtil();
			queryBuilder.setEntity(entity);
			
			orderBy = queryBuilder.buildOrderByClause();
		}
		return orderBy;
	}
	
	/**
	 * Localiza todos os registros de uma determinado objeto de forma ordenada
	 * 
	 * @param <T>		Tipo parametrizado
	 * @param type		Tipo da classe a ser localizada 
	 * @param orderBy	Atributo pelo qual serao ordenados os registros
	 * @return			Lista ordenada contendo todos os registros de uma determinado objeto 
	 */
	public <T> List<T> findAll(Class<T> type, String orderBy) {
		StringBuilder query =	new StringBuilder("select o from ")
								.append(type.getSimpleName())
								.append(" o ");
		
		String order = 	orderBy != null  && !"".equals(orderBy.trim()) ? 
						" order by o." + orderBy : "";
		
		query.append(order);
		
		return findByQuery(query.toString(), null);
	}
	
	/**
	 * Localiza todos os registros de uma determinado objeto de forma ordenada
	 * 
	 * @param <T>		Tipo parametrizado
	 * @param type		Tipo da classe a ser localizada 
	 * @return			Lista ordenada contendo todos os registros de uma determinado objeto 
	 */
	public <T> List<T> findAll(Class<T> type) {
		StringBuilder query =	new StringBuilder("select o from ")
								.append(type.getSimpleName())
								.append(" o ");
		
		return findByQuery(query.toString(), null);
	}

	
	/**
	 * Localiza os registros de uma determinado objeto de acordo com os parametros e query passados
	 * 
	 * @param <T>		Tipo parametrizado
	 * @param jpaql		Query contendo a senten�a JPA-QL
	 * @param params	Par�metros para a query
	 * @return			Lista contendo os registros encontrados 
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByQuery(String jpaql, Object[] params) {
		Query query = entityManager.createQuery(jpaql);
	
		setParameters(params, query);
		
		return query.getResultList();
	}

	/**
	 * Localiza os registros de uma determinado objeto de acordo com os parametros e query passados
	 * 
	 * @param <T>			tipo parametrizado
	 * @param jpaql			query contendo a senten�a JPA-QL
	 * @param params		par�metros para a query
	 * @param startIndex	�ndice inicial da pagina��o
	 * @param endIndex		�ndice final da pagina��o
	 * @return				Lista contendo os registros encontrados
	 */
    @SuppressWarnings("unchecked")
	public <T> List<T> findByQuery(String jpaql,Object[] params,int startIndex,int endIndex) {
    	List result = null;
    	
        if (startIndex < 1) {
            startIndex = 1;
        }
        
        if ((endIndex - startIndex) < 0) {
            result = new ArrayList();
        } else {
        	Query query = entityManager.createQuery(jpaql);
        	
        	setParameters(params, query);
        	query.setMaxResults(endIndex - startIndex + 1);
        	query.setFirstResult(startIndex - 1);
        	
        	result = query.getResultList();
        }
        return result;
    }

    /**
     * Define os par�metros da query
     * 
     * @param params par�metros da query
     * @param query  query aonde os par�metros ser�o definidos
     */
	private void setParameters(Object[] params, Query query) {
		if (params != null && params.length > 0) {
			int length = params.length;
			
			for (int i = 0; i < length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
	}
    
	/**
	 * Localiza os registros de uma determinado objeto de acordo com os parametros e query passados
	 * 
	 * @param <T>		Tipo parametrizado
	 * @param name		Named Query que contem a senten�a JPA-QL
	 * @param params	Par�metros para a query
	 * @return			Lista contendo os registros encontrados 
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByNamedQuery(String name, Object[] params) {
		Query q = entityManager.createNamedQuery(name);
	
		if (params != null && params.length > 0) {
			int length = params.length;
			
			for (int i = 0; i < length; i++) {
				q.setParameter(i + 1, params[i]);
			}
		}
		return q.getResultList();
	}

	/**
	 * Obtém os dados de um unico EntityBean 
	 * de acordo com os parametros e query passados
	 * 
	 * @param name		named query que contem a sentença JPA-QL
	 * @param params	parâmetros para a query
	 * @return			EntityBean encontrado
	 */
	public Object findByNamedQueryOneResult(String name, Object[] params) {
		Query query 	= entityManager.createNamedQuery(name);
		Object result	= null;

		try {
			setParameters(params, query);
			
			result = query.getSingleResult();

		} catch (NoResultException e) {
			result = null;
		}
		return result;
	}

	/**
	 * Obtém os dados de um unico EntityBean 
	 * de acordo com os parametros e query passados
	 * 
	 * @param  name			 named query que contem a sentença JPA-QL
	 * @param  params		 parâmetros para a query
	 * @param  setMaxResults se deve ser forçado numero maximo de registros para 1
	 * @return EntityBean encontrado
	 */
	public Object findByNamedQueryOneResult(String name, Object[] params, boolean setMaxResults) {
		Query query 	= entityManager.createNamedQuery(name);
		Object result	= null;

		try {
			setParameters(params, query);
		
			if (setMaxResults) {
				query.setMaxResults(1);
			}
			result = query.getSingleResult();

		} catch (NoResultException e) {
			result = null;
		}
		return result;
	}

	/**
	 * Obtém os dados de um unico EntityBean 
	 * de acordo com os parametros e query passados
	 * 
	 * @param jpaql		query contendo a sentença JPA-QL
	 * @param params	parâmetros para a query
	 * @return			EntityBean encontrado 
	 */
	public Object findByQueryOneResult(String jpaql, Object[] params) {
		Query query 	= entityManager.createQuery(jpaql);
		Object result	= null;

		try {
			setParameters(params, query);
			
			result = query.getSingleResult();

		} catch (NoResultException e) {
			result = null;
		}
		return result;
	}
	
	/**
	 * Localiza uma entidade pelo id
	 * 
	 * @param <T>		Tipo parametrizado
	 * @param classe	Classe da entidade a ser localizada
	 * @param id		id da entidade
	 * @return			Entidade encontrada
	 */
	public <T>T findObject(Class<T> classe, Object id) {
		return  entityManager.find(classe, id);
	}

	/**
	 * Localiza uma entidade 
	 * 
	 * @param <T>		tipo parametrizado
	 * @param entity	Entity Bean 
	 * @return			Entidade encontrada
	 */
	@SuppressWarnings("unchecked")
	public <T>T findObject(Object entity) {
		try {
			Object id = getEntityId(entity);
			
			return (T) entityManager.find(entity.getClass(), id);
			
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
		
	}

	/**
	 * Localiza o id do Entity Bean
	 * 
	 * @param entity	Entity Bean
	 * @return			id do Entity Bean
	 */
	public Object getEntityId(Object entity) {
		return BeansUtil.getEntityId(entity);
	}
	
	/**
	 * Localiza o nome da propriedade id do Entity Bean
	 * 
	 * @param entity	Entity Bean
	 * @return			nome da propriedade id
	 */
	public String getEntityIdPropertyName(Object entity) {
		String propertyName = null;
		
		try {
			for (Field field : entity.getClass().getDeclaredFields()) {
				for (Annotation annotation : field.getAnnotations()) {
					if (annotation instanceof Id) {
						propertyName = field.getName();
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationServiceException(e);
		}
		return propertyName;
	}
	
	/**
	 * Pega o tamanho da paginacao.
	 * 
	 * @return Quantidade de registros por pagina.  
	 */
	public int getPagingSize() {
		int pagingSize = 0;
		
		for (Annotation annotation : entity.getClass().getAnnotations()) {
			if(annotation instanceof SearchBean){
				pagingSize = ((SearchBean) annotation).pagingSize();
				break;
			}
		}
		return pagingSize;
	}

	/**
	 * Verifica se e paginado.
	 * 
	 * @return True se tiver paginacao e false se nao tiver.  
	 */
	public boolean isPaging() {
		boolean paging = true;
		
		for (Annotation annotation : entity.getClass().getAnnotations()) {
			if(annotation instanceof SearchBean){
				paging = ((SearchBean) annotation).isPaging();
				break;
			}
		}
		return paging;
	}

	/**
	 * Define o Entity Bean
	 * 
	 * @param entity Entity Bean
	 */
	public void setEntity(Object entity) {
		this.entity = entity;
	}

	/**
	 * Retorna o total de registros da consulta
	 * 
	 * @return o total de registros da consulta
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Define o total de registros da consulta
	 * 
	 * @param count o total de registros da consulta
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Retorna os par�metros da query
	 * 
	 * @return par�metros da query
	 */
	public QueryParameters getQueryParams() {
		return queryParams;
	}
	
//	/**
//	 * Retorna os par�metros de multi-lista 
//	 * 
//	 * @return par�metros de multi-lista
//	 */
//	public MultiListParameters getMultiListParams() {
//		return multiListParams;
//	}

	/**
	 * Retorna query por criterio
	 * 
	 * @param  classe Entity Bean Class
	 * @return query por criterio
	 */
	protected Criteria createCriteria(Class<?> classe) {
		Session session = (Session) entityManager.getDelegate();
		
		return session.createCriteria(classe);
	}
	
	/**
	 * Retorna a classe do Entity Bean
	 * 
	 * @return classe do Entity Bean
	 */
	public Class<?> getEntityClass() {
		return entity.getClass();
	}

	/**
	 * Set entityManager
	 * 
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	
}












