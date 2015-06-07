package br.ufrn.repositoyMining.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.NMTOKENDatatypeValidator;

import br.ufrn.reposytoryMining.exceptions.retrieveCommitExeption;
import br.ufrn.reposytoryMining.metrics.MetricsFacade;
import br.ufrn.reposytoryMining.metrics.MetricsFacadeInterface;
import br.ufrn.reposytoryMining.metrics.model.Commit;
import br.ufrn.reposytoryMining.metrics.model.Metric;
import br.ufrn.reposytoryMining.metrics.util.CompileSorces;

public class Main {

	public static void main(String args[]) throws retrieveCommitExeption,
			IOException {

		MetricsFacadeInterface facade = new MetricsFacade();

		//progessMetricsByUser(facade);
		
		showMetricsReleases(facade);

	}



	private static void showMetricsReleases(MetricsFacadeInterface facade) {
		Map<String, String> versions = new HashMap<>();

		
		versions.put("4.11",
				"/home/jorge/Área de Trabalho/junit/junit-4.11.jar");
		versions.put("4.12.1",
				"/home/jorge/Área de Trabalho/junit/junit-4.12.1.jar");
		versions.put("4.12.2",
				"/home/jorge/Área de Trabalho/junit/junit-4.12.2.jar");
		versions.put("4.12.3",
				"/home/jorge/Área de Trabalho/junit/junit-4.12.3.jar");
		

		Metric[] m = new Metric[] { Metric.CBO, Metric.DIT, Metric.LCOM,
				Metric.NOC, Metric.NPM };

		facade.plotGraphics(versions, m);
	}
	
	

	private static void progessMetricsByUser(MetricsFacadeInterface facade)
			throws retrieveCommitExeption, IOException {
		String ownerRepository = "junit-team",
				repository = "junit",
				pathCodes = "projetos_git/Junit-Commits/"; //pasta onde devm ser salvos os codigos fontes baixados

		Set<Commit> commits = facade.downloadRelevantCommits(ownerRepository,
				repository, pathCodes);

		Set<String> pathProjects = new HashSet<>();

		for (Commit c : commits) {
			pathProjects.add(c.getPathSourceCode());
			pathProjects.add(c.getPrevius().getPathSourceCode());
		}

		new CompileSorces().compileSourceCode(pathProjects,
				"projetos_git/Junit-Commits/");

		Scanner s = new Scanner(System.in);

		System.out
				.println("Extraia os codigos, execute o script gerado e  em seguida digite algo para continuar");
		s.next();

		Map<String, List<Map<Metric, Double>>> progressUser = new HashMap<>();

		for (Commit c : commits) {

			Map<Metric, Double> progress = facade.getProgressMetrics(c);
			String user = c.getCommit().getCommitter().getHtmlUrl();

			if (progressUser.get(user) == null && progress != null) {
				List<Map<Metric, Double>> listsProgress = new ArrayList<Map<Metric, Double>>();
				listsProgress.add(progress);

				progressUser.put(user, listsProgress);
			} else {

				progressUser.get(user).add(progress);

			}

		}

		System.out.println("========= Contribuicoes do Usuário =========");
		

		for (String user : progressUser.keySet()) {

			System.out.println(user);
			
			double CA = 0, NOC = 0, LCOM = 0, WMC = 0, NPM = 0, DIT = 0, RFC = 0, CBO = 0;

			for (Map<Metric, Double> p : progressUser.get(user)) {
				
				CA += p.get(Metric.CA);
				NOC += p.get(Metric.NOC);
				LCOM += p.get(Metric.LCOM);
				WMC += p.get(Metric.WMC);
				NPM += p.get(Metric.NPM);
				DIT += p.get(Metric.DIT);
				RFC += p.get(Metric.RFC);
				CBO += p.get(Metric.CBO);

			}

			int size = progressUser.get(user).size();
			if (size > 0) {
				
				CA += CA / size;
				NOC += NOC / size;
				LCOM += LCOM / size;
				WMC += WMC / size;
				NPM += NPM / size;
				DIT += DIT / size;
				RFC += RFC / size;
				CBO = CBO / size;

				System.out.println("CA = " + CA);
				System.out.println("NOC = " + NOC);
				System.out.println("LCOM = " + LCOM);
				System.out.println("WMC = " + WMC);
				System.out.println("NPM = " + NPM);
				System.out.println("DIT = " + DIT);
				System.out.println("RFC = " + RFC);
				System.out.println("CBO = " + CBO);

				System.out.println(" \ntotal contribuition : "
						+ (CA + NOC + LCOM + WMC + NPM + DIT + RFC + CBO)
						/ 8);
			}
			
			System.out.println("================================================== \n\n");

		}
	}

}
