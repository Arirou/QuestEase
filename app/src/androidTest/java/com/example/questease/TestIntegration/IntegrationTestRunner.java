package com.example.questease.TestIntegration;


import androidx.test.runner.AndroidJUnitRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "com.example.TestIntegration"
)
public class IntegrationTestRunner extends AndroidJUnitRunner {
}