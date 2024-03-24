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
import io.sadpipers.cdk.type.SafeString;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.constructs.Construct;

@Getter
public class ExistingVpc extends Construct implements BaseConstruct {

  private final IVpc vpc;

  public ExistingVpc(@NotNull final Construct scope, @NotNull final SafeString id, @NotNull final VpcProps props) {
    super(scope, id.getValue());

    final VpcLookupOptions vpcLookupOptions = VpcLookupOptions.builder()
        .ownerAccountId(this.getAccount()).region(this.getRegion())
        .isDefault(props.defaultVpc)
        .tags(props.tags)
        .build();

    this.vpc = software.amazon.awscdk.services.ec2.Vpc.fromLookup(scope, id.getValue(), vpcLookupOptions);
  }

  @Builder
  public static class VpcProps implements software.amazon.awscdk.services.ec2.VpcProps {

    @Singular
    private final Map<String, String> tags = new HashMap<>();

    @Default
    private final Boolean defaultVpc = false;
  }
}