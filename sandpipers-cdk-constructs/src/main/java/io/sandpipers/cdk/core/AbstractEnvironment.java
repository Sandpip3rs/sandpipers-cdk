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

package io.sandpipers.cdk.core;

import static java.util.Objects.requireNonNull;

import io.sadpipers.cdk.type.SafeString;
import java.util.HashMap;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Example usage <code><a href="https://github.com/muhamadto/sandpipers-cdk/blob/main/sandpipers-cdk-examples/sandpipers-cdk-example-lambda/src/main/java/com/sandpipers/cdk/example/lambda/Environment.java">Environment</a></code>.
 */
@Getter
@SuperBuilder
public abstract class AbstractEnvironment {

  private static final HashMap<CharSequence, AbstractEnvironment> environments = new HashMap<>();

  @NotNull
  private final software.amazon.awscdk.Environment awsEnvironment;

  @NotNull
  private final AbstractCostCentre costCentre;

  @NotNull
  private final SafeString environmentKey;

  @NotNull
  private final SafeString environmentName;

  public static AbstractEnvironment of(@NotNull final SafeString environmentKey) {
    return Optional
        .ofNullable(environments.get(environmentKey))
        .orElseThrow(() -> new IllegalArgumentException("Environment not found for awsEnvironment: '" + environmentKey + "'"));
  }

  public static void registerEnvironment(@NotNull final AbstractEnvironment abstractEnvironment) {
    requireNonNull(abstractEnvironment, "'environmentKey' must not be null");

    environments.put(abstractEnvironment.getEnvironmentKey(), abstractEnvironment);
  }
}
