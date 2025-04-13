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

 package io.cdap.directives.transformation;

 import io.cdap.wrangler.api.Arguments;
 import io.cdap.wrangler.api.Directive;
 import io.cdap.wrangler.api.DirectiveExecutionException;
 import io.cdap.wrangler.api.ExecutorContext;
 import io.cdap.wrangler.api.Row;
 import io.cdap.wrangler.api.parser.ByteSize;
 import io.cdap.wrangler.api.parser.ColumnName;
 import io.cdap.wrangler.api.parser.TimeDuration;
 import io.cdap.wrangler.api.parser.UsageDefinition;

 
 import java.util.List;
 
 /**
  * Simplified directive to calculate total byte size and total time.
  */
 public class AggregateStats implements Directive {
   private String byteColumn;
   private String timeColumn;
   private String resultByteColumn;
   private String resultTimeColumn;
 
   private long totalBytes = 0;
   private long totalTime = 0;
 
   //@Override
   public int getNumFields() {
       return 4; // You require 4 arguments: byteCol, timeCol, resultByteCol, resultTimeCol
   }

  // @Override
  
 
 
   @Override
   public void initialize(Arguments args) {
     byteColumn = ((ColumnName) args.value("0")).value();
     timeColumn = ((ColumnName) args.value("1")).value();
     resultByteColumn = ((ColumnName) args.value("2")).value();
     resultTimeColumn = ((ColumnName) args.value("3")).value();
   }

   @Override
   public UsageDefinition define() {
     return UsageDefinition.builder("aggregate-stats").build();
   }
   
 
   @Override
   public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
     for (Row row : rows) {
       Object byteObj = row.getValue(byteColumn);
       Object timeObj = row.getValue(timeColumn);
 
       if (byteObj instanceof ByteSize) {
         totalBytes += ((ByteSize) byteObj).getBytes();
       }
 
       if (timeObj instanceof TimeDuration) {
         totalTime += ((TimeDuration) timeObj).getMilliseconds();
       }
     }
 
     Row result = new Row();
     result.add(resultByteColumn, totalBytes);    // In bytes
     result.add(resultTimeColumn, totalTime);     // In milliseconds
 
     return java.util.Collections.singletonList(result);

   }

 
   @Override
   public void destroy() {
     // No cleanup needed
   }
   public String getOutputField() {
    // Return a comma-separated string of the output fields (you can customize this if needed)
    return resultByteColumn + "," + resultTimeColumn;
}

 }
 