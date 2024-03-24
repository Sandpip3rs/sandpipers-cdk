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

package io.sandpipers.cdk.core.construct;

import static io.sandpipers.cdk.core.util.Utils.kebabToCamel;

import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.AbstractEnvironment;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.DefaultStackSynthesizer;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class BaseStack extends Stack {

  public BaseStack(@NotNull final AbstractApp app, @NotNull final AbstractEnvironment environment) {
    this(app, createStackProps(app, environment));
  }

  public BaseStack(@NotNull final AbstractApp app, @NotNull final StackProps props) {
    super(app, props.getStackName(), props);
  }

  private static StackProps createStackProps(final AbstractApp app, final AbstractEnvironment environment) {
    final String stackName = "%s%sStake"
        .formatted(kebabToCamel(environment.getCostCentre().getValueAsString()), kebabToCamel(app.getApplicationName().getValue()));

    return StackProps.builder()
        .synthesizer(createDefaultStackSynthesizer(environment))
        .stackName(stackName)
        .env(environment.getAwsEnvironment())
        .build();
  }

  private static DefaultStackSynthesizer createDefaultStackSynthesizer(final AbstractEnvironment environment) {
    return DefaultStackSynthesizer.Builder.create()
        .qualifier(environment.getCostCentre().getValueAsString())
        .build();
  }
}