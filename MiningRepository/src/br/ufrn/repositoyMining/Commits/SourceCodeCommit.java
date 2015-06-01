package br.ufrn.repositoyMining.Commits;

import java.io.IOException;
import java.util.concurrent.Callable;

import br.ufrn.reposytoryMining.metrics.model.Commit;
import br.ufrn.reposytoryMining.metrics.util.DownloadProject;
import br.ufrn.reposytoryMining.metrics.util.LocalShell;

public class SourceCodeCommit implements Callable<String> {

	private String localPath;
	private Commit commit;
	
	

	public SourceCodeCommit(String localPath, Commit commit) {
		super();
		this.localPath = localPath;
		this.commit = commit;
	}

	// recupera o codigo fonte do commit e extrai o zip
	@Override
	public String call() throws Exception {
		
		downloadCommit(commit);
		
		LocalShell shell = new LocalShell();
		
		shell.executeCommand("unzip "+localPath+"/"+commit.getCommit().getSha()+".zip -d"+localPath);
		
		return localPath+"/"+commit.getCommit().getSha();
	}

	private void downloadCommit(Commit c) throws IOException {

		String urlDownload = c.getCommit().getUrl()
				.replace("https://api.", "https://").replace("/repos/", "/")
				.replace("/commits/", "/archive/")
				+ ".zip";

		DownloadProject downloadProject = new DownloadProject();

		downloadProject.downloadFromURL(urlDownload,
				localPath, c.getCommit()
						.getSha());
	}

}
