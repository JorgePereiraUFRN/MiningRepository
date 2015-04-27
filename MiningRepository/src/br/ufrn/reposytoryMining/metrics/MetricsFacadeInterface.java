/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.reposytoryMining.metrics;

import java.util.List;
import java.util.Map;

import br.ufrn.reposytoryMining.metrics.model.ClassMetrics;
import br.ufrn.reposytoryMining.metrics.model.Metric;
import br.ufrn.reposytoryMining.metrics.model.PackageMetrics;

/**
 *
 * @author jorge
 */
public interface MetricsFacadeInterface {
    
  // void calcMetrics();
    
   Map<String, ClassMetrics> getClassMetrics(String pahtProject);
    
   Map<String, PackageMetrics> getPackageMetrics(String pahtProject);
    
   Map<Metric, Double> getAverageMetricsPoject(String pahtProject);
   
   void plotGraphics(Map<String, String> pathVersions, Metric[] metrics);
   
}
