/*
 * Copyright (C) 2012 SeqWare
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.seqware.queryengine.tutorial;

import com.github.seqware.queryengine.Constants;
import com.github.seqware.queryengine.factory.SWQEFactory;
import com.github.seqware.queryengine.impl.HBaseStorage;
import com.github.seqware.queryengine.model.FeatureSet;
import com.github.seqware.queryengine.model.Reference;
import com.github.seqware.queryengine.system.ReferenceCreator;
import com.github.seqware.queryengine.system.Utility;
import com.github.seqware.queryengine.system.importers.SOFeatureImporter;
import com.github.seqware.queryengine.util.SGID;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 * This is a quick and sample application built on top of our API, created for
 * Poster testing. It demonstrates query restrictions and exporting. Based on
 * VCFDumper. This will dump VCF files given a FeatureSet that was originally
 * imported from a VCF file.
 *
 * @author dyuen
 */
public class Poster {

    public static final String HG_19 = "hg_19";
    public static final int CUT_OFF = 3000;
    
    private String[] args;
    Map<String, String> keyValues = new HashMap<String, String>();

    public static void main(String[] args) throws IOException {
        Poster dumper = new Poster(args);
        dumper.benchmark();
    }
    
    public Poster(String[] args) {
        this.args = args;
    }

    public void benchmark() throws IOException {
        if (args.length != 2) {
            System.err.println(args.length + " arguments found");
            System.out.println(Poster.class.getSimpleName() + " <outputKeyValueFile> <input file dir>");
            System.exit(-1);
        }

        File outputFile = Utility.checkOutput(args[0]);

        // check if reference has been properly created
        Reference reference = SWQEFactory.getQueryInterface().getLatestAtomByRowKey("hg_19", Reference.class);
        if (reference == null) {
            SGID refID = ReferenceCreator.mainMethod(new String[]{HG_19});
            reference = SWQEFactory.getQueryInterface().getAtomBySGID(Reference.class, refID);
        }
       

        // record reference, starting disk space
        keyValues.put("referenceID", reference.getSGID().getRowKey());
        recordSpace("start");
        Utility.writeKeyValueFile(outputFile, keyValues);
        
        int count = 0;
        // go through all input files
        File fileDirectory = new File(args[1]);
        File[] listFiles = fileDirectory.listFiles();
        SGID fSet_sgid = null;
        for(File inputFile : listFiles){
            // record start and finish time
            Date startDate = new Date();
            keyValues.put(count + "-start-date-long" , Long.toString(startDate.getTime()));
            keyValues.put(count + "-start-date-human" , startDate.toString());
            
            // run without unnecessary parameters
            if (fSet_sgid == null){
                fSet_sgid =
                    SOFeatureImporter.runMain(new String[]{"-w", "VCFVariantImportWorker",
                        "-i", inputFile.getAbsolutePath(),
                        "-r", reference.getSGID().getRowKey()});
            } else{
                fSet_sgid =
                    SOFeatureImporter.runMain(new String[]{"-w", "VCFVariantImportWorker",
                        "-i", inputFile.getAbsolutePath(),
                        "-r", reference.getSGID().getRowKey(),
                        "-f", fSet_sgid.getRowKey()
                    });
            }
            
            FeatureSet fSet = SWQEFactory.getQueryInterface().getLatestAtomBySGID(fSet_sgid, FeatureSet.class);
            keyValues.put(count + "-featuresSet-id" , fSet.getSGID().getRowKey());
            keyValues.put(count + "-featuresSet-id-timestamp" , Long.toString(fSet.getSGID().getBackendTimestamp().getTime()));
            // runs count query
            
            keyValues.put(count + "-start-count-date-long" , Long.toString(System.currentTimeMillis()));
            long fsetcount = fSet.getCount();
            keyValues.put(count + "-features-loaded" , Long.toString(fsetcount));
            keyValues.put(count + "-end-count-date-long" , Long.toString(System.currentTimeMillis()));
            
            Date endDate = new Date();
            keyValues.put(count + "-end-date-long" , Long.toString(endDate.getTime()));
            keyValues.put(count + "-end-date-human" , endDate.toString());
            recordSpace(String.valueOf(count));
            Utility.writeKeyValueFile(outputFile, keyValues);
        }
    }

    private void recordSpace(String key) throws IOException {
        try{
        Configuration conf = new Configuration();
        HBaseStorage.configureHBaseConfig(conf);
        HBaseConfiguration.addHbaseResources(conf);
        FileSystem fs = FileSystem.get(conf);
        Path homeDirectory = fs.getHomeDirectory();
        Path root = homeDirectory.getParent().getParent();
        Path hbase = new Path(root, "hbase");
        ContentSummary contentSummary = fs.getContentSummary(hbase);
        long spaceConsumedinGB = convertToGB(contentSummary);
        keyValues.put(key + "-total-space-in-GB", Long.toString(spaceConsumedinGB));
        
        if (spaceConsumedinGB > CUT_OFF){
            return;
        }
        
        Path featureTable = new Path (hbase, "/"+Constants.NAMESPACE+".hbaseTestTable_v2.Feature."+HG_19);
        contentSummary = fs.getContentSummary(featureTable);
        spaceConsumedinGB = convertToGB(contentSummary);
        keyValues.put(key + "-feature-space-in-GB", Long.toString(spaceConsumedinGB));
        } catch (FileNotFoundException e){
            /** throw away, this is ok the first time **/
        }
        
    }

    private long convertToGB(ContentSummary contentSummary) {
        // odd, it seems like length reports the equivalent of "hadoop fs -du -s
        long spaceConsumedinGB = contentSummary.getLength()/1024/1024/1024/1024;
        return spaceConsumedinGB;
    }



}