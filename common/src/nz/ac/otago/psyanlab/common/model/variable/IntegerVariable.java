
package nz.ac.otago.psyanlab.common.model.variable;

import com.google.gson.annotations.Expose;

import nz.ac.otago.psyanlab.common.model.Variable;
import nz.ac.otago.psyanlab.common.model.util.MethodId;
import nz.ac.otago.psyanlab.common.model.util.ParameterId;

public class IntegerVariable extends Variable {
    @Expose
    int value;

    public IntegerVariable() {
    }

    @MethodId(METHOD_SET_AND_USE)
    public int chainSetVariableValue(@ParameterId(PARAM_VALUE) int value) {
        return value;
    }

    @Override
    public String getValue() {
        return String.valueOf(value);
    }

    @MethodId(METHOD_GET)
    public int getVariableValue() {
        return value;
    }

    @MethodId(METHOD_SET)
    public void setVariableValue(@ParameterId(PARAM_VALUE) int value) {

    }
}