package framework.presentation.bean;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import framework.model.service.ApplicationServiceException;
import framework.model.service.BaseApplicationService;
import framework.model.service.QueryParameters;
import framework.presentation.faces.FacesUtil;

/**
 * Esta classe auxilia na implementa��o dos metodos b�sicos de CRUD,
 * para beans com suporte a seam. 
 * Deve ser estendida pelos JSF Managed Beans da aplica��o 
 * 
 * @author Luiz Alberto
 * 
 * @param <E> Entity Bean default
 *
 */
@SuppressWarnings("rawtypes")
public class BaseApplicationBean<E> extends BaseManagedBean<E> {

	public static final String DEFAULT_EDIT_VIEW_ID	= "default-edit-view-id";
	public static final String DEFAULT_SAVE_VIEW_ID	= "default-save-view-id";
	

//	@RequestParameter(value = "requestDefaultViewPath")
//	protected String requestDefaultViewPath;
//
//	@RequestParameter(value = "rowIndex")
//	protected String rowIndex;
	
	protected List<E> entityList;

	protected String defaultViewPath;
	protected E entity;
     

	
	/**
	 * M�todo default utilizado para pesquisas utilizando CRUDService
	 * 
	 * @return view default de retorno
	 */
	public String search() {
		E entity 	= getEntity();
		entityList	= applicationService.search(entity, offset);

		setParamsPaging();
		
		return defaultViewPath;
	}

	/**
	 * Define os parametros para pagina��o
	 * 
	 */
	protected void setParamsPaging() {
		int count	= applicationService.getCount();
		E entity 	= getEntity();
		currentPage	= 1;
		
		setSessionAttribute(List.class.getName(), entityList);
		setSessionAttribute(SEARCH_ENTITY, entity);
		setSessionAttribute(QUERY_COUNT, count);
		setSessionAttribute(OFFSET, currentPage);
		
		this.entity = null;
	}

	/**
	 * Ordena a lista de resultados
	 * 
	 * @param entityProperty propriedade do Entity Bean pela qual ser� ordenada a query
	 */
	public void sortListByProperty(String entityProperty) {
		setServiceQueryCount();
		
		E entity 	= getSearchEntity();
		currentPage = 1;
		offset 		= "0";
		entityList	= applicationService.search(entity, offset, entityProperty);
		
		setSessionOrderByProperty(entityProperty);
		setSessionAttribute(OFFSET, currentPage);
		setSessionAttribute(List.class.getName(), entityList);
	}
	
	/**
	 * Define os parametros para pagina��o 
	 * 
	 * @param service classe de servi�o
	 */
	protected void setServiceParamsPaging(BaseApplicationService service) {
		int count	= service.getCount();
		currentPage	= 1;
		
		setSessionAttribute(List.class.getName(), entityList);
		setSessionAttribute(QUERY_PARAMS, service.getQueryParams());
		setSessionAttribute(QUERY_COUNT, count);
		setSessionAttribute(OFFSET, currentPage);
		
		this.entity = null;
	}
	
	/**
	 * M�todo default utilizado para pesquisas paginadas
	 * 
	 */
	protected void searchPaging() {
		E entity 	= getSearchEntity();
		entityList	= applicationService.search(entity, offset);
		setSessionAttribute(List.class.getName(), entityList);
	}
	
	/**
	 * Executa a busca a partir da primeira pagina do 
	 * resultado da consulta do banco
	 * 
	 */
	public void first() {
		int count 	= (Integer)getSessionAttribute(QUERY_COUNT);
		currentPage = 1;
		offset 		= "0";
		
		setSessionAttribute(OFFSET, currentPage);
		applicationService.setCount(count);
		
		searchPaging();
	}

	/**
	 * Executa a busca a partir da primeira pagina do 
	 * resultado da consulta do banco
	 * 
	 */
	public void returnFirst() {
		int count 	= (Integer)getSessionAttribute(QUERY_COUNT);
		currentPage = 1;
		offset 		= "0";
		
		setSessionAttribute(OFFSET, currentPage);
		applicationService.setCount(count);
		
		getSearchPaging();
	}

