/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.reposytoryMining.metrics;

import br.ufrn.reposytoryMining.metrics.model.ClassMetrics;
import br.ufrn.reposytoryMining.metrics.model.Metric;
import br.ufrn.reposytoryMining.metrics.model.PackageMetrics;
import br.ufrn.reposytoryMining.metrics.util.Calc;
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
public class MetricsFacade implements Frontend, MetricsFacadeInterface {

	private final JSMCore core;
	private final Set<String> input;
	private boolean isDone = false;

	private final Table<String, Class, Object> resultsClass = HashBasedTable
			.create();
	private final Table<String, Class, Object> resultsPackage = HashBasedTable
			.create();
	private final Table<String, Class, Object> resultsCollection = HashBasedTable
			.create();

	public MetricsFacade(String pathProject) throws MetricPreparationException {
		this.core = new JSMCore();
		input = new HashSet<String>();
		input.add(pathProject);

		// CKJM
		core.registerMetricCollection(new CKJM());
		// Package metrics
		core.registerMetricCollection(new nl.rug.jbi.jsm.metrics.packagemetrics.PackageMetrics());

	}

	@Override
	public void init() {
		final Set<String> classNames = FluentIterable.from(input)
				.transform(new Function<String, File>() {
					@Override
					public File apply(String fileName) {
						return new File(fileName);
					}
				}).filter(new Predicate<File>() {
					@Override
					public boolean apply(File file) {
						return file.exists();
					}
				}).transformAndConcat(new Function<File, Iterable<String>>() {
					@Override
					public Iterable<String> apply(File file) {
						try {
							return ClassDiscoverer.findClassNames(file);
						} catch (IOException e) {
							return ImmutableList.of();
						}
					}
				}).toSet();

		final URL[] dataSources = FluentIterable.from(input)
				.transform(new Function<String, File>() {
					@Override
					public File apply(String fileName) {
						return new File(fileName);
					}
				}).filter(new Predicate<File>() {
					@Override
					public boolean apply(File file) {
						return file.exists();
					}
				}).transform(new Function<File, URL>() {
					@Override
					public URL apply(File file) {
						try {
							return file.toURI().toURL();
						} catch (MalformedURLException e) {
							return null;
						}
					}
				}).filter(new Predicate<URL>() {
					@Override
					public boolean apply(URL url) {
						return url != null;
					}
				}).toArray(URL.class);

		core.process(this, classNames, dataSources);
	}

	@Override
	public void processResult(List<MetricResult> resultList) {
		for (final MetricResult result : resultList) {
			final Table<String, Class, Object> targetTable;

			switch (result.getScope()) {
			case CLASS:
				targetTable = resultsClass;
				break;
			case PACKAGE:
				targetTable = resultsPackage;
				break;
			case COLLECTION:
				targetTable = resultsCollection;
				break;
			default:
				throw new IllegalStateException("Unknown scope: "
						+ result.getScope());
			}
			targetTable.put(result.getIdentifier(), result.getMetricClass(),
					result.getValue());
		}
	}

	@Override
	public void signalDone() {
		isDone = true;

	}

	@Override
	public synchronized void calcMetrics() {
		if (!isDone) {
			resultsClass.clear();
			resultsCollection.clear();
			resultsPackage.clear();
			init();
		}
	}

	@Override
	public Map<String, ClassMetrics> getClassMetrics() {

		Map<String, ClassMetrics> metrics = new HashMap<String, ClassMetrics>();

		if (!isDone) {

			calcMetrics();
			while (!isDone) {
				try {
					Thread.sleep(1000 * 1);
				} catch (InterruptedException ex) {
					Logger.getLogger(MetricsFacade.class.getName()).log(
							Level.SEVERE, null, ex);
				}

			}

		}

		Map<String, Map<Class, Object>> map = resultsClass.rowMap();
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
	public Map<String, PackageMetrics> getPackageMetrics() {

		Map<String, PackageMetrics> metricsPackage = new HashMap<>();

		if (!isDone) {

			calcMetrics();
			while (!isDone) {
				try {
					Thread.sleep(1000 * 1);
				} catch (InterruptedException ex) {
					Logger.getLogger(MetricsFacade.class.getName()).log(
							Level.SEVERE, null, ex);
				}

			}

		}
		Map<String, Map<Class, Object>> map = resultsPackage.rowMap();
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
	public Map<Metric, Double> getAverageMetricsPoject() {

		Map<String, ClassMetrics> classMetrics = getClassMetrics();

		return Calc.getAverageClassMetrics(classMetrics);

	}

	@Override
	public void plotGraphics(Map<String, String> pathVersions, Metric[] metrics) {

		Map<String, Map<Metric, Double>> metricsProjects = new HashMap<>();

		
		for (String version : pathVersions.keySet()) {

			try {
				MetricsFacadeInterface facade = new MetricsFacade(pathVersions.get(version));

				Map<Metric, Double> metricsClass = facade
						.getAverageMetricsPoject();

				metricsProjects.put(version , metricsClass);

			} catch (MetricPreparationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

			plot = new PlotGraphic("Metric: " + m, m.toString(), 10, values);

			plot.plot();
		}

	}

	public static void main(String[] args) throws MetricPreparationException {

		MetricsFacadeInterface metricsFacade = new MetricsFacade(
				"/home/jorge/projetos_git/spring-boot-master/spring-boot/target/spring-boot-1.3.0.BUILD-SNAPSHOT.jar");

		// Map<String, ClassMetrics> classMetrics =
		// metricsFacade.getClassMetrics();

		// Map<String, PackageMetrics> packageMetrics =
		// metricsFacade.getPackageMetrics();

		// System.out.println("\n\nclasses");
		// printList(classMetrics.values());

		// System.out.println("\n\npacotes");
		// printList(packageMetrics.values());

		// Map<Metric, Double> average =
		// metricsFacade.getAverageMetricsPoject();
		// printMap(average);

		Map<String , String> versions = new HashMap<>();

		versions.put("1.0", "/home/jorge/projetos_git/spring-boot-master/spring-boot/target/spring-boot-1.3.0.BUILD-SNAPSHOT.jar");
		versions.put("1.0.1","/home/jorge/ec2.jar");
		versions.put("1.1","/home/jorge/workspace/Diver");
		versions.put("1.1.1", "/home/jorge/workspace/Email");
		versions.put("1.1.2","/home/jorge/workspace/loadDriver");

		Metric[] m = new Metric[] { Metric.CBO, Metric.DIT };

		metricsFacade.plotGraphics(versions, m);

		// metricsFacade.calcMetrics();

		// Thread.sleep(1000 * 10);

		/*
		 * Map<String, Map<Class, Object>> map = metricsFacade.getClassMetrics()
		 * .rowMap();
		 * 
		 * printMetrics(map);
		 * 
		 * System.out.println("\n\n Package metrics\n"); map =
		 * metricsFacade.getPackageMetrics().rowMap();
		 */

		// printMetrics(map);

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

	/*
	 * private static void printMetrics(Map<String, Map<Class, Object>> map) {
	 * 
	 * for (String key : map.keySet()) {
	 * 
	 * Map<Class, Object> m = map.get(key);
	 * 
	 * System.out.println("\n" + key);
	 * 
	 * for (Class c : m.keySet()) { System.out.println(c.getName() + "   " +
	 * m.get(c)); } } }
	 */
}
