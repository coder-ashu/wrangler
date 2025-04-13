
/*
 *  Copyright © 2017-2019 Cask Data, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
//D:\zeotap_project\wrangler\wrangler-core\src\test\java\io\cdap\directives\transformation

package io.cdap.directives.transformation;

import io.cdap.wrangler.TestingRig;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AggregateStatsTest {

  @Test
  public void testAggregateStatsTotal() throws Exception {
    // Input rows
    Row row1 = new Row("data_transfer_size", new ByteSize("512KB"));
    row1.add("response_time", new TimeDuration("1s"));

    Row row2 = new Row("data_transfer_size", new ByteSize("1MB"));
    row2.add("response_time", new TimeDuration("1500ms"));

    List<Row> input = Arrays.asList(row1, row2);

    // Recipe to run
    String[] recipe = new String[] {
      "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec MB s total"
    };

    List<Row> output = TestingRig.execute(recipe, input);

    // Assertions
    assertEquals(1, output.size());
    Row result = output.get(0);

    double expectedBytes = (512 * 1024 + 1 * 1024 * 1024) / (1024.0 * 1024.0); // in MB
    double expectedTime = (1000 + 1500) / 1000.0; // in seconds

    assertEquals(expectedBytes, (double) result.getValue("total_size_mb"), 0.001);
    assertEquals(expectedTime, (double) result.getValue("total_time_sec"), 0.001);
  }
}
