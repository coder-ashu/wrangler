/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.cdap.wrangler.api.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ByteSize}.
 */
public class ByteSizeTest {

    @Test
    public void testBytesParsing() {
        ByteSize size = new ByteSize("1024B");
        assertEquals(1024L, size.getBytes());
    }

    @Test
    public void testKilobytesParsing() {
        ByteSize size = new ByteSize("1KB");
        assertEquals(1024L, size.getBytes());
    }

    @Test
    public void testMegabytesParsing() {
        ByteSize size = new ByteSize("2MB");
        assertEquals(2 * 1024 * 1024L, size.getBytes());
    }

    @Test
    public void testGigabytesParsing() {
        ByteSize size = new ByteSize("3GB");
        assertEquals(3L * 1024 * 1024 * 1024, size.getBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        new ByteSize("invalid");
    }
}