/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ly.base.common.model.fastjson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ListSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ly.base.common.util.ReflectionUtil;
import com.github.pagehelper.Page;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class PageSerializer implements ObjectSerializer {

    public static final PageSerializer instance = new PageSerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
                                                                                                       throws IOException {

        boolean writeClassName = serializer.isEnabled(SerializerFeature.WriteClassName);

        SerializeWriter out = serializer.getWriter();

        Type elementType = null;
        if (writeClassName) {
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType param = (ParameterizedType) fieldType;
                elementType = param.getActualTypeArguments()[0];
            }
        }

        if (object == null) {
            out.write("null");
            return;
        }
        
        Page<?> list = ReflectionUtil.convertObjectToBean(object, Page.class);
        //如果不是page对象则使用list序列化
        if (list==null) {
			ListSerializer.instance.write(serializer, object, fieldName, fieldType, features);
			return;
		}
        if (list.size() == 0) {
            out.append("{total:0,list:[]}");
            return;
        }

        SerialContext context = serializer.getContext();
        serializer.setContext(context, object, fieldName, 0);

        ObjectSerializer itemSerializer = null;
        try {
            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
            	out.append("{");
            	out.append("total:");
            	out.append(String.valueOf(list.getTotal()));
            	out.append(",list:");
                out.append('[');
                serializer.incrementIndent();

                int i = 0;
                for (Object item : list) {
                    if (i != 0) {
                        out.append(',');
                    }

                    serializer.println();
                    if (item != null) {
                        if (serializer.containsReference(item)) {
                            serializer.writeReference(item);
                        } else {
                            itemSerializer = serializer.getObjectWriter(item.getClass());
                            SerialContext itemContext = new SerialContext(context, object, fieldName, 0, 0);
                            serializer.setContext(itemContext);
                            itemSerializer.write(serializer, item, i, elementType, 0);
                        }
                    } else {
                        serializer.getWriter().writeNull();
                    }
                    i++;
                }

                serializer.decrementIdent();
                serializer.println();
                out.append(']');
                out.append("}");
                return;
            }

        	out.append("{");
        	out.append("total:");
        	out.append(String.valueOf(list.getTotal()));
        	out.append(",list:");
            out.append('[');
            int i = 0;
            for (Object item : list) {
                if (i != 0) {
                    out.append(',');
                }
                
                if (item == null) {
                    out.append("null");
                } else {
                    Class<?> clazz = item.getClass();

                    if (clazz == Integer.class) {
                        out.writeInt(((Integer) item).intValue());
                    } else if (clazz == Long.class) {
                        long val = ((Long) item).longValue();
                        if (writeClassName) {
                            out.writeLong(val);
                            out.write('L');
                        } else {
                            out.writeLong(val);
                        }
                    } else {
                        SerialContext itemContext = new SerialContext(context, object, fieldName, 0, 0);
                        serializer.setContext(itemContext);

                        if (serializer.containsReference(item)) {
                            serializer.writeReference(item);
                        } else {
                            itemSerializer = serializer.getObjectWriter(item.getClass());
                            itemSerializer.write(serializer, item, i, elementType, 0);
                        }
                    }
                }
                i++;
            }
            out.append(']');
            out.append("}");
        } finally {
            serializer.setContext(context);
        }
    }

}
