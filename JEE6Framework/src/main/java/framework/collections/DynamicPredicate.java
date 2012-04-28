package framework.collections;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;

/**
 * Utilitario para beans
 * 
 * @author DBA Eng. Sist.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DynamicPredicate implements Predicate {
    
	public static final int EVALUATE_BY_PROPERTY_AND_COMPARABLE = 0;
	public static final int EVALUATE_BY_PROPERTY_AND_OBJECT 	= 1;
	public static final int EVALUATE_BY_OBJECT					= 2;
	
	private Comparable	value;
	private String 		property;
    private Object 		object;
    private int			evaluateType;
    
    
    /**
     * Construtor com os parametros para comparacao.
     * 
     * @param property  propriedade do bean
     * @param value     valor a ser comparado
     */
    public DynamicPredicate(String property, Comparable value) {
        this.evaluateType	= EVALUATE_BY_PROPERTY_AND_COMPARABLE;
        this.property   	= property;
        this.value      	= value;
    }

    /**
     * Construtor com os parametros para comparacao.
     * 
     * @param property	propriedade do bean
     * @param value		valor a ser comparado
     */
    public DynamicPredicate(String property, Object value) {
        this.evaluateType	= EVALUATE_BY_PROPERTY_AND_OBJECT;
        this.property   	= property;
        this.object      	= value;
    }

    /**
     * Construtor com o parametro para comparacao.
     * 
     * @param object objeto a ser encontrado
     */
    public DynamicPredicate(Object object) {
        this.evaluateType	= EVALUATE_BY_OBJECT;
        this.object 		= object;
    }

    /**
     * Retorna a comparacao com o valor a ser encontrado.
     * 
     * @param   input bean para comparacao
     * @return  o resultado da comparacao. 
     */
    public boolean evaluate(Object input) {
        boolean isEquals = false;
        
        try {
        	switch (evaluateType) {
        		case EVALUATE_BY_PROPERTY_AND_COMPARABLE:
        			Comparable comparable = input != null ? (Comparable)PropertyUtils.getProperty(input, property) : null;
        			isEquals = comparable != null ? value.compareTo(comparable) == 0 : false;  
        			break;
				case EVALUATE_BY_PROPERTY_AND_OBJECT:
					Object objectValue = input != null ? PropertyUtils.getProperty(input, property) : null;
					isEquals = objectValue != null ? this.object.equals(objectValue) : false;
					break;
				default:
					isEquals = input != null && this.object != null ? this.object.equals(input) : false;
					break;
			}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return isEquals;
    }
}

