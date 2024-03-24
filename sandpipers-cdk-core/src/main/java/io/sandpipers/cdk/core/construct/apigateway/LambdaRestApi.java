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

package io.sandpipers.cdk.core.construct.apigateway;

import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sadpipers.cdk.type.SafeString;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apigateway.LambdaRestApiProps;
import software.constructs.Construct;

/**
 * L3 Construct representing AWS::ApiGateway::RestApi (Lambda Rest Api)
 * <p> Example usage can be found in <a
 * href="https://github.com/muhamadto/sandpipers-cdk/blob/main/sandpipers-cdk-examples/sandpipers-cdk-example-lambda/src/main/java/com/sandpipers/cdk/example/lambda/LambdaStack.java">sandpipers-cdk-example-lambda</a></p>
 */
@Getter
public class LambdaRestApi extends Construct implements BaseConstruct {

  private software.amazon.awscdk.services.apigateway.LambdaRestApi lambdaRestApi;

  public LambdaRestApi(
      @NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final LambdaRestApiProps props) {
    super(scope, id.getValue());

    this.lambdaRestApi = software.amazon.awscdk.services.apigateway.LambdaRestApi.Builder.create(this, id.getValue())
        .handler(props.getHandler())
        .integrationOptions(props.getIntegrationOptions())
        .proxy(props.getProxy())
        .deploy(props.getDeploy())
        .domainName(props.getDomainName())
        .policy(props.getPolicy())
        .defaultCorsPreflightOptions(props.getDefaultCorsPreflightOptions())
        .defaultIntegration(props.getDefaultIntegration())
        .defaultMethodOptions(props.getDefaultMethodOptions())
        .cloudWatchRole(props.getCloudWatchRole())
        .cloudWatchRoleRemovalPolicy(props.getCloudWatchRoleRemovalPolicy())
        .deployOptions(props.getDeployOptions())
        .description(props.getDescription())
        .disableExecuteApiEndpoint(props.getDisableExecuteApiEndpoint())
        .endpointExportName(props.getEndpointExportName())
        .endpointTypes(props.getEndpointTypes())
        .failOnWarnings(props.getFailOnWarnings())
        .parameters(props.getParameters())
        .retainDeployments(props.getRetainDeployments())
        .apiKeySourceType(props.getApiKeySourceType())
        .binaryMediaTypes(props.getBinaryMediaTypes())
        .cloneFrom(props.getCloneFrom())
        .endpointConfiguration(props.getEndpointConfiguration())
        .minCompressionSize(props.getMinCompressionSize())
        .build();
  }
}
