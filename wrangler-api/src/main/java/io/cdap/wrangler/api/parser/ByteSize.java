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

 

 package io.cdap.wrangler.api.parser;

 import com.google.gson.JsonElement;
 import com.google.gson.JsonPrimitive;
 
 import java.util.Locale;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
/**
 * Represents a size with byte conversion capabilities.
 */

 public class ByteSize implements Token {
 
   private static final Pattern PATTERN = Pattern.compile("^([0-9]*\\.?[0-9]+)\\s*([a-zA-Z]{1,2})$");
 
   private final double originalValue;
   private final String unit;
   private final long bytes;
   private final String rawInput;
 
   public ByteSize(String value) {
     this.rawInput = value;
     Matcher matcher = PATTERN.matcher(value.trim());
 
     if (!matcher.matches()) {
       throw new IllegalArgumentException("Invalid byte size format: " + value);
     }
 
     this.originalValue = Double.parseDouble(matcher.group(1));
     this.unit = matcher.group(2).toUpperCase(Locale.ROOT);
     this.bytes = convertToBytes(originalValue, unit);
   }
 
   private long convertToBytes(double value, String unit) {
     switch (unit) {
       case "B": return (long) value;
       case "KB": return (long) (value * 1024);
       case "MB": return (long) (value * 1024 * 1024);
       case "GB": return (long) (value * 1024 * 1024 * 1024);
       case "TB": return (long) (value * 1024L * 1024L * 1024L * 1024L);
       default: throw new IllegalArgumentException("Unknown byte unit: " + unit);
     }
   }
 
   public long getBytes() {
     return bytes;
   }
 
   @Override
   public Object value() {
     return bytes;
   }
 
   @Override
   public TokenType type() {
     return TokenType.BYTE_SIZE;
   }
 
   @Override
   public JsonElement toJson() {
     return new JsonPrimitive(bytes);
   }
 
   @Override
   public String toString() {
     return rawInput + " = " + bytes + " bytes";
   }
 }


 
 