package friends.model.service;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;

import framework.model.service.BaseApplicationService;
import friends.model.entity.Usuario;
import friends.model.exception.EmailNotFoundException;
import friends.model.exception.LoginException;
import friends.util.SecurityUtil;

@Named
@Stateless
public class UsuarioService extends BaseApplicationService {

	private static final long serialVersionUID = 4209461691622816791L;
	
	
	
	public void saveUsuario(Usuario usuario) {
		criptografaSenha(usuario);
		
		super.save(usuario);
	}

	public Usuario verificaLogin(Usuario login) 
					throws	EmailNotFoundException,
							LoginException {
		criptografaSenha(login);
		
		Usuario usu = (Usuario)findByNamedQueryOneResult(
									Usuario.findExistEmail, 
									new Object[]{
										login.getEmail()
									}
								);
		if (usu == null) {
			throw new EmailNotFoundException();
		}

		usu = (Usuario) findByNamedQueryOneResult(
							Usuario.findExistUsuario, 
							new Object[]{
								login.getEmail(),
								login.getSenha()
							}
						);
		if (usu == null) {
			throw new LoginException("msg_senha_invalida", true);
		}
		return usu;
	}

	private void criptografaSenha(Usuario usuario) {
		try {
			String senhaCriptografada = SecurityUtil.criptografa(usuario.getSenha());
			
			usuario.setSenha(senhaCriptografada);
			
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}
	
}












