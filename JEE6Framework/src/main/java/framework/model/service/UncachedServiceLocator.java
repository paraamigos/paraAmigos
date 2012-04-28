package framework.model.service;

import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * Classe utilit�ria para servi�os de localiza��o
 * 
 * @author  Luiz Alberto
 */
public class UncachedServiceLocator  {
	
	private static UncachedServiceLocator instance;
	private InitialContext context;

    /**
     * Inicializa a classe
     * 
     */
    private UncachedServiceLocator() {
        try {
            context = new InitialContext();
        } catch (NamingException e) {
            throw new ServiceLocatorException(e);
        } 
    }

    /**
     * Retorna a instancia da classe
     *  
     * @return a instancia da classe 
     */
    public static UncachedServiceLocator instance() {
        if(instance == null) {
            instance = new UncachedServiceLocator();
        }
        return instance;
    }

    /**
     * Retorna um servi�o atraves do nome
     * 
     * @param   jndiName nome do servi�o
     * @return  um servi�o atraves do nome 
     */
    public Object getService(String jndiName) {
        try  {
        	return context.lookup(jndiName);
        } catch(NamingException ex) {
            throw new ServiceLocatorException(ex);
        }
    }


}





