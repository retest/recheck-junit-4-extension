# <a href="https://retest.dev"><img src="https://assets.retest.org/retest/ci/logos/recheck-screen.svg" width="300"/></a>

JUnit 4 Extension for [recheck](https://github.com/retest/recheck). Automatic set up and tear down of tests using recheck.

## Features
* Calls `startTest` on all `RecheckLifecycle` objects before each test.
* Calls `capTest` on all `RecheckLifecycle` objects after each test.

## Advantages
The extension automatically calls `startTest` and `capTest`. So it is no longer required to call those methods manually. This reduces boiler plate code and ensures the lifecycle within a test using recheck.

## Usage
Recheck JUnit extension defines a JUnit 4 rule. The rule needs to know the instance of the test in order to search for `RecheckLifecycle` fields. It can be used by adding the following code to your test class.
```
@Rule
public RecheckRule recheckRule = new RecheckRule(this);
```

### Prerequisites
Requires JUnit 4. For JUnit Jupiter support look at [recheck extension for JUnit Jupiter](https://github.com/retest/recheck-junit-jupiter-extension)