/**
 * 初始化用户考勤详情对话框
 */
var UserAttendanceInfoDlg = {
    userAttendanceInfoData : {}
};

/**
 * 清除数据
 */
UserAttendanceInfoDlg.clearData = function() {
    this.userAttendanceInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserAttendanceInfoDlg.set = function(key, val) {
    this.userAttendanceInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserAttendanceInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserAttendanceInfoDlg.close = function() {
    parent.layer.close(window.parent.UserAttendance.layerIndex);
}

/**
 * 收集数据
 */
UserAttendanceInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('checkYearMonth')
    .set('checkTime1')
    .set('checkTime2')
    .set('deptId');
}

/**
 * 提交添加
 */
UserAttendanceInfoDlg.addSubmit = function() {
    this.clearData();
    this.collectData();
    if($("#scannerProductName").val().length==0){
        Feng.error("请进行扫码操作!");
        return
    }
    if($("#introducerName").val().length==0){
        Feng.error("请进读卡操作!");
        return
    }
    var jifen1=$("#countPrice").val();
    var jifen2=$("#scannerProductNumSumJifen").val()
    if(parseFloat(jifen1)<parseFloat(jifen2)){
        if($("#dtype").val()==0){
            Feng.error("当前用户积分不足!");
            return
        }else if($("#dtype").val()==1){
            if($("#payMoney").val()<=0){
                Feng.error("请输入有效金额!");
                return
            }
        }


    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jifenduihuan/add", function(data){
        Feng.success("兑换成功!");
        $("#cardCode").val("")
        $("#introducerName").val("")
        $("#introducerId").val("")
        $("#name").val("")
        $("#levelID").val("")
        $("#address").val("")
        $("#countPrice").val("")
        $("#scannerProductName").val("")
        $("#scannerProductOneJifen").val("")
        $("#scannerProductNumSumNum").val("")
        $("#scannerProductNumSumJifen").val("")
        $("#productId").val("")
        $("#payMoney").val("0")
    },function(data){
        Feng.error("兑换失败!" + data.responseJSON.message + "!");
    });
    ajax.set({memberId:$("#introducerId").val()
        ,productId:$("#productId").val(),
        productNum:$("#scannerProductNumSumNum").val(),
        payMoney:$("#payMoney").val()});
    ajax.start();
}

/**
 * 提交修改
 */
UserAttendanceInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/userAttendance/update", function(data){
        Feng.success("修改成功!");
        window.parent.UserAttendance.table.refresh();
        UserAttendanceInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userAttendanceInfoData);
    ajax.start();
}

$(function() {

});
