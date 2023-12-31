
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

import org.apache.poi.util.GenericRecordUtil;
import org.apache.poi.util.LittleEndianOutput;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Defines whether to recalculate before saving (set to true)
 *
 * @version 2.0-pre
 */
public final class SaveRecalcRecord extends StandardRecord {
    public static final short sid = 0x5f;
    private short field_1_recalc;

    public SaveRecalcRecord() {
    }

    public SaveRecalcRecord(SaveRecalcRecord other) {
        super(other);
        field_1_recalc = other.field_1_recalc;
    }

    public SaveRecalcRecord(RecordInputStream in) {
        field_1_recalc = in.readShort();
    }

    /**
     * set whether to recalculate formulas/etc before saving or not
     *
     * @param recalc - whether to recalculate or not
     */
    public void setRecalc(boolean recalc) {
        field_1_recalc = (short) (recalc ? 1 : 0);
    }

    /**
     * get whether to recalculate formulas/etc before saving or not
     *
     * @return recalc - whether to recalculate or not
     */
    public boolean getRecalc() {
        return (field_1_recalc == 1);
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_1_recalc);
    }

    protected int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    @Override
    public SaveRecalcRecord copy() {
        return new SaveRecalcRecord(this);
    }

    @Override
    public HSSFRecordTypes getGenericRecordType() {
        return HSSFRecordTypes.SAVE_RECALC;
    }

    @Override
    public Map<String, Supplier<?>> getGenericProperties() {
        return GenericRecordUtil.getGenericProperties("recalc", this::getRecalc);
    }
}
