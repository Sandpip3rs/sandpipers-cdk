# sandpipers-cdk-assertions

AssertJ-like fluent assertions for AWS CDK testing

### Description

When working with the AWS CDK, it is important to test your stacks to ensure that they are well-formed and that they match your expectations.
AWS offers a framework to verify that your stack is well-formed and that it matches your expectations. This framework can be found [here](https://docs.aws.amazon.com/cdk/latest/guide/testing.html).

Here is an example test that a stack contains a specific role:
```java
  template.hasResourceProperties("AWS::IAM::Role", Match.objectEquals(
    Collections.singletonMap("AssumeRolePolicyDocument", Map.of(
        "Version", "2012-10-17",
        "Statement", Collections.singletonList(Map.of(
            "Action", "sts:AssumeRole",
            "Effect", "Allow",
            "Principal", Collections.singletonMap(
                "Service", Collections.singletonMap(
                    "Fn::Join", Arrays.asList(
                            "",
                            Arrays.asList("states.", Match.anyValue(), ".amazonaws.com")
                    )
                )
            )
        ))
    ))
));
```

####  Sandpipers' CDK fluent testing library
As you might have noticed, the tests written this way are verbose and present challenges in terms of readability and maintainability. However, don't despair! `sandpipers-cdk-assertions` allows you to write tests in a more readable and maintainable way. The library is inspired by the [AssertJ](https://assertj.github.io/doc/) library and provides a fluent API to test your CDK stacks.

To illustrate, an equivalent representation to the aforementioned example can be found below:
```java
CDKStackAssert.assertThat(template)
  .containsRoleWithManagedPolicyArn(managedPolicyArn)
  .hasAssumeRolePolicyDocument("states.amazonaws.com", null, "Allow", "2012-10-17", "sts:AssumeRole");
```

### Usage

* Import the [sandpipers-cdk-bom](..%2Fsandpipers-cdk-bom/README.md) in your project (if you haven't already done so)
* Add the following dependency to your `pom.xml` file:
    ```xml
    <dependency>
      <groupId>io.sandpipers</groupId>
      <artifactId>sandpipers-cdk-assertions</artifactId>
      <scope>test</scope>
    </dependency>
    ```

* Create a class `TemplateSupport` in your testing packages. This class will be used to load the template and create the `CDKStackAssert` object.

    ```java
    public abstract class TemplateSupport {
    
      public static final String ENV = "test";
      public static final String TEST_CDK_BUCKET = "test-cdk-bucket";
      public static final String QUALIFIER = "test";
      static Template template;
      private static final String STACK_NAME = "example-lambda-function-test-stack";
      
      @TempDir
      private static Path TEMP_DIR;
    
      @BeforeAll
      static void initAll() throws IOException {
        final Path lambdaCodePath = TestLambdaUtils.getTestLambdaCodePath(TEMP_DIR);
    
        final Map<String, String> tags = createTags(ENV, TAG_VALUE_COST_CENTRE);
        final App app = new App();
        final ExampleLambdaStack stack = StackUtils.createStack(app, STACK_NAME, lambdaCodePath.toString(), QUALIFIER, TEST_CDK_BUCKET, ENV);
    
        tags.entrySet().stream()
            .filter(tag -> Objects.nonNull(tag.getValue()))
            .forEach(tag -> Tags.of(app).add(tag.getKey(), tag.getValue()));
    
        template = Template.fromStack(stack);
      }
    
      @AfterAll
      static void cleanup() {
        template = null;
      }
    }
    ```
* Extend the `TemplateSupport` class in your test classes and use the `CDKStackAssert` object to verify your stack.

    ```java
    class LambdaTest extends TemplateSupport {
    
      public static final String TEST = "test";
    
      @Test
      void should_have_lambda_function() {
    
        CDKStackAssert.assertThat(template)
            .containsFunction("example-lambda-function")
            .hasHandler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
            .hasCode("test-cdk-bucket", "(.*).zip")
            .hasRole("examplelambdafunctionrole(.*)")
            .hasDependency("examplelambdafunctionrole(.*)")
            .hasDependency("examplelambdafunctionroleDefaultPolicy(.*)")
            .hasTag("COST_CENTRE", TAG_VALUE_COST_CENTRE)
            .hasTag("ENV", TEST)
            .hasEnvironmentVariable("ENV", TEST)
            .hasEnvironmentVariable("SPRING_PROFILES_ACTIVE", TEST)
            .hasDescription("Lambda example")
            .hasMemorySize(512)
            .hasRuntime("provided.al2")
            .hasTimeout(3);
      }
    }
    ```