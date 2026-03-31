package com.utils;

import com.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequenceStepHelper extends BasePage {

    private static final String[] ELEMENT_KEY_HEADERS = {"ElementName", "Name", "Key", "LocatorName", "Target"};
    private static final String[] ACTION_HEADERS = {"Action", "Command", "StepAction", "Operation"};
    private static final String[] VALUE_HEADERS = {"Value", "Text", "Input", "DataValue", "TargetValue"};
    private static final String[] DATA_KEY_HEADERS = {"DataKey", "TestDataKey", "ValueKey", "Key"};
    private static final String[] EXPECTED_HEADERS = {"Expected", "ExpectedValue", "Assert", "VerifyValue"};
    private static final String[] LOCATOR_TYPE_HEADERS = {"LocatorType", "By", "Type"};
    private static final String[] LOCATOR_VALUE_HEADERS = {"LocatorValue", "Locator", "Selector", "Value"};
    private static final String[] WAIT_HEADERS = {"WaitSeconds", "Wait", "Pause"};

    public SequenceStepHelper(WebDriver driver) {
        setDriver(driver);
    }

    public void runSequence(String sequenceStepsCsv, String elementListCsv, String testDataCsv, int testDataRowIndex) {
        Map<String, String> testData = TestDataUtil.getCsvRecord(testDataCsv, testDataRowIndex);
        runSequence(sequenceStepsCsv, elementListCsv, testData);
    }

    public void runSequence(String sequenceStepsCsv, String elementListCsv, Map<String, String> testData) {
        List<Map<String, String>> steps = TestDataUtil.readCsvFile(sequenceStepsCsv);
        List<Map<String, String>> elements = TestDataUtil.readCsvFile(elementListCsv);
        Map<String, Map<String, String>> elementMap = buildLookupMap(elements, ELEMENT_KEY_HEADERS);

        for (Map<String, String> step : steps) {
            executeStep(step, elementMap, testData);
        }
    }

    public void executeStep(Map<String, String> stepRow, Map<String, Map<String, String>> elementMap, Map<String, String> testData) {
        String action = getValue(stepRow, ACTION_HEADERS);
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Missing action in sequence step: " + stepRow);
        }

        String elementKey = getValue(stepRow, ELEMENT_KEY_HEADERS);
        String rawValue = getValue(stepRow, VALUE_HEADERS);
        String dataKey = getValue(stepRow, DATA_KEY_HEADERS);
        String expectedValue = getValue(stepRow, EXPECTED_HEADERS);
        String waitValue = getValue(stepRow, WAIT_HEADERS);

        String resolvedValue = resolvePlaceholders(rawValue, testData);
        if ((resolvedValue == null || resolvedValue.isBlank()) && dataKey != null && !dataKey.isBlank()) {
            resolvedValue = testData.get(dataKey.trim());
        }

        By locator = null;
        if (elementKey != null && !elementKey.isBlank()) {
            Map<String, String> elementData = findElementData(elementMap, elementKey);
            locator = buildLocator(elementData);
        }

        switch (action.trim().toLowerCase()) {
            case "click":
            case "tap":
                ensureLocator(locator, action);
                click(locator);
                break;
            case "input":
            case "type":
            case "sendkeys":
            case "enter":
                ensureLocator(locator, action);
                if (resolvedValue == null) {
                    throw new IllegalArgumentException("Missing value for input action in step: " + stepRow);
                }
                set(locator, resolvedValue);
                break;
            case "clear":
                ensureLocator(locator, action);
                find(locator).clear();
                break;
            case "hover":
            case "mouseover":
                ensureLocator(locator, action);
                hover(locator);
                break;
            case "hoverandclick":
            case "clickafterhover":
                ensureLocator(locator, action);
                hoverAndClick(locator, locator);
                break;
            case "waitforvisibility":
            case "waitvisible":
            case "wait":
                if (locator != null) {
                    waitForVisibility(locator);
                } else if (waitValue != null && !waitValue.isBlank()) {
                    sleepSeconds(parsePositiveInt(waitValue, 1));
                }
                break;
            case "waitforclickable":
                ensureLocator(locator, action);
                waitForClickability(locator);
                break;
            case "verifydisplayed":
            case "verifyexists":
            case "assertdisplayed":
                ensureLocator(locator, action);
                if (!isElementDisplayed(locator)) {
                    throw new AssertionError("Expected element to be displayed: " + elementKey);
                }
                break;
            case "verifytext":
            case "asserttext":
                ensureLocator(locator, action);
                String actualText = find(locator).getText();
                if (resolvedValue == null) {
                    resolvedValue = expectedValue;
                }
                if (resolvedValue == null) {
                    throw new IllegalArgumentException("Missing expected value for verifyText action in step: " + stepRow);
                }
                if (!actualText.equalsIgnoreCase(resolvedValue.trim())) {
                    throw new AssertionError("Expected text '" + resolvedValue + "' but found '" + actualText + "' for element " + elementKey);
                }
                break;
            case "verifycontains":
            case "assertcontains":
                ensureLocator(locator, action);
                String actual = find(locator).getText();
                if (resolvedValue == null) {
                    resolvedValue = expectedValue;
                }
                if (resolvedValue == null || !actual.contains(resolvedValue)) {
                    throw new AssertionError("Expected element text to contain '" + resolvedValue + "' but found '" + actual + "' for element " + elementKey);
                }
                break;
            case "navigate":
            case "open":
                if (resolvedValue == null || resolvedValue.isBlank()) {
                    throw new IllegalArgumentException("Missing URL for navigate action in step: " + stepRow);
                }
                driver.get(resolvedValue);
                break;
            case "switchtab":
            case "switchtonewtab":
                switchToNewTab();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported sequence action: " + action);
        }
    }

    private String getValue(Map<String, String> row, String[] keys) {
        for (String key : keys) {
            if (row.containsKey(key) && row.get(key) != null && !row.get(key).isBlank()) {
                return row.get(key).trim();
            }
        }
        return null;
    }

    private Map<String, Map<String, String>> buildLookupMap(List<Map<String, String>> rows, String[] keyHeaders) {
        Map<String, Map<String, String>> lookup = new HashMap<>();
        for (Map<String, String> row : rows) {
            String key = getValue(row, keyHeaders);
            if (key == null || key.isBlank()) {
                continue;
            }
            lookup.put(key.trim(), row);
            lookup.put(key.trim().toLowerCase(), row);
        }
        return lookup;
    }

    private Map<String, String> findElementData(Map<String, Map<String, String>> elementMap, String key) {
        if (elementMap == null || key == null) {
            return null;
        }
        String trimmed = key.trim();
        Map<String, String> found = elementMap.get(trimmed);
        if (found == null) {
            found = elementMap.get(trimmed.toLowerCase());
        }
        if (found == null) {
            throw new IllegalArgumentException("Element key not found in element list CSV: " + key);
        }
        return found;
    }

    private By buildLocator(Map<String, String> elementData) {
        if (elementData == null) {
            throw new IllegalArgumentException("Element data cannot be null when resolving locator.");
        }

        String locatorType = getValue(elementData, LOCATOR_TYPE_HEADERS);
        String locatorValue = getValue(elementData, LOCATOR_VALUE_HEADERS);

        if ((locatorType == null || locatorType.isBlank()) && locatorValue != null) {
            String[] split = locatorValue.split("=", 2);
            if (split.length == 2) {
                locatorType = split[0].trim();
                locatorValue = split[1].trim();
            }
        }

        if (locatorType == null || locatorValue == null) {
            throw new IllegalArgumentException("Invalid element locator configuration: " + elementData);
        }

        switch (locatorType.trim().toLowerCase()) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "css":
            case "cssselector":
                return By.cssSelector(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "classname":
            case "class":
                return By.className(locatorValue);
            case "tagname":
            case "tag":
                return By.tagName(locatorValue);
            case "linktext":
                return By.linkText(locatorValue);
            case "partiallinktext":
                return By.partialLinkText(locatorValue);
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }

    private String resolvePlaceholders(String rawValue, Map<String, String> testData) {
        if (rawValue == null || rawValue.isBlank() || testData == null || testData.isEmpty()) {
            return rawValue;
        }

        Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");
        Matcher matcher = pattern.matcher(rawValue);
        StringBuffer builder = new StringBuffer();

        while (matcher.find()) {
            String token = matcher.group(1).trim();
            String replacement = testData.getOrDefault(token, "");
            matcher.appendReplacement(builder, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    private void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private int parsePositiveInt(String input, int defaultValue) {
        try {
            int value = Integer.parseInt(input.trim());
            return value > 0 ? value : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void ensureLocator(By locator, String action) {
        if (locator == null) {
            throw new IllegalArgumentException("Action '" + action + "' requires a locator element.");
        }
    }
}
