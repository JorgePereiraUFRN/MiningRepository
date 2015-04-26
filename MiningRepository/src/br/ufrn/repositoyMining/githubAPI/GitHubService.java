package br.ufrn.repositoyMining.githubAPI;

import java.io.IOException;
import java.util.List;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class GitHubService {
	private static GHRepository rep;
	private static GitHub github;
	
	List<String> getListClassesModifiedByAuthor(String repOwner, String repName , String authorName){
		List<String> output = null;
		try {
			github = GitHub.connect();
			rep = github.getRepository(repOwner+"/"+repName);
			
			
			System.out.println(rep.getFullName());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
}