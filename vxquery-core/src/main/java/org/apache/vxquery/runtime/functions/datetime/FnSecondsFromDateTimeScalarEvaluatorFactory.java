package org.apache.vxquery.runtime.functions.datetime;

import org.apache.vxquery.datamodel.accessors.TaggedValuePointable;
import org.apache.vxquery.datamodel.accessors.atomic.XSDateTimePointable;
import org.apache.vxquery.datamodel.values.ValueTag;

import edu.uci.ics.hyracks.algebricks.runtime.base.IScalarEvaluatorFactory;

public class FnSecondsFromDateTimeScalarEvaluatorFactory extends AbstractValueFromDateTimeScalarEvaluatorFactory {
    private static final long serialVersionUID = 1L;

    public FnSecondsFromDateTimeScalarEvaluatorFactory(IScalarEvaluatorFactory[] args) {
        super(args);
    }

    @Override
    protected int getInputTag() {
        return ValueTag.XS_DATETIME_TAG;
    }

    @Override
    protected int getReturnTag() {
        return ValueTag.XS_DECIMAL_TAG;
    }

    @Override
    protected long getValueAsInteger(TaggedValuePointable tvp) {
        XSDateTimePointable datetimep = (XSDateTimePointable) XSDateTimePointable.FACTORY.createPointable();
        tvp.getValue(datetimep);
        return datetimep.getMilliSecond();
    }
}
