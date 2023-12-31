/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;

import java.util.Calendar;

/**
 * Implementation of Excel TODAY() Function<br>
 */
public final class Today extends Fixed0ArgFunction {
	public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
		Calendar now = LocaleUtil.getLocaleCalendar();
		now.clear(Calendar.HOUR);
        now.set(Calendar.HOUR_OF_DAY,0);
		now.clear(Calendar.MINUTE);
		now.clear(Calendar.SECOND);
		now.clear(Calendar.MILLISECOND);
		return new NumberEval(DateUtil.getExcelDate(now.getTime()));
	}
}
