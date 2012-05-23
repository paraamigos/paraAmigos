package friends.presentation.bean;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import framework.presentation.bean.BaseApplicationBean;
import framework.presentation.faces.FacesUtil;
import friends.model.entity.Usuario;
import friends.model.exception.EmailNotFoundException;
import friends.model.exception.LoginException;
import friends.model.exception.RegistroUsuarioException;
import friends.model.service.UsuarioService;

@Named
@ConversationScoped
public class UsuarioBean extends BaseApplicationBean<Usuario> implements Serializable {

	private static final long serialVersionUID = -8384065833713279054L;
 
	@Inject
	private UsuarioService usuarioService;
	
	private Usuario login = new Usuario();

	private String msgProcess;
	private String msgEmail;
	private String msgSenha;
	private String msgNome;
	


	public String save() {
		Usuario usuario = getEntity();
		String outcome = "home";
		
		try {
			validateRegistro(usuario);

			usuarioService.saveUsuario(usuario);

		} catch (RegistroUsuarioException e) {
			outcome = "validaRegistro";
		} catch (LoginException e) {
			outcome = "validaRegistro";
		}
		return outcome;
	}

	private void validateRegistro(Usuario usuario) 
			throws	RegistroUsuarioException, 
					LoginException {
		
		msgProcess 	 = FacesUtil.getResourceBundleMessage("msg", "msg_process_cad");
		String senha = usuario.getSenha();
		String nome  = usuario.getNome();
		
		if (StringUtils.isBlank(nome)) {
			msgNome = FacesUtil.getResourceBundleMessage("msg", "msg_nome_required");
			throw new RegistroUsuarioException();
		}
		validaLogin(usuario);
		
		if (senha.length() < 6) {
			msgSenha = FacesUtil.getResourceBundleMessage("msg", "msg_senha_menor6");
			throw new RegistroUsuarioException();
		}
	}
	
	public String logar() {
		String outcome = "home";
		
		try {
			validaLogin(login);
			
			Usuario usuario = usuarioService.verificaLogin(login);
			
			setEntity(usuario);
			
		} catch (LoginException e) {
			outcome = catchLoginException(e);
		} catch (EmailNotFoundException e) {
			outcome = catchEmailNotFoundException();
		}
		return outcome;
	}

	private String catchEmailNotFoundException() {
		msgProcess = FacesUtil.getResourceBundleMessage("msg", "msg_process_logar");
		
		msgEmail = FacesUtil.getResourceBundleMessage("msg", "msg_email_nao_cad");
	
		Usuario usuario = getEntity();
		usuario.setEmail(null);
		usuario.setSenha(null);
		
		return "validaRegistro";
	}

	private String catchLoginException(LoginException e) {
		if (e.getMessageKey() != null) {
			msgSenha = FacesUtil.getResourceBundleMessage(
								"msg", e.getMessageKey()
							);
		}
		return "index";
	}

	private void validaLogin(Usuario login) throws LoginException {
		String email = login.getEmail();
		String senha = login.getSenha();
		
		if (StringUtils.isBlank(email)) {
			msgEmail = FacesUtil.getResourceBundleMessage("msg", "msg_email_required");
			throw new LoginException();
		}
		if (StringUtils.isBlank(senha)) {
			msgSenha = FacesUtil.getResourceBundleMessage("msg", "msg_senha_required");
			throw new LoginException();
		}
	}
	
	public Usuario getLogin() {
		return login;
	}

	public void setLogin(Usuario login) {
		this.login = login;
	}

	public String getMsgEmail() {
		return msgEmail;
	}
	
	public String getMsgSenha() {
		return msgSenha;
	}

	public String getMsgNome() {
		return msgNome;
	}

	public String getMsgProcess() {
		return msgProcess;
	}
}
