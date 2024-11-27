package com.example.questease.TestIntegration;

import org.junit.runner.*;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "app/src/androidTest/java/com/example/questease/TestIntegration/features",
        glue = "com.example.TestIntegration"
)
public class RunCucumberTest {
}