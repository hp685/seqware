/**
 * 
 */
package net.sourceforge.seqware.queryengine.tools.importers.workers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import net.sourceforge.seqware.queryengine.backend.model.Coverage;
import net.sourceforge.seqware.queryengine.backend.model.Variant;
import net.sourceforge.seqware.queryengine.backend.store.Store;
import net.sourceforge.seqware.queryengine.backend.store.impl.BerkeleyDBStore;
import net.sourceforge.seqware.queryengine.tools.importers.Importer;
import net.sourceforge.seqware.queryengine.tools.importers.VariantImporter;

/**
 * @author boconnor
 *
 */
public class ImportWorker extends Thread {

  String workerName = null;
  Importer pmi = null;
  Store store = null;
  String input = null;
  boolean compressed = false;
  int minCoverage;
  int maxCoverage;
  float minSnpQuality;
  boolean includeSNV;
  int fastqConvNum;
  boolean includeIndels;
  boolean includeCoverage = false;
  int binSize = 0;

  public ImportWorker() { }
  
  public ImportWorker(String workerName, Importer pmi, Store store, String input, 
      boolean compressed, int minCoverage, int maxCoverage, float minSnpQuality, boolean includeSNV,
      int fastqConvNum, boolean includeIndels, boolean includeCoverage, int binSize) {
    this.workerName = workerName;
    this.pmi = pmi;
    this.store = store;
    this.input = input;
    this.compressed = compressed;
    this.minCoverage = minCoverage;
    this.maxCoverage = maxCoverage;
    this.minSnpQuality = minSnpQuality;
    this.includeSNV = includeSNV;
    this.fastqConvNum = fastqConvNum;
    this.includeIndels = includeIndels;
    this.includeCoverage = includeCoverage;
    this.binSize = binSize;
  }

  public void run() { }
 
  // autogenerated
  
  public String getWorkerName() {
    return workerName;
  }

  public void setWorkerName(String workerName) {
    this.workerName = workerName;
  }

  public Importer getPmi() {
    return pmi;
  }

  public void setPmi(Importer pmi) {
    this.pmi = pmi;
  }

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public boolean isCompressed() {
    return compressed;
  }

  public void setCompressed(boolean compressed) {
    this.compressed = compressed;
  }

  public int getMinCoverage() {
    return minCoverage;
  }

  public void setMinCoverage(int minCoverage) {
    this.minCoverage = minCoverage;
  }

  public int getMaxCoverage() {
    return maxCoverage;
  }

  public void setMaxCoverage(int maxCoverage) {
    this.maxCoverage = maxCoverage;
  }

  public float getMinSnpQuality() {
    return minSnpQuality;
  }

  public void setMinSnpQuality(float minSnpQuality) {
    this.minSnpQuality = minSnpQuality;
  }

  public boolean isIncludeSNV() {
    return includeSNV;
  }

  public void setIncludeSNV(boolean includeSNV) {
    this.includeSNV = includeSNV;
  }

  public int getFastqConvNum() {
    return fastqConvNum;
  }

  public void setFastqConvNum(int fastqConvNum) {
    this.fastqConvNum = fastqConvNum;
  }

  public boolean isIncludeIndels() {
    return includeIndels;
  }

  public void setIncludeIndels(boolean includeIndels) {
    this.includeIndels = includeIndels;
  }

  public boolean isIncludeCoverage() {
    return includeCoverage;
  }

  public void setIncludeCoverage(boolean includeCoverage) {
    this.includeCoverage = includeCoverage;
  }

  public int getBinSize() {
    return binSize;
  }

  public void setBinSize(int binSize) {
    this.binSize = binSize;
  }
  
}