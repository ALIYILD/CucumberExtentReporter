package com.cucumber.listener;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Cucumber custom format listener which generates ExtentsReport html file
 */
public class ExtentCucumberFormatter implements Reporter, Formatter {

    private ExtentReports extent;
    private ExtentTest featureTest;
    private ExtentTest scenarioTest;
    private LinkedList<Step> testSteps;

    public ExtentCucumberFormatter(File filePath) {

        if (!filePath.getPath().equals("")) {
            String reportPath = filePath.getPath();
            this.extent = new ExtentReports(reportPath);
        } else {
            this.extent = new ExtentReports("output/Run_" + System.currentTimeMillis() + "/report.html");
        }

        this.testSteps = new LinkedList<Step>();
    }

    public void before(Match match, Result result) {

    }

    public void result(Result result) {
        if ("passed".equals(result.getStatus())) {
            scenarioTest.log(LogStatus.PASS, testSteps.poll().getName(), "PASSED");
        } else if ("failed".equals(result.getStatus())) {
            scenarioTest.log(LogStatus.FAIL, testSteps.poll().getName(), result.getErrorMessage());
        } else if ("skipped".equals(result.getStatus())) {
            scenarioTest.log(LogStatus.SKIP, testSteps.poll().getName(), "SKIPPED");
        } else if ("undefined".equals(result.getStatus())) {
            scenarioTest.log(LogStatus.UNKNOWN, testSteps.poll().getName(), "UNDEFINED");
        }
    }

    public void after(Match match, Result result) {

    }

    public void match(Match match) {

    }

    public void embedding(String s, byte[] bytes) {

    }

    public void write(String s) {
        extent.close();
    }

    public void syntaxError(String s, String s1, List<String> list, String s2, Integer integer) {

    }

    public void uri(String s) {

    }

    public void feature(Feature feature) {
        featureTest = extent.startTest("Feature: " + feature.getName());
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {

    }

    public void examples(Examples examples) {

    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
        scenarioTest = extent.startTest("Scenario: " + scenario.getName());

        for (Tag tag : scenario.getTags()) {
            scenarioTest.assignCategory(tag.getName());
        }
    }

    public void background(Background background) {

    }

    public void scenario(Scenario scenario) {

    }

    public void step(Step step) {
        testSteps.add(step);
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {
        extent.endTest(scenarioTest);
        featureTest.appendChild(scenarioTest);
    }

    public void done() {

    }

    public void close() {

    }

    public void eof() {
        extent.endTest(featureTest);
        extent.flush();
    }

}
