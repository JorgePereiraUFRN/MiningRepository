/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.reposytoryMining.metrics;

import br.ufrn.repositoyMining.Commits.RelevantCommits;
import br.ufrn.repositoyMining.Commits.SourceCodeCommit;
import br.ufrn.reposytoryMining.exceptions.retrieveCommitExeption;
import br.ufrn.reposytoryMining.metrics.model.ClassMetrics;
import br.ufrn.reposytoryMining.metrics.model.Commit;
import br.ufrn.reposytoryMining.metrics.model.Metric;
import br.ufrn.reposytoryMining.metrics.model.PackageMetrics;
import br.ufrn.reposytoryMining.metrics.util.Calc;
import br.ufrn.reposytoryMining.metrics.util.CompileSorces;
import br.ufrn.reposytoryMining.metrics.util.DownloadProject;
import br.ufrn.reposytoryMining.metrics.util.MeasureMetrics;
import br.ufrn.reposytoryMining.metrics.util.PlotGraphic;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.MergeStatus;

import nl.rug.jbi.jsm.core.JSMCore;
import nl.rug.jbi.jsm.core.calculator.MetricResult;
import nl.rug.jbi.jsm.core.pipeline.MetricPreparationException;
import nl.rug.jbi.jsm.frontend.Frontend;
import nl.rug.jbi.jsm.metrics.ckjm.CA;
import nl.rug.jbi.jsm.metrics.ckjm.CBO;
import nl.rug.jbi.jsm.metrics.ckjm.CKJM;
import nl.rug.jbi.jsm.metrics.ckjm.DIT;
import nl.rug.jbi.jsm.metrics.ckjm.LCOM;
import nl.rug.jbi.jsm.metrics.ckjm.NOC;
import nl.rug.jbi.jsm.metrics.ckjm.NPM;
import nl.rug.jbi.jsm.metrics.ckjm.RFC;
import nl.rug.jbi.jsm.metrics.ckjm.WMC;
import nl.rug.jbi.jsm.metrics.packagemetrics.IIPED;
import nl.rug.jbi.jsm.metrics.packagemetrics.IIPUD;
import nl.rug.jbi.jsm.metrics.packagemetrics.IPCI;
import nl.rug.jbi.jsm.metrics.packagemetrics.IPSC;
import nl.rug.jbi.jsm.metrics.packagemetrics.PF;
import nl.rug.jbi.jsm.util.ClassDiscoverer;

/**
 *
 * @author jorge
 */
public class MetricsFacade implements /* Frontend, */MetricsFacadeInterface {

	private static Map<Metric, Double> idealVelues = new HashMap<>();

	public MetricsFacade() {
		idealVelues.put(Metric.CA, 7.0);
		idealVelues.put(Metric.NOC, 17.0);
		idealVelues.put(Metric.LCOM, 2.0);
		idealVelues.put(Metric.WMC, 14.0);
		idealVelues.put(Metric.NPM, 8.0);
		idealVelues.put(Metric.DIT, 2.0);
		idealVelues.put(Metric.RFC, 27.0);
		idealVelues.put(Metric.CBO, 7.0);
	}

	private synchronized void calcMetrics(MeasureMetrics measure) {
		if (!measure.isDone()) {
			measure.getResultsClass().clear();
			measure.getResultsCollection().clear();
			measure.getResultsPackage().clear();
			measure.init();
		}
	}

