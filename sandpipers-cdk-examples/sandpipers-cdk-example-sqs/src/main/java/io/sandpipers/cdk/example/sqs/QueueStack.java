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

import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.sqs.Queue;
import io.sandpipers.cdk.core.construct.sqs.Queue.QueueProps;
import io.sadpipers.cdk.type.SafeString;
import org.jetbrains.annotations.NotNull;

public class QueueStack extends BaseStack {

  public QueueStack(@NotNull AbstractApp app, @NotNull Environment environment) {
    super(app, environment);

    final QueueProps queueProps = QueueProps.builder()
        .deadLetterQueueMaxReceiveCount(12)
        .build();

    new Queue<>(this, SafeString.of("Queue"), queueProps);
  }
}
