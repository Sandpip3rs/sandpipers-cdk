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

package io.sandpipers.cdk.assertion;

import static software.amazon.awscdk.assertions.Match.stringLikeRegexp;

import java.util.Map;
import java.util.Map.Entry;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.assertions.Template;

@SuppressWarnings("unchecked")
public class CDKStackAssert extends AbstractAssert<CDKStackAssert, Template> {

  private CDKStackAssert(final Template actual) {
    super(actual, CDKStackAssert.class);
  }

  /**
   * Fluent assertions for CDK resources. Assertions are done directly on an object of {@link software.amazon.awscdk.assertions.Template}.
   *
   * @param actual {@link Template} instance
   * @return {@link CDKStackAssert} instance
   */
  public static CDKStackAssert assertThat(final Template actual) {
    return new CDKStackAssert(actual);
  }

  /**
   * Fluent assertions for <code>AWS::ApiGateway::RestApi</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link RestApiAssert} should be used instead.
   * <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id rest api id
   * @return {@link RestApiAssert} instance
   */
  public RestApiAssert containsRestApi(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.APIGATEWAY_RESTAPI, id);

    return RestApiAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::ApiGateway::Account</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link ApiAccountAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id of the rest api account
   * @return {@link ApiAccountAssert} instance
   */
  public ApiAccountAssert containsApiAccount(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.APIGATEWAY_ACCOUNT, id);

    return ApiAccountAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::ApiGateway::Deployment</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link ApiDeploymentAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id Rest API deployement id
   * @return {@link ApiDeploymentAssert} instance
   */
  public ApiDeploymentAssert containsApiDeployment(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.APIGATEWAY_DEPLOYMENT, id);

    return ApiDeploymentAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::ApiGateway::Method</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link ApiMethodAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id Rest API method id
   * @return {@link ApiMethodAssert} instance
   */
  public ApiMethodAssert containsApiMethod(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.APIGATEWAY_METHOD, id);

    return ApiMethodAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::ApiGateway::Stage</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link ApiStageAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id Rest API stage id
   * @return {@link ApiStageAssert} instance
   */
  public ApiStageAssert containsApiStage(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.APIGATEWAY_STAGE, id);

    return ApiStageAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::ApiGateway::Resource</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link ApiResourceAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id Rest API resource id
   */
  public ApiResourceAssert containsApiResource(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.APIGATEWAY_RESOURCE, id);

