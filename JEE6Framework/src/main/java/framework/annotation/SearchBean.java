package framework.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anota��o para classes BaseCRUDBean que idicam o mapeamento geral para busca.
 * <BR>
 * Esta anota��o deve ser utilizadas em CRUDBeans para indicar as propriedades gerais de busca.   
 * <BR>
 * S� deve ser utilizada como anota��o de classes.
 * <BR><BR>
 * Exemplo:
 * <P><DD>@SearchBean(pagingSize=20, isPaging=true)
 * <BR><DD>public class ClienteBean extends BaseCRUDBean{</P></DD>
 * 
 * @author Luiz Alberto
 * 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface SearchBean {

	/**
	 * Define se o grid vai conter paginacao.
	 * O padr�o � sim para vir poucas linhas de cada vez.
	 *  
	 * @return Se o grid e paginavel.
	 */
	boolean isPaging() default false;

	/**
	 * Define a quantidade registros por pagina.
	 * O padr�o � 10.
	 *  
	 * @return Tamanho em do grid.
	 */
	int pagingSize() default 10;



}
