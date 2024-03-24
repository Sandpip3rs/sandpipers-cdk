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
import software.amazon.awscdk.assertions.Template;

public class RestApiTest extends TemplateSupport {

  @BeforeAll
  static void initAll() {
    final Application app = new Application();

    final RestApiStack restApiStack = new RestApiStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
    tagResources(restApiStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, app.getApplicationName());

    template = Template.fromStack(restApiStack);
  }

  @Test
  void should_have_rest_api() {

    assertThat(template)
        .containsRestApi("^RestApi[a-zA-Z0-9]{8}$")
        .hasTag("APPLICATION_NAME", "apigateway-cdk-example")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST);
  }

  @Test
  void should_have_rest_api_account() {
    assertThat(template)
        .containsApiAccount("^RestApiAccount[a-zA-Z0-9]{8}$")
        .hasCloudWatchRole("^RestApiCloudWatchRole[a-zA-Z0-9]{8}$")
        .hasDependency("^RestApi[a-zA-Z0-9]{8}$")
        .hasUpdateReplacePolicy("Retain")
        .hasDeletionPolicy("Retain");
  }

  @Test
  void should_have_rest_api_deployment() {

    assertThat(template)
        .containsApiDeployment("^RestApiDeployment(.*)$")
        .hasRestApiId("^RestApi[a-zA-Z0-9]{8}$")
        .hasDescription("Automatically created by the RestApi construct")
        .hasDependency("RestApiusersid[a-zA-Z0-9]{8}$")
        .hasDependency("RestApiusersid[a-zA-Z0-9]{8}$")
        .hasDependency("RestApiusersGET[a-zA-Z0-9]{8}$")
        .hasDependency("RestApiusersPOST[a-zA-Z0-9]{8}$")
        .hasDependency("RestApiusers[a-zA-Z0-9]{8}$");
  }

  @Test
  void should_have_rest_api_stage() {

    assertThat(template)
        .containsApiStage("^RestApiDeploymentStageTest[a-zA-Z0-9]{8}$")
        .hasStageName("Test")
        .hasRestApiId("^RestApi[a-zA-Z0-9]{8}$")
        .hasDeploymentId(("^RestApiDeployment(.*)$"))
        .hasDependency("^RestApiAccount[a-zA-Z0-9]{8}$")
        .hasTag("APPLICATION_NAME", "apigateway-cdk-example")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST);
  }

  @Test
  void should_have_root_resources() {

    assertThat(template)
        .containsApiResource("^RestApiusers[a-zA-Z0-9]{8}$")
        .hasPath("users")
        .hasRestApiId("^RestApi[a-zA-Z0-9]{8}$")
        .hasParentId("^RestApi[a-zA-Z0-9]{8}$");

    assertThat(template)
        .containsApiResource("^RestApiusersid[a-zA-Z0-9]{8}$")
        .hasPath("{id}")
        .hasRestApiId("^RestApi[a-zA-Z0-9]{8}$")
        .hasParentId("^RestApiusers[a-zA-Z0-9]{8}$");

 }

  @Test
  void should_have_methods() {

    assertThat(template)
        .containsApiMethod("^RestApiusersGET[a-zA-Z0-9]{8}$")
        .hasHttpMethod("GET")
        .hasIntegration("ANY", "MOCK", null)
        .hasAuthorizationType("NONE")
        .hasResourceId("^RestApiusers[a-zA-Z0-9]{8}$")
        .hasRestApiId(("^RestApi[a-zA-Z0-9]{8}$"));

    assertThat(template)
        .containsApiMethod("^RestApiusersPOST[a-zA-Z0-9]{8}$")
        .hasHttpMethod("POST")
        .hasIntegration("ANY", "MOCK", null)
        .hasAuthorizationType("NONE")
        .hasResourceId("^RestApiusers[a-zA-Z0-9]{8}$")
        .hasRestApiId(("^RestApi[a-zA-Z0-9]{8}$"));

    assertThat(template)
        .containsApiMethod("^RestApiusersidGET[a-zA-Z0-9]{8}$")
        .hasHttpMethod("GET")
        .hasIntegration("ANY", "MOCK", null)
        .hasAuthorizationType("NONE")
        .hasResourceId("^RestApiusersid[a-zA-Z0-9]{8}$")
        .hasRestApiId(("^RestApi[a-zA-Z0-9]{8}$"));
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
        .containsRole("^RestApiCloudWatchRole[a-zA-Z0-9]{8}$")
        .hasManagedPolicyArn(managedPolicyArn)
        .hasAssumeRolePolicyDocument(principal, null, effect, policyDocumentVersion, "sts:AssumeRole")
        .hasDeletionPolicy("Retain")
        .hasUpdateReplacePolicy("Retain")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "apigateway-cdk-example");
  }

  @Test
  void should_have_custom_domain_name(){
    assertThat(template)
        .containsApiDomainName("^RestApiCustomDomain[a-zA-Z0-9]{8}$")
        .hasDomainName("sandpipers.yeah")
        .hasRegionalCertificateArn("^Certificate[a-zA-Z0-9]{8}$")
        .hasEndpointConfiguration("REGIONAL")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "apigateway-cdk-example");
  }

  @Test
  void should_have_certificate(){
    assertThat(template)
        .containsCertificate("^Certificate[a-zA-Z0-9]{8}$")
        .hasDomainName("sandpipers.yeah")
        .hasDomainValidationOptions("sandpipers.yeah", "yeah")
        .hasValidationMethod("EMAIL")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "apigateway-cdk-example");
  }

  @Test
  void should_have_path_mapping(){
    assertThat(template)
        .containsApiPathMapping("^RestApiCustomDomainMapSandpipersApigatewayCdkExampleStakeRestApi(.*)$")
        .hasDomainName("^RestApiCustomDomain[a-zA-Z0-9]{8}$")
        .hasRestApiId(("^RestApi[a-zA-Z0-9]{8}$"))
        .hasStage("^RestApiDeploymentStageTest[a-zA-Z0-9]{8}$");
  }
}
