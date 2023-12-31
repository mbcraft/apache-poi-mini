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

package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.eval.*;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
import org.apache.poi.ss.formula.functions.ArrayFunction;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.formula.functions.Indirect;
import org.apache.poi.ss.formula.ptg.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * This class creates <tt>OperationEval</tt> instances to help evaluate <tt>OperationPtg</tt>
 * formula tokens.
 *
 * @author Josh Micich
 */
final class OperationEvaluatorFactory {

	private static final Map<Byte, Function> _instancesByPtgClass = initialiseInstancesMap();

	private OperationEvaluatorFactory() {
		// no instances of this class
	}

	private static Map<Byte, Function> initialiseInstancesMap() {
		Map<Byte, Function> m = new HashMap<>(32);

		m.put(AddPtg.instance.getSid(), TwoOperandNumericOperation.AddEval); // 0x03
		m.put(SubtractPtg.instance.getSid(), TwoOperandNumericOperation.SubtractEval); // 0x04
		m.put(MultiplyPtg.instance.getSid(), TwoOperandNumericOperation.MultiplyEval); // 0x05
		m.put(DividePtg.instance.getSid(), TwoOperandNumericOperation.DivideEval); // 0x06
		m.put(PowerPtg.instance.getSid(), TwoOperandNumericOperation.PowerEval); // 0x07
		m.put(ConcatPtg.instance.getSid(), ConcatEval.instance); // 0x08
		m.put(LessThanPtg.instance.getSid(), RelationalOperationEval.LessThanEval); // 0x09
		m.put(LessEqualPtg.instance.getSid(), RelationalOperationEval.LessEqualEval); // 0x0a
		m.put(EqualPtg.instance.getSid(), RelationalOperationEval.EqualEval); // 0x0b
		m.put(GreaterEqualPtg.instance.getSid(), RelationalOperationEval.GreaterEqualEval); // 0x0c
		m.put(GreaterThanPtg.instance.getSid(), RelationalOperationEval.GreaterThanEval); // 0x0D
		m.put(NotEqualPtg.instance.getSid(), RelationalOperationEval.NotEqualEval); // 0x0e
		m.put(IntersectionPtg.instance.getSid(), IntersectionEval.instance); // 0x0f
		m.put(RangePtg.instance.getSid(), RangeEval.instance); // 0x11
		m.put(UnaryPlusPtg.instance.getSid(), UnaryPlusEval.instance); // 0x12
		m.put(UnaryMinusPtg.instance.getSid(), UnaryMinusEval.instance); // 0x13
		m.put(PercentPtg.instance.getSid(), PercentEval.instance); // 0x14

		return m;
	}

	/**
	 * returns the OperationEval concrete impl instance corresponding
	 * to the supplied operationPtg
	 */
	public static ValueEval evaluate(OperationPtg ptg, ValueEval[] args,
			OperationEvaluationContext ec) {
		if(ptg == null) {
			throw new IllegalArgumentException("ptg must not be null");
		}
		Function result = _instancesByPtgClass.get(ptg.getSid());
		FreeRefFunction udfFunc = null;
		if (result == null) {
			if (ptg instanceof AbstractFunctionPtg) {
				AbstractFunctionPtg fptg = (AbstractFunctionPtg)ptg;
				int functionIndex = fptg.getFunctionIndex();
				switch (functionIndex) {
					case FunctionMetadataRegistry.FUNCTION_INDEX_INDIRECT:
						udfFunc = Indirect.instance;
						break;
					case FunctionMetadataRegistry.FUNCTION_INDEX_EXTERNAL:
						udfFunc = UserDefinedFunction.instance;
						break;
					default:
						result = FunctionEval.getBasicFunction(functionIndex);
						break;
				}
			}
		}
		if (result != null) {
			EvaluationSheet evalSheet = ec.getWorkbook().getSheet(ec.getSheetIndex());
			EvaluationCell evalCell = evalSheet.getCell(ec.getRowIndex(), ec.getColumnIndex());

		    if (evalCell != null && result instanceof ArrayFunction) {
				ArrayFunction func = (ArrayFunction) result;
				if(evalCell.isPartOfArrayFormulaGroup()){
					// array arguments must be evaluated relative to the function defining range
					CellRangeAddress ca = evalCell.getArrayFormulaRange();
					return func.evaluateArray(args, ca.getFirstRow(), ca.getFirstColumn());
				} else if (ec.isArraymode()){
					return func.evaluateArray(args, ec.getRowIndex(), ec.getColumnIndex());
				}
			}

			return  result.evaluate(args, ec.getRowIndex(), ec.getColumnIndex());
		} else if (udfFunc != null){
			return  udfFunc.evaluate(args, ec);
		}

		throw new RuntimeException("Unexpected operation ptg class (" + ptg.getClass().getName() + ")");
	}
}