	/**
	 * M�todo n�o default utilizado para pesquisas paginadas
	 * 
	 */
	protected void getSearchPaging() {
		QueryParameters queryParams	= getSessionQueryParameters();
		queryParams.setOffset(offset);
		
		entityList = applicationService.search(queryParams);
		
		setSessionAttribute(List.class.getName(), entityList);
	}
	
	/**
	 * Executa a busca a partir da pagina anterior do 
	 * resultado da consulta do banco
	 * 
	 */
	public void previous() {
		applicationService.setEntity(getSearchEntity());
		
		currentPage		= (Integer)getSessionAttribute(OFFSET);
		int count 		= (Integer)getSessionAttribute(QUERY_COUNT);
		int pagingSize 	= applicationService.getPagingSize();
		
		applicationService.setCount(count);
		
		if (currentPage == 1) {
			first();
		} else if (currentPage == count) {
			int resto	= count % pagingSize; 
			int div 	= count / pagingSize;
			currentPage = resto == 0 ? div : div + 1;
			
			searchPrevious();
		} else {
			searchPrevious();
		}
	}

	/**
	 * Executa a busca a partir da pagina anterior do 
	 * resultado da consulta do banco
	 * 
	 */
	public void returnPrevious() {
		QueryParameters queryParams	= (QueryParameters)getSessionAttribute(QUERY_PARAMS);
		currentPage					= (Integer)getSessionAttribute(OFFSET);
		int count 					= (Integer)getSessionAttribute(QUERY_COUNT);
		int pagingSize 				= queryParams.getPagingSize();
		
		applicationService.setCount(count);
		
		if (currentPage == 1) {
			returnFirst();
		} else if (currentPage == count) {
			int resto	= count % pagingSize; 
			int div 	= count / pagingSize;
			currentPage = resto == 0 ? div : div + 1;
			
			getSearchPrevious();
		} else {
			getSearchPrevious();
		}
	}

	/**
	 * Executa a busca a partir da pagina anterior do 
	 * resultado da consulta do banco
	 * 
	 */
	protected void getSearchPrevious() {
		currentPage--;
		setSessionAttribute(OFFSET, currentPage);
		
		offset = String.valueOf(currentPage - 1);
		getSearchPaging();
	}

	/**
	 * Executa a busca a partir da pagina anterior do 
	 * resultado da consulta do banco
	 * 
	 */
	protected void searchPrevious() {
		currentPage--;
		setSessionAttribute(OFFSET, currentPage);
		
		offset = String.valueOf(currentPage - 1);
		searchPaging();
	}
	
	/**
	 * Executa a busca a partir da pr�xima pagina do 
	 * resultado da consulta do banco
	 * 
	 */
	public void next() {
		applicationService.setEntity(getSearchEntity());

		currentPage		= (Integer)getSessionAttribute(OFFSET);
		int count 		= (Integer)getSessionAttribute(QUERY_COUNT);
		int pagingSize	= applicationService.getPagingSize();

		applicationService.setCount(count);
		
		if (currentPage == count) {
			last();
		} else if (currentPage == 1 && count > pagingSize) {
			searchNext();
		} else if (currentPage == 1 && count <= pagingSize) {
			first();
		} else if (((currentPage) * pagingSize) + 1 < count) {
			searchNext();
		} else {
			last();
		}
	}

	/**
	 * Executa a busca a partir da pr�xima pagina do 
	 * resultado da consulta do banco
	 * 
	 */
	public void returnNext() {
		QueryParameters queryParams	= (QueryParameters)getSessionAttribute(QUERY_PARAMS);
		currentPage					= (Integer)getSessionAttribute(OFFSET);
		int count 					= (Integer)getSessionAttribute(QUERY_COUNT);
		int pagingSize				= queryParams.getPagingSize();

		applicationService.setCount(count);
		
		if (currentPage == count) {
			returnLast();
		} else if (currentPage == 1 && count > pagingSize) {
			getSearchNext();
		} else if (currentPage == 1 && count <= pagingSize) {
			returnFirst();
		} else if (((currentPage) * pagingSize) + 1 < count) {
			getSearchNext();
		} else {
			returnLast();
		}
	}

	/**
	 * Executa a busca a partir da pr�xima pagina do 
	 * resultado da consulta do banco
	 * 
	 */
	protected void getSearchNext() {
		currentPage++;
		setSessionAttribute(OFFSET, currentPage);
		
		offset = String.valueOf(currentPage - 1);
		getSearchPaging();
	}

