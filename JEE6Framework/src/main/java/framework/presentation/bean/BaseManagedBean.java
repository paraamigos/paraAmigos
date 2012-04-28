package framework.presentation.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.apache.commons.beanutils.PropertyUtils;

import framework.model.service.ApplicationServiceException;
import framework.model.service.DefaultApplicationService;
import framework.model.service.QueryParameters;
import framework.presentation.faces.FacesUtil;

/**
 * Esta classe � a base do bean de implementa��o dos metodos b�sicos de CRUD,
 * para beans com suporte a seam. 
 * 
 * @author Luiz Alberto
 * 
 */
@SuppressWarnings("rawtypes")
public class BaseManagedBean<E> extends BaseBean {

	public static final String ORDER_BY_PROPERTY	= "ORDER_BY_PROPERTY";
	public static final String MULTI_LIST_PARAMS	= "MULTI_LIST_PARAMS";	
	public static final String SEARCH_ENTITY		= "SEARCH_ENTITY";
	public static final String QUERY_PARAMS			= "QUERY_PARAMS";
	public static final String QUERY_COUNT			= "QUERY_COUNT";
	public static final String OFFSET 				= "offset";
	

	@Inject
	protected DefaultApplicationService applicationService;
	
	protected int currentPage;
	protected String offset;


	
	/**
	 * Obtem lista de Select Items a partir do tipo de um EntityBean.
	 * Primeiramente o metodo busca todas as instancias do EntityBean 
	 * a partir da base de dados e em seguida popula a lista de Select Items.
	 * 
	 * @param  entityClass		tipo do EntityBean
	 * @param  orderByProperty	propriedade que será usada como order by na query
	 * @param  labelProperty	propriedade do EntityBean para label do Select
	 * @param  optionSelectKey	chave no messages para o label quando n&atilde;o houver item selecionado	
	 * @return lista de Select Items
	 */
	protected List<SelectItem> getSelectItemsOptionSelect(
				Class entityClass, 
				String orderByProperty, 
				String labelProperty,
				String optionSelectKey
			) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			List<SelectItem> listItems = getSelectItems(entityClass,orderByProperty,labelProperty);
			addOptionSelect(optionSelectKey, items, listItems);

