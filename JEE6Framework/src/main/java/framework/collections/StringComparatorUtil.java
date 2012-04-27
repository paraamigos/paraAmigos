package framework.collections;

import java.util.Comparator;


/**
 * Classe utilitaria para compara��o de Strings
 * 
 * @author DBA Engenharia de Sistemas LTDA.
 */
@SuppressWarnings({"rawtypes"})
public class StringComparatorUtil implements Comparator<String> {

    /**
     * Implementa a compara��o da interface Comparator
     * @param 	primString		primeira string da compara��o
     * @param 	secondString	segunda string da compara��o
     * @return	resultado da compara��o
     */
	public int compare(String primString, String secondString) {
		return sort(primString, secondString);
	}
	
    /**
     * Implementa a compara��o da interface Comparator em ordem ascendente
     * 
     * @param 	primeiroString	primeiro objeto da compara��o
     * @param 	segundoString	segundo objeto da compara��o
     * @return	resultado da compara��o
     */
    private int sort(String primeiroString, String segundoString) {
        try {
            return	sortIgnoreCase(primeiroString, segundoString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Implementa a compara��o da interface Comparator desconsiderando carvateres maiusculos e minusculos
     * 
     * @param 	primeiroString	primeira string da compara��o
     * @param 	segundoString	segunda string da compara��o
     * @return	resultado da compara��o
     */
    private int sortIgnoreCase(String primeiroString, String segundoString) {
    	String primeiroComparable   = primeiroString;
    	String segundoComparable 	= segundoString ;
    	Integer partialResult 		= compareNullValues(primeiroComparable,segundoComparable);
    	int comparacao				= 0;
    	
        if (partialResult == null) {
			primeiroComparable = StringUtil.retiraAcento(primeiroComparable);
			segundoComparable = StringUtil.retiraAcento(segundoComparable);
			comparacao = primeiroComparable.compareToIgnoreCase(segundoComparable);
		} else {
			comparacao = partialResult;
		}
        return comparacao;
    }
    
    /**
     * Verifica se algum dois parametros s�o nulos
     * 
     * @param 	primComparable	 primeiro comparable da compara��o
     * @param 	segundComparable segundo comparable da compara��o
     * @return	resultado da compara��o
     */
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
    
}













