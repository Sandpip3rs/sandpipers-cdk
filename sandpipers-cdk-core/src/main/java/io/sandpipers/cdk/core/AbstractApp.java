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

import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.util.Constants;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.Tags;

public abstract class AbstractApp extends App {

  public static void tagResources(
      @NotNull final Stack stack,
      @NotNull final AbstractEnvironment environment,
      @NotNull final SafeString applicationName) {
    Tags.of(stack)
        .add(Constants.KEY_COST_CENTRE, Constants.VALUE_COST_CENTRE);

    Tags.of(stack)
        .add(Constants.KEY_APPLICATION_NAME, applicationName.getValue());

    Tags.of(stack)
        .add(Constants.KEY_ENVIRONMENT, environment.getEnvironmentName().getValue());
  }

  @NotNull
  public abstract SafeString getApplicationName();
}