	/**
	 * Executa a busca a partir da �ltima pagina do 
	 * resultado da consulta do banco
	 * 
	 */
	public void returnLast() {
		QueryParameters queryParams	= (QueryParameters)getSessionAttribute(QUERY_PARAMS);
		int count 					= (Integer)getSessionAttribute(QUERY_COUNT);
		
		applicationService.setCount(count);
		
		int pagingSize	= queryParams.getPagingSize();
		currentPage 	= count;
		int resto		= count % pagingSize; 
		int div 		= count / pagingSize;
		int off			= resto == 0 ? div : div + 1;
		offset 			= String.valueOf(off - 1);
		
		setSessionAttribute(OFFSET, currentPage);
		getSearchPaging();
	}
	
	/**
	 * Executa a busca a partir da pr�xima pagina do 
	 * resultado da consulta do banco
	 * 
	 */
	protected void searchNext() {
		++currentPage;
		setSessionAttribute(OFFSET, currentPage);
		
		offset = String.valueOf(currentPage - 1);
		searchPaging();
	}
	
	/**
	 * Executa busca paginada no banco a partir da pagina 
	 * passada como par�metro
	 * 
	 * @param offset p�gina do banco passada como par�metro 
	 */
	public void getPageByOffSet(int offset) {
		setParamsOffSet(offset, false);
		searchPaging();
	}

	/**
	 * Executa busca customizada paginada no banco a partir da pagina 
	 * passada como par�metro
	 * 
	 * @param offset p�gina do banco passada como par�metro 
	 */
	public void getPageFromQueryParamsByOffSet(int offset) {
		setParamsOffSet(offset, true);
		getSearchPaging();
	}
	
	/**
	 * Define os par�metros para busca paginada no banco
	 * pela p�gina do banco passada como par�metro
	 * 
	 * @param offset 			p�gina do bancopassada como par�metro
	 * @param fromQueryParams	indica se os par�metros de pagina��o vir�o da query customizada (QueryParameters)
	 */
	private void setParamsOffSet(int offset, boolean fromQueryParams) {
		int initialPage	= fromQueryParams ? getInitialPageFromQueryParams() : getInitialPage();
		currentPage 	= offset + initialPage - 1;
		this.offset 	= String.valueOf(currentPage - 1);
		
		setSessionAttribute(OFFSET, currentPage);
		setServiceQueryCount();
	}
	
	/**
	 * Executa a busca a partir da �ltima pagina do 
	 * resultado da consulta do banco
	 * 
	 */
	public void last() {
		int count = (Integer)getSessionAttribute(QUERY_COUNT);
		applicationService.setEntity(getSearchEntity());
		applicationService.setCount(count);
		
		int pagingSize	= applicationService.getPagingSize();
		currentPage 	= count;
		int resto		= count % pagingSize; 
		int div 		= count / pagingSize;
		int off			= resto == 0 ? div : div + 1;
		offset 			= String.valueOf(off - 1);
		
		setSessionAttribute(OFFSET, currentPage);
		searchPaging();
	}

	/**
	 * Prepara para salvar 
	 * 
	 * @return default view id
	 */
	//@Begin(join = true, flushMode = FlushModeType.MANUAL)
	public String prepareSave() {
		return DEFAULT_SAVE_VIEW_ID;
	}

	/**
	 * Prepara para update 
	 * 
	 * @return default view id
	 */
	@SuppressWarnings("unchecked")
	public String prepareUpdate() {
		List<E> list	= (List<E>) getSessionAttribute(List.class.getName());
		String rowIndex = FacesUtil.getParameter("rowIndex");
		int index 		= Integer.parseInt(rowIndex);
		E selected 		= list.get(index);
		entity 			= (E) applicationService.findObject(selected);
		
		return DEFAULT_EDIT_VIEW_ID;
	}

	/**
	 * Prepara para deletar 
	 * 
	 * @return default view id
	 */
	public String prepareRemove() {
		return null;
	}

