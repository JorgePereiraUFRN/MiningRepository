package br.ufrn.repositoyMining.githubAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class GitHubService {
	private final GitHubClient client;
	private final CommitService cservice;
	private RepositoryId repId;

	public GitHubService() {
		client = new GitHubClient();
		cservice = new CommitService(client);
		client.setOAuth2Token(TokenOAuth.getToken());
	}

	public void setRepository(String repOwner, String repName) {
		repId = RepositoryId.create(repOwner, repName);
	}

	List<String> getListClassesModifiedByCommit(String commitSha) {
		List<String> output = new ArrayList<String>();
		try {
			RepositoryCommit repCommit = cservice.getCommit(repId, commitSha);
			List<CommitFile> files = repCommit.getFiles();
			for (Iterator<CommitFile> iterator = files.iterator(); iterator
					.hasNext();) {
				CommitFile commitFile = iterator.next();
				String className;
				try {
					className = extractClassName(commitFile.getFilename());
					output.add(className);
				} catch (IsNotAClassException e) {
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	public static String extractClassName(String filename)
			throws IsNotAClassException {
		String[] splited = filename.split("/");
		String className = splited[splited.length - 1];
		if (!className.contains(".java")) {
			throw new IsNotAClassException();
		}
		return className.replace(".java", "");
	}
	
	public static void main(String[] args){
		String isClass = "MiningRepository/src/br/ufrn/reposytoryMining/metrics/util/PlotGraphic.java";
		String isNotClass = "MiningRepository/libs/org.eclipse.egit.github.core-4.0.0-SNAPSHOT-javadoc.jar";
		
		try {
			String output1 = extractClassName(isClass);
			System.out.println(output1);
		} catch (IsNotAClassException e) {
			System.out.println("Error");
		}
		try {
			String output2 = extractClassName(isNotClass);
			System.out.println(output2);
		} catch (IsNotAClassException e) {
			System.out.println("Correct");
		}
		
		GitHubService service = new GitHubService();
		service.setRepository("JorgePereiraUFRN","MiningRepository");
		List<String> files = service.getListClassesModifiedByCommit("711bf895c0f77a96a347ad6fcfa896ee92a7abff");
		System.out.println("Classes: ");
		for (String string : files) {
			System.out.println(string);
		}
		
	}

}