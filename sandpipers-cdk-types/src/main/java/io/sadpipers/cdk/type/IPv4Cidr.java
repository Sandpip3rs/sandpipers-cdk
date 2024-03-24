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
import java.util.regex.Pattern;
import org.typefactory.MessageCode;
import org.typefactory.StringType;
import org.typefactory.TypeParser;

/**
 * A String representing cidr block.
 */
public class IPv4Cidr extends StringType implements CharSequence {
  private static final int MIN_SIZE = 7;
  private static final int MAX_SIZE = 18;
  private static final String SPECIAL_CHARS = "./";

  private static final MessageCode ERROR_MESSAGE = MessageCode.of(
      "invalid.cidr",
      "must be a %d-%d character(s) value containing Alphanumeric as well as './".formatted(MIN_SIZE, MAX_SIZE)
  );

  private static final TypeParser TYPE_PARSER = TypeParser.builder()
      .messageCode(ERROR_MESSAGE)
      .acceptDigits0to9()
      .acceptChars(SPECIAL_CHARS.toCharArray())
      .matchesRegex(Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}+|2[0-4][0-9]|25[0-5])\\.){3}+([0-9]|[1-9][0-9]|1[0-9]{2}+|2[0-4][0-9]|25[0-5])(\\/(\\d|[1-2]\\d|3[0-2]))$"))
      .forbidWhitespace()
      .convertEmptyToNull()
      .minSize(MIN_SIZE)
      .maxSize(MAX_SIZE)
      .build();

  @JsonCreator
  public static IPv4Cidr of(final CharSequence value) {
    return TYPE_PARSER.parseToStringType(value, IPv4Cidr::new);
  }

  @JsonValue
  public String getValue() {
    return value();
  }

  private IPv4Cidr(final String value) {
    super(value);
  }
}
