/*
 * Date: 2016.06.27
 * Author: Luoxue-xu.github.io
 * version: 1.0.0
 */

require(['require.config', 'jquery', 'navbar', 'utils', 'config'], function (r, $, n, utils, config) {
    var loginUser = sessionStorage.getItem(utils.loginUser);
    if (!loginUser) {
        //自动登录时将信息存储到cookies
        var userInfo = $.cookie('autoLoginUser');
        $.removeCookie('autoLoginUser');
        if (userInfo) {
            sessionStorage.setItem(utils.loginUser, userInfo);
            loginUser = userInfo;
        }
    }
    var user = JSON.parse(loginUser);

    if (user) {
        $('.index-user-name em').html(user.name);
        if (user.sysDepartment) {
            $('.index-user-name i').html(user.sysDepartment.name);
        }
        if (user.sysOrganization) {
            $('.index-brand-name').html(user.sysOrganization.name);
        }
        $('.index-user-photo').html('<img src="' + user.face + '" alt="" title="">');
    }

    function getOut() {
        utils.ajax({
            url: config.api.logout,
            success: function (data) {
                //清空缓存
                utils.clearSession();
                window.location.href = utils.baseUrl + "src/login.html";
            }
        });
    }
    $('.index-user-out').click(function () {
        getOut();
    })
});
