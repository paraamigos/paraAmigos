package framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anota��o para classes Entity Bean que idicam os atributos que ser�o utilizados para realizar a pesquisa.
 *
 * S� deve ser utilizada como anota��o de campos.
 * <BR><BR>
 * Exemplo:
 * <P><DD>@SearchField(operador=SearchField.LIKE)
 * <BR><DD>private String nome;</P></DD>
 * 
 * @author Luiz Alberto
 * 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD)
public @interface SearchField {
	public static final String WHERE_CLAUSE		= " where ";
	public static final String AND_CLAUSE		= " and ";
	public static final String LIKE_ESQUERDA	= "like_esquerda";
	public static final String LIKE_DIREITA 	= "like_direita";
	public static final String IGUAL			= " = ";
	public static final String MAIOR			= " > ";
	public static final String MAIOR_IGUAL		= " >= ";
	public static final String MENOR			= " < ";
	public static final String MENOR_IGUAL		= " <= ";
	public static final String LIKE				= " like ";

	
	/**
	 * Nome do campo que est� mapeado na entidade(BaseEntity).
	 * N�o � obrigat�rio, se n�o for preenchido ser� utilizado o nome do proprio atributo. 
	 * @return Nome do campo.
	 */
	String campo() default "";

	/**
	 * Operador da pesquisa(IGUAL, MAIOR, LIKE).
	 * Deve ser definido de acordo com as constantes definidas nesta classe. 
	 * @return Operador.
	 */
	String operador() default IGUAL;

	/**
	 * Define se a pesquisa vai diferenciar maiusculas de minusculas.
	 * N�o � obrigat�rio, o padr�o � n�o diferenciar maiusculas de minusculas. 
	 * @return Boolean se � case sensitive.
	 */
	boolean ignoreCase() default false;

		
	/**
	 * Define a posi��o de ordena��o(cl�usula order by).
	 * Definir a order atrav�s de numeros inteiros, os menores n�mero viram primeiro na ordem.
	 * @return Numero de ordena��o.
	 */
	int ordem() default 0;

	/**
	 * Indica se a ordem � ascendente ou descendente.
	 * O padr�o � ascendente.
	 * @return Boolean se � ascendente.
	 */
	boolean ordemAscendente() default true;
	

	/**
	 * Label do campo.
	 * Ser� mostrado no t�tulo
	 * N�o � obrigat�rio, se n�o for preenchido ser� utilizado o nome do proprio atributo. 
	 * @return Label do campo.
	 */
	String label() default "";
	

}
