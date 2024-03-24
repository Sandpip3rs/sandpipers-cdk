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

package io.sandpipers.cdk.example.route53;

import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.ec2.Vpc;
import io.sandpipers.cdk.core.construct.ec2.Vpc.VpcProps;
import io.sandpipers.cdk.core.construct.route53.PrivateARecord;
import io.sandpipers.cdk.core.construct.route53.PrivateARecord.PrivateARecordProps;
import io.sandpipers.cdk.core.construct.route53.PublicARecord;
import io.sandpipers.cdk.core.construct.route53.PublicARecord.PublicARecordProps;
import io.sadpipers.cdk.type.IPv4Cidr;
import io.sadpipers.cdk.type.SafeString;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.RecordTarget;

public class Route53Stack extends BaseStack {

  public Route53Stack(@NotNull final AbstractApp app, @NotNull final Environment environment) {
    super(app, environment);

    final VpcProps vpcProps = VpcProps.builder()
        .iPv4Cidr(IPv4Cidr.of("192.168.1.0/24"))
        .natGateways(0)
        .build();

     Vpc vpc = new Vpc(this, SafeString.of("Vpc"), vpcProps);

    final PublicARecordProps publicARecordProps = PublicARecordProps.builder()
        .recordName(app.getApplicationName().getValue())
        .domainName("services.sandpipers.yeah")
        .vpcId(vpc.getVpc().getVpcId())
        .target(RecordTarget.fromValues("services.load.balancer"))
        .zone(getPublicHostedZone("PublicHostedZone"))
        .build();

    new PublicARecord<>(this, SafeString.of("PublicARecord"), publicARecordProps);

    final PrivateARecordProps privateARecordProps = PrivateARecordProps.builder()
        .recordName(app.getApplicationName().getValue())
        .domainName("internal.services.sandpipers.yeah")
        .vpcId(vpc.getVpc().getVpcId())
        .target(RecordTarget.fromValues("services.load.balancer"))
        .zone(getPublicHostedZone("PrivateHostedZone"))
        .build();
    new PrivateARecord<>(this, SafeString.of("PrivateARecord"), privateARecordProps);
  }

  private HostedZone getPublicHostedZone(final String hostedZoneId) {
    return HostedZone.Builder.create(this, hostedZoneId)
        .zoneName("sandpipers.yeah")
        .build();
  }
}
