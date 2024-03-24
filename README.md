# sandpipers-cdk

Create and test AWS CDK stacks with ease using the Sandpipers CDK libraries.

### Description

This project aims to create L3 constructs with sensible defaults that can be used as building blocks for your CDK stacks. It also provides a set of
libraries to test your CDK stacks in a readable and maintainable way inspired by the [AssertJ](https://assertj.github.io/doc/) library.

The project is divided into the following modules:

* [sandpipers-cdk-bom](sandpipers-cdk-bom): A Bill of Materials (BOM) for the Sandpipers CDK libraries.
* [sandpipers-cdk-core](sandpipers-cdk-core): Opinionated L3 constructs with sensible defaults. It, also, provides base App and Stack classes that
  serve as a starting point for your CDK projects. It uses the [type-factory project](https://github.com/type-factory/type-factory/tree/main) to
  create and validate input to some of the constructs properties. This allows for a more robust and type-safe API.
* [sandpipers-cdk-assertions](sandpipers-cdk-assertions): Fluent assertions for AWS CDK testing.
* [sandpipers-cdk-examples](sandpipers-cdk-examples): Examples of how to use the [sandpipers-cdk-core](sandpipers-cdk-core)
  and [sandpipers-cdk-assertions](sandpipers-cdk-assertions) libraries.

### Usage

* Import the [sandpipers-cdk-bom](..%2Fsandpipers-cdk-bom/README.md) into to your `pom.xml` file.
  ```xml
  <dependencyManagement>
    <dependency>
      <groupId>io.sandpipers</groupId>
      <artifactId>sandpipers-cdk-bom</artifactId>
      <version>version</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencyManagement>
  ```


* Add the [sandpipers-cdk-core](sandpipers-cdk-core) into to your `pom.xml` file, if you want to use the L3 constructs provided by the project.
    ```xml
    <dependency>
      <groupId>io.sandpipers</groupId>
      <artifactId>sandpipers-cdk-core</artifactId>
      <scope>test</scope>
    </dependency>
    ```
* Use the constructs provided by the [sandpipers-cdk-core](sandpipers-cdk-core) in your CDK stacks. Examples of these constructs can be found in
  the [sandpipers-cdk-examples](sandpipers-cdk-examples) module. However, here is a sneak peek of how to use the `Lambda` construct:
  ```java
        final CustomRuntime2023FunctionProps functionProps = CustomRuntime2023FunctionProps.builder()
          .description("Example Function for CDK")
          .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
          .code(Code.fromAsset(testLambdaCodePath))
          .deadLetterTopicEnabled(true)
          .environment(Map.of("ENV", "TEST"))
          .build();

      final CustomRuntime2023Function<CustomRuntime2023FunctionProps> function =
          new CustomRuntime2023Function<>(this, SafeString.of("Function"), functionProps);
  ```
* Add the [sandpipers-cdk-assertions](sandpipers-cdk-assertions) into to your `pom.xml` file, if you want to use the fluent assertions provided by the
  project.
    ```xml
    <dependency>
      <groupId>io.sandpipers</groupId>
      <artifactId>sandpipers-cdk-assertions</artifactId>
      <scope>test</scope>
    </dependency>
    ```
* Use the assertions provided by the [sandpipers-cdk-assertions](sandpipers-cdk-assertions) in your CDK tests. Examples of these assertions can be
  found in the [sandpipers-cdk-examples](sandpipers-cdk-examples) module. However, here is a sneak peek of how to use
  the [CDKStackAssert.java](sandpipers-cdk-assertions%2Fsrc%2Fmain%2Fjava%2Fio%2Fsandpipers%2Fcdk%2Fassertion%2FCDKStackAssert.java) assertions class
  for testing a Lambda function:
  ```java
        CDKStackAssert.assertThat(template)
          .containsFunction("^Function[A-Z0-9]{8}$")
          .hasHandler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
          .hasCode("^cdk-sandpipers-assets-\\$\\{AWS\\:\\:AccountId\\}-\\$\\{AWS\\:\\:Region\\}$", "(.*).zip")
          .hasRole("^FunctionServiceRole[A-Z0-9]{8}$")
          .hasDependency("^FunctionServiceRoleDefaultPolicy[A-Z0-9]{8}$")
          .hasDependency("^FunctionServiceRole[A-Z0-9]{8}$")
          .hasTag("COST_CENTRE", "Sandpipers")
          .hasTag("ENVIRONMENT", TEST)
          .hasTag("APPLICATION_NAME", "lambda-cdk-example")
          .hasEnvironmentVariable("ENV", TEST)
          .hasEnvironmentVariable("SPRING_PROFILES_ACTIVE", TEST)
          .hasDescription("Example Function for CDK")
          .hasMemorySize(512)
          .hasRuntime("provided.al2023")
          .hasTimeout(10)
          .hasMemorySize(512)
          .hasDeadLetterTarget("^FunctionDeadLetterTopic[A-Z0-9]{8}$");
  ```