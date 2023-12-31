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

package org.apache.poi.hssf.eventusermodel.dummyrecord;


import org.apache.poi.hssf.record.HSSFRecordTypes;
import org.apache.poi.util.GenericRecordUtil;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A dummy record for when we're missing a cell in a row,
 *  but still want to trigger something
 */
public final class MissingCellDummyRecord extends DummyRecordBase {
	private final int row;
	private final int column;

	public MissingCellDummyRecord(int row, int column) {
		this.row = row;
		this.column = column;
	}
	public int getRow() { return row; }
	public int getColumn() { return column; }

	@Override
	public MissingCellDummyRecord copy() {
		return this;
	}

	@Override
	public HSSFRecordTypes getGenericRecordType() {
		return null;
	}

	@Override
	public Map<String, Supplier<?>> getGenericProperties() {
		return GenericRecordUtil.getGenericProperties(
			"row", this::getRow,
			"column", this::getColumn
		);
	}
}
