package framework.presentation.faces;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FacesUtil {

	
	public static void addMessage(String message, Severity severity) {
		FacesContext ctx = getFacesCtx();
		
		FacesMessage facesMessage = new FacesMessage(message);
		
		if (severity != null) {
			facesMessage.setSeverity(severity);	
		}
		ctx.addMessage(null, facesMessage);
	}
	
	public static String getResourceBundleMessage(String resourceBundleVar, String key) {
		Application  app = getApplication();
		FacesContext ctx = getFacesCtx();
		
		ResourceBundle bundle = app.getResourceBundle(ctx, resourceBundleVar);
		
		return bundle.getString(key);
	}
	
	public static Application getApplication() {
		return getFacesCtx().getApplication();
	}
	
	public static FacesContext getFacesCtx() {
		return FacesContext.getCurrentInstance();
	}
	
	/**
	 * Obt�m o request a partir do FacesContext
	 * 
	 * @return request
	 */
	public static HttpServletRequest getRequest() {
		FacesContext facesCtx	= FacesContext.getCurrentInstance();
		ExternalContext extCtx	= facesCtx.getExternalContext();
		
		return (HttpServletRequest) extCtx.getRequest();  
	}

	/**
	 * Obtém o response a partir do FacesContext
	 * 
	 * @return response
	 */
	public static HttpServletResponse getResponse() {
		FacesContext facesCtx	= FacesContext.getCurrentInstance();
		ExternalContext extCtx	= facesCtx.getExternalContext();
		
		return (HttpServletResponse) extCtx.getResponse();  
	}
	
	/**
	 * Obt�m a sess�o a partir do FacesContext
	 * 
	 * @return sess�o
	 */
	public static HttpSession getSession() {
		HttpServletRequest request = getRequest();
		
		return request.getSession();
	}

	/**
	 * Obt�m um atributo da sess�o 
	 * 
	 * @return atributo da sess�o 
	 */
	public static Object getSessionAttribute(String name) {
		return getSession().getAttribute(name);
	}

	/**
	 * Define um atributo da sess�o
	 * 
	 * @param name chave
	 * @param obj  objeto
	 */
	public static void setSessionAttribute(String name, Object obj) {
		getSession().setAttribute(name, obj);
	}

	/**
	 * Obt�m um atributo do request 
	 * 
	 * @return atributo do request 
	 */
	public static Object getRequestAttribute(String name) {
		return getRequest().getAttribute(name);
	}

	/**
	 * Define um atributo de request
	 * 
	 * @param name chave
	 * @param obj  objeto
	 */
	public static void setRequestAttribute(String name, Object obj) {
		getRequest().setAttribute(name, obj);
	}

	/**
	 * Obt�m o contexto servlet da aplica��o a partir do FacesContext
	 * 
	 * @return contexto servlet da aplica��o
	 */
	public static ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	/**
	 * Obt�m um par�metro de request a partir do FacesContext
	 * 
	 * @param 	paramName nome do par�metro
	 * @return	par�metro de request
	 */
	public static String getParameter(String paramName) {
		return getRequest().getParameter(paramName);
	}
	
	
}