	@Override
	public Map<String, ClassMetrics> getClassMetrics(String pahtProject) {

		MeasureMetrics measure = new MeasureMetrics(pahtProject);

		Map<String, ClassMetrics> metrics = new HashMap<String, ClassMetrics>();

		if (!measure.isDone()) {

			calcMetrics(measure);
			while (!measure.isDone()) {
				try {
					Thread.sleep(1000 * 1);
				} catch (InterruptedException ex) {
					Logger.getLogger(MetricsFacade.class.getName()).log(
							Level.SEVERE, null, ex);
				}

			}

		}

		Map<String, Map<Class, Object>> map = measure.getResultsClass()
				.rowMap();
		ClassMetrics metricsClass = null;

		for (String key : map.keySet()) {

			metricsClass = new ClassMetrics();

			metricsClass.setClasse(key);

			Map<Class, Object> m = map.get(key);

			for (Class c : m.keySet()) {

				try {

					// não me pergunte o pq dessa gambiarra, só sei q ta
					// funcinando!
					if (c.getName().equals(CA.class.getName())) {
						metricsClass.setCA((((Integer) m.get(c)) * 1.0) * 1.0);
					} else if (c.getName().equals(CBO.class.getName())) {
						metricsClass.setCBO(((Integer) m.get(c)) * 1.0);
					} else if (c.getName().equals(DIT.class.getName())) {
						metricsClass.setDIT(((Integer) m.get(c)) * 1.0);
					} else if (c.getName().equals(LCOM.class.getName())) {
						metricsClass.setLCOM(((Integer) m.get(c)) * 1.0);
					} else if (c.getName().equals(NOC.class.getName())) {
						metricsClass.setNOC(((Integer) m.get(c)) * 1.0);
					} else if (c.getName().equals(NPM.class.getName())) {
						metricsClass.setNPM(((Integer) m.get(c)) * 1.0);
					} else if (c.getName().equals(RFC.class.getName())) {
						metricsClass.setRFC(((Integer) m.get(c)) * 1.0);
					} else if (c.getName().equals(WMC.class.getName())) {
						metricsClass.setWMC(((Integer) m.get(c)) * 1.0);
					}

				} catch (ClassCastException e) {
					// depois logar
					System.out.println(e);
				}
			}

			// pega o nome da classe (nome nao qualificado)
			int v = key.lastIndexOf('.');
			String className = key.substring(v + 1);
			metrics.put(className, metricsClass);

		}
		return metrics;
	}

	@Override
	public Map<String, PackageMetrics> getPackageMetrics(String pahtProject) {

		Map<String, PackageMetrics> metricsPackage = new HashMap<>();

		MeasureMetrics measure = new MeasureMetrics(pahtProject);

		if (!measure.isDone()) {

			calcMetrics(measure);
			while (!measure.isDone()) {
				try {
					Thread.sleep(1000 * 1);
				} catch (InterruptedException ex) {
					Logger.getLogger(MetricsFacade.class.getName()).log(
							Level.SEVERE, null, ex);
				}

			}

		}
		Map<String, Map<Class, Object>> map = measure.getResultsPackage()
				.rowMap();
		PackageMetrics metrics = null;

		for (String key : map.keySet()) {

			metrics = new PackageMetrics();
			metrics.setPackageName(key);

			Map<Class, Object> m = map.get(key);

			for (Class c : m.keySet()) {

				try {

					if (c.getName().equals(IIPED.class.getName())) {
						metrics.setIIPED((Double) m.get(c));
					} else if (c.getName().equals(IIPUD.class.getName())) {
						metrics.setIIPUD((Double) m.get(c));
					} else if (c.getName().equals(IPCI.class.getName())) {
						metrics.setIPCI((Double) m.get(c));
					} else if (c.getName().equals(IPSC.class.getName())) {
						metrics.setIPSC((Double) m.get(c));
					} else if (c.getName().equals(PF.class.getName())) {
						metrics.setPF((Double) m.get(c));
					}
				} catch (ClassCastException e) {
					// depois logar
					System.out.println(e);
				}

			}
			metricsPackage.put(key, metrics);
		}

		return metricsPackage;

	}

	@Override
	public Map<Metric, Double> getAverageMetricsPoject(String pahtProject) {

		Map<String, ClassMetrics> classMetrics = getClassMetrics(pahtProject);

		return Calc.getAverageClassMetrics(classMetrics);

	}

	@Override
	public void plotGraphics(Map<String, String> pathVersions, Metric[] metrics) {

		Map<String, Map<Metric, Double>> metricsProjects = new HashMap<>();

		for (String version : pathVersions.keySet()) {

			Map<Metric, Double> metricsClass = getAverageMetricsPoject(pathVersions
					.get(version));

			System.out.println("metricas da versao "+version);
			
			for(Metric m: metricsClass.keySet()){
				System.out.println(m +": "+ metricsClass.get(m));
			}
			
			metricsProjects.put(version, metricsClass);

			Scanner s = new Scanner(System.in);
			System.out.println("digite algo e pressione entrer para continuar");
			s.next();
		}

		PlotGraphic plot = null;

		for (Metric m : metrics) {

			// values of metrics by revision
			Map<String, Double> values = new HashMap<>();

			for (String vers : metricsProjects.keySet()) {

				// pega as metricas de uma versao do projeto
				Map<Metric, Double> metricValues = metricsProjects.get(vers);
				// poe o mapa a versao do projeto e o valor da metrica pra aqela
				// versao
				values.put(vers, metricValues.get(m));
			}

			plot = new PlotGraphic("Metric: " + m, m.toString(),
					idealVelues.get(m), values);

			plot.plot();
		}

	}

