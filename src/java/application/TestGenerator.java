/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import beans.Scene;
import beans.TestInstance;
import beans.Testcase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.Convert;

/**
 *
 * @author gplatono
 */
public class TestGenerator {
    
    private static String[] relations = {
        "above", 
        "below", 
        "to the right",
        "to the left",
        "in front of",
        "behind",
        "near",
        "at",
        "in",
        "over",
        "under",
        "between",
        "on",
        "touching"};
    public TestGenerator(String sceneDirectory) {
        
    }
    
    public static TestInstance generate(String appPath) {
        Testcase testcase = new Testcase();
        TestInstance testInstance = new TestInstance();
        PrintWriter writer = null;
        String datasetPath = appPath + "scenes/";        
        
        try{
        writer = new PrintWriter(appPath + "Log", "UTF-8");
        }
        catch(Exception ex) {
        }
        
        try {               
            //ArrayList<Scene> scenes = JDBCHelper.getScenes();
            //List<String> scenePaths = scenes.stream()
                //.map(i -> i.getPath())
                //.collect(Collectors.toList());
            ArrayList<Testcase> testcases = JDBCHelper.getEnabledTestcases();
        
            Random rand = new Random();
            testcase = testcases.get(rand.nextInt(testcases.size()));
            testInstance.setTestcase(testcase);
            Scene testScene = JDBCHelper.getSceneById(testcase.getSceneID());
            //testInstance.setScenePath("scenes" + File.separator + scenePaths.get(testInstance.getTestcase().getSceneID() - 1));
            testInstance.setImagePath(testScene.getPath());
        
            String testQuery = null;                        
            
            if(testInstance.getTestcase().getQueryType() == 0) { 
                testQuery = "Is " + testInstance.getTestcase().getRelatum() + " " + 
                        testInstance.getTestcase().getRelation() + " " + testInstance.getTestcase().getReferent1()+ "?";
            }
            else {
                testQuery = "Where is " + testInstance.getTestcase().getRelatum() + " in the presented scene? Please describe its location relative to other objects. Use the following relations only:<br> ";
                for (String rel : relations) {
                    testQuery += "<b>" + rel + "</b><br>";
                }
            }               

            testInstance.setQuery(testQuery);
            }
            catch(Exception ex) {     
                writer.println(ex.getMessage());
            }
            return testInstance;
    }
    
}
