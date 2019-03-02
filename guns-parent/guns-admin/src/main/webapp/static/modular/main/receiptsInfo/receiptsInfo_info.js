/**
 * 初始化小票信息详情对话框
 */
var ReceiptsInfoInfoDlg = {
    receiptsInfoInfoData : {}
};

/**
 * 清除数据
 */
ReceiptsInfoInfoDlg.clearData = function() {
    this.receiptsInfoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ReceiptsInfoInfoDlg.set = function(key, val) {
    this.receiptsInfoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ReceiptsInfoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ReceiptsInfoInfoDlg.close = function() {
    parent.layer.close(window.parent.ReceiptsInfo.layerIndex);
}

/**
 * 收集数据
 */
ReceiptsInfoInfoDlg.collectData = function() {
    this
    .set('id')
    .set('createTime')
    .set('deptId')
    .set('receiptsBase64Img')
    .set('memberId')
    .set('memberPhone')
    .set('memberName')
    .set('playMoney')
    .set('status');
}

/**
 * 提交添加
 */
ReceiptsInfoInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/receiptsInfo/add", function(data){
        Feng.success("添加成功!");
        window.parent.ReceiptsInfo.table.refresh();
        ReceiptsInfoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.receiptsInfoInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ReceiptsInfoInfoDlg.editSubmit = function() {
    $("#hidenDiv").css("display","none")
    window.print();
}

$(function() {

});

function commit() {
    $("#hidenDiv").css("display","none")
    // document.getElementById("WebBrowser").ExecWB(6,2)
    document.getElementById("WebBrowser").ExecWB(8,1)
    document.getElementById("WebBrowser").ExecWB(6,2)
}