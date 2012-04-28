package framework.collections;

import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtils;



/**
 * Classe utilitaria para compara��o de objetos
 * 
 * @author DBA Eng. Sist.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ComparatorUtil implements Comparator {
	private boolean comparaAcentuacao = false;
    private String 	property;
    private	boolean isInverse;

    
    
    /**
     * Inicializa a classe
     * 
     * @param 	property propriedade do bean que 
     * 			sera usada na compara��o
     */	
    public ComparatorUtil(String property) {
        this.property = property;
    }
    
	/**
	 * Inicializa a classe
	 * 
	 * @param property	propriedade do bean que sera usada na compara��o
	 * @param isInverse	se ser� ordenado por ordem inversa
	 */
	public ComparatorUtil(String property, boolean isInverse) {
		this.property 	= property;
		this.isInverse	= isInverse;
	}
	
	
    /**
     * Implementa a compara��o da interface Comparator
     * @param 	primObjeto		primeiro objeto da compara��o
     * @param 	segundObjeto	segundo objeto da compara��o
     * @return	resultado da compara��o
     */
	public int compare(Object primObject, Object secondObject) {
		if (isInverse) {
			return invert(primObject, secondObject);
		} else {
			return sort(primObject, secondObject);
		}
	}
	
    /**
     * Implementa a compara��o da interface Comparator em ordem ascendente
     * 
     * @param 	primeiroObjeto		primeiro objeto da compara��o
     * @param 	segundoObjeto	segundo objeto da compara��o
     * @return	resultado da compara��o
     */
    private int sort(Object primeiroObjeto, Object segundoObjeto) {
        try {
        	Object primeiroValor = PropertyUtils.getProperty(primeiroObjeto,property);
        	Object segundoValor	 = PropertyUtils.getProperty(segundoObjeto,property);

            return	primeiroValor instanceof String ?
            		sortIgnoreCase(primeiroValor, segundoValor) :
            		sortComparable(primeiroValor, segundoValor);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int sortComparable(
    			Object primeiroObjeto, 
    			Object segundoObjeto
    		) {
    	Comparable primeiroComparable = (Comparable) primeiroObjeto;
    	Comparable segundoComparable  = (Comparable) segundoObjeto;
    	Integer partialResult 		  = compareNullValues(primeiroComparable,segundoComparable);
        
        return	partialResult != null ?
        		partialResult :
        		primeiroComparable.compareTo(
        			segundoComparable
        		);
    }
    
    private int sortIgnoreCase(Object primeiroObjeto, Object segundoObjeto) {
    	String primeiroComparable   = (String) primeiroObjeto;
    	String segundoComparable 	= (String) segundoObjeto ;
    	Integer partialResult 		= compareNullValues(primeiroComparable,segundoComparable);
    	int comparacao				= 0;
    	
        if (partialResult == null) {
			if (comparaAcentuacao) {
				primeiroComparable = StringUtil.retiraAcento(primeiroComparable);
				segundoComparable = StringUtil.retiraAcento(segundoComparable);
			}
			comparacao = primeiroComparable.compareToIgnoreCase(segundoComparable);
		} else {
			comparacao = partialResult;
		}
        return comparacao;
    }
    
	private Integer compareNullValues(
				Comparable primComparable,
				Comparable segundComparable
			) {
		Integer result = null;
		
		if (primComparable == null && segundComparable != null)  {
			result = -1;
		} else if (primComparable == null && segundComparable == null)  {
			result = 0;
		} else if (primComparable != null && segundComparable == null)  {
			result = 1;
		}
		return result;
	}

    /**
     * Implementa a compara��o da interface Comparator em ordem descendente
     * @param 	primObject		primeiro objeto da compara��o
     * @param 	secondObject	segundo objeto da compara��o
     * @return	resultado da compara��o
     */
    private int invert(Object primObject, Object secondObject) {
        try {
            Comparable primComparable   = (Comparable)PropertyUtils.getProperty(primObject,   property);
            Comparable secondComparable = (Comparable)PropertyUtils.getProperty(secondObject, property);
            
            if (secondComparable == null && primComparable != null)  {
                return -1;
            }
            else if (primComparable == null && secondComparable == null)  {
                return 0;
            }
            else if (secondComparable != null && primComparable == null)  {
                return 1;
            }

            return secondComparable.compareTo(primComparable);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

    /**
     * Define se a comparacao sera com caracteres acentuados 
     * 
     * @param comparaAcentuacao parametro que define a comparacao
     */
    public void setComparaAcentuacao(boolean comparaAcentuacao) {
		this.comparaAcentuacao = comparaAcentuacao;
	}
    
}













