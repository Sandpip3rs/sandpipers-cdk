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

import io.sadpipers.cdk.type.AlphanumericString;
import java.util.HashMap;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Example usage <code><a href="https://github.com/muhamadto/sandpipers-cdk/blob/main/sandpipers-cdk-examples/sandpipers-cdk-example-lambda/src/main/java/com/sandpipers/cdk/example/lambda/CostCentre.java">CostCentre</a></code>.
 */
@Getter
@SuperBuilder
public abstract class AbstractCostCentre {

  private static final HashMap<CharSequence, AbstractCostCentre> costCentres = new HashMap<>();

  @NotNull
  private final AlphanumericString value;

  @NotNull
  public String getValueAsString() {
    return value.getValue();
  }

  public static AbstractCostCentre of(@NotNull final AlphanumericString costCentre) {
    return Optional
        .ofNullable(costCentres.get(costCentre))
        .orElseThrow(() -> new IllegalArgumentException("CostCentre not found for costCentre: '" + costCentre + "'"));
  }

  public static void registerCostCentre(@NotNull final AbstractCostCentre costCentre) {
    requireNonNull(costCentre, "'costCentre' must not be null");

    costCentres.put(costCentre.getValue(), costCentre);
  }
}