	/**
	 * Persiste um Entity Bean no banco
	 * 
	 * @return default view id
	 */
	public String save() {
		try {
			applicationService.save(entity);
			addMsgSuccessDefaultFromResource();
		} catch (ApplicationServiceException ase) {	
			catchApplicationException(ase);
		} catch (Exception e) {
			catchException(e);
		}
		return null;
	}

	/**
	 * Atualiza um Entity Bean no banco
	 * 
	 * @return default view id
	 */
	public String update() {
		try {
			Object persistent = applicationService.findObject(entity);
			
			BeanUtils.copyProperties(persistent, entity);
			
			applicationService.update(persistent);
			addMsgUpdateDefaultFromResource();
			
		} catch (ApplicationServiceException ase) {	
			catchApplicationException(ase);
		} catch (Exception e) {
			e.printStackTrace();
			catchException(e);
		}
		return null;
	}

	/**
	 * Deleta um Entity Bean no banco
	 * 
	 * @return default view id
	 */
	public String remove() {
		try {
			applicationService.remove(entity);
			addMsgSuccessDefaultFromResource();
			
		} catch (ApplicationServiceException ase) {	
			catchApplicationException(ase);
		} catch (Exception e) {
			catchException(e);
		}
		return null;
	}
	
	/**
	 * Localiza todos os registros da entidade default 
	 * 
	 */
	public void findAllEntities() {
		//createService();
		
		entityList = applicationService.findAll(getEntityClass());
	}
	
	/**
	 * Retorna a lista de entidades
	 * 
	 * @return a lista de entidades
	 */
	public List<E> getEntityList() {
		return entityList;
	}

	/**
	 * Recupera o nome da entidade principal do bean.
	 * 
	 * @return Nome da entidade principal do bean definida na anota��o da classe.  
	 */
	public String getEntityName() {
		E entity = getEntity();
		
		return entity.getClass().getSimpleName();
	}
	
	/**
	 * Cria a inst�ncia do Entity Bean default
	 * 
	 * @return inst�ncia do Entity Bean default
	 */
	protected E createEntity() {
		try {
			Class<E> entityClass = getEntityClass();
			
			entity = entityClass.newInstance();
			
			return entity;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanException(e);
		}
	}

//	protected void createService() {
//		if (applicationService == null) {
//			try {
//				UncachedServiceLocator locator	= UncachedServiceLocator.instance();
//				Class<T> serviceClass 			= getApplicationServiceClass();
//				String name 					= serviceClass.getSimpleName();
//				
//				 applicationService = (BaseApplicationService) locator.getService(name);
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new BeanException(e);
//			}
//		}
//	}
	
	/**
	 * Retorna o Entity Bean default
	 * 
	 * @return o Entity Bean 
	 */
	public E getEntity() {
		return entity != null ? entity : createEntity();
	}

	/**
	 * Define o Entity Bean default
	 * 
	 * @param entity Entity Bean 
	 */
	public void setEntity(E entity) {
		this.entity = entity;
	}
	
	/**
	 * Retorna o path default da view de resposta
	 * 
	 * @return path default da view de resposta
	 */
	public String getDefaultViewPath() {
		return defaultViewPath;
	}

	/**
	 * Define o path default da view de resposta
	 * 
	 * @param defaultViewPath path default da view de resposta
	 */
	public void setDefaultViewPath(String defaultViewPath) {
		this.defaultViewPath = defaultViewPath;
	}

	/**
	 * Define no crudService o total de registros 
	 * da query da sess�o
	 * 
	 */
	protected void setServiceQueryCount() {
		int count = (Integer) getSessionAttribute(QUERY_COUNT);
		applicationService.setCount(count);
	}
	
	/**
	 * Retorna a p�gina atual do banco
	 * 
	 * @return p�gina atual do banco
	 */
	public int getCurrentPage() {
		this.currentPage	= (Integer) getSessionAttribute(OFFSET);
		int count 			= (Integer) getSessionAttribute(QUERY_COUNT);
		int current			= this.currentPage;
		
		if (currentPage == count) {
			int pagingSize	= getPagingSize();
			current = calcCurrent(count, pagingSize);
		}
		return current;
	}

	/**
	 * Retorna o �ndice da p�gina corrente da barra de pagina��o
	 * 
	 * @param index	�ndice da itera��o da barra de pagina��o
	 * @return 		�ndice da p�gina corrente da barra de pagina��o
	 */
	public int getCurrentPageIndex(int index) {
		return index + getInitialPage() - 1;
	}

