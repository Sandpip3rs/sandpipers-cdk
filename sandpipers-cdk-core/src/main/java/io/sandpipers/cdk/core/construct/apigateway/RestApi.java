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

import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.construct.BaseConstruct;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Size;
import software.amazon.awscdk.services.apigateway.ApiKeySourceType;
import software.amazon.awscdk.services.apigateway.CorsOptions;
import software.amazon.awscdk.services.apigateway.DomainNameOptions;
import software.amazon.awscdk.services.apigateway.EndpointConfiguration;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.IRestApi;
import software.amazon.awscdk.services.apigateway.Integration;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.constructs.Construct;

@Getter
public class RestApi extends Construct implements BaseConstruct {

  private software.amazon.awscdk.services.apigateway.RestApi restApi;

  public RestApi(
      @NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final RestApiProps props) {
    super(scope, id.getValue());

    this.restApi = software.amazon.awscdk.services.apigateway.RestApi.Builder.create(this, id.getValue())
        .defaultCorsPreflightOptions(props.getDefaultCorsPreflightOptions())
        .defaultIntegration(props.getDefaultIntegration())
        .defaultMethodOptions(props.getDefaultMethodOptions())
        .cloudWatchRole(props.getCloudWatchRole())
        .cloudWatchRoleRemovalPolicy(props.getCloudWatchRoleRemovalPolicy())
        .deploy(props.getDeploy())
        .deployOptions(props.getDeployOptions())
        .description(props.getDescription())
        .disableExecuteApiEndpoint(props.getDisableExecuteApiEndpoint())
        .domainName(props.getDomainName())
        .endpointExportName(props.getEndpointExportName())
        .endpointTypes(props.getEndpointTypes())
        .failOnWarnings(props.getFailOnWarnings())
        .parameters(props.getParameters())
        .policy(props.getPolicy())
        .retainDeployments(props.getRetainDeployments())
        .apiKeySourceType(props.getApiKeySourceType())
        .binaryMediaTypes(props.getBinaryMediaTypes())
        .cloneFrom(props.getCloneFrom())
        .endpointConfiguration(props.getEndpointConfiguration())
        .minCompressionSize(props.getMinCompressionSize())
        .build();
  }

  @Getter
  @SuperBuilder
  public static class RestApiProps implements software.amazon.awscdk.services.apigateway.RestApiProps {

    @Nullable
    private StageOptions deployOptions;

    @Nullable
    private DomainNameOptions domainName;

    @Nullable
    private CorsOptions defaultCorsPreflightOptions;

    @Nullable
    private Integration defaultIntegration;

    @Nullable
    private MethodOptions defaultMethodOptions;

    @Nullable
    private Boolean cloudWatchRole;

    @Nullable
    private RemovalPolicy cloudWatchRoleRemovalPolicy;

    @Nullable
    private Boolean deploy;

    @Nullable
    private String description;

    @Nullable
    private Boolean disableExecuteApiEndpoint;

    @Nullable
    private String endpointExportName;

    @Singular
    private List<EndpointType> endpointTypes;

    @Nullable
    private Boolean failOnWarnings;

    @Nullable
    private Map<String, String> parameters;

    @Nullable
    private PolicyDocument policy;

    @Nullable
    private Boolean retainDeployments;

    @Nullable
    private ApiKeySourceType apiKeySourceType;

    @Singular
    private List<String> binaryMediaTypes;

    @Nullable
    private IRestApi cloneFrom;

    @Nullable
    private EndpointConfiguration endpointConfiguration;

    @Nullable
    @Range(from = 0, to = 10485760)
    // non-negative between 0 and 10485760 (10M) bytes
    private Size minCompressionSize;
  }

}
