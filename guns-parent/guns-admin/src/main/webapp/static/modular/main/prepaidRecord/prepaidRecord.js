/**
 * 充值记录管理初始化
 */
var PrepaidRecord = {
    id: "PrepaidRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PrepaidRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '姓名', field: 'prepaidRecordMemberName', visible: true, align: 'center', valign: 'middle'},
        {title: '电话号码', field: 'prepaidRecordMemberPhone', visible: true, align: 'center', valign: 'middle'},
        {title: '预充值时间', field: 'prepaidRecordTime', visible: true, align: 'center', valign: 'middle'},
        {title: '预充值金额', field: 'prepaidRecordMoney', visible: true, align: 'center', valign: 'middle'},

    ];
};

/**
 * 检查是否选中
 */
PrepaidRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        PrepaidRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加充值记录
 */
PrepaidRecord.openAddPrepaidRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加充值记录',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/prepaidRecord/prepaidRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看充值记录详情
 */
PrepaidRecord.openPrepaidRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '充值记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/prepaidRecord/prepaidRecord_update/' + PrepaidRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除充值记录
 */
PrepaidRecord.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/prepaidRecord/delete", function (data) {
            Feng.success("删除成功!");
            PrepaidRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("prepaidRecordId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询充值记录列表
 */
PrepaidRecord.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    PrepaidRecord.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PrepaidRecord.initColumn();
    var table = new BSTable(PrepaidRecord.id, "/prepaidRecord/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    PrepaidRecord.table = table.init();
});
