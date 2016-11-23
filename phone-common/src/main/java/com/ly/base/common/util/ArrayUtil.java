package com.ly.base.common.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.util.ConcurrentReferenceHashMap;

import com.ly.base.common.model.lambda.BeanOperator;
import com.ly.base.common.model.lambda.FieldOperator;
import com.ly.base.common.model.lambda.ListOperator;
import com.ly.base.common.model.lambda.MapOperator;

/**
 * 数组工具类
 * 
 * @author LeiYong
 */
public class ArrayUtil {
	private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentReferenceHashMap<Class<?>, Field[]>(
			256);
	private static final Field[] NO_FIELDS = {};

	/**
	 * 对list执行排序
	 * 
	 * @param list
	 * @param asc
	 *            是否正序排序
	 * @return
	 */
	public static <T extends Comparable<T>> List<T> sortList(List<T> list, boolean asc) {
		if (list == null) {
			return list;
		}
		list.sort((T a, T b) -> {
			if (asc) {
				return a.compareTo(b);
			} else {
				return b.compareTo(a);
			}
		});
		return list;
	}

	/**
	 * 对list中数据执行过滤
	 * 
	 * @param list
	 * @param predicate
	 *            过滤接口
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		return (List<T>) Arrays.asList(list.stream().filter(predicate).toArray());
	}

	/**
	 * 遍历List 请勿做删除操作,需执行删除请调用list原生foreach((T t)->{list.remove(t);})
	 * 
	 * @param list
	 * @param operator
	 * @return 仅当operate返回false时,停止遍历并返回false
	 */
	public static <T> boolean foreach(List<T> list, ListOperator<T> operator) {
		if (list == null) {
			return true;
		}
		for (int i = 0; i < list.size(); i++) {
			boolean res = operator.operator(list.get(i), i);
			if (!res) {
				return res;
			}
		}
		return true;
	}

	/**
	 * 遍历Array 请勿做删除操作,需执行删除请调用list原生foreach((T t)->{list.remove(t);})
	 * 
	 * @param array
	 * @param operator
	 * @return 仅当operate返回false时,停止遍历并返回false
	 */
	public static <T> boolean foreach(T[] array, ListOperator<T> operator) {
		if (array == null) {
			return true;
		}
		for (int i = 0; i < array.length; i++) {
			boolean res = operator.operator(array[i], i);
			if (!res) {
				return res;
			}

		}
		return true;
	}

	/**
	 * 遍历Set
	 * 
	 * @param list
	 * @param operator
	 * @return 仅当operate返回false时,停止遍历并返回false
	 */
	public static <T> boolean foreach(Set<T> set, BeanOperator<T> operator) {
		if (set == null) {
			return true;
		}
		for (T k : set) {
			boolean res = operator.operator(k);
			if (!res) {
				return res;
			}
		}
		return true;
	}

	/**
	 * 遍历Map
	 * 
	 * @param list
	 * @param operator
	 * @return 仅当operate返回false时,停止遍历并返回false
	 */
	public static <K, V> boolean foreach(Map<K, V> map, MapOperator<K, V> operator) {
		if (map == null) {
			return false;
		}
		return foreach(map.keySet(), (k) -> {
			return operator.operator(k, map.get(k));
		});
	}

	/**
	 * 遍历Bean
	 * 此处参考spring遍历bean方法,采用jdk1.8特性重写
	 * @param obj
	 * @param operator
	 * @param fieldFilter 属性过滤器,不过滤为null
	 * @return 仅当operate返回false时,停止遍历并返回false
	 */
	public static<K,V> boolean foreach(Object obj,boolean inSuper,FieldOperator operator){
		if (obj==null) {
			return true;
		}

		Class<?> targetClass = obj.getClass();
		do {
			Field[] fields = getDeclaredFields(targetClass);
			for (Field field : fields) {
				boolean res = operator.operator(field, ReflectionUtil.getFieldAndSetAccessible(field, obj));
				if (!res) {
					return res;
				}
			}
		}
		while (inSuper&&(targetClass = targetClass.getSuperclass()) != null && targetClass != Object.class);
		return true;
	}
	/**
	 * 获取Bean属性,并缓存
	 * @param clazz
	 * @return
	 */
	private static Field[] getDeclaredFields(Class<?> clazz) {
		Field[] result = declaredFieldsCache.get(clazz);
		if (result == null) {
			result = clazz.getDeclaredFields();
			declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
		}
		return result;
	}
}
