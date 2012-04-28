package framework.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Classe utilitaria para tratamento de collections.
 * 
 * @author DBA Eng. Sist.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CollectionsUtil  {

    /**
     * Construtor privado para evitar instancias desnecessarias 
     * da classe.
     */
    private CollectionsUtil() {
    }


    /**
     * Retorna se uma Collection est� vazia
     * 
     * @param  collection Collection a ser verificada
     * @return se uma Collection est� vazia
     */
    public static boolean isEmpty(Collection<?> collection) {
    	return collection == null || collection.isEmpty();
    }
    
    /**
     * Retorna se uma Collection n�o est� vazia
     * 
     * @param  collection Collection a ser verificada
     * @return se uma Collection n�o est� vazia
     */
    public static boolean isNotEmpty(Collection<?> collection) {
    	return collection != null && !collection.isEmpty();
    }
    
    /**
     * Localiza um objeto(bean) dentro de uma collection 
     * 
     * @param collection	collection de beans
     * @param beanProperty	propriedade do bean que retorna o valor a ser comparado
     * @param value			valor a ser comparado
     * @return objeto da collection que possui o valor a ser encontrado
     */
    public static final <T> T findInObjectTree(Collection<T> collection, String beanProperty, Object value) {
    	try {
    		boolean isPropertyNotEmpty = beanProperty != null && !beanProperty.trim().equals("") && beanProperty.indexOf(".") > 0;
    		
    		return	isPropertyNotEmpty ? getObjectInTree(collection, beanProperty, value) : find(collection, beanProperty, value);
    				
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * Localiza um objeto(bean) dentro de uma collection 
     * 
     * @param 	collection		collection de beans
     * @param 	beanProperty	propriedade do bean que retorna o valor a ser comparado
     * @param	value			valor a ser comparado
     * @throws 	Exception		em caso de erro
     * @return objeto da collection que possui o valor a ser encontrado
     */
    private static final <T> T getObjectInTree(Collection<T> collection, String beanProperty, Object value) throws Exception {
		String[] properties = StringUtils.split(beanProperty, ".");
    	T 		 encontrado = null;
		
		for (Object object : collection) {
			Object valueObject = object;
			
			for (String property : properties) {
				valueObject	= valueObject != null ? PropertyUtils.getProperty(valueObject, property) : valueObject;
			}
			encontrado = (T) (value.equals(valueObject) ? object : encontrado);
		}
    	return encontrado;
    }
    
    /**
     * Localiza todas as ocorr�ncias de um objeto(bean) dentro de uma collection
     * de acordo com um valor e propriedade que retorna tal valor.
     * 
     * @param   collection  	collection de beans 
     * @param   beanProperty    propriedade do bean que retorna o valor a ser comparado
     * @param   value       	valor a ser comparado
     * @return  cole��o que possui todas as ocorr�ncias do objeto(bean)
     */
    public static final <T> Collection<T> findAllInObjectTree(Collection<T> collection, String beanProperty, Object value) {
    	boolean isPropertyNotEmpty = beanProperty != null && !beanProperty.trim().equals("") && beanProperty.indexOf(".") > 0;
    	
    	return	isPropertyNotEmpty ? getAllInObjectTree(collection, beanProperty, value) : findAll(collection, beanProperty, value);	
    }
    
    /**
     * Localiza todas as ocorr�ncias de um objeto(bean) dentro de uma collection
     * de acordo com um valor e propriedade que retorna tal valor.
     * 
     * @param   collection  	collection de beans 
     * @param   beanProperty    propriedade do bean que retorna o valor a ser comparado
     * @param   value       	valor a ser comparado
     * @return  cole��o que possui todas as ocorr�ncias do objeto(bean)
     */
    public static final <T> Collection<T> getAllInObjectTree(Collection<T> collection, String beanProperty, Object value) {
    	Collection<T> list = new ArrayList<T>();
    	
    	try {
    		String[] properties = StringUtils.split(beanProperty, ".");
    		
    		for (Object object : collection) {
    			Object valueObject = object;
    			
    			for (String property : properties) {
    				valueObject	= valueObject != null ? PropertyUtils.getProperty(valueObject, property) : valueObject;
    			}
    			@SuppressWarnings("unused")
				boolean result = value.equals(valueObject) ? list.add((T)object) : false;
    		}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	return list;
    }
    
    /**
     * Localiza um objeto(bean) dentro de uma collection
     * 
     * @param   object objeto a ser encontrado 
     * @return  objeto da collection que possui o valor a ser encontrado
     */
    public static final Object find(Collection collection, Object object) { 
        return CollectionUtils.find(collection, new DynamicPredicate(object));
    }

    /**
     * Localiza um objeto(bean) dentro de uma collection
     * de acordo com um valor e propriedade que retorna tal valor.
     * 
     * @param   collection  collection de beans 
     * @param   property    propriedade do bean que retorna o valor a ser comparado
     * @param   value       valor a ser comparado
     * @return  objeto da collection que possui o valor a ser encontrado
     */
    public static final <T> T find(Collection<T> collection, String property, Object value) { 
        return (T) CollectionUtils.find(collection, new DynamicPredicate(property, value));
    }

 
    /**
     * Localiza um objeto(bean) dentro de um array de objectos(beans)
     * 
     * @param 	objectArray	array de objectos(beans)
     * @param 	property	propriedade do bean que retorna o valor a ser comparado	
     * @param 	value		valor a ser comparado	
     * @return	objeto do array que possui o valor a ser encontrado
     */
    public static final Object find(Object[] objectArray, String property, Object value) {
    	return CollectionUtils.find(Arrays.asList(objectArray), new DynamicPredicate(property, value));
    }

    /**
     * Localiza um objeto(bean) dentro de um array de objectos(beans)
     * 
     * @param 	objectArray	array de objectos(beans)
     * @param 	property	propriedade do bean que retorna o valor a ser comparado	
     * @param 	value		valor a ser comparado	
     * @return	objeto do array que possui o valor a ser encontrado
     */
    public static final Object find(Object[] objectArray, String property, Comparable value) {
    	return find(Arrays.asList(objectArray), property, value);
    }

    /**
     * Localiza todas as ocorr�ncias de um objeto(bean) dentro de uma collection
     * de acordo com um valor e propriedade que retorna tal valor.
     * 
     * @param   collection  collection de beans 
     * @param   property    propriedade do bean que retorna o valor a ser comparado
     * @param   value       valor a ser comparado
     * @return  cole��o que possui todas as ocorr�ncias do objeto(bean)
     */
    public static final <T> Collection<T> findAll(Collection<T> collection, String property, Object value) {
    	Collection<T> list = new ArrayList<T>();
    	
    	try {
    		for (Object object : collection) {
        		Object 	objectValue	= PropertyUtils.getProperty(object, property);
        		
        	    @SuppressWarnings("unused")
        		boolean result = value.equals(objectValue) ? list.add((T)object) : false;
    		}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	return list;
    }


    /**
     * Localiza todas as ocorr�ncias de um objeto(bean) dentro de um array
     * de acordo com um valor e propriedade que retorna tal valor.
     * 
     * @param   array  		array de beans 
     * @param   property    propriedade do bean que retorna o valor a ser comparado
     * @param   value       valor a ser comparado
     * @return  cole��o que possui todas as ocorr�ncias do objeto(bean)
     */
    public static final Collection findAll(Object[] array, String property, Comparable value) {
    	return findAll(Arrays.asList(array), property, value);
    }

    /**
     * Localiza todas as ocorr�ncias de um objeto(bean) dentro de uma collection
     * de acordo com um objecto.
     * 
     * @param   collection  collection de objects 
     * @param   object      objecto a ser comparado
     * @return  cole��o que possui todas as ocorr�ncias do objeto
     */
    public static final Collection findAll(Collection collection, Object object) {
    	Collection  list = new ArrayList();
    	
        try {
        	for (Object type : collection) {
                if (object.equals(type)) {
                    list.add(type);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    	return list;
    }

    /**
     * Ordena uma cole��o de beans.
     * 
     * @param collection    	cole��o de beans.
     * @param property      	propriedade do bean que sera usada na compara��o
     * @param comparaAcentuacao compara caracteres com acentuacao
     */
    public static final void sort(Collection collection, String property, boolean comparaAcentuacao) {
        List list = (List)collection;
        
        ComparatorUtil comparador = new ComparatorUtil(property);
        comparador.setComparaAcentuacao(comparaAcentuacao);
        
        Collections.sort(list, comparador);
    }
    
    /**
     * Ordena uma cole��o de beans.
     * 
     * @param collection    cole��o de beans ou VOs.
     * @param property      propriedade do bean que sera usada na compara��o
     */
    public static final void sort(Collection collection, String property) {
        List list = (List)collection;
        Collections.sort(list, new ComparatorUtil(property));
    }

    /**
     * Ordena uma cole��o (Set) de beans 
     * 
     * @param  set cole��o (Set) de beans
     * @param  property propriedade do bean que sera usada na compara��o
     * @param  comparaAcentuacao compara caracteres com acentuacao
     * @return cole��o (Set) de beans ordenados
     */
	public static <T> Set<T> sortSet(Set<T> set, String property, boolean comparaAcentuacao) {
        ComparatorUtil comparador = new ComparatorUtil(property);
        comparador.setComparaAcentuacao(comparaAcentuacao);

		Set<T> sortedSet = new TreeSet<T>(comparador);
		if (set != null && !set.isEmpty()) {
			sortedSet.addAll(set);
		}
		return set == null ? set : sortedSet;
	}
    
    /**
     * Ordena uma cole��o (Set) de beans 
     * 
     * @param  set cole��o (Set) de beans
     * @param  property propriedade do bean que sera usada na compara��o
     * @return cole��o (Set) de beans ordenados
     */
	public static <T> Set<T> sortSet(Set<T> set, String property) {
		Set<T> sortedSet = new TreeSet<T>(
							 new ComparatorUtil(property)
						   );
		if (set != null && !set.isEmpty()) {
			sortedSet.addAll(set);
		}
		return set == null ? set : sortedSet;
	}
    
    /**
     * Ordena uma array de beans ou VOs.
     * @param 	objectArray	array de objectos(beans)
     * @param 	property	propriedade do bean que retorna o valor a ser comparado	
     */
    public static final void sort(Object[] objectArray, String property) {
    	Arrays.sort(objectArray, new ComparatorUtil(property));
    }

    /**
     * Ordena uma cole��o de beans ou VOs em ordem inversa.
     * @param collection    cole��o de beans ou VOs.
     * @param property      propriedade do bean que sera usada na compara��o
     */
    public static final void reverse(List collection, String property) {
        Collections.sort(collection, new ComparatorUtil(property, true));
    }


    /**
     * Ordena uma array de beans ou VOs em orderm inversa.
     * @param 	objectArray	array de objectos(beans)
     * @param 	property	propriedade do bean que retorna o valor a ser comparado	
     */
    public static final void reverse(Object[] objectArray, String property) {
    	Arrays.sort(objectArray, new ComparatorUtil(property, true));
    }

    /**
     * Retorna uma lista com objetos distintos 
     * semelhante � fun��o distinct do SQL  
     * 
     * @param 	list Cole��o a ser distinguida 
     * @return 	Retorna uma lista com objetos distintos
     * @deprecated Usar o metodo distinctCollection ou o metodo getDistinctCollection 
     */
    public static final Collection distinct(Collection list) {
    	Collection  finalList 		= new ArrayList();
    	Collection  distinctList	= new ArrayList();
    	
    	try {
			for (Iterator i = list.iterator(); i.hasNext();) {
				Object 		type	= i.next();
				Collection	all 	= findAll(list, type);
				
				if (all.size() > 1 && find(distinctList, type) == null) 
					distinctList.add(type);
				else 
					finalList.add(type);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!distinctList.isEmpty()) {
			finalList.addAll(distinctList);
		}
		return finalList;
    }

    /**
     * Aplica um distinct a uma cole��o passada como par�metro.
     * 
     * @param	collection cole��o a ser distinguida
     * @return	cole��o distinguida
     */
    public static final Collection getDistinctCollection(Collection collection) {

    	Collection distinct = collection != null ? new ArrayList(collection) : collection;
    
   		distinctCollection(distinct);
     
    	return distinct;
    }
    
    /**
	 * Aplica um distinct a uma cole��o passada como par�metro.  	 
     * 
     * @param collection cole��o a ser distinguida
     */
    public static final void distinctCollection(Collection<? extends Object> collection) {
    	if (collection != null && !collection.isEmpty()) {
       		Set<? extends Object> distinctSet = new HashSet<Object>(collection);

       		collection.clear();

       		((Collection<Object>)collection).addAll(distinctSet);
    	}
    }
    
}

















