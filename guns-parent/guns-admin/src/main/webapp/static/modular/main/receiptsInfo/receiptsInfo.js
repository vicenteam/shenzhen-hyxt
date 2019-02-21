/**
 * 小票信息管理初始化
 */
var ReceiptsInfo = {
    id: "ReceiptsInfoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ReceiptsInfo.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '购买人姓名', field: 'memberName', visible: true, align: 'center', valign: 'middle'},
            {title: '购买人电话', field: 'memberPhone', visible: true, align: 'center', valign: 'middle'},
            {title: '购买时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '支付金额', field: 'playMoney', visible: true, align: 'center', valign: 'middle'},
            {title: '小票图片', field: 'receiptsBase64Img', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
                    return "<img style='width: 100px;height: 25px' src='data:image/png;base64,"+value+"'>";
                }},
            {title: '操作', field: 'receiptsBase64Img', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
                   return "<button onclick='openPage("+row.id+")' class='btn btn-info'>查看</button>";
                }}



            // {title: '', field: 'status', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ReceiptsInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ReceiptsInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加小票信息
 */
ReceiptsInfo.openAddReceiptsInfo = function () {
    var index = layer.open({
        type: 2,
        title: '添加小票信息',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/receiptsInfo/receiptsInfo_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看小票信息详情
 */
function openPage(id){
    var index = layer.open({
        type: 2,
        title: '小票信息详情',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/receiptsInfo/receiptsInfo_update/' + id
    });
    this.layerIndex = index;
}
ReceiptsInfo.openReceiptsInfoDetail = function (id) {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '小票信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/receiptsInfo/receiptsInfo_update/' + id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除小票信息
 */
ReceiptsInfo.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/receiptsInfo/delete", function (data) {
            Feng.success("删除成功!");
            ReceiptsInfo.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("receiptsInfoId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询小票信息列表
 */
ReceiptsInfo.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ReceiptsInfo.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ReceiptsInfo.initColumn();
    var table = new BSTable(ReceiptsInfo.id, "/receiptsInfo/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    ReceiptsInfo.table = table.init();
});