    return ApiResourceAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::IAM::Policy</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link PolicyAssert} should be used instead.
   * <br/><br/>
   * <p>
   * Example usage can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-lambda/src/test/java/com/sandpipers/cdk/example/lambda">sandpipers-cdk-example-lambda/test</a>
   * </p>
   *
   * @param id policy id
   * @return {@link PolicyAssert} instance
   */
  public PolicyAssert containsPolicy(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.POLICY, id);

    return PolicyAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::IAM::Role</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link RoleAssert} should be used instead.
   * <br/><br/>
   * <p>
   * Example usage can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-lambda/src/test/java/com/sandpipers/cdk/example/lambda">sandpipers-cdk-example-lambda/test</a>
   * </p>
   *
   * @param id managed policy arn
   * @return {@link RoleAssert} instance
   */
  public RoleAssert containsRole(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.ROLE, id);

    return RoleAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::Lambda::Function</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link LambdaAssert} should be used instead.
   * <br/><br/>
   * <p>
   * Example usage can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-lambda/src/test/java/com/sandpipers/cdk/example/lambda">sandpipers-cdk-example-lambda/test</a>
   * </p>
   *
   * @param id expected function id
   * @return {@link LambdaAssert} instance
   */
  public LambdaAssert containsFunction(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.LAMBDA_FUNCTION, id);

    return LambdaAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::Lambda::EventInvokeConfig</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link LambdaEventInvokeConfigAssert} should
   * be used instead.
   *
   * <p>
   * Example usage can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-lambda/src/test/java/com/sandpipers/cdk/example/lambda">sandpipers-cdk-example-lambda/test</a>
   * </p>
   *
   * @param id the id of the lambda event invoke config
   * @return {@link LambdaEventInvokeConfigAssert} instance
   */
  public LambdaEventInvokeConfigAssert containsLambdaEventInvokeConfig(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.LAMBDA_EVENT_INVOKE_CONFIG, id);

    return LambdaEventInvokeConfigAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::Permission::Permission</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link LambdaPermissionAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in:
   * <ul>
   *   <li><a href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-lambda/src/test/java/com/sandpipers/cdk/example/lambda">sandpipers-cdk-example-lambda/test</a></li>
   *   <li><a href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a></li>
   * </ul>
   * </p>
   *
   * @param id the name of the lambda function
   * @return {@link LambdaPermissionAssert} instance
   */
  public LambdaPermissionAssert containsLambdaPermission(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.LAMBDA_PERMISSION, id);

    return LambdaPermissionAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::ApiGateway::BasePathMapping</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link ApiDomainNameAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id the name of the lambda function
   * @return {@link ApiDomainNameAssert} instance
   */
  public ApiDomainNameAssert containsApiDomainName(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.APIGATEWAY_DOMAIN_NAME, id);

    return ApiDomainNameAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::ApiGateway::BasePathMapping</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link ApiBasePathMappingAssert} should be
   * used instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id the name of the lambda function
   * @return {@link ApiBasePathMappingAssert} instance
   */
  public ApiBasePathMappingAssert containsApiPathMapping(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.APIGATEWAY_BASE_PATH_MAPPING, id);

    return ApiBasePathMappingAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::SQS::QueuePolicy</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link QueuePolicyAssert} should be used
   * instead.
   *
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *       CDKStackAssert.assertThat(template)
   *         .containsQueuePolicy("examplelambdafunctionfailurequeue(.*)", "sqs:SendMessage", "examplelambdafunctionfailurequeue(.*)", "sns.amazonaws.com")
   *         .hasEffect("Allow")
   *         .hasCondition("ArnEquals", "aws:SourceArn", "examplelambdafunctionfailuretopic(.*)");
   *     }
   * </pre>
   *
   * @param id the queue policy id
   * @return {@link QueuePolicyAssert} instance
   */
  public QueuePolicyAssert containsQueuePolicy(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.QUEUE_POLICY, id);

    return QueuePolicyAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::SQS::Queue</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link QueueAssert} should be used instead.
   *
   * <p>
   * Example usage can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-sqs/src/test/java/com/sandpipers/cdk/example/sqs">sandpipers-cdk-example-sqs/test</a>
   * </p>
   *
   * @param id the key of the queue
   * @return {@link QueueAssert} instance
   */
  public QueueAssert containsQueue(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.QUEUE, id);

    return QueueAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::SNS::Topic</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link TopicAssert} should be used instead.
   *
   * <p>
   * Example usage can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-sns/src/test/java/com/sandpipers/cdk/example/sns">sandpipers-cdk-example-sns/test</a>
   * </p>
   *
   * @param id the key of the queue
   * @return {@link TopicAssert} instance
   */
  public TopicAssert containsTopic(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.TOPIC, id);

    return TopicAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::SNS::Subscription</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link TopicSubscriptionAssert} should be
   * used instead.
   *
   * <p>
   * Example usage can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-sns/src/test/java/com/sandpipers/cdk/example/sns">sandpipers-cdk-example-sns/test</a>
   * </p>
   *
   * @param id the topic subscription id
   * @return {@link TopicSubscriptionAssert} instance
   */
  public TopicSubscriptionAssert containsTopicSubscription(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.TOPIC_SUBSCRIPTION, id);

    return TopicSubscriptionAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::CertificateManager::Certificate</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link CertificateAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-apigateway/src/test/java/com/sandpipers/cdk/example/apigateway">sandpipers-cdk-example-apigateway/test</a>
   * </p>
   *
   * @param id the name of the lambda function
   * @return {@link CertificateAssert} instance
   */
  public CertificateAssert containsCertificate(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.CERTIFICATEMANAGER_CERTIFICATE, id);

    return CertificateAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::Route53::ARecord</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link RecordSetAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-route53/src/test/java/com/sandpipers/cdk/example/route53">sandpipers-cdk-example-route53/test</a>
   * </p>
   *
   * @param id the name of the lambda function
   * @return {@link RecordSetAssert} instance
   */
  public RecordSetAssert containsRecordSet(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.ROUTE53_RECORD_SET, id);

    return RecordSetAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::Route53::ARecord</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link HostedZoneAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-route53/src/test/java/com/sandpipers/cdk/example/route53">sandpipers-cdk-example-route53/test</a>
   * </p>
   *
   * @param id the name of the lambda function
   * @return {@link HostedZoneAssert} instance
   */
  public HostedZoneAssert containsHostedZone(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.ROUTE53_HOSTED_ZONE, id);

    return HostedZoneAssert.assertThat(resource.getValue());
  }

  /**
   * Fluent assertions for <code>AWS::DynamoDB::GlobalTable</code>. Assertions are done directly on an object of
   * {@link software.amazon.awscdk.assertions.Template}. If a resource map has been extracted from, then {@link DynamoDBGlobalTableAssert} should be used
   * instead. <br/><br/>
   * <p>
   * Example usages can be found in <a
   * href="https://github.com/muhamadto/sandpipers-cdk/tree/main/sandpipers-cdk-examples/sandpipers-cdk-example-dynamodb/src/test/java/com/sandpipers/cdk/example/dynamodb">sandpipers-cdk-example-dynamodb/test</a>
   * </p>
   *
   * @param id the name of the lambda function
   * @return {@link DynamoDBGlobalTableAssert} instance
   */
  public DynamoDBGlobalTableAssert containsDynamoDBTable(final String id) {

    final Entry<String, Map<String, Object>> resource = containsResource(actual, CdkResourceType.DYNAMODB_GLOBAL_TABLE, id);

    return DynamoDBGlobalTableAssert.assertThat(resource.getValue());
  }

  private Entry<String, Map<String, Object>> containsResource(
      final Template template,
      final CdkResourceType cdkResourceType,
      final String id) {

    final Entry<String, Map<String, Object>> resource = template.findResources(cdkResourceType.getValue())
        .entrySet()
        .stream()
        .filter(entry -> stringLikeRegexp(id).test(entry.getKey()).getIsSuccess())
        .findFirst()
        .orElseThrow();

    Assertions.assertThat(resource)
        .isNotNull();

    return resource;
  }
}
