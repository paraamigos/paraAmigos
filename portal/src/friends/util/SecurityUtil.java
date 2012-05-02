package friends.util;

import java.security.MessageDigest;

public class SecurityUtil {

	private static final String ALGORIT_SHA_256 = "SHA-256";
	private static final String ENCODE_UTF_8 = "UTF-8";


	
	public static String criptografa(String input) throws Exception {
		byte[] bytes = input.getBytes(ENCODE_UTF_8);
		
		MessageDigest messageDigest = MessageDigest.getInstance(ALGORIT_SHA_256);
		byte[] cripto = messageDigest.digest(bytes);
		
		return new String(cripto, ENCODE_UTF_8);
	}
	
}
