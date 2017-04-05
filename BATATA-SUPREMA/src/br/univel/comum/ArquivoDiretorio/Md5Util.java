package br.univel.comum.ArquivoDiretorio;

import java.io.*;
import java.security.MessageDigest;

/**
 * Com base no código encontrado em:
 * http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java/304350
 *
 */
public class Md5Util {

	private static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	/**
	 * Retorna o md5 a partir do nome do arquivo.
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String getMD5Checksum(String filename) {
		byte[] b;
		try {
			b = createChecksum(filename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public static void main(String args[]) {
		
		String md5 = Md5Util.getMD5Checksum("bull.jpg");
		System.out.println(md5);

	}
}