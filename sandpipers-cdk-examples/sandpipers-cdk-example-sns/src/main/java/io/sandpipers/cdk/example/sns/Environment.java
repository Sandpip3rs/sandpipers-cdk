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

package io.sandpipers.cdk.example.sns;

import static io.sandpipers.cdk.core.util.Constants.AWS_REGION_AP_SOUTHEAST_2;

import io.sandpipers.cdk.core.AbstractEnvironment;
import io.sadpipers.cdk.type.AWSAccount;
import io.sadpipers.cdk.type.SafeString;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Environment extends AbstractEnvironment {

  public static final Environment SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

  static {
    final AWSAccount awsAccount = AWSAccount.of("111111111111");
    final SafeString awsRegion = SafeString.of(AWS_REGION_AP_SOUTHEAST_2);

    final software.amazon.awscdk.Environment awsEnvironment = software.amazon.awscdk.Environment.builder()
        .account(awsAccount.getValue())
        .account(awsRegion.getValue())
        .build();

    SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2 = Environment.builder()
        .awsEnvironment(awsEnvironment)
        .costCentre(CostCentre.SANDPIPERS)
        .environmentName(SafeString.of("TEST"))
        .environmentKey(SafeString.of("SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2"))
        .build();

    registerEnvironment(SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
  }
}