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
 * A String that can only contain chars {@code [a-zA-Z0-9]} and is not blank. Empty values are converted to null.
 */
public class SafeStringWithWhitespace extends StringType implements CharSequence {

  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 500;
  private static final String SPECIAL_CHARS = ".:_-";
  private static final MessageCode ERROR_MESSAGE = MessageCode.of(
      "invalid.string.safe.whitespace",
      "must be a %d-%d character(s) value containing Alphanumeric as well as '.:_-' and whitespaces".formatted(MIN_SIZE, MAX_SIZE)
  );

  private static final TypeParser TYPE_PARSER = TypeParser.builder()
      .messageCode(ERROR_MESSAGE)
      .acceptLettersAtoZ()
      .acceptDigits0to9()
      .acceptChars(SPECIAL_CHARS.toCharArray())
      .preserveCase()
      .convertEmptyToNull()
      .normalizeWhitespace()
      .minSize(MIN_SIZE)
      .maxSize(MAX_SIZE)
      .build();

  @JsonCreator
  public static SafeStringWithWhitespace of(final CharSequence value) {
    return TYPE_PARSER.parseToStringType(value, SafeStringWithWhitespace::new);
  }

  @JsonValue
  public String getValue() {
    return value();
  }

  private SafeStringWithWhitespace(final String value) {
    super(value);
  }
}
