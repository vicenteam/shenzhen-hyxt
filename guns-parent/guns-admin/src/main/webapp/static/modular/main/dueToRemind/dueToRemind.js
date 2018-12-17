/**
 * 商品追销管理管理初始化
 */
var DueToRemind = {
    id: "DueToRemindTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
DueToRemind.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
            // {title: '商品id', field: 'productId', visible: true, align: 'center', valign: 'middle'},
            {title: '商品名称', field: 'productName',width:'400px', visible: true, align: 'center', valign: 'middle'},
            {title: '商品类型', field: 'productType', visible: true, align: 'center', valign: 'middle'},
        {title: '提示信息', field: 'dueToRemindContext',width:'600px', visible: true, align: 'center', valign: 'middle'},
            {title: '到期提醒天数', field: 'dueToRemindDays', visible: true, align: 'center', valign: 'middle'},
            // {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            // {title: '修改时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
            // {title: '', field: 'createUserId', visible: true, align: 'center', valign: 'middle'},
            // {title: '', field: 'updateUserId', visible: true, align: 'center', valign: 'middle'},

            {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
                    if(value==1){
                        return '停用';
                    }else {
                        return '可用';
                    }
                }},{title: '操作', field: 'status', visible: true,align: 'center', valign: 'middle',formatter: function (value, row, index) {
                    if(value==1){
                        return ' <button class="fa fa-check layui-btn layui-btn-xs layui-btn-normal" onclick="btn_update('+row.id+')">启用</button>';
                    }else {
                        return '<button class="fa fa-close layui-btn layui-btn-xs layui-bg-red" onclick="btn_update('+row.id+')">停用</button>';
                    }

                }},
    ];
};

/**
 * 检查是否选中
 */
DueToRemind.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        DueToRemind.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加商品追销管理
 */
DueToRemind.openAddDueToRemind = function () {
    var index = layer.open({
        type: 2,
        title: '添加商品追销管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/dueToRemind/dueToRemind_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看商品追销管理详情
 */
DueToRemind.openDueToRemindDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '商品追销管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/dueToRemind/dueToRemind_update/' + DueToRemind.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除商品追销管理
 */
DueToRemind.delete = function () {
    if (this.check()) {
        layer.confirm('您确定要进行该操作吗？', {btn: ['确定', '取消']}, function () {
            var ajax = new $ax(Feng.ctxPath + "/dueToRemind/delete", function (data) {
                Feng.success("删除成功!");
                DueToRemind.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("dueToRemindId",this.seItem.id);
            ajax.start();
        });

    }
};

/**
 * 查询商品追销管理列表
 */
DueToRemind.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    DueToRemind.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = DueToRemind.initColumn();
    var table = new BSTable(DueToRemind.id, "/dueToRemind/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    DueToRemind.table = table.init();
});

function btn_update(id) {
    console.log(id)
    layer.confirm('您确定要进行该操作吗？', {btn: ['确定', '取消']}, function () {
        layer.closeAll('dialog');
        var ajax = new $ax(Feng.ctxPath + "/dueToRemind/updateStatus", function (data) {
            Feng.success("操作成功!");
            DueToRemind.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("id",id);
        ajax.start();
    });

}
