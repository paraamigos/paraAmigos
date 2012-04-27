package friends.model.service;

import java.security.MessageDigest;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;

import framework.model.service.BaseApplicationService;
import friends.model.entity.Usuario;
import friends.model.exception.LoginException;

@Named
@Stateless
public class UsuarioService extends BaseApplicationService {

	private static final long serialVersionUID = 4209461691622816791L;
	
	private static final String ALGORIT_SHA_256 = "SHA-256";
	private static final String ENCODE_UTF_8 = "UTF-8";
	
	
	
	public void saveUsuario(Usuario usuario) {
		criptografaSenha(usuario);
		
		super.save(usuario);
	}

	private void criptografaSenha(Usuario usuario) {
		try {
			byte[] senha = usuario.getSenha().getBytes(ENCODE_UTF_8);
			
			MessageDigest messageDigest = MessageDigest.getInstance(ALGORIT_SHA_256);
			byte[] cripto = messageDigest.digest(senha);
			String senhaCripto = new String(cripto, ENCODE_UTF_8);
			
			usuario.setSenha(senhaCripto);
			
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}
	

	public Usuario verificaLogin(Usuario login) throws LoginException {
		criptografaSenha(login);
		
		Usuario usu = (Usuario)findByNamedQueryOneResult(
									Usuario.findExistLogin, 
									new Object[]{
										login.getEmail(),
										login.getSenha()
									}
								);
		if (usu == null) {
			throw new LoginException();
		}
		
		return usu;
	}

}












