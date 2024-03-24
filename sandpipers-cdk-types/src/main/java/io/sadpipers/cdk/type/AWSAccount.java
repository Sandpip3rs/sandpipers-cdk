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

package io.sadpipers.cdk.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.typefactory.MessageCode;
import org.typefactory.StringType;
import org.typefactory.TypeParser;

/**
 * A String that can only contain chars {@code [0-9]{12}} and is not blank. Empty values are converted to null.
 */
public class AWSAccount extends StringType implements CharSequence {

  private static final int MIN_SIZE = 12;
  private static final int MAX_SIZE = 12;
  private static final MessageCode ERROR_MESSAGE = MessageCode.of(
      "invalid.aws.account",
      "must be a %d digits".formatted(MAX_SIZE)
  );

  private static final TypeParser TYPE_PARSER = TypeParser.builder()
      .messageCode(ERROR_MESSAGE)
      .acceptDigits0to9()
      .preserveCase()
      .forbidWhitespace()
      .convertEmptyToNull()
      .minSize(MIN_SIZE)
      .maxSize(MAX_SIZE)
      .build();

  @JsonCreator
  public static AWSAccount of(final CharSequence value) {
    return TYPE_PARSER.parseToStringType(value, AWSAccount::new);
  }

  @JsonValue
  public String getValue() {
    return value();
  }

  private AWSAccount(final String value) {
    super(value);
  }
}
