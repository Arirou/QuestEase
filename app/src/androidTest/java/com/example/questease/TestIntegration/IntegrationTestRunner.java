package com.example.questease.TestIntegration;
import io.cucumber.junit.CucumberOptions;

@CucumberOptions(glue = { "com.mytest.steps" }, tags = "~@wip" , features = { "features" })
public class IntegrationTestRunner {
}
