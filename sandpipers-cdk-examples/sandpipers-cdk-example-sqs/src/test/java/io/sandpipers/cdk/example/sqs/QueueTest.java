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

import static io.sandpipers.cdk.assertion.CDKStackAssert.assertThat;
import static io.sandpipers.cdk.core.AbstractApp.tagResources;
import static io.sandpipers.cdk.example.sqs.Environment.SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Template;

public class QueueTest extends TemplateSupport {

  @BeforeAll
  static void initAll() {

    final Application app = new Application();

    final QueueStack queueStack = new QueueStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
    tagResources(queueStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, app.getApplicationName());

    template = Template.fromStack(queueStack);
  }

  @Test
  void should_have_queue_with_dead_letter_queue() {
    assertThat(template)
        .containsQueue("^Queue[a-zA-Z0-9]{8}$")
        .hasDeadLetterQueue("^QueueDeadLetterQueue[a-zA-Z0-9]{8}$")
        .hasMaxRetrialCount(12)
        .hasUpdateReplacePolicy("Retain")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "sqs-cdk-example");
  }

  @Test
  void should_have_dead_letter_queue() {
    assertThat(template)
        .containsQueue("^QueueDeadLetterQueue[a-zA-Z0-9]{8}$")
        .hasUpdateReplacePolicy("Retain")
        .hasTag("COST_CENTRE", "Sandpipers")
        .hasTag("ENVIRONMENT", TEST)
        .hasTag("APPLICATION_NAME", "sqs-cdk-example");
  }
}
