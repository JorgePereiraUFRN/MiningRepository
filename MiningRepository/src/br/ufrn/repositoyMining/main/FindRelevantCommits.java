package br.ufrn.repositoyMining.main;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

public class FindRelevantCommits {

	public static void main(String[] args) {

		try {
			GitHubClient client = new GitHubClient();
			client.setOAuth2Token("9e9ed0cd365691a55ab3c63ad8a86447f3560d9e");
			
			
			CommitService cservice = new CommitService(client);
			
			//RepositoryId repId = RepositoryId.create("spring-projects", "spring-boot");
			//RepositoryId repId = RepositoryId.create("JorgePereiraUFRN", "MiningRepository");
			RepositoryId repId = RepositoryId.create("eclipse", "smarthome");
			
			List<RepositoryCommit> rc = cservice.getCommits(repId);
			Map<Integer, String> treeMap = new TreeMap<Integer, String>(
		    		new Comparator<Integer>() {
		    			@Override
		    			public int compare(Integer arg0, Integer arg1){
		    				return arg1.compareTo(arg0);
		    			}
					});
			if (!rc.isEmpty()) {
				
				for (Iterator iterator = rc.iterator(); iterator.hasNext();) {
					RepositoryCommit repositoryCommit = (RepositoryCommit) iterator
							.next();
					RepositoryCommit temp  = cservice.getCommit(repId, repositoryCommit.getSha());
					treeMap.put(temp.getStats().getTotal(), repositoryCommit.getSha());
								
				}
			} else {
				System.out.println("lista vazia");
			}
			
		    
		    for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
		    	 System.out.println("Changes : " + entry.getKey() 
                        + " SHA1 do commit : " + entry.getValue());
			} 
 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