	@Override
	public Set<Commit> downloadRelevantCommits(String ownerRepository,
			String repository, String pathCodes) throws retrieveCommitExeption {

		RelevantCommits relevantCommits = new RelevantCommits(ownerRepository,
				repository);

		try {
			Map<String, List<Commit>> commits = relevantCommits
					.getRelevantCommitsOfUser();

			Map<String, String> downloadedCommits = new HashMap();

			Set<Commit> mostRelevantCommits = new HashSet<Commit>();

			ExecutorService pool = Executors.newCachedThreadPool();

			Set<Future<String>> futures = new HashSet<>();

			int contUsers = 1;
			for (String user : commits.keySet()) {

				// ordena os commits do usuário de acordo com o numero de
				// modificações
				Commit commitsUser[] = commits.get(user).toArray(
						new Commit[commits.get(user).size()]);

				Arrays.sort(commitsUser, new Comparator<Commit>() {
					@Override
					public int compare(Commit arg0, Commit arg1) {

						int modificacoes1 = arg0.getCommit().getStats()
								.getTotal();
						int modificacoes2 = arg1.getCommit().getStats()
								.getTotal();

						if (modificacoes1 > modificacoes2)
							return -1;
						if (modificacoes1 < modificacoes2)
							return 1;
						return 0;
					}

				});

				int contCommits = 1;
				for (Commit c : commitsUser) {

					// o commit ja foi baixado pq era o previus de um outro q
					// tbm foi baixado
					if (downloadedCommits.get(c.getCommit().getSha()) == null) {

						Callable<String> sourceCode = new SourceCodeCommit(
								"/home/jorge/" + pathCodes, c, repository);

						Future<String> future = pool.submit(sourceCode);

						futures.add(future);

						downloadedCommits.put(c.getCommit().getCommit()
								.getSha(), null);

						mostRelevantCommits.add(c);
					}

					// baixa o codigo da revisão anterior para depois fazer a
					// comparação entre as metricas
					if (c.getPrevius() != null
							&& downloadedCommits.get(c.getPrevius().getCommit()
									.getSha()) == null) {

						Callable<String> sourceCode = new SourceCodeCommit(
								"/home/jorge/" + pathCodes, c.getPrevius(),
								repository);
						Future<String> future = pool.submit(sourceCode);

						futures.add(future);

						downloadedCommits.put(c.getPrevius().getCommit()
								.getCommit().getSha(), null);

					}

					// baixar no maximo 3 commits de cada usuário
					if (contCommits++ == 2) {
						break;
					}

				}

				// baixar no maximo de 5 usuários
				if (contUsers++ == 2) {
					break;
				}

			}

			for (Future<String> f : futures) {
				String pathCode = f.get();
				System.out.println("commit downloaded : " + pathCode);
			}

			return mostRelevantCommits;

		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new retrieveCommitExeption();
		}

	}

