package com.ly.base.common.model.fastjson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ly.base.common.util.NumberUtil;

/**
 * @author LeiYong
 */
public class BigDecimalCustomSerializer extends BigDecimalCodec {

    private int digits = 2;
    private boolean fullZero = true;

    public BigDecimalCustomSerializer() {
		super();
	}

	public BigDecimalCustomSerializer(int digits, boolean fullZero) {
		super();
		this.digits = digits;
		this.fullZero = fullZero;
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            out.write('0');
            return;
        }

        BigDecimal val = (BigDecimal) object;
        String value = val.toString();
        out.write(NumberUtil.decimal(value, digits, fullZero));
        if (out.isEnabled(SerializerFeature.WriteClassName) && fieldType != BigDecimal.class && val.scale() == 0) {
            out.write('.');
        }
    }
}
