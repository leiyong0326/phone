/*
 * author: luoxu-xu.github.io;
 * date: 2016.06.30;
 * about: 一些jquery插件
 * version: 1.0.0
 */

define(['utils'], function (utils) {
    // 工具集
    var format = {
        // 状态
        status: function (value) {
            if (value && value == '1') {
                return '正常';
            }
            return '禁用';
        },
        statusText: function (value) {
            if (value && value == '1') {
                return '禁用';
            }
            return '启用';
        },
        statusStyle: function (value) {
            if (value && value == '1') {
                return 'cy-btn-orange';
            }
            return 'cy-btn-green';
        },
        // 日期
        date: function (value, format) {
            if (format == undefined) {
                format = 'YYYY-MM-DD hh:mm';
            }
            if (value && value > 0) {
                return utils.dateFormat(value, format);
            } else {
                return '未知';
            }
        },
        display: function (value) {
            return value ? '' : 'style="display:none;';
        },
        isTrue: function (value) {
            return value ? '是' : '否';
        },
        roleJob: function (value) {
            if (value == '0') {
                return '普通';
            }
            if (value == '1') {
                return '销售';
            }
            if (value == '2') {
                return '售后';
            }
            if (value == '3') {
                return '续保';
            }
        },
        scoreType: function (value) {
            if (value == '0') {
                return 'attention';
            }
            if (value == '1') {
                return 'userInfo';
            }
            if (value == '2') {
                return 'userCarInfo';
            }
            if (value == '3') {
                return 'login';
            }
            if (value == '4') {
                return 'twoGroup';
            }
            if (value == '5') {
                return 'groupBuy';
            }
            if (value == '6') { //抽奖
                return 'prize';
            }
            if (value == '7') { //推荐有礼
                return 'recommend';
            }
            if (value == '8') { //在线预约
                return 'appointment';
            }
            if (value == '9') { //预定
                return 'order';
            }
            if (value == 'a') {
                return 'share';
            }
            if (value == 'b') {
                return 'shareCar';
            }
            if (value == 'c') { //开通违章查询
                return 'buyPeccancy';
            }
            if (value == 'd') { //违章代缴
                return 'peccancySubscribe';
            }
            if (value == 'e') { //油卡充值
                return 'oilCard';
            }
            if (value == 'f') { //救援服务
                return 'rescue';
            }
            if (value == 'g') { //意见反馈
                return 'feedback';
            }
            if (value == 'h') { //问卷调查
                return 'questionnaire';
            }
            if (value == 'i') { //续保询价
                return 'insurance';
            }
            if (value == 'j') { //在线充值
                return 'topIn';
            }
            if (value == 'k') { //普通购车消费
                return 'consumerBuyCar';
            }
            if (value == 'l') { //购买特价
                return 'consumerBargainCar';
            }
            if (value == 'm') { //购买二手车
                return 'consumerSecondCar';
            }
            if (value == 'n') { //检测维修消费
                return 'consumerCheck';
            }
            if (value == 'o') { //保险维修消费
                return 'consumerMaintain';
            }
            if (value == 'p') { //保险消费
                return 'consumerInsurance';
            }
            if (value == 'q') { //保养消费
                return 'consumerSafe';
            }
            if (value == 'r') { //美容消费
                return 'consumerBeauty';
            }
            if (value == 's') {//其它消费
        	return  'consumerOther';
            }
            return '';
        },
        scoreValue: function (value) {
            if (value == 'attention') {
                return '0';
            }
            if (value == 'userInfo') {
                return '1';
            }
            if (value == 'userCarInfo') {
                return '2';
            }
            if (value == 'login') {
                return '3';
            }
            if (value == 'twoGroup') {
                return '4';
            }
            if (value == 'groupBuy') {
                return '5';
            }
            if (value == 'prize') { //抽奖
                return '6';
            }
            if (value == 'recommend') { //推荐有礼
                return '7';
            }
            if (value == 'appointment') { //在线预约
                return '8';
            }
            if (value == 'order') { //预定
                return '9';
            }
            if (value == 'share') {
                return 'a';
            }
            if (value == 'shareCar') {
                return 'b';
            }
            if (value == 'buyPeccancy') { //开通违章查询
                return 'c';
            }
            if (value == 'peccancySubscribe') { //违章代缴
                return 'd';
            }
            if (value == 'oilCard') { //油卡充值
                return 'e';
            }
            if (value == 'rescue') { //救援服务
                return 'f';
            }
            if (value == 'feedback') { //意见反馈
                return 'g';
            }
            if (value == 'questionnaire') { //问卷调查
                return 'h';
            }
            if (value == 'insurance') { //续保询价
                return 'i';
            }
            if (value == 'topIn') { //在线充值
                return 'j';
            }
            if (value == 'consumerBuyCar') { //普通购车消费
                return 'k';
            }
            if (value == 'consumerBargainCar') { //购买特价
                return 'l';
            }
            if (value == 'consumerSecondCar') { //购买二手车
                return 'm';
            }
            if (value == 'consumerCheck') { //检测消费
                return 'n';
            }
            if (value == 'consumerMaintain') { //维修消费
                return 'o';
            }
            if (value == 'consumerInsurance') { //续保消费
                return 'p';
            }
            if (value == 'consumerSafe') { //保养消费
                return 'q';
            }
            if (value == 'consumerOther') { //其它消费
                return 'r';
            }
            return '';
        },
        carLevel: function (value) {
            if (value == '0') {
                return '微型车';
            }
            if (value == '1') {
                return '小型车';
            }
            if (value == '2') {
                return '紧凑车型';
            }
            if (value == '3') {
                return '中等车型';
            }
            if (value == '4') {
                return '高级车型';
            }
            if (value == '5') {
                return '豪华车型';
            }
            if (value == '6') {
                return '三厢车型';
            }
            if (value == '7') {
                return 'MPV车型';
            }
            if (value == '8') {
                return 'CDV车型';
            }
            if (value == '9') {
                return 'SUV车型';
            }

        },
        sex: function (value) {
            if (value == '0') {
                return '女';
            }
            if (value == '1') {
                return '男';
            }
        },
        carImgType: function (value) {
            if (value == '0') {
                return '空间';
            }
            if (value == '1') {
                return '内饰';
            }
            if (value == '2') {
                return '外观';
            }
            if (value == '9') {
                return '列表首图';
            }
        },
        carImgColor: function (value) {
            if (value == '0') {
                return '红色';
            }
            if (value == '1') {
                return '橙色';
            }
            if (value == '2') {
                return '黄色';
            }
            if (value == '3') {
                return '绿色';
            }
            if (value == '4') {
                return '蓝色';
            }
            if (value == '5') {
                return '青色';
            }
            if (value == '6') {
                return '紫色';
            }
            if (value == '7') {
                return '白色';
            }
            if (value == '8') {
                return '黑色';
            }
            if (value == '9') {
                return '银色';
            }
        },
        payWay: function (value) {
            if (value == '0') {
                return '现金';
            }
            if (value == '1') {
                return '微信';
            }
            if (value == '2') {
                return '支付宝';
            }
        }
    }
    return format;
});
