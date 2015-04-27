package br.ufrn.repositoyMining.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

public class FindColaborators {

	public static void main(String[] args) {
		try {
			GitHubClient client = new GitHubClient();
			client.setOAuth2Token("9e9ed0cd365691a55ab3c63ad8a86447f3560d9e");

			CommitService cservice = new CommitService(client);

			// RepositoryId repId = RepositoryId.create("spring-projects","spring-boot");
			//RepositoryId repId = RepositoryId.create("JorgePereiraUFRN","MiningRepository");
		    RepositoryId repId = RepositoryId.create("eclipse", "smarthome");
			List<RepositoryCommit> rc;
			Map<String, Integer> map = new HashMap<String, Integer>();
			rc = cservice.getCommits(repId);
			if (!rc.isEmpty()) {
				for (Iterator iterator = rc.iterator(); iterator.hasNext();) {
					RepositoryCommit repositoryCommit = (RepositoryCommit) iterator
							.next();
					//RepositoryCommit temp = cservice.getCommit(repId, repositoryCommit.getSha());
					//String name = temp.getCommit().getAuthor().getName();
					String name = null;
					try{
					 name = repositoryCommit.getCommitter().getLogin();
					}catch(NullPointerException ex){
						
					}
					if(!map.containsKey(name)){
						map.put(name, 1);
						System.out.println("inserindo: "+name);
					}else{
						map.replace(name, map.get(name)+1);
					}
				}
			} else {
				System.out.println("lista vazia");
			}
			
			for (Map.Entry<String, Integer > entry : map.entrySet()) {
		    	 System.out.println("User : " + entry.getKey() 
                       + " no. commits : " + entry.getValue());
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
