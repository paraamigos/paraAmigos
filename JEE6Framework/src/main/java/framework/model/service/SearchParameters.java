package framework.model.service;

/**
 * Esta classe contem parametros para queries customizadas 
 * 
 * @author Luiz Alberto
 *
 */
public abstract class SearchParameters {
	
	protected String offset;
	protected boolean paging;
	protected int pagingSize;	

	
	/**
	 * Retorna tipo de parametriza��o de query
	 * 
	 * @return tipo de parametriza��o
	 */
	public abstract boolean isQueryByExample();
	
	/**
	 * Retorna total de registros por pagina
	 * 
	 * @return total de registros por pagina
	 */
	public int getPagingSize() {
		return pagingSize;
	}

	/**
	 * Define total de registros por pagina
	 * 
	 * @param pagingSize total de registros por pagina
	 */
	public void setPagingSize(int pagingSize) {
		this.pagingSize = pagingSize;
	}

	/**
	 * Retorna a p�gina do banco
	 * 
	 * @return p�gina do banco
	 */
	public String getOffset() {
		return offset;
	}
	
	/**
	 * Define p�gina do banco
	 *  
	 * @param offset p�gina do banco
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}

	/**
	 * Retorna se a query � paginada no banco
	 * 
	 * @return se a query � paginada no banco
	 */
	public boolean isPaging() {
		return paging;
	}

	/**
	 * Define se a query � paginada no banco
	 * 
	 * @param paging se a query � paginada no banco
	 */
	public void setPaging(boolean paging) {
		this.paging = paging;
	}
	
}
