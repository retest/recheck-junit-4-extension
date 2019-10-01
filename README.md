# <a href="https://retest.dev"><img src="https://assets.retest.org/retest/ci/logos/recheck-screen.svg" width="300"/></a>

[![Build Status](https://travis-ci.com/retest/recheck-junit-4-extension.svg?branch=master)](https://travis-ci.com/retest/recheck-junit-4-extension)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck-junit-4-extension/blob/master/LICENSE)

JUnit 4 extension for [recheck](https://github.com/retest/recheck). Automatic set up and tear down of tests using recheck.

## Features
* Calls `startTest` on given `RecheckLifecycle` object before each test.
* Calls `capTest` on given `RecheckLifecycle` object after each test.
* Calls `cap` on given `RecheckLifecycle` object after each test.

## Advantages
The extension automatically calls `startTest`, `capTest` and `cap`. So it is no longer required to call those methods manually. This reduces boiler plate code and ensures the life cycle within a test using recheck.

## Usage
Recheck JUnit extension defines a JUnit 4 Rule. The rule needs to know the instance of the used `RecheckLifecycle` element. The instance can be given during construction or afterwards during setup. The following code demonstrates both ways.

### Recheck instance given during construction

```
private RecheckLifecycle re = new RecheckImpl();
@Rule
public RecheckRule recheckRule = new RecheckRule(re);
```

### Recheck instance given during setup

```
@Rule
public RecheckRule recheckRule = new RecheckRule();
private RecheckLifecycle re;

@Before
public void before() {
	re = new RecheckImpl();
	recheckRule.use(re);
}
```

### Prerequisites
Requires JUnit 4. For JUnit Jupiter support look at [recheck extension for JUnit Jupiter](https://github.com/retest/recheck-junit-jupiter-extension)

## License

This project is licensed under the [AGPL license](LICENSE).