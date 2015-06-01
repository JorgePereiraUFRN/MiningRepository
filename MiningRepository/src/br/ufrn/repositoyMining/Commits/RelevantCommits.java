package br.ufrn.repositoyMining.Commits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.kohsuke.github.GHRepository;

import br.ufrn.repositoyMining.githubAPI.TokenOAuth;
import br.ufrn.reposytoryMining.metrics.model.Commit;

public class RelevantCommits {

	private String owner;
	private String repository;

	public RelevantCommits(String owner, String repository) {
		super();
		this.owner = owner;
		this.repository = repository;
	}

	private class ValueComparator implements Comparator<String> {

		private Map<String, List<Commit>> map;

		public ValueComparator(Map<String, List<Commit>> map) {
			this.map = map;
		}

		public int compare(String a, String b) {
			Integer s1 = map.get(a).size();
			Integer s2 = map.get(b).size();

			if (s1 > s2)
				return -1;
			if (s1 < s2)
				return 1;
			return 0;
		}
	}

	// retorna um map ordenado de acordo com onúmer de commmits de cada usuário
	// (usuário com maior numero de commits vem primeiro)
	public Map<String, List<Commit>> getRelevantCommitsOfUser()
			throws IOException, InterruptedException, ExecutionException {

		// ordena o map de acordo com o numero de commits do usuário
		Map<String, List<Commit>> map = getCommitsByUser();
		Map<String, List<Commit>> sortedMap = new TreeMap<String, List<Commit>>(
				new ValueComparator(map));

		sortedMap.putAll(map);

		// ordena a lista de commits de cada usuário de acordo commu numero de
		// modificações em cada commit
		for (String user : sortedMap.keySet()) {

			List<Commit> commitsUser = sortCommitsByRelevance(sortedMap
					.get(user));

			// sortedMap.get(user).clear();
			// sortedMap.get(user).addAll(commitsUser);

			System.out.println(" =====  Commits de:  " + user + " =====");

			for (Commit c : commitsUser) {
				System.out.println("hash: " + c.getCommit().getSha()
						+ " modificações: "
						+ c.getCommit().getStats().getTotal()+"\n url: "+c.getCommit().getUrl());
				if (c.getPrevius() != null) {
					System.out.println("previus: "
							+ c.getPrevius().getCommit().getSha() + "\n");
				}
			}

		}

		return sortedMap;
	}

	private Map<String, List<Commit>> getCommitsByUser() throws IOException, InterruptedException, ExecutionException {

		Set<Commit> commits = getAllCommits();

		Map<String, List<Commit>> commitsByUser = new HashMap<String, List<Commit>>();

		for (Commit c : commits) {

			// System.out.println( c.getCommit().getAuthor().getHtmlUrl());

			if (c.getCommit().getAuthor() != null) {
				String key = c.getCommit().getAuthor().getHtmlUrl();

				if (commitsByUser.get(key) == null) {
					List<Commit> listCommits = new ArrayList<>();
					listCommits.add(c);
					commitsByUser.put(key, listCommits);
				} else {
					List<Commit> listCommits = commitsByUser.get(key);
					listCommits.add(c);
				}

			}
		}

		return commitsByUser;
	}

	private List<Commit> sortCommitsByRelevance(List<Commit> listCommits) {

		Commit commits[] = listCommits.toArray(new Commit[listCommits.size()]);

		Arrays.sort(commits, new Comparator<Commit>() {
			@Override
			public int compare(Commit arg0, Commit arg1) {

				int modificacoes1 = arg0.getCommit().getStats().getTotal();
				int modificacoes2 = arg1.getCommit().getStats().getTotal();

				if (modificacoes1 > modificacoes2)
					return -1;
				if (modificacoes1 < modificacoes2)
					return 1;
				return 0;
			}

		});

		return Arrays.asList(commits);
	}

	private Set<Commit> getAllCommits() throws IOException, InterruptedException, ExecutionException {

		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(TokenOAuth.getToken());

		final CommitService cservice = new CommitService(client);

		// RepositoryId repId = RepositoryId.create("spring-projects",
		// "spring-boot");
		final RepositoryId repId = RepositoryId.create(owner, repository);
		// RepositoryId repId = RepositoryId.create("eclipse", "smarthome");

		List<RepositoryCommit> rc = cservice.getCommits(repId);

		//List<Commit> commits = new ArrayList<Commit>();

		Commit commit = null;
		Commit previus = null;
		int cont = 0;
		// faz o commit apontar para o commit anterior (depois s lista vaiser
		// rearrajada e preciaremos desse ponteiro)
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		Map<Commit, Future<RepositoryCommit>> commits = new HashMap<>();
		
		for (final RepositoryCommit c : rc) {

			Commit aux = commit;

			commit = new Commit();
			// pega as informações do commit
				
			Future<RepositoryCommit> f = executor.submit(new Callable<RepositoryCommit>() {
				@Override
				public RepositoryCommit call() throws Exception {
					return cservice.getCommit(repId, c.getSha());
				}
			});
				
			
			if (aux != null) {
				aux.setPrevius(commit);
			}

			commits.put(commit, f);

			// recuperar ate 100 commits
			if (cont++ == 200) {
				break;
			}

		}
	
		for(Commit c: commits.keySet()){
			
			c.setCommit(commits.get(c).get());
		}
		
		
		return commits.keySet();

	}

	public static void main(String args[]) throws IOException, InterruptedException, ExecutionException {

		RelevantCommits r = new RelevantCommits("junit-team", "junit");

		r.getRelevantCommitsOfUser();

		/*
		 * for(Commit c : getAllCommits()){
		 * System.out.println(c.getCommit().getSha
		 * ()+" anterior: "+c.getPrevius().getCommit().getSha()); }
		 */
	}

}
