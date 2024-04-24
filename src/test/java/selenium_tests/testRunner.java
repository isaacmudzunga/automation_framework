package selenium_tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue = {"selenium_tests.steps"},
        plugin = {"pretty", "summary", "html:target/cucumber-reports"},
	    dryRun = true,
	    monochrome = true
)
public class testRunner {
}
