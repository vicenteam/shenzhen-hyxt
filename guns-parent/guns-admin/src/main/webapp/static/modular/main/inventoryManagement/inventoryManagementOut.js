/**
 * 商品库存管理初始化
 */
var InventoryManagement = {
    id: "InventoryManagementTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
InventoryManagement.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
        // {title: '', field: 'deptid', visible: true, align: 'center', valign: 'middle'},
        {title: '商品id', field: 'integralrecordtypeid', visible: false, align: 'center', valign: 'middle'},
        {title: '商品名称', field: 'productname', visible: true, width: '500px', align: 'center', valign: 'middle'},
        {
            title: '商品类别',
            field: 'producttype',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                if (value == 0) {
                    return '礼品类';
                } else if (value == 1) {
                    return '积分兑换类';
                } else if (value == 2) {
                    return '销售类';
                } else if (value == 3) {
                    return '积分+金额类';
                } else {
                    return '-';
                }
            }
        },
        {
            title: '记录状态',
            field: 'status',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                if (value == 0) {
                    return '添加库存';
                } else if (value == 1) {
                    return '减少库存';
                } else {
                    return '-';
                }
            }
        },
        {
            title: '变化数量',
            field: 'consumptionNum',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter:function (value, row, index) {
                if (row.status == 0) {
                    return '<span style="color: green">+' + value + '</span>';
                } else {
                    return '<span style="color: red">-' + value + '</span>';
                }
            }
        },
        {title: '所属下级', field: 'deptId', visible: true, align: 'center', valign: 'middle',},
        {title: '记录时间', field: 'createtime', visible: true, align: 'center', valign: 'middle',},
        {title: '操作人', field: 'createuserid', visible: true, align: 'center', valign: 'middle'},
        // {title: '消耗库存人id(meimberid)', field: 'memberid', visible: true, align: 'center', valign: 'middle'},

    ];
};

/**
 * 检查是否选中
 */
InventoryManagement.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        InventoryManagement.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加商品库存
 */
InventoryManagement.openAddInventoryManagement = function () {
    var index = layer.open({
        type: 2,
        title: '单商品库存出库',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/inventoryManagement/inventoryManagement_out_add'
    });
    this.layerIndex = index;
};
/**
 * 点击添加商品库存
 */
InventoryManagement.openAddInventoryManagementAll = function () {
    var index = layer.open({
        type: 2,
        title: '多商品库存出库',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/inventoryManagement/inventoryManagement_out_add_all'
    });
    this.layerIndex = index;
};
/**
 * 打开查看商品库存详情
 */
InventoryManagement.openInventoryManagementDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '商品库存详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/inventoryManagement/inventoryManagement_update/' + InventoryManagement.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除商品库存
 */
InventoryManagement.delete = function () {
    if (this.check()) {
        var id = this.seItem.id;
        layer.confirm('您确定要删除本条数据吗？', {btn: ['确定', '取消']}, function () {
            layer.closeAll('dialog');
            var ajax = new $ax(Feng.ctxPath + "/inventoryManagement/delete", function (data) {
                Feng.success("删除成功!");
                InventoryManagement.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("inventoryManagementId", id);
            ajax.start();
        });
    }
};

/**
 * 查询商品库存列表
 */
InventoryManagement.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['status'] = $("#status").val();
    InventoryManagement.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = InventoryManagement.initColumn();
    var table = new BSTable(InventoryManagement.id, "/inventoryManagement/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    table.setQueryParams({status:1});
    InventoryManagement.table = table.init();
});
