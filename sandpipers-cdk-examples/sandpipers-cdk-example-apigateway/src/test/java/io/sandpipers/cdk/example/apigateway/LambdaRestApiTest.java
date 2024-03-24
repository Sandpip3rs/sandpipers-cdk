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

package io.sandpipers.cdk.example.apigateway;

import static io.sandpipers.cdk.assertion.CDKStackAssert.assertThat;
import static io.sandpipers.cdk.core.AbstractApp.tagResources;
import static io.sandpipers.cdk.example.apigateway.Environment.SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import software.amazon.awscdk.assertions.Template;

public class LambdaRestApiTest extends TemplateSupport {

  @BeforeAll
  static void initAll() {
    final Application app = new Application();

    final LambdaRestApiStack lambdaRestApiStack = new LambdaRestApiStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
    tagResources(lambdaRestApiStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, app.getApplicationName());

    template = Template.fromStack(lambdaRestApiStack);
  }

  @Test
  void should_have_rest_api() {

    assertThat(template)
        .containsRestApi("^LambdaRestApi[a-zA-Z0-9]{8}$")
        .hasTag("APPLICATION_NAME", "apigateway-cdk-example")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST);
  }

  @Test
  void should_have_rest_api_account() {
    assertThat(template)
        .containsApiAccount("^LambdaRestApiAccount[a-zA-Z0-9]{8}$")
        .hasCloudWatchRole("^LambdaRestApiCloudWatchRole[a-zA-Z0-9]{8}$")
        .hasDependency("^LambdaRestApi[a-zA-Z0-9]{8}$")
        .hasUpdateReplacePolicy("Retain")
        .hasDeletionPolicy("Retain");
  }

  @Test
  void should_have_rest_api_deployment() {

    assertThat(template)
        .containsApiDeployment("^LambdaRestApiDeployment(.*)$")
        .hasRestApiId("^LambdaRestApi[a-zA-Z0-9]{8}$")
        .hasDescription("Automatically created by the RestApi construct")
        .hasDependency("^LambdaRestApiproxyANY[a-zA-Z0-9]{8}$")
        .hasDependency("^LambdaRestApiproxy[a-zA-Z0-9]{8}$")
        .hasDependency("^LambdaRestApiANY[a-zA-Z0-9]{8}$");
  }

  @Test
  void should_have_rest_api_stage() {

    assertThat(template)
        .containsApiStage("^LambdaRestApiDeploymentStageTest[a-zA-Z0-9]{8}$")
        .hasStageName("Test")
        .hasRestApiId("^LambdaRestApi[a-zA-Z0-9]{8}$")
        .hasDeploymentId(("^LambdaRestApiDeployment(.*)$"))
        .hasDependency("^LambdaRestApiAccount[a-zA-Z0-9]{8}$")
        .hasTag("APPLICATION_NAME", "apigateway-cdk-example")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST);
  }

  @Test
  void should_have_resource() {

    assertThat(template)
        .containsApiResource("^LambdaRestApiproxy[a-zA-Z0-9]{8}$")
        .hasPath("{proxy+}")
        .hasRestApiId("^LambdaRestApi[a-zA-Z0-9]{8}$")
        .hasParentId("^LambdaRestApi[a-zA-Z0-9]{8}$");
  }

  @Test
  void should_have_methods() {

    final String IntegrationArn = "^arn:aws:apigateway::ap-southeast-2::lambda:path/2015-03-31/functions/Function[a-zA-Z0-9]{8}/invocations$";

    assertThat(template)
        .containsApiMethod("^LambdaRestApiANY[a-zA-Z0-9]{8}$")
        .hasHttpMethod("ANY")
        .hasIntegration("POST", "AWS_PROXY", IntegrationArn)
        .hasAuthorizationType("NONE")
        .hasResourceId("^LambdaRestApi[a-zA-Z0-9]{8}$")
        .hasRestApiId(("^LambdaRestApi[a-zA-Z0-9]{8}$"));

    assertThat(template)
        .containsApiMethod("^LambdaRestApiproxyANY[a-zA-Z0-9]{8}$")
        .hasHttpMethod("ANY")
        .hasIntegration("POST", "AWS_PROXY", IntegrationArn)
        .hasAuthorizationType("NONE")
        .hasResourceId("^LambdaRestApiproxy[a-zA-Z0-9]{8}$")
        .hasRestApiId(("^LambdaRestApi[a-zA-Z0-9]{8}$"));
  }

  @Test
  void should_have_role_with_AmazonAPIGatewayPushToCloudWatchLogs_policy_for_rest_api_to_push_logs_to_cloud_watch() {
    final Map<String, String> principal  = Map.of("Service", "apigateway.amazonaws.com");
    final String effect = "Allow";
    final String policyDocumentVersion = "2012-10-17";
    final String managedPolicyArn = ":iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs";

    // TODO change hasManagedPolicyArn and use the following instead
    // final String managedPolicyArn = "arn:aws::iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs"

    assertThat(template)
        .containsRole("^LambdaRestApiCloudWatchRole[a-zA-Z0-9]{8}$")
        .hasManagedPolicyArn(managedPolicyArn)
        .hasAssumeRolePolicyDocument(principal, null, effect, policyDocumentVersion, "sts:AssumeRole")
        .hasDeletionPolicy("Retain")
        .hasUpdateReplacePolicy("Retain")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "apigateway-cdk-example");
  }

  @ParameterizedTest
  @CsvSource(
      {
          "^LambdaRestApiANYApiPermissionSandpipersApigatewayCdkExampleStakeLambdaRestApi[a-zA-Z0-9]{8}ANY[a-zA-Z0-9]{8}$, ^arn:aws:execute-api::AWS::AccountId:LambdaRestApi[A-Z0-9]{8}/LambdaRestApiDeploymentStageTest[A-Z0-9]{8}/\\*/$",
          "^LambdaRestApiproxyANYApiPermissionSandpipersApigatewayCdkExampleStakeLambdaRestApi[a-zA-Z0-9]{8}ANYproxy[a-zA-Z0-9]{8}$, ^arn:aws:execute-api::AWS::AccountId:LambdaRestApi[A-Z0-9]{8}/LambdaRestApiDeploymentStageTest[A-Z0-9]{8}/\\*/\\*$",
          "^LambdaRestApiproxyANYApiPermissionTestSandpipersApigatewayCdkExampleStakeLambdaRestApi[a-zA-Z0-9]{8}ANYproxy[a-zA-Z0-9]{8}$, ^arn:aws:execute-api::AWS::AccountId:LambdaRestApi[A-Z0-9]{8}/test-invoke-stage/\\*/\\*$",
          "^LambdaRestApiANYApiPermissionTestSandpipersApigatewayCdkExampleStakeLambdaRestApi[a-zA-Z0-9]{8}ANY[a-zA-Z0-9]{8}$, ^arn:aws:execute-api::AWS::AccountId:LambdaRestApi[A-Z0-9]{8}/test-invoke-stage/\\*/$"

      }
  )
  void should_have_permission_to_allow_rest_api_to_call_lambda(final String lambdaPermissionResourceId,
      final String sourceArnPattern) {

    assertThat(template)
        .containsLambdaPermission(lambdaPermissionResourceId)
        .hasLambdaPermission("^Function[A-Z0-9]{8}$",
            "lambda:InvokeFunction",
            "apigateway.amazonaws.com",
            sourceArnPattern);
  }
}