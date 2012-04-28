package framework.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

/**
 * Classe utilit�ria para reflex�o em beans
 * 
 * @author Luiz Alberto Sodr&eacute;
 */
@SuppressWarnings("rawtypes")
public class BeansUtil {

	/**
	 * Construtor privado
	 */
	private BeansUtil() {
	}


	/**
	 * Retorna os campos de um objeto
	 * 
	 * @param  object objeto
	 * @return os campos de um objeto
	 */
	public static Field[] getDeclaredFields(Object object) {
		Class classe = object.getClass();
		
		return classe.getDeclaredFields();
	}
	
	/**
	 * Retorna o valor do campo de um objeto
	 * 
	 * @param  field  campo do objeto
	 * @param  object objeto
	 * @return valor do campo de um objeto
	 */
	public static Object getFieldValue(Field field, Object object) {
		try {
			field.setAccessible(true);
			
			return field.get(object);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Formata a propriedade do bean para que possa ser 
	 * invocada por reflection
	 * 
	 * @param 	propertyName propriedade do bean 
	 * @return	propriedade do bean formatada
	 */
	public static String formatPropertyName(String propertyName) {
		
		return	!StringUtils.isEmpty(propertyName) ?
				propertyName.substring(0, 1).toUpperCase() +
				propertyName.substring(1, propertyName.length()) :
				propertyName; 
	}

	/**
	 * Formata a propriedade do bean para que possa ser 
	 * utilizada com a conven&ccedil;&atilde;o JavaBean
	 * 
	 * @param  propertyName propriedade do bean 
	 * @return propriedade do bean formatada
	 */
	public static String formatGetPropertyName(String propertyName) {
		
		return	!StringUtils.isEmpty(propertyName) ?
				propertyName.substring(3, 4).toLowerCase() +
				propertyName.substring(4, propertyName.length()) :
				propertyName; 
	}
	
	/**
	 * Retorna o Tipo retornado pelo metodo anotado 
	 * com @Id
	 * 
	 * @param 	classe Entity Bean mapeado com JPA
	 * @return	Tipo retornado pelo metodo anotado com @Id
	 */
	public static Class getIdMethodReturnType(Class classe) {
		Class returnType = null;
		
		for (Method method : classe.getDeclaredMethods()) {
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof Id) {
					returnType = method.getReturnType();
				}
			}
		}
		if (returnType == null) {
			for (Field field : classe.getDeclaredFields()) {
				for (Annotation annotation : field.getAnnotations()) {
					if (annotation instanceof Id) {
						field.setAccessible(true);
						returnType = field.getType();
					}
				}
			}
		}
		return returnType;
	}
	
	public static Class getGenericSuperclass(Class klass) {
		ParameterizedType paramType = (ParameterizedType)klass.getGenericSuperclass();
		
		return (Class) paramType.getActualTypeArguments()[0];
	}
	
	/**
	 * Localiza o id do Entity Bean
	 * 
	 * @param  entity Entity Bean
	 * @return id do Entity Bean
	 */
	public static Object getEntityId(Object entity) {
		Class classe	= entity.getClass();
		Object entityId = null;
		try {
			for (Field field : classe.getDeclaredFields()) {
				Annotation[] annotations = field.getAnnotations();
				entityId = getByIdAnnotation(annotations,entity,null,field);
				
				if (entityId != null) {
					break;
				}
			}
			if (entityId == null) {
				for (Method method : classe.getDeclaredMethods()) {
					Annotation[] annotations = method.getAnnotations();
					entityId = getByIdAnnotation(annotations,entity,method,null);
					
					if (entityId != null) {
						break;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entityId;
	}

	/**
	 * Retorna o id do EntityBean
	 * 
	 * @param  annotations	anotações da Entidade
	 * @param  entity		EntityBean
	 * @param  method		metodo corrente da entidade
	 * @param  field		atributo corrente da entidade
	 * @return o id do EntityBean
	 */
	private static Object getByIdAnnotation(
					Annotation[] annotations,
					Object entity,
					Method method,
					Field field
				) {
		Object id = null;
		
		try {
			for (Annotation annotation : annotations) {
				if (annotation instanceof Id) {
					if (field != null) {
						field.setAccessible(true);
						id = field.get(entity);
					} else {
						id = method.invoke(entity);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return id;
	}

	/**
	 * Localiza o nome da propriedade id do Entity Bean
	 * 
	 * @param 	entity Entity Bean
	 * @return	nome da propriedade id
	 */
	public static String getEntityIdPropertyName(Object entity) {
		Class classe		= entity.getClass();
		String propertyName = null;
		
		for (Field field : classe.getDeclaredFields()) {
			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof Id) {
					propertyName = field.getName();
					break;
				}
			}
		}
		if (propertyName == null) {
			for (Method method : classe.getDeclaredMethods()) {
				for (Annotation annotation : method.getAnnotations()) {
					if (annotation instanceof Id) {
						propertyName = formatGetPropertyName(method.getName());
						break;
					}
				}
			}
		}
		return propertyName;
	}
	
	
}
