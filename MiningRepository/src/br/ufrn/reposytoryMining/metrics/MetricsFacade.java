/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.reposytoryMining.metrics;

import br.ufrn.repositoyMining.Commits.RelevantCommits;
import br.ufrn.reposytoryMining.exceptions.retrieveCommitExeption;
import br.ufrn.reposytoryMining.metrics.model.ClassMetrics;
import br.ufrn.reposytoryMining.metrics.model.Commit;
import br.ufrn.reposytoryMining.metrics.model.Metric;
import br.ufrn.reposytoryMining.metrics.model.PackageMetrics;
import br.ufrn.reposytoryMining.metrics.util.Calc;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

					if (c.getName().equals(CA.class.getName())) {
						metricsClass.setCA((Integer) m.get(c));
					} else if (c.getName().equals(CBO.class.getName())) {
						metricsClass.setCBO((Integer) m.get(c));
					} /*
					 * else if (c.getName().equals(CBO.class.getName())) {
					 * metricsClass.setCBO((Float) m.get(c)); }
					 */else if (c.getName().equals(DIT.class.getName())) {
						metricsClass.setDIT((Integer) m.get(c));
					} else if (c.getName().equals(LCOM.class.getName())) {
						metricsClass.setLCOM((Integer) m.get(c));
					} else if (c.getName().equals(NOC.class.getName())) {
						metricsClass.setNOC((Integer) m.get(c));
					} else if (c.getName().equals(NPM.class.getName())) {
						metricsClass.setNPM((Integer) m.get(c));
					} else if (c.getName().equals(RFC.class.getName())) {
						metricsClass.setRFC((Integer) m.get(c));
					} else if (c.getName().equals(WMC.class.getName())) {
						metricsClass.setWMC((Integer) m.get(c));
					}

				} catch (ClassCastException e) {
					// depois logar
					System.out.println(e);
				}
			}

			metrics.put(key, metricsClass);

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

			metricsProjects.put(version, metricsClass);

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
			String repository) throws retrieveCommitExeption {

		RelevantCommits relevantCommits = new RelevantCommits(ownerRepository,
				repository);

		try {
			Map<String, List<Commit>> commits = relevantCommits
					.getRelevantCommitsOfUser();

			Map<String, String> downloadedCommits = new HashMap();

			Set<Commit> mostRelevantCommits = new HashSet<Commit>();

			for (String user : commits.keySet()) {
				
				
				int cont = 0;
				for (Commit c : commits.get(user)) {

					
					// o commit ja foi baixado pq era o previus de um outro q
					// tbm foi baixado
					if (downloadedCommits.get(c.getCommit().getSha()) == null) {

						DownloadCommit(c);

						downloadedCommits.put(c.getCommit().getCommit()
								.getSha(), null);
						mostRelevantCommits.add(c);
					}

					//baixa o codigo da revisão anterior para depois fazer a comparação entre as metricas
					if (c.getPrevius() != null && downloadedCommits.get(c.getPrevius().getCommit()
							.getSha()) == null) {

						DownloadCommit(c.getPrevius());

						downloadedCommits.put(c.getPrevius().getCommit()
								.getCommit().getSha(), null);

					}
					
					System.out.println(" ==== Commit baixado =="+
					"\n hash: "+c.getCommit().getSha()+" commiter: "+c.getCommit().getAuthor().getHtmlUrl());
					
					//baixar no maximo 5 commits de cada usuário
					if(cont++ == 5){
						break;
					}
				

				}

			}

		} catch (IOException e) {
			throw new retrieveCommitExeption();
		}

		return null;
	}

	private void DownloadCommit(Commit c) throws IOException {
		
		String urlDownload = c.getCommit().getUrl()
				.replace("https://api.", "https://").replace("/repos/", "/")
				.replace("/commits/", "/archive/")
				+ ".zip";

		DownloadProject downloadProject = new DownloadProject();
		
		downloadProject.downloadFromURL(urlDownload,
				"/home/jorge/projetos_git/Junit-Commits/", c.getCommit()
						.getSha());
	}

	public static void main(String[] args) throws MetricPreparationException {

		MetricsFacadeInterface metricsFacade = new MetricsFacade();

		// testeGetClassMetrics(metricsFacade);

		// testGetPackageMetrics(metricsFacade);

		// testGetAverageMetrics(metricsFacade);

		testPlotGraphic(metricsFacade);

		//baixarCodigoFonte(metricsFacade);

	}

	private static void baixarCodigoFonte(MetricsFacadeInterface metricsFacade) {
		try {
			metricsFacade.downloadRelevantCommits("junit-team", "junit");
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
