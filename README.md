# <a href="https://retest.dev"><img src="https://assets.retest.org/retest/ci/logos/recheck-screen.svg" width="300"/></a>

[![Build Status](https://travis-ci.com/retest/recheck-junit-4-extension.svg?branch=master)](https://travis-ci.com/retest/recheck-junit-4-extension)
[![Latest recheck-junit-4-extension on Maven Central](https://maven-badges.herokuapp.com/maven-central/de.retest/recheck-junit-4-extension/badge.svg?style=flat)](https://mvnrepository.com/artifact/de.retest/recheck-junit-4-extension)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck-junit-4-extension/blob/master/LICENSE)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/retest/recheck-junit-4-extension/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)
[![code with hearth by retest](https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-retest-C1D82F.svg)](https://retest.de/)

[JUnit 4](https://junit.org/junit4/) extension for [recheck](https://github.com/retest/recheck). Automatic set up and tear down of tests using recheck.

## Features

* Calls `startTest` on given `RecheckLifecycle` object before each test.
* Calls `capTest` on given `RecheckLifecycle` object after each test.
* Calls `cap` on given `RecheckLifecycle` object after each test.

## Advantages

The extension automatically calls `startTest`, `capTest` and `cap`. So it is no longer required to call those methods manually. This reduces boilerplate code and ensures the life cycle within a test using recheck.

## Build tools

You can add ***recheck-junit-4-extension*** as an external dependency to your project. It is available in [Maven central](https://mvnrepository.com/artifact/de.retest/recheck-junit-4-extension) or via the [release-page](https://github.com/retest/recheck-junit-4-extension/releases), which allows you to include it into your favorite build tool.

For the current version, please refer to the release-page.

### Maven

```xml
<dependency>
	<groupId>de.retest</groupId>
	<artifactId>recheck-junit-4-extension</artifactId>
	<version>${LATEST_VERSION_FROM_ABOVE_LINK}</version>
</dependency>
```

### Gradle

```gradle
compile 'de.retest:recheck-junit-4-extension:${LATEST_VERSION_FROM_ABOVE_LINK}'
```

## Usage

Recheck JUnit extension defines a [JUnit 4 Rule](https://github.com/junit-team/junit4/wiki/Rules). The rule needs to know the instance of the used `RecheckLifecycle` element. The instance can be given during construction or afterwards during setup. The following code demonstrates both ways.

### Recheck instance given during construction

```java
// Define RecheckLifecycle instance before Rule
private RecheckLifecycle re = new RecheckImpl();

// Let rule know which RecheckLifecycle instance should be administered
@Rule
public RecheckRule recheckRule = new RecheckRule(re);
```

### Recheck instance given during setup

```java
@Rule
public RecheckRule recheckRule = new RecheckRule();
private RecheckLifecycle re;

@Before
public void before() {
	re = new RecheckImpl();
	
	// Let rule know which RecheckLifecycle instance should be administered
	recheckRule.use(re);
}
```

### Prerequisites

Requires [JUnit 4](https://mvnrepository.com/artifact/junit/junit). For [JUnit Jupiter](https://junit.org/junit5/) support look at [recheck extension for JUnit Jupiter](https://github.com/retest/recheck-junit-jupiter-extension).

## License

This project is licensed under the [AGPL license](LICENSE).
