package framework.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;

import framework.reflection.BeansUtil;

/**
 * Validador de Beans. Verifica, por exemplo,
 * campos obrigat&oacute;rios do Bean
 * 
 * @author DBA Eng. de Sistemas
 * @since 03/09/2011
 */
public class BeanValidator {

	
	/**
	 * Construtor default 
	 * 
	 */
	private BeanValidator() {
	}
	
	/**
 	 * Valida se todos os campos de um bean que est&atilde;o
	 * anotados com Required est&atilde;o preenchidos  
	 * 
	 * @param bean bean a ser validado
	 */
	public static void validateAll(Object bean) {
		Field[] fields 		= BeansUtil.getDeclaredFields(bean);
		BeanErrors errors	= new BeanErrors();
		
		for (Field field : fields) {
			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof Required) {
					Required required = (Required) annotation;
					
					try {
						validateRequiredValue(field, required, bean);
						
					} catch (BeanValidatorException vex) {
						String labelFieldKey = vex.getLabelFieldKey();
						String messageKey	 = vex.getMessageKey();
						
						errors.addError(
							new BeanError(
								labelFieldKey, messageKey
							)
						);
					}
				}
			}
		}
		if (errors.hasErrors()) {
			throw new BeanValidatorException(errors);
		}
	}
	
	
	/**
 	 * Valida se os campos de um bean que est&atilde;o
	 * anotados com Required est&atilde;o preenchidos  
	 * 
	 * @param bean bean a ser validado
	 */
	public static void validate(Object bean) {
		Field[] fields = BeansUtil.getDeclaredFields(bean);
		
		for (Field field : fields) {
			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof Required) {
					Required required = (Required) annotation;
					
					validateRequiredValue(field, required, bean);
					break;
				}
			}
		}
	}
	
	/**
	 * Valida se um campo String foi preenchido
	 * 
	 * @param field	campo	
	 * @param requiredAnnotation anota&ccedil;&atilde;o para campo obrigatorio
	 * @param bean bean a ser validado
	 */
	private static void validateRequiredValue(Field field,Required requiredAnnotation,Object bean) {
		Object value = BeansUtil.getFieldValue(field, bean);
		
		if (value instanceof String) {
			String stringValue = (String) value;
			
			if (StringUtils.isBlank(stringValue)) {
				throwBeanValidatorException(requiredAnnotation);
			}
		} else if (value == null) {
			throwBeanValidatorException(requiredAnnotation);
		}
	}


	/**
	 * Lan&ccedil;a a exce&ccedil;&atilde;o de valida&ccedil;&atilde;o
	 * 
	 * @param requiredAnnotation anota&ccedil;&atilde;o para campo obrigatorio
	 */
	private static void throwBeanValidatorException(Required requiredAnnotation) {
		String lblKey 		  = requiredAnnotation.labelFieldKey();
		String msgKey 		  = requiredAnnotation.messageKey();
		boolean notBlankLabel = StringUtils.isNotBlank(lblKey);
		boolean notBlankMsg   = StringUtils.isNotBlank(msgKey);
		
		if (notBlankLabel && notBlankMsg) {
			throw new BeanValidatorException(msgKey, lblKey);
		} else if (notBlankMsg) {
			throw new BeanValidatorException(msgKey);
		} else {
			throw new BeanValidatorException();
		}
	}

}








