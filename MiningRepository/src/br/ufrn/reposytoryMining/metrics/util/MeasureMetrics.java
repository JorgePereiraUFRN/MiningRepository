package br.ufrn.reposytoryMining.metrics.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;

import nl.rug.jbi.jsm.core.JSMCore;
import nl.rug.jbi.jsm.core.calculator.MetricResult;
import nl.rug.jbi.jsm.core.pipeline.MetricPreparationException;
import nl.rug.jbi.jsm.frontend.Frontend;
import nl.rug.jbi.jsm.metrics.ckjm.CKJM;
import nl.rug.jbi.jsm.util.ClassDiscoverer;

public class MeasureMetrics implements Frontend {

	private final JSMCore core;
	private final Set<String> input;
	private volatile boolean isDone = false;

	private final Table<String, Class, Object> resultsClass = HashBasedTable
			.create();
	private final Table<String, Class, Object> resultsPackage = HashBasedTable
			.create();
	private final Table<String, Class, Object> resultsCollection = HashBasedTable
			.create();

	public MeasureMetrics(String pathProject) {
		this.core = new JSMCore();
		input = new HashSet<String>();
		input.add(pathProject);

		// CKJM
		try {
			core.registerMetricCollection(new CKJM());
			// Package metrics
			core.registerMetricCollection(new nl.rug.jbi.jsm.metrics.packagemetrics.PackageMetrics());
		} catch (MetricPreparationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public Table<String, Class, Object> getResultsClass() {
		return resultsClass;
	}

	public Table<String, Class, Object> getResultsPackage() {
		return resultsPackage;
	}

	public Table<String, Class, Object> getResultsCollection() {
		return resultsCollection;
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

}
