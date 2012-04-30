package friends.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries ({
	@NamedQuery(
		name  = "Usuario.findExistEmail",
		query = "select u " +
				"from 	Usuario u " +
				"where	u.email = ? "
		 
	),
	@NamedQuery(
		name  = "Usuario.findExistUsuario",
		query = "select u " +
				"from 	Usuario u " +
				"where	u.email = ? " +
				"and 	u.senha = ?"
		 
	)
})
public class Usuario implements Serializable {

	private static final long serialVersionUID = -3901064097251122735L;
	
	public static final String findExistUsuario = "Usuario.findExistUsuario"; 

	public static final String findExistEmail = "Usuario.findExistEmail"; 

	private String email;
	private String nome;
	private String senha;
	
	
	
	
	/**
	 * @return the email
	 */
	@Id
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}
	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	
	
	
	
}
