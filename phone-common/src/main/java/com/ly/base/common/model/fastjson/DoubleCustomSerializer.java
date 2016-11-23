package com.ly.base.common.model.fastjson;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.DoubleSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ly.base.common.util.NumberUtil;

/**
 * @author LeiYong
 */
public class DoubleCustomSerializer extends DoubleSerializer {
    private int digits = 2;
    private boolean fullZero = true;

    public DoubleCustomSerializer(){
    }

    public DoubleCustomSerializer(int digits,boolean fullZero){
        this.digits = digits;
        this.fullZero = fullZero;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.getWriter();

        if (object == null) {
            out.write('0');
            return;
        }

        double doubleValue = ((Double) object).doubleValue();

        if (Double.isNaN(doubleValue)||Double.isInfinite(doubleValue)) {
            out.write('0');
        } else {
            String doubleText = Double.toString(doubleValue);
            NumberUtil.decimal(doubleText, digits, fullZero);
            out.append(doubleText);

            if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
                out.write('D');
            }
        }
    }
}
