package com.ly.base.service.provideImpl.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.base.common.redis.CacheAccessException;
import com.ly.base.common.redis.RedisClientSupport;
import com.ly.base.common.redis.extents.RedisExtendsOperate;
import com.ly.base.common.system.redis.RedisKeyConfig;
import com.ly.base.common.util.ArrayUtil;
import com.ly.base.common.util.ReflectionUtil;
import com.ly.base.common.util.SpringBeanUtil;
import com.ly.base.common.util.StringUtil;
import com.ly.base.core.model.sys.SysMenu;
import com.ly.base.core.provide.sys.SysMenuService;
import com.ly.base.dao.sys.SysMenuMapper;
import com.ly.base.service.provideImpl.BaseServiceImpl;

/**
 * 业务处理,数据缓存
 * @author LeiYong
 * @date 2016年10月02日
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {
	private static final String[] FUNC_STRING = {"edit","del","show"};

	@Autowired
	public void setBaseMapper(SysMenuMapper baseMapper) {
		super.setBaseMapper(baseMapper);
	}
	public SysMenuMapper getBaseMapper() {
		return (SysMenuMapper) super.getBaseMapper();
	}
	/**
	 * 查询用户的菜单
	 * @param rolePk
	 * @return
	 */
	@Override
	public List<SysMenu> findAllMenuByRole(Integer rolePk) {
		String mainKey = RedisKeyConfig.getMenuMenuCacheKey(rolePk);
		RedisClientSupport redisClientSupport = SpringBeanUtil.getRedisClientSupport();
		RedisExtendsOperate<SysMenu> extendsOperate = new RedisExtendsOperate<>(redisClientSupport);
		try {
			List<SysMenu> resultList = extendsOperate.readListByIndex(mainKey);
			if (resultList != null) {
				return resultList;
			}
		} catch (CacheAccessException e1) {
		}
		List<SysMenu> list = getBaseMapper().findAllMenuByRole(rolePk, "0");
		try {
			if (list != null && list.size() > 0) {
				extendsOperate.cacheListAndIndex(list, mainKey, SysMenu.class, "pk", (Object t) -> {
					return RedisKeyConfig.getMenuCacheKey(t.toString());
				});
			}
		} catch (CacheAccessException e) {
		}
		return list;
	}

	/**
	 * 查询用户的功能
	 * @param rolePk
	 * @return
	 */
	@Override
	public List<SysMenu> findAllFuncByRole(Integer rolePk) {
		String mainKey = RedisKeyConfig.getMenuFuncCacheKey(rolePk);
		RedisClientSupport redisClientSupport = SpringBeanUtil.getRedisClientSupport();
		RedisExtendsOperate<SysMenu> extendsOperate = new RedisExtendsOperate<>(redisClientSupport);
		try {
			List<SysMenu> resultList = extendsOperate.readListByIndex(mainKey);
			if (resultList != null) {
				return resultList;
			}
		} catch (CacheAccessException e1) {
		}
		List<SysMenu> list = getBaseMapper().findAllMenuByRole(rolePk, "1");
		try {
			if (list != null && list.size() > 0) {
				extendsOperate.cacheListAndIndex(list, mainKey, SysMenu.class, "pk", (Object t) -> {
					return RedisKeyConfig.getMenuCacheKey(t.toString());
				});
			}
		} catch (CacheAccessException e) {
		}
		return list;
	}


	/**
	 * 查询用户的功能按钮归组为String
	 * 如:[{01:show,edit,del},{02:show,edit,del}]
	 * @param rolePk
	 * @param menuType //取消参数,避免只有访问权限的情况
	 * @return
	 */
	@Override
	public List<SysMenu> findAllFuncStringByRole(Integer rolePk) {

		String mainKey = RedisKeyConfig.getMenuFuncStringCacheKey(rolePk);
		// 创建一个扩展操作对象
		// 因为每个upPk不适合做为缓存主键,so,无法使用扩展方式缓存
		// RedisExtendsOperate extendsOperate = new
		// RedisExtendsOperate(redisClientSupport);
		try {
			// List<SysMenu> resultList =
			// extendsOperate.readListByIndex(mainKey);
			RedisClientSupport redisClientSupport = SpringBeanUtil.getRedisClientSupport();
			Object keyValue = redisClientSupport.getValue(mainKey);
			if (keyValue!=null&&!keyValue.toString().equals("")) {
					JSONArray ja = JSON.parseArray(keyValue.toString());
					if (ja != null) {
						List<SysMenu> menuList = new ArrayList<>(ja.size());
						boolean res = ArrayUtil.foreach(ja, (m, i) -> {
							JSONObject jo = ReflectionUtil.convertObjectToBean(m, JSONObject.class);
							if (jo != null) {
								try {
									SysMenu sm = JSON.toJavaObject(jo, SysMenu.class);
									menuList.add(sm);
								} catch (Exception e) {
									return false;
								}
								return true;
							}
							return false;
						});
						if (res) {
							return menuList;
						}
					}
				}
			
		} catch (CacheAccessException e1) {
		}
		List<SysMenu> list = getBaseMapper().findAllMenuStringByRole(rolePk);
		try {
			if (list != null && list.size() > 0) {
				List<SysMenu> menus = new ArrayList<>();
				Map<String, SysMenu> menuMap = new HashMap<>();
				ArrayUtil.foreach(list, (m,i)->{
					if (m==null) {
						return false;
					}
					Pattern pattern = Pattern.compile("^(\\d+)(\\w*)$");
					Matcher matcher = pattern.matcher(m.getPk());
					if (matcher.matches()) {
						String pk = matcher.group(1);
						String func = matcher.group(2);
						SysMenu sm;
						if (menuMap.get(pk)==null) {
							sm = new SysMenu();
							sm.setPk(pk);
							menus.add(sm);
							menuMap.put(pk, sm);
						}else{
							sm = menuMap.get(pk);
						}
						//存储值
						if (ArrayUtils.contains(FUNC_STRING, func)) {
							if (StringUtils.isEmpty(sm.getName())) {
								sm.setName(func);
							}else{
								sm.setName(StringUtil.appendStringNotNull(",", sm.getName(),func));
							}
						}else{
							sm.setText(m.getText());
						}
					}else{
						System.out.println("is not match");
					}
					return true;
				});
				RedisClientSupport redisClientSupport = SpringBeanUtil.getRedisClientSupport();
				redisClientSupport.putValue(mainKey, JSON.toJSONString(menus));
				return menus;
				// 因为每个upPk不适合做为缓存主键,so,无法使用扩展方式缓存
				// extendsOperate.cacheListAndIndex(list, mainKey,
				// SysMenu.class, "upPk", (Object t)->{return
				// RedisKeyConfig.getMenuStringCacheKey(t.toString());});
			}
		} catch (CacheAccessException e) {
		}
		return list;
	}
	/**
	 * 查询用户的菜单
	 * @param rolePk
	 * @param menuType 
	 * @return
	 */
	@Override
	public List<SysMenu> findAllMenuByRole(Integer rolePk,String menuType){
		return getBaseMapper().findAllMenuByRole(rolePk, menuType);
	}

	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public SysMenu selectByPrimaryKey(String pk){
		return getBaseMapper().selectByPrimaryKey(pk);
	}
	/**
	 * 通过主键删除
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public int deleteByPrimaryKey(String pk){
		return getBaseMapper().deleteByPrimaryKey(pk);
	}
	/**
	 * 通过主键批量删除
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	public int deleteByBatch(String[] pks){
		return getBaseMapper().deleteByBatch(pks);
	}
	/**
	 * 通过主键启用
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public int enable(String updateBy,String pk){
		return getBaseMapper().updateState(updateBy,"enable","1",pk);
	}
	/**
	 * 通过主键禁用
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public int disable(String updateBy,String pk){
		return getBaseMapper().updateState(updateBy,"enable","0",pk);
	}
	/**
	 * 通过主键批量启用
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	public int enables(String updateBy,String... pks){
		return getBaseMapper().updateStates(updateBy,"enable","1",pks);
	}
	/**
	 * 通过主键批量禁用
	 * 
	 * @param pks
	 * @return
	 */ 
	@Override
	public int disables(String updateBy,String... pks){
		return getBaseMapper().updateStates(updateBy,"enable","0",pks);
	}
}