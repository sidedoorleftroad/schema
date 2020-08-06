package com.yuqi.engine.operator;

import com.google.common.collect.ImmutableList;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;
import com.yuqi.engine.io.IO;
import com.yuqi.sql.util.TypeConversionUtils;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/8/20 18:52
 **/
public class SlothValueOperator implements Operator, IO {
    private final ImmutableList<ImmutableList<RexLiteral>> values;
    private final RelDataType relDataType;

    //这里需要搞成Literal ???,
    private Iterator<List<Value>> r;

    public SlothValueOperator(ImmutableList<ImmutableList<RexLiteral>> values, RelDataType relDataType) {
        this.values = values;
        this.relDataType = relDataType;
    }

    @Override
    public void open() {
        r = values.stream()
                .map(l -> l.stream()
                        .map(r -> {
                                final SqlTypeName sqlTypeName = r.getType().getSqlTypeName();
                                DataType dataType = TypeConversionUtils.getBySqlTypeName(sqlTypeName);
                                return new Value(r.getValue2(), dataType);
                        }).collect(Collectors.toList())
                ).collect(Collectors.toList())
                .iterator();

    }

    @Override
    public List<Value> next() {
        if (r.hasNext()) {
            return r.next();
        }

        return null;
    }

    @Override
    public void close() {

    }
}