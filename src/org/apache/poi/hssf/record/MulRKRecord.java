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

package org.apache.poi.hssf.record;

import org.apache.poi.common.usermodel.GenericRecord;
import org.apache.poi.hssf.util.RKUtil;
import org.apache.poi.util.GenericRecordUtil;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.RecordFormatException;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Used to store multiple RK numbers on a row.  1 MulRk = Multiple Cell values.
 * HSSF just converts this into multiple NUMBER records.  READ-ONLY SUPPORT!
 *
 * @since 2.0-pre
 */
public final class MulRKRecord extends StandardRecord {
	public static final short sid = 0x00BD;

	private final int	  field_1_row;
	private final short   field_2_first_col;
	private final RkRec[] field_3_rks;
	private final short   field_4_last_col;

	public int getRow() {
		return field_1_row;
	}

	/**
	 * starting column (first cell this holds in the row)
	 * @return first column number
	 */
	public short getFirstColumn() {
		return field_2_first_col;
	}

	/**
	 * ending column (last cell this holds in the row)
	 * @return first column number
	 */
	public short getLastColumn() {
		return field_4_last_col;
	}

	/**
	 * get the number of columns this contains (last-first +1)
	 * @return number of columns (last - first +1)
	 */
	public int getNumColumns() {
		return field_4_last_col - field_2_first_col + 1;
	}

	/**
	 * returns the xf index for column (coffset = column - field_2_first_col)
	 *
     * @param coffset the coffset = column - field_2_first_col
     *
	 * @return the XF index for the column
	 */
	public short getXFAt(int coffset) {
		return field_3_rks[coffset].xf;
	}

	/**
	 * returns the rk number for column (coffset = column - field_2_first_col)
	 *
	 * @param coffset the coffset = column - field_2_first_col
	 *
	 * @return the value (decoded into a double)
	 */
	public double getRKNumberAt(int coffset) {
		return RKUtil.decodeNumber(field_3_rks[coffset].rk);
	}

	/**
	 * @param in the RecordInputstream to read the record from
	 */
	public MulRKRecord(RecordInputStream in) {
		field_1_row = in.readUShort();
		field_2_first_col = in.readShort();
		field_3_rks = RkRec.parseRKs(in);
		field_4_last_col = in.readShort();
	}

	@Override
    public short getSid()
	{
		return sid;
	}

	@Override
    public void serialize(LittleEndianOutput out) {
		throw new RecordFormatException( "Sorry, you can't serialize MulRK in this release");
	}
	@Override
    protected int getDataSize() {
		throw new RecordFormatException( "Sorry, you can't serialize MulRK in this release");
	}

	private static final class RkRec implements GenericRecord {
		public static final int ENCODED_SIZE = 6;
		public final short xf;
		public final int   rk;

		private RkRec(RecordInputStream in) {
			xf = in.readShort();
			rk = in.readInt();
		}

		public static RkRec[] parseRKs(RecordInputStream in) {
			int nItems = (in.remaining()-2) / ENCODED_SIZE;
			RkRec[] retval = new RkRec[nItems];
			for (int i=0; i<nItems; i++) {
				retval[i] = new RkRec(in);
			}
			return retval;
		}

		@Override
		public Map<String, Supplier<?>> getGenericProperties() {
			return GenericRecordUtil.getGenericProperties(
				"xf", () -> xf,
				"rk", () -> rk
			);
		}
	}

	@Override
	public MulRKRecord copy() {
		// immutable - so OK to return this
		return this;
	}

	@Override
	public HSSFRecordTypes getGenericRecordType() {
		return HSSFRecordTypes.MUL_RK;
	}

	@Override
	public Map<String, Supplier<?>> getGenericProperties() {
		return GenericRecordUtil.getGenericProperties(
			"row", this::getRow,
			"firstColumn", this::getFirstColumn,
			"lastColumn", this::getLastColumn,
			"rk", () -> field_3_rks
		);
	}
}
