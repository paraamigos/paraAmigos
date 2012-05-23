package framework.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation para campos obrigat&oacute;rios
 * 
 * @author Luiz Sodré
 * @since 05/09/2011
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({
	ElementType.FIELD,
	ElementType.METHOD
})
public @interface Required {

	/**
	 * Define a chave da mensagem de erro no ResourceBundle
	 * 
	 * @return chave da mensagem de erro 
	 */
	String messageKey() default "";
	
	
	/**
	 * Define a chave do label ou nome do campo no ResourceBundle
	 *  
	 * @return chave do label ou nome do campo
	 */
	String labelFieldKey() default "";

	
}