	@Override
	public Map<Metric, Double> getProgressMetrics(Commit commit) {

		if(commit == null || commit.getPrevius() == null)
			return null;
		
		Map<String, ClassMetrics> classMetrics = getClassMetrics(commit.getPathSourceCode());

		Map<String, ClassMetrics> oldClassMetrics = getClassMetrics(commit.getPrevius().getPathSourceCode());

		List<CommitFile> changedClass = commit.getCommit().getFiles();

		Map<Metric, Double> progressMetrics = new HashMap<>();

		ClassMetrics averageProjectMetrics = null;
		
		ClassMetrics currentM = null, oldM = null;
		

		MetricContribuition mContribuition = new MetricContribuition();
		Map<Metric, Double> contribuition = null;
		
		double CA = 0,  NOC = 0, LCOM = 0, WMC = 0, NPM = 0, DIT = 0, RFC = 0, CBO = 0;
		int cont = 0;
		
		for (CommitFile file : changedClass) {

			if (file.getFilename().contains(".java")) {

				String[] aux = file.getFilename().split("/");

				if (aux != null && aux.length > 0) {

					String className = aux[aux.length - 1].replace(".java", "");

					currentM = classMetrics.get(className);
					
					if (file.getStatus().equalsIgnoreCase("modified")) {
						
						oldM = oldClassMetrics.get(className);
						
	
					} else if (file.getStatus().equalsIgnoreCase("added")) {

						if (averageProjectMetrics == null) {

							averageProjectMetrics = getAvgValuesProjectMetrics(oldClassMetrics);	
							
						}
						
						oldM = averageProjectMetrics;
					}
					
					//System.out.println("---------------------current "+currentM);
					
					if(currentM != null && oldM != null){
						
						contribuition = mContribuition
								.calcMetricContibuition(currentM, oldM);
						
						CA += contribuition.get(Metric.CA);
						NOC += contribuition.get(Metric.NOC);
						LCOM += contribuition.get(Metric.LCOM);
						WMC += contribuition.get(Metric.WMC);
						NPM += contribuition.get(Metric.CA);
						DIT += contribuition.get(Metric.DIT);
						RFC += contribuition.get(Metric.RFC);
						CBO += contribuition.get(Metric.CBO);
						
						cont ++;
						contribuition = null;
						//System.out.println(progressMetrics);
					}else{
						System.out.println("------- metricas nulas! --------"+className+"\n"
								+ " status "+file.getStatus()+" commit: "+commit.getCommit().getSha()
								+"\nprevius :"+commit.getPrevius().getCommit().getSha());
						//System.out.println("current "+currentM);
						//System.out.println("old "+oldM);
					}
						

				}
						

			}
			
			
		}
		
		if(cont > 0){
			
			progressMetrics.put(Metric.CA, CA / cont);
			progressMetrics.put(Metric.CBO, CBO / cont);
			progressMetrics.put(Metric.DIT, DIT / cont);
			progressMetrics.put(Metric.LCOM, LCOM /cont);
			progressMetrics.put(Metric.NOC, NOC / cont);
			progressMetrics.put(Metric.NPM, NPM / cont);
			progressMetrics.put(Metric.RFC, RFC / cont);
			progressMetrics.put(Metric.WMC, WMC / cont);
		}else{
			//nennhum arquivo de codigo fonte foi modificado
			
			progressMetrics.put(Metric.CA, 0.0);
			progressMetrics.put(Metric.CBO, 0.0);
			progressMetrics.put(Metric.DIT, 0.0);
			progressMetrics.put(Metric.LCOM, 0.0);
			progressMetrics.put(Metric.NOC, 0.0);
			progressMetrics.put(Metric.NPM, 0.0);
			progressMetrics.put(Metric.RFC, 0.0);
			progressMetrics.put(Metric.WMC, 0.0);
		}
		

		return progressMetrics;

	}

