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

package io.sandpipers.cdk.core.construct.ec2;

import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sadpipers.cdk.type.IPv4Cidr;
import io.sadpipers.cdk.type.SafeString;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.IpAddresses;
import software.amazon.awscdk.services.ec2.IpProtocol;
import software.amazon.awscdk.services.ec2.Ipv6Addresses;
import software.amazon.awscdk.services.ec2.NatProvider;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.constructs.Construct;

@Getter
public class Vpc extends Construct implements BaseConstruct {

  private final IVpc vpc;

  public Vpc(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final VpcProps props) {
    super(scope, id.getValue());

    final software.amazon.awscdk.services.ec2.Vpc.Builder vpcBuilder = software.amazon.awscdk.services.ec2.Vpc.Builder.create(this, id.getValue())
        .ipProtocol(props.getIpProtocol())
        .ipAddresses(IpAddresses.cidr(props.getIPv4Cidr().getValue()))
        .maxAzs(props.getMaxAzs())
        .natGatewayProvider(props.getNatProvider())
        .natGateways(props.getNatGateways());

    if (props.ipProtocol == IpProtocol.DUAL_STACK) {
      vpcBuilder.ipv6Addresses(Ipv6Addresses.amazonProvided());
    }

    this.vpc = vpcBuilder.build();
  }

  private SubnetConfiguration createSubnetConfiguration(final String name,
      final SubnetType subnetType,
      final IpProtocol ipProtocol,
      final boolean reserved,
      final boolean mapPublicIpOnLaunch,
      final int cidrMask) {
    final SubnetConfiguration.Builder subnetConfigBuilder = SubnetConfiguration.builder()
        // general properties
        .name(name)
        .subnetType(subnetType)
        .reserved(reserved)

        // IPv4 specific properties
        .mapPublicIpOnLaunch(mapPublicIpOnLaunch)
        .cidrMask(cidrMask);

    // new IPv6 specific property
    if (ipProtocol == IpProtocol.DUAL_STACK) {
      subnetConfigBuilder.ipv6AssignAddressOnCreation(true);
    }

    return subnetConfigBuilder.build();
  }

  @Getter
  @Builder
  public static class VpcProps implements software.amazon.awscdk.services.ec2.VpcProps {

    private static final software.amazon.awscdk.services.ec2.VpcProps VPC_PROPS = software.amazon.awscdk.services.ec2.VpcProps.builder().build();

    @NotNull
    private final IPv4Cidr iPv4Cidr;

    @NotNull
    private final Number natGateways;

    @Default
    @Nullable
    private final NatProvider natProvider = NatProvider.gateway();

    @Default
    @Nullable
    private final IpProtocol ipProtocol = VPC_PROPS.getIpProtocol();

    @Default
    @Nullable
    private final Number maxAzs = VPC_PROPS.getMaxAzs();
  }
}