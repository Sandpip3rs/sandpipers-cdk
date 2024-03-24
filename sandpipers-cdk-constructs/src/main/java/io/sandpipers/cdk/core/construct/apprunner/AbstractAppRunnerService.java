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

package io.sandpipers.cdk.core.construct.apprunner;

import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sandpipers.cdk.core.construct.apprunner.AbstractAppRunnerService.AppRunnerServiceProps;
import io.sadpipers.cdk.type.AWSArn;
import io.sadpipers.cdk.type.AWSEcrImageIdentifier;
import io.sadpipers.cdk.type.AppRunnerCpu;
import io.sadpipers.cdk.type.AppRunnerMemory;
import io.sadpipers.cdk.type.NumericString;
import io.sadpipers.cdk.type.Path;
import io.sadpipers.cdk.type.Protocol;
import io.sadpipers.cdk.type.SafeString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.services.apprunner.CfnService;
import software.amazon.awscdk.services.apprunner.CfnService.EgressConfigurationProperty;
import software.amazon.awscdk.services.apprunner.CfnService.HealthCheckConfigurationProperty;
import software.amazon.awscdk.services.apprunner.CfnService.ImageConfigurationProperty;
import software.amazon.awscdk.services.apprunner.CfnService.ImageRepositoryProperty;
import software.amazon.awscdk.services.apprunner.CfnService.IngressConfigurationProperty;
import software.amazon.awscdk.services.apprunner.CfnService.InstanceConfigurationProperty;
import software.amazon.awscdk.services.apprunner.CfnService.NetworkConfigurationProperty;
import software.amazon.awscdk.services.apprunner.CfnService.SourceConfigurationProperty;
import software.amazon.awscdk.services.apprunner.alpha.Secret;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

@Getter
public abstract class AbstractAppRunnerService<T extends AppRunnerServiceProps> extends Construct implements BaseConstruct {

  protected static final String INSTANCE_ROLE_PRINCIPAL = "tasks.apprunner.amazonaws.com";
  protected static final String INSTANCE_ROLE_ID_SUFFIX = "InstanceRole";
  protected static final String PARTITION_AWS = "aws";

  protected CfnService service;

  public AbstractAppRunnerService(@NotNull final Construct scope, @NotNull final SafeString id) {
    super(scope, id.getValue());
  }

  @NotNull
  protected CfnService createService(@NotNull final Construct scope, @NotNull final SafeString id, @NotNull final T props) {
    return createService(scope, id, props, null);
  }

  @NotNull
  protected CfnService createService(
      @NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props,
      @Nullable final AWSArn vpcConnectorArn) {
    final NetworkConfigurationProperty networkConfiguration = createNetworkConfiguration(props.getEgressType(), vpcConnectorArn,
        props.getPubliclyAccessible());

    final SourceConfigurationProperty sourceConfigurationProperty = createSourceConfigurationProperty(props);

    final HealthCheckConfigurationProperty healthCheckConfigurationProperty = createHealthCheckConfigurationProperty(props);

    final InstanceConfigurationProperty instanceConfigurationProperty = createInstanceConfigurationProperty(id, props);

    return CfnService.Builder.create(scope, id.getValue())
        .networkConfiguration(networkConfiguration).sourceConfiguration(sourceConfigurationProperty)
        .healthCheckConfiguration(healthCheckConfigurationProperty)
        .instanceConfiguration(instanceConfigurationProperty)
        .build();
  }

  @NotNull
  protected SourceConfigurationProperty createSourceConfigurationProperty(@NotNull final T props) {
    final ImageConfigurationProperty imageConfigurationProperty = ImageConfigurationProperty.builder()
        .port(props.getPort().getValue())
        .runtimeEnvironmentSecrets(props.getPolicyStatements())
        .runtimeEnvironmentVariables(props.getEnvironmentVariables())
        .build();

    final ImageRepositoryProperty imageRepositoryProperty = ImageRepositoryProperty.builder()
        .imageIdentifier(props.getAwsEcrImageIdentifier().getValue())
        .imageRepositoryType(props.getImageRepositoryType().getValue())
        .imageConfiguration(imageConfigurationProperty).build();

    return SourceConfigurationProperty.builder().imageRepository(imageRepositoryProperty).build();
  }

  @NotNull
  protected InstanceConfigurationProperty createInstanceConfigurationProperty(@NotNull final SafeString id, @NotNull final T props) {

    final Role instanceRole = createRole(id, INSTANCE_ROLE_ID_SUFFIX, INSTANCE_ROLE_PRINCIPAL);
    props.policyStatements.forEach(instanceRole::addToPolicy);

    return InstanceConfigurationProperty.builder()
        .cpu(props.getCpu().getValue())
        .memory(props.getMemory().getValue())
        .instanceRoleArn(instanceRole.getRoleArn()).build();
  }

  @NotNull
  protected HealthCheckConfigurationProperty createHealthCheckConfigurationProperty(@NotNull final T props) {
    return HealthCheckConfigurationProperty.builder()
        .protocol(props.getHealthCheckProtocol().getValue())
        .path(props.getHealthCheckPath().getValue()).interval(props.getHealthCheckInterval())
        .timeout(props.getHealthCheckTimeout())
        .unhealthyThreshold(props.getUnhealthyThreshold())
        .build();
  }

  private NetworkConfigurationProperty createNetworkConfiguration(final SafeString egressType, final AWSArn vpcConnectorArn,
      final boolean isPubliclyAccessible) {
    // TODO check if vpcConnectorArn is null and egressType is VPC
    final EgressConfigurationProperty egressConfigurationProperty = EgressConfigurationProperty.builder()
        .egressType(egressType.getValue())
        .vpcConnectorArn(vpcConnectorArn.getValue())
        .build();

    final IngressConfigurationProperty ingressConfiguration = IngressConfigurationProperty.builder()
        .isPubliclyAccessible(isPubliclyAccessible)
        .build();

    return NetworkConfigurationProperty.builder().egressConfiguration(egressConfigurationProperty)
        .ingressConfiguration(ingressConfiguration)
        .build();
  }

  private Role createRole(@NotNull final SafeString serviceId, final String idSuffix, final String assumedByPrincipal) {
    final String id = serviceId + idSuffix;

    return Role.Builder.create(this, id)
        .assumedBy(new ServicePrincipal(assumedByPrincipal))
        .build();
  }

  @Getter
  @SuperBuilder
  public static class AppRunnerServiceProps {

    @NotNull
    private final AWSEcrImageIdentifier awsEcrImageIdentifier;

    @NotNull
    private SafeString imageRepositoryType;

    @NotNull
    private final Path healthCheckPath;

    @Default
    private final NumericString port = NumericString.of("8080");

    @Singular
    private final Map<String, Secret> environmentSecrets = new HashMap<>();

    @Singular
    private final List<Properties> environmentVariables = new ArrayList<>();

    @Singular
    protected final List<PolicyStatement> policyStatements = new ArrayList<>();

    @Default
    private final Number healthCheckInterval = 5;

    @Default
    private final Number healthCheckTimeout = 2;

    @Default
    private final Number unhealthyThreshold = 5;

    @Default
    private final Protocol healthCheckProtocol = Protocol.of("HTTP");

    @Default
    protected final AppRunnerMemory memory = AppRunnerMemory.of("2 GB");

    @Default
    protected final AppRunnerCpu cpu = AppRunnerCpu.of("1 vCPU");

    private final SafeString egressType;

    private final Boolean publiclyAccessible;
  }
}