	private ClassMetrics getAvgValuesProjectMetrics(
			Map<String, ClassMetrics> oldClassMetrics) {
		ClassMetrics averageProjectMetrics;
		Map<Metric, Double> metrics = Calc
				.getAverageClassMetrics(oldClassMetrics);

		averageProjectMetrics = new ClassMetrics();
		averageProjectMetrics.setCA(metrics.get(Metric.CA));
		averageProjectMetrics.setCBO(metrics.get(Metric.CBO));
		averageProjectMetrics.setDIT(metrics.get(Metric.DIT));
		averageProjectMetrics.setLCOM(metrics.get(Metric.LCOM));
		averageProjectMetrics.setNOC(metrics.get(Metric.NOC));
		averageProjectMetrics.setNPM(metrics.get(Metric.NPM));
		averageProjectMetrics.setRFC(metrics.get(Metric.RFC));
		averageProjectMetrics.setWMC(metrics.get(Metric.WMC));
		return averageProjectMetrics;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws MetricPreparationException,
			retrieveCommitExeption, IOException {

		MetricsFacadeInterface metricsFacade = new MetricsFacade();

		// testeGetClassMetrics(metricsFacade);

		// testGetPackageMetrics(metricsFacade);

		// testGetAverageMetrics(metricsFacade);

		// testPlotGraphic(metricsFacade);

		// baixarCodigoFonte(metricsFacade);

		Set<Commit> relevanteCommits = metricsFacade.downloadRelevantCommits(
				"junit-team", "junit", "projetos_git/Junit-Commits/");

		Set<String> pathProjects = new HashSet<>();

		for (Commit c : relevanteCommits) {
			pathProjects.add(c.getPathSourceCode());
			pathProjects.add(c.getPrevius().getPathSourceCode());
		}

		new CompileSorces().compileSourceCode(pathProjects,
				"projetos_git/Junit-Commits/");

		Scanner s = new Scanner(System.in);

		System.out
				.println("Exetue o script gerado e  em seguida pressione qualquer tecla para continuar");
		s.next();

		for (Commit c : relevanteCommits) {

			if (c != null && c.getPrevius() != null) {
				/*System.out.println("Contribuição do commit "
						+ c.getCommit().getSha() + " desenvolvedor: "
						+ c.getCommit().getAuthor().getHtmlUrl());*/

				Map<Metric, Double> progress = metricsFacade
						.getProgressMetrics(c);

				String msg = "Contribuição do commit "
						+ c.getCommit().getSha() + " desenvolvedor: "
						+ c.getCommit().getAuthor().getHtmlUrl()+"\n";
				
				for (Metric m : progress.keySet()) {
					msg += m.toString() + ": " + progress.get(m)+"\n";
					//System.out.println(m.toString() + ": " + progress.get(m));
				}

				JOptionPane.showMessageDialog(null, msg);
			} else {
				System.out.println("Commit anteior nulo");

			}
		}

		System.out.println("FIM");
	}

	private static void baixarCodigoFonte(MetricsFacadeInterface metricsFacade) {
		try {
			// metricsFacade.downloadRelevantCommits("junit-team", "junit");
			metricsFacade.downloadRelevantCommits("JorgePereiraUFRN",
					"MiningRepository", "projetos_git/Junit-Commits/");
		} catch (retrieveCommitExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void testPlotGraphic(MetricsFacadeInterface metricsFacade) {
		Map<String, String> versions = new HashMap<>();

		// versions.put("4.2.19",
		// "/home/jorge/Área de Trabalho/hinernate/hibernate-core-4.2.19.Final.jar");
		// versions.put("4.2.18",
		// "/home/jorge/Área de Trabalho/hinernate/hibernate-core-4.2.18.Final.jar");
		// versions.put("4.2.7",
		// "/home/jorge/Área de Trabalho/hinernate/hibernate-core-4.2.7.Final.jar");
		// versions.put("4.2.6",
		// "/home/jorge/Área de Trabalho/hinernate/hibernate-core-4.2.6.Final.jar");
		versions.put("4.2.5",
				"/home/jorge/Área de Trabalho/hinernate/hibernate-core-4.2.5.Final.jar");
		versions.put("4.2.4",
				"/home/jorge/Área de Trabalho/hinernate/hibernate-core-4.2.4.Final.jar");
		versions.put("4.2.3",
				"/home/jorge/Área de Trabalho/hinernate/hibernate-core-4.2.3.Final.jar");
		versions.put("4.2.2",
				"/home/jorge/Área de Trabalho/hinernate/hibernate-core-4.2.2.Final.jar");

		versions.put("3.5",
				"/home/jorge/Área de Trabalho/hinernate/hibernate-core-3.5.0-Final.jar");
		versions.put("3.6",
				"/home/jorge/Área de Trabalho/hinernate/hibernate-core-3.6.0.CR2.jar");
		versions.put("5.0",
				"/home/jorge/Área de Trabalho/hinernate/hibernate-core-5.0.0.Beta1.jar");

		Metric[] m = new Metric[] { Metric.CBO, Metric.DIT, Metric.LCOM,
				Metric.NOC, Metric.NPM };

		metricsFacade.plotGraphics(versions, m);
	}

	private static void testGetAverageMetrics(
			MetricsFacadeInterface metricsFacade) {
		Map<Metric, Double> average = metricsFacade
				.getAverageMetricsPoject("/home/jorge/projetos_git/github-api-github-api-1.68/target/github-api-1.68.jar");
		printMap(average);
	}

	private static void testGetPackageMetrics(
			MetricsFacadeInterface metricsFacade) {
		Map<String, PackageMetrics> packageMetrics = metricsFacade
				.getPackageMetrics("/home/jorge/projetos_git/github-api-github-api-1.68/target/github-api-1.68.jar");

		System.out.println("\n package metrics");
		printList(packageMetrics.values());
	}

	private static void testeGetClassMetrics(
			MetricsFacadeInterface metricsFacade) {
		Map<String, ClassMetrics> classMetrics = metricsFacade
				.getClassMetrics("/home/jorge/projetos_git/github-api-github-api-1.68/target/github-api-1.68.jar");

		System.out.println("\n classes metrics " + classMetrics.size());
		printList(classMetrics.values());
	}

	private static void printList(Collection list) {

		for (Object o : list) {
			System.out.println(o.toString());
		}

	}

	private static void printMap(Map m) {

		for (Object key : m.keySet()) {
			System.out.println(key + ": " + m.get(key));
		}

	}

}
