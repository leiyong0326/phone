package com.ly.base.service.provideImpl;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ly.base.common.em.ext.LogEnum;
import com.ly.base.common.model.Model;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.core.key.KeyGenerate;
import com.ly.base.core.provide.BaseService;
import com.ly.base.dao.BaseMapper;
/**
 * 基础Service实现类
 * @author LeiYong
 *
 * @param <T>
 */
public  class BaseServiceImpl<T> implements BaseService<T> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

	protected BaseMapper<T> baseMapper;

	@Autowired
	protected KeyGenerate keyGenerate;
	
	
	public BaseMapper<T> getBaseMapper() {
		return baseMapper;
	}

	public void setBaseMapper(BaseMapper<T> baseMapper) {
		this.baseMapper = baseMapper;
	}

	@Override
	@Transactional(readOnly=false)
	public T insertSelective(T data) {
		if (logger.isDebugEnabled()) {
			logger.debug("insertSelective(T) - start"); //$NON-NLS-1$
		}

		setPk(data);
		int result = baseMapper.insertSelective(data);
		if (result==1) {
			if (logger.isDebugEnabled()) {
				logger.debug("insertSelective(T) - end"); //$NON-NLS-1$
			}
			return data;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("insertSelective(T) - end"); //$NON-NLS-1$
		}
		return null;
	}
	@Override
	@Transactional(readOnly=false)
	public int insertBatch(List<T> list) {
		if (logger.isDebugEnabled()) {
			logger.debug("insertBatch(List<T>) - start"); //$NON-NLS-1$
		}

		ArrayUtil.foreach(list,(t,i) -> setPk(t));
		int returnint = baseMapper.insertBatch(list);
		if (logger.isDebugEnabled()) {
			logger.debug("insertBatch(List<T>) - end"); //$NON-NLS-1$
		}
		return returnint;
	}


	@Override
	@Transactional(readOnly=false)
	public int updateByPK(T data) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateByPK(T) - start"); //$NON-NLS-1$
		}
		
		int returnint = baseMapper.updateByPrimaryKey(data);
		if (logger.isDebugEnabled()) {
			logger.debug("updateByPK(T) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	@Override
	@Transactional(readOnly=true)
	public Page<T> findByPage(T queryInfo, int pageNum, int pageSize,String orderBy) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByPage(T, int, int, String) - start"); //$NON-NLS-1$
		}

		PageHelper.startPage(pageNum, pageSize);
		Page<T> returnPage = baseMapper.selectAllByCondition(queryInfo, orderBy);
		if (logger.isDebugEnabled()) {
			logger.debug("findByPage(T, int, int, String) - end"); //$NON-NLS-1$
		}
		return returnPage;
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<T> findAll(T queryInfo, String orderBy) {
		if (logger.isDebugEnabled()) {
			logger.debug("findAll(T, String) - start"); //$NON-NLS-1$
		}

		Page<T> returnPage = baseMapper.selectAllByCondition(queryInfo, orderBy);
		if (logger.isDebugEnabled()) {
			logger.debug("findAll(T, String) - end"); //$NON-NLS-1$
		}
		return returnPage;
	}
	@Transactional(readOnly=true)
	public Page<T> findByPage(List<Model> conditions, int pageNum, int pageSize, String order) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByPage(List<Model>, int, int, String) - start"); //$NON-NLS-1$
		}

		PageHelper.startPage(pageNum, pageSize);
		Page<T> returnPage = getBaseMapper().selectExtend(conditions, null, order);
		if (logger.isDebugEnabled()) {
			logger.debug("findByPage(List<Model>, int, int, String) - end"); //$NON-NLS-1$
		}
		return returnPage;
	}

	@Transactional(readOnly=true)
	public Page<T> findAll(List<Model> conditions, String order) {
		if (logger.isDebugEnabled()) {
			logger.debug("findAll(List<Model>, String) - start"); //$NON-NLS-1$
		}

		Page<T> returnPage = getBaseMapper().selectExtend(conditions, null, order);
		if (logger.isDebugEnabled()) {
			logger.debug("findAll(List<Model>, String) - end"); //$NON-NLS-1$
		}
		return returnPage;
	}
	/**
	 * 设置pk主键
	 * @param t
	 */
	public boolean setPk(T t){
		if (logger.isDebugEnabled()) {
			logger.debug("setPk(T) - start"); //$NON-NLS-1$
		}

		LogEnum em = getKeyGenerateEnum();
		if (em != null) {
			Field idField = ReflectionUtil.findField(t.getClass(), "pk");
			//如果不存在id元素则采用String方式生成
			if (idField == null || !(idField.getType().isAssignableFrom(Integer.class)||idField.getType().isAssignableFrom(Long.class))) {
				Field pkField = ReflectionUtil.findField(t.getClass(), "pk");
				if (pkField==null || !pkField.getType().isAssignableFrom(String.class)) {
					throw new RuntimeException(String.format("生成主键遇到错误,获取不到主键信息%s",t.getClass()));
				}
				try {
					pkField.setAccessible(true);
					pkField.set(t, keyGenerate.generateStringKey(em));
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
					logger.error("setPk(T)", e); //$NON-NLS-1$
					throw new RuntimeException("生成主键遇到错误");
				}
			} else {
				//如果存在id元素则生成Long类型主键
				try {
					idField.setAccessible(true);
					idField.set(t, keyGenerate.generateLongKey(em).intValue());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.error("setPk(T)", e); //$NON-NLS-1$
					throw new RuntimeException("生成主键遇到错误");
				}
			}

			if (logger.isDebugEnabled()) {
				logger.debug("setPk(T) - end"); //$NON-NLS-1$
			}
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setPk(T) - end"); //$NON-NLS-1$
		}
		return false;
	}
	/**
	 * 获取枚举类型
	 * @return
	 */
	public LogEnum getKeyGenerateEnum(){
		if (logger.isDebugEnabled()) {
			logger.debug("getKeyGenerateEnum() - start"); //$NON-NLS-1$
		}

		Class<?> cls = ReflectionUtil.getGenericSuperclass(this.getClass());
		if (cls!=null) {
			LogEnum em = LogEnum.valueOf(cls.getSimpleName());

			if (logger.isDebugEnabled()) {
				logger.debug("getKeyGenerateEnum() - end"); //$NON-NLS-1$
			}
			return em;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getKeyGenerateEnum() - end"); //$NON-NLS-1$
		}
		return null;
	}

}
