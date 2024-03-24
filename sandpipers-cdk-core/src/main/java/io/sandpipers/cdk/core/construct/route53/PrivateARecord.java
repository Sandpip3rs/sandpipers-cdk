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

import io.sandpipers.cdk.core.construct.route53.AbstractARecord.ARecordProps;
import io.sadpipers.cdk.type.SafeString;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import software.constructs.Construct;

/**
 * L3 Construct representing AWS::Route53::RecordSet (A Record) for private zone
 *<p> Example usage can be found in <a href="https://github.com/muhamadto/sandpipers-cdk/blob/main/sandpipers-cdk-examples/sandpipers-cdk-example-route53/src/main/java/com/sandpipers/cdk/example/route53/Route53Stack.java">sandpipers-cdk-example-route53</a></p>
 */
public class PrivateARecord<T extends ARecordProps> extends AbstractARecord<T> {

  public PrivateARecord(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id, props);
  }
  @Getter
  @SuperBuilder
  public static class PrivateARecordProps extends ARecordProps {

    private final Boolean privateZone = true;

    @Default
    private final String domainName = "internal.sandpipers.com";
  }
}
