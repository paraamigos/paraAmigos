package framework.presentation.bean;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Classe base para CRUD beans do framework
 * 
 * @author Luiz Alberto
 */
public class BaseBean {


	/**
	 * Obt�m o request a partir do FacesContext
	 * 
	 * @return request
	 */
	protected HttpServletRequest getRequest() {
		FacesContext facesCtx	= FacesContext.getCurrentInstance();
		ExternalContext extCtx	= facesCtx.getExternalContext();
		
		return (HttpServletRequest) extCtx.getRequest();  
	}

	/**
	 * Obt�m a sess�o a partir do FacesContext
	 * 
	 * @return sess�o
	 */
	protected HttpSession getSession() {
		HttpServletRequest request = getRequest();
		
		return request.getSession();
	}

	/**
	 * Obt�m um atributo da sess�o 
	 * 
	 * @return atributo da sess�o 
	 */
	protected Object getSessionAttribute(String name) {
		return getSession().getAttribute(name);
	}

	/**
	 * Define um atributo da sess�o
	 * 
	 * @param name chave
	 * @param obj  objeto
	 */
	protected void setSessionAttribute(String name, Object obj) {
		getSession().setAttribute(name, obj);
	}

	/**
	 * Obt�m um atributo do request 
	 * 
	 * @return atributo do request 
	 */
	protected Object getRequestAttribute(String name) {
		return getRequest().getAttribute(name);
	}

	/**
	 * Define um atributo de request
	 * 
	 * @param name chave
	 * @param obj  objeto
	 */
	protected void setRequestAttribute(String name, Object obj) {
		getRequest().setAttribute(name, obj);
	}

	/**
	 * Obt�m o contexto servlet da aplica��o a partir do FacesContext
	 * 
	 * @return contexto servlet da aplica��o
	 */
	protected ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	/**
	 * Obt�m um par�metro de request a partir do FacesContext
	 * 
	 * @param 	paramName nome do par�metro
	 * @return	par�metro de request
	 */
	protected String getParameter(String paramName) {
		return getRequest().getParameter(paramName);
	}

}