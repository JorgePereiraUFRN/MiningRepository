package br.ufrn.repositoyMining.main;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

public class TesteGitHubAPIForJava {
	private static GHRepository rep;
	private static GitHub github;

	public static void main(String[] args) {
		
		
		try {
			github = GitHub.connect();
			rep = github.getRepository("");
			//System.out.println(rep.getFullName());
			PagedIterable<GHCommit> picommit = rep.listCommits();
			if(picommit == null){
				System.out.println("null");
			}
			for (Iterator<GHCommit> iterator = picommit.iterator(); iterator.hasNext();) {
				GHCommit commit = iterator.next();
				System.out.println(commit.getLinesChanged());
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
