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

package io.sandpipers.cdk.core.construct.apprunner.experimental;

import io.sandpipers.cdk.core.construct.apprunner.experimental.AbstractPrivateEgressAppRunnerService.PrivateEgressAppRunnerServiceProps;
import io.sadpipers.cdk.type.SafeString;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apprunner.alpha.EcrSource;
import software.amazon.awscdk.services.apprunner.alpha.HealthCheck;
import software.amazon.awscdk.services.apprunner.alpha.Service;
import software.amazon.awscdk.services.apprunner.alpha.VpcConnector;
import software.amazon.awscdk.services.ecr.IRepository;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;

public class PublicIngressPrivateEgressAppRunnerService<T extends PrivateEgressAppRunnerServiceProps> extends
    AbstractPrivateEgressAppRunnerService<T> {

  public PublicIngressPrivateEgressAppRunnerService(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id);

    final String serviceId = id.getValue();

    final IRepository repository = getRepository(scope, props);

    final EcrSource ecrSource = createEcrSource(props, repository);

    final HealthCheck healthCheck = createHealthCheck(props);

    final VpcConnector vpcConnector = createVpcConnector(scope, props);

    final Role instanceRole = createRole(serviceId, INSTANCE_ROLE_ID_SUFFIX, INSTANCE_ROLE_PRINCIPAL);
    props.policyStatements.forEach(instanceRole::addToPolicy);

    final Role accessRole = createRole(serviceId, ACCESS_ROLE_ID_SUFFIX, ACCESS_ROLE_PRINCIPAL);
    repository.grantPull(accessRole);

    this.service = Service.Builder.create(scope, serviceId)
        .source(ecrSource)
        .healthCheck(healthCheck)
        .vpcConnector(vpcConnector)
        .instanceRole(instanceRole)
        .accessRole(accessRole)
        .memory(props.memory)
        .cpu(props.cpu)
        .build();
  }
}