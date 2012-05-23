package friends.util;

import framework.validator.BeanValidator;
import framework.validator.BeanValidatorException;
import friends.model.entity.Usuario;

public class MainTstUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Usuario u = new Usuario();
		//u.setEmail("sdf@sdfsd.df");
		u.setNome("Fulano da Silva");
		u.setSenha("xpto");
		
		try {
			BeanValidator.validate(u);
		} catch (BeanValidatorException e) {
			System.out.println(e.getMessageKey());
		}
	}

}
