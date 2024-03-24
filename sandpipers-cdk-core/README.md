# sandpipers-cdk-core

This opinionated module provides L3 constructs.

### Description

These constructs are built with some defaults that might be different from the ones provided by the
AWS CDK. The goal is to provide a set of constructs that are easy to use and that can be used as building blocks for your stacks.

It uses the [type-factory project](https://github.com/type-factory/type-factory/tree/main) to create and validate input to some of the constructs properties. This allows me to provide a more robust and type-safe API.

It also provides base App and Stack classes that serve as a starting point for your CDK projects.

### Usage

* Import the [sandpipers-cdk-bom](..%2Fsandpipers-cdk-bom/README.md) in your project (if you haven't already done so)
* Add the following dependency to your `pom.xml` file:
    ```xml
    <dependency>
      <groupId>io.sandpipers</groupId>
      <artifactId>sandpipers-cdk-core</artifactId>
      <scope>test</scope>
    </dependency>
    ```
