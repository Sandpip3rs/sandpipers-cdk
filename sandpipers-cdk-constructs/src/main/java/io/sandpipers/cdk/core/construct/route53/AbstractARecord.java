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

package io.sandpipers.cdk.core.construct.route53;

import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sandpipers.cdk.core.construct.route53.AbstractARecord.ARecordProps;
import io.sadpipers.cdk.type.SafeString;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.route53.ARecord.Builder;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneProviderProps;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.RecordSet;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.constructs.Construct;

@Getter
public abstract class AbstractARecord<T extends ARecordProps> extends Construct implements BaseConstruct {

  private final RecordSet recordSet;

  public AbstractARecord(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id.getValue());

    final Builder builder = Builder.create(this, id.getValue())
        .ttl(props.getTtl())
        .recordName(props.getRecordName())
        .target(props.getTarget());

    this.recordSet = props.getZone() == null
        ? builder.zone(getHostedZone(props)).build()
        : builder.zone(props.getZone()).build();

    this.recordSet.applyRemovalPolicy(props.getRemovalPolicy());
  }

  @NotNull
  private IHostedZone getHostedZone(final ARecordProps props) {

    return HostedZone.fromLookup(this, "HostedZone", HostedZoneProviderProps.builder()
        .domainName(props.getDomainName())
        .privateZone(props.getPrivateZone())
        .vpcId(props.getVpcId())
        .build());
  }

  @Getter
  @SuperBuilder
  public static class ARecordProps implements software.amazon.awscdk.services.route53.ARecordProps {

    private final Boolean privateZone = false;

    private final String domainName = null;

    @NotNull
    private final String recordName;

    @NotNull
    private final String vpcId;

    @NotNull
    private RecordTarget target;

    @Default
    private final RemovalPolicy removalPolicy = RemovalPolicy.DESTROY;

    @Default
    @Nullable
    private final IHostedZone zone = null;

    @Default
    @Nullable
    private final Duration ttl = Duration.minutes(30);
  }
}
