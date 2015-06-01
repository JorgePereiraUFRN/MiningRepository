package br.ufrn.reposytoryMining.metrics.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadProject {

	public static File downloadFromURL(String stringUrl, String pathLocal,
			String hash) throws IOException {

		// Encapsula a URL num objeto java.net.URL
		URL url = new URL(stringUrl);

		String nomeArquivoLocal = url.getPath();

		// Cria streams de leitura (este metodo ja faz a conexao)...
		InputStream is = url.openStream();

		// ... e de escrita.
		FileOutputStream fos = new FileOutputStream(pathLocal + hash);

		// Le e grava byte a byte.
		int umByte = 0;
		while ((umByte = is.read()) != -1) {
			fos.write(umByte);
		}

		is.close();
		fos.close();

		// apos criar o arquivo fisico, retorna referencia para o mesmo
		return new File(pathLocal + hash);

	}

	public static void main(String args[]) {

		// gravaArquivoDeURL("https://github.com/JorgePereiraUFRN/MiningRepository/archive/6a1115cbf44a57fac856c14bd4ba87700fd445b1.zip",
		// "/home/jorge/projetos_git/");
	}

}
