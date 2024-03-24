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

import io.sandpipers.cdk.core.construct.apprunner.AbstractPrivateEgressAppRunnerService.PrivateEgressAppRunnerServiceProps;
import io.sadpipers.cdk.type.AWSArn;
import io.sadpipers.cdk.type.SafeString;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apprunner.alpha.VpcConnector;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.constructs.Construct;

@Getter
public abstract class AbstractPrivateEgressAppRunnerService<T extends PrivateEgressAppRunnerServiceProps> extends AbstractAppRunnerService<T> {

  public AbstractPrivateEgressAppRunnerService(@NotNull final Construct scope, @NotNull final SafeString id) {
    super(scope, id);
  }

  @NotNull
  protected AWSArn getVPCConnectorArn(@NotNull final Construct scope, @NotNull final T props) {
    final VpcConnector vpcConnector = createVpcConnector(scope, props);
    return AWSArn.of(vpcConnector.getVpcConnectorArn());
  }

  @NotNull
  protected VpcConnector createVpcConnector(@NotNull final Construct scope, @NotNull final T props) {
    final SubnetSelection subnetSelection = SubnetSelection.builder().subnetType(props.getSubnetType()).build();

    final IVpc vpc = props.getVpc();
    return VpcConnector.Builder.create(scope, vpc.getVpcId() + "connector").vpc(vpc).vpcSubnets(subnetSelection).build();

  }

  @Getter
  @SuperBuilder
  public static class PrivateEgressAppRunnerServiceProps extends AppRunnerServiceProps {

    @NotNull
    private final IVpc vpc;

    @NotNull
    private final SubnetType subnetType;
  }
}