package com.ly.base.common.model.fastjson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.github.pagehelper.Page;

public class PageDeserializer implements ObjectDeserializer {

    public final static PageDeserializer instance = new PageDeserializer();

    @SuppressWarnings({ "unchecked"})
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {

    	if (parser.getLexer().token() == JSONToken.NULL) {
            parser.getLexer().nextToken(JSONToken.COMMA);
            return null;
        }
        String input = parser.getInput();
//        final String inputValue = input.replaceAll("\\{.+list:\\[(.++)\\]\\}", "$1");
//        ReflectionUtil.setField(ReflectionUtil.findField(parser.getClass(), "input"), parser,inputValue );
        Page<T> list = null;
        if (null==parser.getInput()||"null".equals(input)) {
			return null;
		}
        JSONObject jo = parser.parseObject();
        JSONArray ja = jo.getJSONArray("list");
        if (ja!=null) {
            List<T> vList = (List<T>) ja;
			list = new Page<>();
			list.addAll(vList);
	        list.setTotal(jo.getIntValue("total"));
		}

        return (T) list;
    }

    public Class<?> getRawClass(Type type) {

        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        } else {
            throw new JSONException("TODO");
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}
