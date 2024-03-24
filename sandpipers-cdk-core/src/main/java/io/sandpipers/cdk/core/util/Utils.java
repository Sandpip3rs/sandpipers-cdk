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

package io.sandpipers.cdk.core.util;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Utils {

  public static String prefixId(final String application, final String id) {
    return "%s%s%s".formatted(Constants.VALUE_COST_CENTRE, application, id);
  }

  public static String kebabToCamel(final String kebabCaseString) {
    String[] words = StringUtils.split(kebabCaseString, '-');
    for (int i = 1; i < words.length; i++) {
      words[i] = StringUtils.capitalize(words[i]);
    }
    return StringUtils.capitalize(StringUtils.join(words));
  }
}
