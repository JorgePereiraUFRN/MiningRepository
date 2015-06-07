package br.ufrn.reposytoryMining.metrics.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class CompileSorces {

	public void compileSourceCode(Set<String> pathsCode, String pathProjects)
			throws IOException {

		FileWriter file = new FileWriter(new File("/home/jorge/"+pathProjects + "/script.sh"));
		PrintWriter writer = new PrintWriter(file);

		for (String pathCode : pathsCode) {

			String message = " compilando commit " + pathCode;
			writer.println("echo \""+message+"\"");
			writer.println("cd");
			writer.println("cd " + pathCode);
			writer.println("mvn compile");

		}

		file.close();

		LocalShell shell = new LocalShell();

		shell.executeCommand("chmod +x "+pathProjects+"script.sh");
		shell.executeCommand("./"+pathProjects+"script.sh");
		

	}

}
