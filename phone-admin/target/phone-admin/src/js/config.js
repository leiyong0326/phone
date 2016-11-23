/*
 * date: 2016.09.22
 * version: 1.0
 */

define([], function () {

    var config = {
        api: {
            // 登录
            login: 'sysUser/login', // 登录
            logout: 'sysUser/logout', // 退出

            // 系统管理
            findMenuFunc: 'sysMenu/findMenuFunc', // 当前用户的权限

            // 活动管理
            searchActList: 'actMaster/findMaster', // 查询活动列表
            addAct: 'actMaster/saveOrUpdate', // 新增活动
            searchOneAct: 'actMaster/getMaster', // 查询某个活动
            delAct: 'actMaster/delete', // 删除活动
            changeActStatus: 'actMaster/updateStatus', //修改活动状态
            searchActInfo: 'actMaster/getMaster', // 查询活动详情
            saveActInfo: 'actMaster/saveOrUpdate', // 保存活动详情
            searchType: 'actType/findType', // 查询活动类型
            searchUser: 'actAttend/findAttend', // 查询参与对象条件
            searchGift: 'actGift/findGift', // 查询奖品
            searchModel: 'giftMaster/findGiftMaster', // 查询抽奖模板列表
            getModelInfo: 'giftMaster/getGiftMaster', // 查看抽奖模板详情

            // 奖品卡券管理
            searchGiftMasterList: 'giftMaster/findGiftMaster', // 获取奖品卡券列表
            delGiftMaster: 'giftMaster/delete', // 删除奖品卡券
            searchGiftInfo: 'giftMaster/getGiftMaster', // 查询奖品模板详情
            saveGiftInfo: 'giftMaster/saveOrUpdate', // 保存奖品详情
            searchGiftNameList: 'actGift/findGift', // 查询奖品名称列表

            // 商城管理
            searchShopCarBrand: 'shopCarBrand/findByOrg', // 查询当前4s店品牌
            searchShopCarClass: 'shopCarClass/find', // 查询世界所有车系
            searchShopOrgClass: 'shopOrgClass/find', // 查询当前4s店车系
            addShopOrgClass: 'shopOrgClass/save', // 新增车系
            searchOneShopOrgClass: 'shopOrgClass/get', // 查询某个车系
            delShopOrgClass: 'shopOrgClass/delete', // 删除车系

            // 养车
            getMtInsurance: 'insurance/getMtInsurance', // 获取续保询价详情
            sendInsurancePrice: 'insurance/updateStatus' // 续保询价报价
        }
    };

    return config;

});