	/**
	 * Retorna o �ndice da p�gina corrente da barra de pagina��o
	 * 
	 * @param index	�ndice da itera��o da barra de pagina��o
	 * @return 		�ndice da p�gina corrente da barra de pagina��o
	 */
	public int getCurrentPageIndexFromQueryParams(int index) {
		return index + getInitialPageFromQueryParams() - 1;
	}

	/**
	 * Verifica se � a p�gina atual da barra de pagin��o
	 * 
	 * @return true se for, false se n�o
	 */
	public boolean isAtualPage(int index) {
		int page = index + getInitialPage() - 1;
		
		return page == getCurrentPage();
	}
	
	/**
	 * Verifica se � a p�gina atual da barra de pagin��o
	 * 
	 * @param	index �ndice da itera��o da barra de pagina��o  	
	 * @return	true se for, false se n�o
	 */
	public boolean isAtualPageFromQueryParams(int index) {
		int page = index + getInitialPageFromQueryParams() - 1;
		
		return page == getCurrentPageFromQueryParams(null);
	}

	/**
	 * Retorna a p�gina atual do banco
	 * 
	 * @param listKey chave/nome dos par�metros multi-lista
	 * @return p�gina atual do banco
	 */
	protected int getCurrentPageFromQueryParams(String listKey) {
		int current = 0;
		
		if (listKey == null) {
			this.currentPage	= (Integer) getSessionAttribute(OFFSET);
			int count 			= (Integer) getSessionAttribute(QUERY_COUNT);
			current				= this.currentPage;
			
			if (currentPage == count) {
				int pagingSize	= getSessionQueryParameters().getPagingSize();
				current 		= calcCurrent(count, pagingSize);
			}
		} 
		return current;
	}

	/**
	 * Retorna a primeira pagina do range da barra de pagina��o
	 * 
	 * @return primeira pagina do range da barra de pagina��o
	 */
	public int getInitialPage() {
		return (((int) (getCurrentPage() - 1) / 10) * 10) + 1;
	}

	/**
	 * Retorna a primeira pagina do range da barra de pagina��o
	 * 
	 * @return primeira pagina do range da barra de pagina��o
	 */
	public int getInitialPageFromQueryParams() {
		return (((int) (getCurrentPageFromQueryParams(null) - 1) / 10) * 10) + 1;
	}

	/**
	 * Retorna a ultima pagina do range da barra de pagina��o
	 * 
	 * @return ultima pagina do range da barra de pagina��o
	 */
	public int getFinalPage() {
		int pagingSize		= getPagingSize();
		int count 			= (Integer) getSessionAttribute(QUERY_COUNT);
		int finalPage		= getInitialPage() + 9;
		int resto			= count % pagingSize; 
		int div 			= count / pagingSize;
		int finalMaxPage	= resto == 0 ? div : div + 1;
		
		return Math.min(finalPage, finalMaxPage);
	}

	/**
	 * Retorna a ultima pagina do range da barra de pagina��o
	 * 
	 * @return ultima pagina do range da barra de pagina��o
	 */
	public int getFinalPageFromQueryParams() {
		int pagingSize		= getSessionQueryParameters().getPagingSize();
		int count 			= (Integer) getSessionAttribute(QUERY_COUNT);
		int finalPage		= getInitialPageFromQueryParams() + 9;
		int finalMaxPage	= calcCurrent(count, pagingSize);
		
		return Math.min(finalPage, finalMaxPage);
	}

	/**
	 * Calcula a p�gina corrente para a barra de pagina��o
	 * 
	 * @param count			total geral de registros da consulta
	 * @param pagingSize	total de registros por p�gina
	 * @return resultado do c�lculo
	 */
	private int calcCurrent(int count, int pagingSize) {
		int resto	= count % pagingSize; 
		int div 	= count / pagingSize;
		
		return resto == 0 ? div : div + 1;
	}

	/**
	 * Retorna o total de registros por p�gina
	 * 
	 * @return total de registros por p�gina
	 */
	public int getPagingSize() {
		applicationService.setEntity(getSearchEntity());
		
		return applicationService.getPagingSize();
	}
	
}