			items.addAll(listItems);
			
		} catch (Exception e) {
			catchException(e);
		}
		return items;
	}

	/**
	 * Adiciona um SelectItem de op&ccedil;&atilde;o para Selecionar
	 * 
	 * @param optionSelectKey chave da op&ccedil;&atilde;o de sele&ccedil;&atilde;o no arquivo de mensagens
	 * @param items lista de SelectItems
	 * @param list  lista de Entity Beans
	 */
	private void addOptionSelect(String optionSelectKey, List<SelectItem> items, List list) {
		if (optionSelectKey != null && !list.isEmpty()) {
			String optionSelect = getMsgFromResourceBundle(optionSelectKey);
			SelectItem emptyItem = new SelectItem("", optionSelect);
			items.add(emptyItem);
		}
	}	

	/**
	 * Obtem lista de Select Items a partir do tipo de um EntityBean.
	 * Primeiramente o metodo busca todas as instancias do EntityBean 
	 * a partir da base de dados e em seguida popula a lista de Select Items.
	 * 
	 * @param  entityClass		tipo do EntityBean
	 * @param  orderByProperty	propriedade que será usada como order by na query
	 * @param  labelProperty	propriedade que exibirá o label dos itens
	 * @return lista de Select Items 
	 */
	protected List<SelectItem> getSelectItems(
				Class entityClass, 
				String orderByProperty,
				String labelProperty
			) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			List list = orderByProperty != null ?
						findAll(entityClass, orderByProperty) :
						findAll(entityClass);	
			populateSelectItems(labelProperty, items, list);
			
		} catch (Exception e) {
			catchException(e);
		}
		return items;
	}

	/**
	 * Obtem lista de Select Items a partir de uma query nomeada.
	 * Primeiramente o metodo busca todas as instancias do EntityBean 
	 * a partir da base de dados e em seguida popula a lista de Select Items.
	 * 
	 * @param  namedQuery	  	query nomeada
	 * @param  labelProperty	propriedade que exibirá o label dos itens
	 * @param  optionSelectKey	chave no messages para o label quando n&atilde;o houver item selecionado
	 * @return lista de Select Items
	 */
	protected List<SelectItem> getSelectItemsByNamedQuery(
						String namedQuery,String labelProperty,String optionSelectKey) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			List list = findByNamedQuery(namedQuery);	
	
			addOptionSelect(optionSelectKey, items, list);
			populateSelectItems(labelProperty, items, list);
			
		} catch (Exception e) {
			catchException(e);
		}
		return items;
	}

	/**
	 * Obtem lista de Select Items a partir de uma query nomeada.
	 * Primeiramente o metodo busca todas as instancias do EntityBean 
	 * a partir da base de dados e em seguida popula a lista de Select Items.
	 * 
	 * @param  namedQuery	  	query nomeada
	 * @param  params			parametros da query
	 * @param  labelProperty	propriedade que exibirá o label dos itens
	 * @param  optionSelectKey	chave no messages para o label quando n&atilde;o houver item selecionado
	 * @return lista de Select Items
	 */
	protected List<SelectItem> getSelectItemsByNamedQuery(
			String namedQuery, Object[] params, String labelProperty, String optionSelectKey) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			List list = findByNamedQuery(namedQuery, params);

			addOptionSelect(optionSelectKey, items, list);
			populateSelectItems(labelProperty, items, list);

		} catch (Exception e) {
			catchException(e);
		}
		return items;
	}
	
	/**
	 * Popula uma lista de SelecItem a partir de uma lista de EntityBeans
	 * 
	 * @param labelProperty propriedade que exibirá o label dos itens
	 * @param items 		lista de Select Items
	 * @param list			lista de EntityBeans
	 * @throws IllegalAccessException	 em caso de erro de reflection
	 * @throws InvocationTargetException em caso de erro de reflection
	 * @throws NoSuchMethodException	 em caso de erro de reflection
	 */
	private void populateSelectItems(String labelProperty,List<SelectItem>items, List list) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (!list.isEmpty()) {
			for (Object entity: list) {
				Object labelValue = PropertyUtils.getProperty(entity,labelProperty);
				String label 	  = labelValue != null ? labelValue.toString() : "";
				
				items.add(new SelectItem(entity,label));
			}
		}
	}
	
	/**
	 * Localiza entidades baseado numa query nomeada
	 * 
	 * @param <T>			Tipo parametrizado
	 * @param namedQuery	query nomeada
	 * @return lista contendo os registros retornados pela query nomeada
	 */
	private <T> List<T> findByNamedQuery(String namedQuery) {
		
		return applicationService.findByNamedQuery(namedQuery, null);
	}
	
	/**
	 * Localiza entidades baseado numa query nomeada
	 * 
	 * @param <T>			Tipo parametrizado
	 * @param namedQuery	query nomeada
	 * @param params		parametros da query
	 * @return lista contendo os registros retornados pela query nomeada
	 */
	private <T> List<T> findByNamedQuery(String namedQuery, Object[] params) {
		
		return applicationService.findByNamedQuery(namedQuery, params);
	}
	
	/**
	 * Localiza todos os registros de uma determinado objeto de forma ordenada
	 * 
	 * @param <T>		Tipo parametrizado
	 * @param type		Tipo da classe a ser localizada 
	 * @param orderBy	Atributo pelo qual serao ordenados os registros
	 * @return			Lista ordenada contendo todos os registros de uma determinado objeto 
	 */
	protected <T> List<T> findAll(Class<T> type, String orderBy) {
		
		return applicationService.findAll(type, orderBy);
	}

	/**
	 * Localiza todos os registros de uma determinado objeto de forma ordenada
	 * 
	 * @param <T>		Tipo parametrizado
	 * @param type		Tipo da classe a ser localizada 
	 * @return			Lista ordenada contendo todos os registros de uma determinado objeto 
	 */
	protected <T> List<T> findAll(Class<T> type) {
		
		return applicationService.findAll(type);
	}	
	
	/**
	 * Retorna a propriedade pela qual a lista de resultados ser� ordenada
	 * 
	 * @return propriedade pela qual a lista de resultados ser� ordenada
	 */
	protected String getSessionOrderByProperty() {
		return (String) getSessionAttribute(ORDER_BY_PROPERTY);
	}
	
	/**
	 * Seta na sess�o a propriedade pela qual a lista de resultados ser� ordenada
	 * 
	 * @param entityProperty propriedade pela qual a lista de resultados ser� ordenada
	 */
	protected void setSessionOrderByProperty(String entityProperty) {
		setSessionAttribute(entityProperty, ORDER_BY_PROPERTY);
	}
	
	/**
	 * Retorna a SEARCH_ENTITY
	 * 
	 * @return SEARCH_ENTITY
	 */
	@SuppressWarnings("unchecked")
	public E getSearchEntity() {
		return (E) getSessionAttribute(SEARCH_ENTITY);
	}

	
	/**
	 * Retorna a QueryParameters da sess�o
	 * 
	 * @return QueryParameters da sess�o
	 */
	protected QueryParameters getSessionQueryParameters() {
		return (QueryParameters)getSessionAttribute(QUERY_PARAMS);
	}

	/**
	 * Retorna mensagem do ResourceBundle 
	 * definido no faces-config.xml 
	 * 
	 * @param  messageKey
	 * @return mensagem do ResourceBundle
	 */
	protected String getMsgFromResourceBundle(String messageKey) {
		return FacesUtil.getResourceBundleMessage("msg", messageKey);
	}
	
	/**
	 * Adiciona a mensagem de sucesso default da aplica��o
	 * 
	 */
	protected void addMsgSuccessDefaultFromResource() {
		String message = getMsgFromResourceBundle("msg.operacao.sucess");
		
		FacesUtil.addMessage(message, FacesMessage.SEVERITY_INFO);
	}	

	/**
	 * Adiciona a mensagem de sucesso default da aplica��o
	 * 
	 */
	protected void addMsgUpdateDefaultFromResource() {
		String message = getMsgFromResourceBundle("msg.update.sucess");
		
		FacesUtil.addMessage(message, FacesMessage.SEVERITY_INFO);
	}	
	
	/**
	 * Trata o erro e adiciona a mensagem default de erro de 
	 * servi�o / transa��o da aplica��o 
	 * 
	 * @param appEx cont�m o erro de servi�o da aplica��o
	 */
	protected void catchApplicationException(ApplicationServiceException appEx) {
		appEx.printStackTrace();
		
		String message = getMsgFromResourceBundle("msg.err.geral.aplicacao");
		
		FacesUtil.addMessage(message, FacesMessage.SEVERITY_ERROR);
	}

	/**
	 * Trata o erro e adiciona a mensagem default de erro de 
	 * servi�o / transa��o da aplica��o 
	 * 
	 * @param ex cont�m o erro de servi�o da aplica��o
	 */
	protected void catchException(Exception ex) {
		ex.printStackTrace();
		
		String message = getMsgFromResourceBundle("msg.err.geral.aplicacao");
		
		FacesUtil.addMessage(message, FacesMessage.SEVERITY_ERROR);
	}

	/**
	 * Retorna tipo parametrizado (generic)
	 * 
	 * @param  index indice do tipo
	 * @return tipo parametrizado 
	 */
	protected Class getParameterizedType(int index) {
		Type type 					= getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		Class classe	 			= (Class) paramType.getActualTypeArguments()[index];
		
		return classe;
	}
	
	/**
	 * Retorna a classe da entidade default.
	 * 
	 * @return a classe da entidade default.
	 */
	@SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
		return (Class<E>) getParameterizedType(0);
	}

	
}





