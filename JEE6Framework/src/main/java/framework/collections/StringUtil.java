package framework.collections;

import org.apache.commons.lang.StringUtils;


/**
 * Utilitario para Strings
 * 
 * @author DBA Eng. de Sistemas
 */
public class StringUtil {

	
	/**
	 * Construtor privado para evitar 
	 * instancias desnecessarias
	 */
	private StringUtil() {
	}

	/**
	 * Remove acentos das vogais de uma String
	 * 
	 * @param  palavra String a ser tratada
	 * @return String sem acentos
	 */
	public static String retiraAcento(String palavra) {
		String semAcento = null;
		
		if (StringUtils.isNotBlank(palavra)) {
			semAcento = "";
			for (int i = 0; i < palavra.length(); i++) {
				char c = palavra.charAt(i);
				
				switch (c) {
					case 'á': semAcento += 'a'; break;
					case 'Á': semAcento += 'A'; break;
					case 'à': semAcento += 'a'; break;
					case 'À': semAcento += 'A'; break;
					case 'â': semAcento += 'a'; break;
					case 'Â': semAcento += 'A'; break;
					case 'ã': semAcento += 'a'; break;
					case 'Ã': semAcento += 'A'; break;
					case 'é': semAcento += 'e'; break;
					case 'É': semAcento += 'E'; break;
					case 'ê': semAcento += 'e'; break;
					case 'Ê': semAcento += 'E'; break;
					case 'í': semAcento += 'i'; break;
					case 'Í': semAcento += 'I'; break;
					case 'ó': semAcento += 'o'; break;
					case 'Ó': semAcento += 'O'; break;
					case 'ô': semAcento += 'o'; break; 
					case 'Ô': semAcento += 'O'; break;
					case 'õ': semAcento += 'o'; break;
					case 'Õ': semAcento += 'O'; break;
					case 'ú': semAcento += 'u'; break;
					case 'Ú': semAcento += 'U'; break;
					default: semAcento += c;
				}
			}
		}
		return semAcento != null ? semAcento : palavra;
	}
	
}
