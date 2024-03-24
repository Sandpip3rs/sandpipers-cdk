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

package io.sandpipers.cdk.example.sqs;

import static io.sandpipers.cdk.example.sqs.Environment.SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

import io.sandpipers.cdk.core.AbstractApp;
import io.sadpipers.cdk.type.SafeString;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class Application extends AbstractApp {

  private static final SafeString APPLICATION_NAME = SafeString.of("sqs-cdk-example");

  public static void main(String[] args) {
    final Application app = new Application();

    final QueueStack queueStack = new QueueStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
    tagResources(queueStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, APPLICATION_NAME);

    final QueueStack fifoQueueStack = new QueueStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
    tagResources(fifoQueueStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, APPLICATION_NAME);

    app.synth();
  }

  @NotNull
  @Override
  public SafeString getApplicationName() {
    return SafeString.of(APPLICATION_NAME);
  }
}
