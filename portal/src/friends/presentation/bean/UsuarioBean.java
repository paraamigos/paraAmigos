package friends.presentation.bean;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import framework.presentation.bean.BaseApplicationBean;
import friends.model.entity.Usuario;
import friends.model.exception.LoginException;
import friends.model.service.UsuarioService;

@Named
@ConversationScoped
public class UsuarioBean extends BaseApplicationBean<Usuario> implements Serializable {

	private static final long serialVersionUID = -8384065833713279054L;
 
	@Inject
	private UsuarioService usuarioService;
	
	private Usuario login = new Usuario();
	
	
	
	public String logar() {
		String outcome = "home";
		
		try {
			Usuario usuario = usuarioService.verificaLogin(login);
			
			setEntity(usuario);
			
		} catch (LoginException e) {
			outcome = "validaRegistro";
		}
		
		return outcome;
	}
	
	public String save() {
		Usuario usuario = getEntity();
		
		usuarioService.saveUsuario(usuario);
		
		return "home";
	}

	public Usuario getLogin() {
		return login;
	}

	public void setLogin(Usuario login) {
		this.login = login;
	}

	
	
	
}
