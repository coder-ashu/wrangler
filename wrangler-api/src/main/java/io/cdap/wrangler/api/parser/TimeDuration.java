/*
 * Copyright © 2025 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

/**
 * Represents a duration of time with millisecond precision.
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;




public class TimeDuration implements Token {

  private static final Logger LOG = Logger.getLogger(TimeDuration.class.getName());
  private static final Pattern PATTERN = Pattern.compile("^([0-9]*\\.?[0-9]+)\\s*([a-zA-Z]+)$");

  private final double originalValue;
  private final String unit;
  private final long milliseconds;
  private final String rawInput;

  public TimeDuration(String value) {
    this.rawInput = value;
    Matcher matcher = PATTERN.matcher(value.trim());

    if (!matcher.matches()) {
      LOG.severe("Invalid time duration format: " + value);
      throw new IllegalArgumentException("Invalid time duration format: " + value);
    }

    this.originalValue = Double.parseDouble(matcher.group(1));
    this.unit = matcher.group(2).toLowerCase(Locale.ROOT);
    this.milliseconds = convertToMilliseconds(originalValue, unit);
    LOG.fine("Parsed time duration: " + rawInput + " = " + milliseconds + " ms");
  }

  private long convertToMilliseconds(double value, String unit) {
    switch (unit) {
      case "ms": return (long) value;
      case "s":
      case "sec": return (long) (value * 1000);
      case "m":
      case "min": return (long) (value * 60 * 1000);
      case "h":
      case "hr": return (long) (value * 60 * 60 * 1000);
      default:
        LOG.severe("Unknown time unit: " + unit);
        throw new IllegalArgumentException("Unknown time unit: " + unit);
    }
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  @Override
  public Object value() {
    return milliseconds;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonElement toJson() {
    return new JsonPrimitive(milliseconds);
  }

  @Override
  public String toString() {
    return rawInput + " = " + milliseconds + " ms";
  }
}


