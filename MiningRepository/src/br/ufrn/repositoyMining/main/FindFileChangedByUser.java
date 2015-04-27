package br.ufrn.repositoyMining.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

import br.ufrn.repositoyMining.githubAPI.TokenOAuth;

public class FindFileChangedByUser {
	public static void main(String[] args) {
		try {
			GitHubClient client = new GitHubClient();
			client.setOAuth2Token(TokenOAuth.getToken());

			CommitService cservice = new CommitService(client);

			// RepositoryId repId = RepositoryId.create("spring-projects","spring-boot");
			RepositoryId repId = RepositoryId.create("JorgePereiraUFRN","MiningRepository");
		    //RepositoryId repId = RepositoryId.create("eclipse", "smarthome");
			List<RepositoryCommit> rc;
			rc = cservice.getCommits(repId);
			if (!rc.isEmpty()) {
				for (Iterator iterator = rc.iterator(); iterator.hasNext();) {
					RepositoryCommit repositoryCommit = (RepositoryCommit) iterator
							.next();
					//RepositoryCommit temp = cservice.getCommit(repId, repositoryCommit.getSha());
					//String name = temp.getCommit().getAuthor().getName();
					System.out.println("Commit: "+repositoryCommit.getSha());
					System.out.println("Arquivos: ");
					RepositoryCommit temp = cservice.getCommit(repId, repositoryCommit.getSha());
					List<CommitFile> files = temp.getFiles();
					if(files == null){
						System.out.println("files null");
					}
					for (Iterator<CommitFile> iterator2 = files.iterator(); iterator2
							.hasNext();) {
						CommitFile commitFile = iterator2.next();
						System.out.println(commitFile.getFilename());
						
					}
					
				}
					
			} else {
				System.out.println("lista vazia");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
