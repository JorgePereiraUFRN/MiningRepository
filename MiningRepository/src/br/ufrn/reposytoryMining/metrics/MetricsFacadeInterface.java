/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.reposytoryMining.metrics;

import java.util.List;

import br.ufrn.reposytoryMining.metrics.model.ClassMetrics;
import br.ufrn.reposytoryMining.metrics.model.PackageMetrics;

/**
 *
 * @author jorge
 */
public interface MetricsFacadeInterface {
    
    void calcMetrics();
    
   List<ClassMetrics> getClassMetrics();
    
   List<PackageMetrics> getPackageMetrics();
    
}
