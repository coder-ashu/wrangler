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
 * Tests for {@link TimeDuration}.
 */
public class TimeDurationTest {

    @Test
    public void testMilliseconds() {
        TimeDuration duration = new TimeDuration("100ms");
        assertEquals(100L, duration.getMilliseconds());
    }

    @Test
    public void testSeconds() {
        TimeDuration duration = new TimeDuration("2s");
        assertEquals(2000L, duration.getMilliseconds());
    }

    @Test
    public void testMinutes() {
        TimeDuration duration = new TimeDuration("3min");
        assertEquals(3 * 60 * 1000L, duration.getMilliseconds());
    }

    @Test
    public void testHours() {
        TimeDuration duration = new TimeDuration("1h");
        assertEquals(1 * 60 * 60 * 1000L, duration.getMilliseconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        new TimeDuration("oops");
    }
}
