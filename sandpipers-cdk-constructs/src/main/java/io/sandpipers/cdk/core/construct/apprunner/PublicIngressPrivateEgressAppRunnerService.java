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

import io.sandpipers.cdk.core.construct.apprunner.PublicIngressPrivateEgressAppRunnerService.PublicIngressAbstractPrivateEgressAbstractAppRunnerServiceProps;
import io.sadpipers.cdk.type.SafeString;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import software.constructs.Construct;

public class PublicIngressPrivateEgressAppRunnerService<T extends PublicIngressAbstractPrivateEgressAbstractAppRunnerServiceProps> extends
    AbstractPrivateEgressAppRunnerService<T> {

  public PublicIngressPrivateEgressAppRunnerService(@NotNull final Construct scope, @NotNull final SafeString id, @NotNull final T props) {
    super(scope, id);

    this.service = createService(scope, id, props, getVPCConnectorArn(scope, props));
  }

  @Getter
  @SuperBuilder
  public static class PublicIngressAbstractPrivateEgressAbstractAppRunnerServiceProps extends PrivateEgressAppRunnerServiceProps {

    private final SafeString egressType = SafeString.of("VPC");

    private final Boolean publiclyAccessible = true;
  }
}
