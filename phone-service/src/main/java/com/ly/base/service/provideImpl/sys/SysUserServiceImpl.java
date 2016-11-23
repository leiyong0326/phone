package com.ly.base.service.provideImpl.sys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.ly.base.common.model.Model;
import com.ly.base.common.util.DateUtil;
import com.ly.base.common.util.MyBatisUtil;
import com.ly.base.common.util.SpringBeanUtil;
import com.ly.base.core.model.sys.SysRole;
import com.ly.base.core.model.sys.SysUser;
import com.ly.base.core.provide.sys.SysUserService;
import com.ly.base.dao.sys.SysRoleMapper;
import com.ly.base.dao.sys.SysUserMapper;
import com.ly.base.service.provideImpl.BaseServiceImpl;

/**
 * 业务处理,数据缓存
 * @author LeiYong
 * @date 2016年10月02日
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {

	@Autowired
	public void setBaseMapper(SysUserMapper baseMapper) {
		super.setBaseMapper(baseMapper);
	}
	public SysUserMapper getBaseMapper() {
		return (SysUserMapper) super.getBaseMapper();
	}

	/**
	 * 添加积分
	 */
	@Override
	public int addRatio(BigDecimal ratio, String pk) {
		SysUser su = getBaseMapper().selectByPrimaryKey(pk);
		if (su!=null) {
			su.setRatio(su.getRatio().add(ratio));
			su.setRatioTotal(su.getRatioTotal().add(ratio));
			return getBaseMapper().updateScore(su);
		}
		return 0;
	}
	/**
	 * 更新积分,销量,积分
	 */
	@Override
	public int updateScore(SysUser su) {
		if (su!=null) {
			return getBaseMapper().updateScore(su);
		}
		return 0;
	}
	/**
	 * 登录
	 * @param userName
	 * @param pwd(密文)
	 * @return
	 */
	@Override
	public SysUser loginSystem(String userName, String password) {
		List<Model> conditions = MyBatisUtil.parseBase("LOGIN_NAME,=,"+userName,"PASSWORD,=,"+password);
		Page<SysUser> pages = getBaseMapper().selectExtend(conditions, null, null);
		if (pages.size()==1) {
			getBaseMapper().updateState(pages.get(0).getPk(), "LOGIN_TIME", DateUtil.format(DateUtil.YMD_HMS, new Date()), pages.get(0).getPk());
			return pages.get(0);
		}
		return null;
	}
	/**
	 * 验证用户名是否可用
	 * @param userName
	 * @return
	 */
	@Override
	public int checkLoginName(String userName) {
		List<Model> conditions = MyBatisUtil.parseBase("LOGIN_NAME,=,"+userName);
		List<String> columns = new ArrayList<>();
		columns.add("count(1) as count");
		Page<SysUser> pages = getBaseMapper().selectExtend(conditions, columns, null);
		if (pages.size()==0) {
			return 0;
		}
		return pages.get(0).getCount();
	}
	/**
	 * 修改密码
	 * @param pk
	 * @param pwd
	 * @param newPwd
	 * @return
	 */
	@Override
	public int changePwd(String updateBy,String pk,String pwd){
		return getBaseMapper().updatePassword(updateBy,pwd, new Date(),  pk);
	}
	
	/**
	 * 通过主键查询
	 * 
	 * @param pk
	 * @return
	 */ 
	@Override
	public SysUser selectByPrimaryKey(String pk){
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
	@Override
	public SysUser insertSelective(SysUser data) {
		if (findExtend(data)) {
			return super.insertSelective(data);
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.ly.base.service.provideImpl.BaseServiceImpl#updateByPK(java.lang.Object)
	 */
	@Override
	public int updateByPK(SysUser data) {
		SysUser su = selectByPrimaryKey(data.getPk());
		su.setName(data.getName());
		su.setSex(data.getSex());
		su.setUpPk(data.getUpPk());
		su.setReferrer(data.getReferrer());
		su.setLoginName(data.getLoginName());
		Date updateDate = new Date();
		if (StringUtils.isNotEmpty(data.getPassword())) {
			su.setPassword(data.getPassword());
			su.setUpdatePassword(updateDate);
		}
		su.setRolePk(data.getRolePk());
		su.setPhone(data.getPhone());
		su.setBirthday(data.getBirthday());
		su.setEnable(data.getEnable());
		su.setAlipay(data.getAlipay());
		su.setWechat(data.getWechat());
		su.setEmail(data.getEmail());
		su.setQq(data.getQq());
		su.setCardId(data.getCardId());
		su.setCardAddress(data.getCardAddress());
		su.setAccountPk(data.getAccountPk());
		su.setAddress(data.getAddress());
		su.setFace(data.getFace());
		su.setUpdateTime(updateDate);
		if (findExtend(data)) {
			return super.updateByPK(data);
		}
		return 0;
	}
	/**
	 * 扩展信息查询
	 * @param data
	 * @return
	 */
	private boolean findExtend(SysUser data){
		//如果传递空密码则清空
		if (StringUtils.isEmpty(data.getPassword())) {
			data.setPassword(null);
		}
		String referer = data.getReferrer();
		List<String> columns = new ArrayList<>();
		columns.add("NAME");
		columns.add("PHONE");
		if (StringUtils.isNotEmpty(referer)) {
			List<Model> conditions = MyBatisUtil.parseBase("PK,=,"+referer);
			Page<SysUser> sus = getBaseMapper().selectExtend(conditions, columns, null);
			if (sus==null||sus.size()==0) {
				return false;
			}
			data.setReferrerName(sus.get(0).getName());
			data.setReferrerPhone(sus.get(0).getPhone());
		}
		String upPk = data.getUpPk();
		if (StringUtils.isNotEmpty(upPk)) {
			List<Model> conditions = MyBatisUtil.parseBase("PK,=,"+upPk);
			Page<SysUser> sus = getBaseMapper().selectExtend(conditions, columns, null);
			if (sus==null||sus.size()==0) {
				return false;
			}
			data.setUpName(sus.get(0).getName());
		}
		Integer rolePk = data.getRolePk();
		if (rolePk!=null) {
			SysRoleMapper srm = SpringBeanUtil.getBean(SysRoleMapper.class);
			SysRole sr = srm.selectByPrimaryKey(rolePk);
			if (sr==null) {
				return false;
			}
			data.setRoleName(sr.getName());
		}
		return true;
	}
}