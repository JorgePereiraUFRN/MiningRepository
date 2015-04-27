package br.ufrn.repositoyMining.main;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

import br.ufrn.repositoyMining.githubAPI.TokenOAuth;

public class Teste {

	public static void main(String[] args) {

		try {
			GitHubClient client = new GitHubClient();
			client.setOAuth2Token(TokenOAuth.getToken());
			
			
			CommitService cservice = new CommitService(client);
			
			//RepositoryId repId = RepositoryId.create("spring-projects", "spring-boot");
			RepositoryId repId = RepositoryId.create("JorgePereiraUFRN", "MiningRepository");
			
			List<RepositoryCommit> rc = cservice.getCommits(repId);
			int i = 0;
			if (!rc.isEmpty()) {
				for (Iterator iterator = rc.iterator(); iterator.hasNext();) {
					RepositoryCommit repositoryCommit = (RepositoryCommit) iterator
							.next();
					System.out.println(repositoryCommit.getSha());
					
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
