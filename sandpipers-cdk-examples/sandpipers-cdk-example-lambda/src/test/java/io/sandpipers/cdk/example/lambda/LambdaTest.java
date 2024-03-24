/*
 *  Licensed to Muhammad Hamadto
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package io.sandpipers.cdk.example.lambda;

import static io.sandpipers.cdk.assertion.CDKStackAssert.assertThat;
import static io.sandpipers.cdk.core.AbstractApp.tagResources;
import static io.sandpipers.cdk.example.lambda.Environment.SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Template;

public class LambdaTest extends TemplateSupport {

  @BeforeAll
  static void initAll() {

    final Application app = new Application();

    final LambdaStack lambdaStack = new LambdaStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
    tagResources(lambdaStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, app.getApplicationName());

    template = Template.fromStack(lambdaStack);
  }

  @Test
  void should_have_lambda_function() {

    assertThat(template)
        .containsFunction("^Function[a-zA-Z0-9]{8}$")
        .hasHandler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
        .hasCode("^cdk-sandpipers-assets-ap-southeast-2-(.*)$", "(.*).zip")
        .hasRole("^FunctionServiceRole[a-zA-Z0-9]{8}$")
        .hasDependency("^FunctionServiceRoleDefaultPolicy[a-zA-Z0-9]{8}$")
        .hasDependency("^FunctionServiceRole[a-zA-Z0-9]{8}$")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "lambda-cdk-example")
        .hasEnvironmentVariable("ENV", TEST)
        .hasEnvironmentVariable("SPRING_PROFILES_ACTIVE", TEST)
        .hasDescription("Test Function for CDK")
        .hasMemorySize(512)
        .hasRuntime("provided.al2023")
        .hasTimeout(10)
        .hasMemorySize(512)
        .hasDeadLetterTarget("^FunctionDeadLetterTopic[a-zA-Z0-9]{8}$");
  }

  @Test
  void should_have_default_policy_to_allow_lambda_publish_to_sns() {

    final String policyName = "^FunctionServiceRoleDefaultPolicy[a-zA-Z0-9]{8}$";

    assertThat(template)
        .containsPolicy(policyName)
        .hasName(policyName)
        .isAssociatedWithRole("^FunctionServiceRole[a-zA-Z0-9]{8}$")
        .hasPolicyDocumentStatement(null,
            "^FunctionDeadLetterTopic[a-zA-Z0-9]{8}$",
            "sns:Publish",
            "Allow",
            "2012-10-17");
  }

  @Test
  void should_have_event_invoke_config() {

    assertThat(template)
        .containsLambdaEventInvokeConfig("^FunctionEventInvokeConfig[a-zA-Z0-9]{8}$")
        .hasLambdaEventInvokeConfig("^Function[a-zA-Z0-9]{8}$", null, null)
        .hasQualifier("$LATEST")
        .hasFunctionName("^Function[a-zA-Z0-9]{8}$")
        .hasMaximumRetryAttempts(2)
        .hasMaximumEventAgeInSeconds(60);
  }

  @Test
  void should_have_service_role_with_AWSLambdaBasicExecutionRole_policy_to_assume_by_lambda() {
    final Map<String, String> principal = Map.of("Service", "lambda.amazonaws.com");
    final String effect = "Allow";
    final String policyDocumentVersion = "2012-10-17";
    final String managedPolicyArn = ":iam::aws:policy/service-role/AWSLambdaBasicExecutionRole";

    // TODO change hasManagedPolicyArn and use the following instead
    // final String managedPolicyArn = "arn:aws::iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"

    assertThat(template)
        .containsRole("^FunctionServiceRole[a-zA-Z0-9]{8}$")
        .hasManagedPolicyArn(managedPolicyArn)
        .hasAssumeRolePolicyDocument(principal, null, effect, policyDocumentVersion, "sts:AssumeRole")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "lambda-cdk-example");
  }

  @Test
  void should_have_permission_to_allow_rest_api_call_lambda_from_deployment_stage_test() {
    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "^Function[a-zA-Z0-9]{8}$";
    final String sourceArnPattern = "^arn:aws:execute-api::ap-southeast-2:RestApi[a-zA-Z0-9]{8}/RestApiDeploymentStageTest[a-zA-Z0-9]{8}/\\*/\\*$";
    final String lambdaPermissionResourceId = "^RestApiproxyANYApiPermissionSandpipersLambdaCdkExampleStakeRestApi[a-zA-Z0-9]{8}ANYproxy[a-zA-Z0-9]{8}$";

    assertThat(template)
        .containsLambdaPermission(lambdaPermissionResourceId)
        .hasLambdaPermission(functionName, action, principal, sourceArnPattern);
  }

  @Test
  void should_have_permission_to_allow_rest_api_call_lambda_from_test_invoke_stage() {
    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "^Function[a-zA-Z0-9]{8}$";
    final String sourceArnPattern = "^arn:aws:execute-api::ap-southeast-2:RestApi[a-zA-Z0-9]{8}/test-invoke-stage/\\*/\\*$";
    final String lambdaPermissionResourceId = "^RestApiproxyANYApiPermissionTestSandpipersLambdaCdkExampleStakeRestApi[a-zA-Z0-9]{8}ANYproxy[a-zA-Z0-9]{8}$";

    assertThat(template)
        .containsLambdaPermission(
            lambdaPermissionResourceId)
        .hasLambdaPermission(functionName, action, principal, sourceArnPattern);
  }

  @Test
  void should_have_permission_to_allow_rest_api_call_lambda_from_deployment_stage_test_without_wildcards() {
    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "^Function[a-zA-Z0-9]{8}$";
    final String sourceArnPattern = "^arn:aws:execute-api::ap-southeast-2:RestApi[a-zA-Z0-9]{8}/RestApiDeploymentStageTest[a-zA-Z0-9]{8}/\\*/$";
    final String lambdaPermissionResourceId = "^RestApiANYApiPermissionSandpipersLambdaCdkExampleStakeRestApi[a-zA-Z0-9]{8}ANY[a-zA-Z0-9]{8}$";

    assertThat(template)
        .containsLambdaPermission(lambdaPermissionResourceId)
        .hasLambdaPermission(functionName, action, principal, sourceArnPattern);
  }

  @Test
  void should_have_permission_to_allow_rest_api_call_lambda_from_test_invoke_stage_without_wildcards() {
    final String action = "lambda:InvokeFunction";
    final String principal = "apigateway.amazonaws.com";
    final String functionName = "^Function[a-zA-Z0-9]{8}$";
    final String sourceArnPattern = "^arn:aws:execute-api::ap-southeast-2:RestApi[a-zA-Z0-9]{8}/test-invoke-stage/\\*/$";
    final String lambdaPermissionResourceId = "^RestApiANYApiPermissionTestSandpipersLambdaCdkExampleStakeRestApi[a-zA-Z0-9]{8}ANY[a-zA-Z0-9]{8}$";

    assertThat(template)
        .containsLambdaPermission(lambdaPermissionResourceId)
        .hasLambdaPermission(functionName, action, principal, sourceArnPattern);
  }
}
