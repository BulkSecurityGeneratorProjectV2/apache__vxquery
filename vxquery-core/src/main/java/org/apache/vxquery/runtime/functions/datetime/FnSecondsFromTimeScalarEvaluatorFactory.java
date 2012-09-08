package org.apache.vxquery.runtime.functions.datetime;

import org.apache.vxquery.datamodel.accessors.TaggedValuePointable;
import org.apache.vxquery.datamodel.accessors.atomic.XSTimePointable;
import org.apache.vxquery.datamodel.values.ValueTag;

import edu.uci.ics.hyracks.algebricks.runtime.base.IScalarEvaluatorFactory;

public class FnSecondsFromTimeScalarEvaluatorFactory extends AbstractValueFromDateTimeScalarEvaluatorFactory {
    private static final long serialVersionUID = 1L;

    public FnSecondsFromTimeScalarEvaluatorFactory(IScalarEvaluatorFactory[] args) {
        super(args);
    }

    @Override
    protected int getInputTag() {
        return ValueTag.XS_TIME_TAG;
    }

    @Override
    protected int getReturnTag() {
        return ValueTag.XS_DECIMAL_TAG;
    }

    @Override
    protected long getValueAsInteger(TaggedValuePointable tvp) {
        XSTimePointable timep = (XSTimePointable) XSTimePointable.FACTORY.createPointable();
        tvp.getValue(timep);
        return timep.getMilliSecond();
    }
}
