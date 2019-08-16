/**
 * 新增积分管理初始化
 */
var Integralrecord = {
    id: "IntegralrecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Integralrecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '积分记录编号', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '门店名称', field: 'simplename', visible: true, align: 'center', valign: 'middle'},
            {title: '门店人数', field: 'sumRen', visible: true, align: 'center', valign: 'middle'},
            {title: '积分总分', field: 'sumJifenzongshu', visible: true, align: 'center', valign: 'middle'},
            {title: '消耗积分数', field: 'sumXiaohao', visible: true, align: 'center', valign: 'middle'},
            {title: '签到总分', field: 'sumQiandao', visible: true, align: 'center', valign: 'middle'},
            {title: '积分兑换总分', field: 'sumDuihuan', visible: true, align: 'center', valign: 'middle'},

    ];
};

/**
 * 检查是否选中
 */
Integralrecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Integralrecord.seItem = selected[0];
        return true;
    }
};

/**
 * 查询新增积分列表
 */
Integralrecord.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['memberName'] = $("#memberName").val();
    queryData['cadId'] = $("#cadId").val();
    queryData['type'] = $("#type").val();
    queryData['integralType'] = $("#integralType").val();
    queryData['begindate'] = $("#begindate").val();
    queryData['enddate'] = $("#enddate").val();
    queryData['memberId'] = "";
    Integralrecord.table.refresh({query: queryData});
};

Integralrecord.aaa = function () {
    var queryData = {};
    readDeviceCard();
//校验密码
    RfAuthenticationKey();
    var ret = CZx_32Ctrl.RfRead(DeviceHandle.value,BlockM1.value);
    if(CZx_32Ctrl.lErrorCode == 0) {
        DevBeep();
        $.ajax({
            url: '/membermanagement/getUserInfo',
            // data: {value:ret},
            data: {value: $("#readDeviceCard").val()},
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            async: false,
            success: function (data) {
                if (data.id != undefined) {
                    queryData['operator'] = $("#operator").val();
                    queryData['memberName'] = $("#memberName").val();
                    queryData['cadId'] = $("#cadId").val();
                    queryData['type'] = $("#type").val();
                    queryData['integralType'] = $("#integralType").val();
                    queryData['begindate'] = $("#begindate").val();
                    queryData['enddate'] = $("#enddate").val();
                    queryData['memberId'] = data.memberid;
                } else {
                    if (data == "202") {
                        Feng.error("该卡已挂失无法执行该操作!");
                    } else {
                        queryData['operator'] = $("#operator").val();
                        queryData['memberName'] = $("#memberName").val();
                        queryData['cadId'] = $("#cadId").val();
                        queryData['type'] = $("#type").val();
                        queryData['integralType'] = $("#integralType").val();
                        queryData['begindate'] = $("#begindate").val();
                        queryData['enddate'] = $("#enddate").val();
                        queryData['memberId'] = -1;
                    }
                }

                Integralrecord.table.refresh({query: queryData});
            }
        })
    }
};

Integralrecord.form = function () {
    var queryData = {};
    queryData['operator'] = $("#operator").val();
    queryData['memberName'] = $("#memberName").val();
    queryData['condition'] = $("#condition").val();
    queryData['cadId'] = $("#cadId").val();
    queryData['type'] = $("#type").val();
    queryData['integralType'] = $("#integralType").val();
    queryData['begindate'] = $("#begindate").val();
    queryData['enddate'] = $("#enddate").val();
    queryData['memberId'] = -1;
    return queryData;
};

$(function () {
    var defaultColunms = Integralrecord.initColumn();
    var table = new BSTable(Integralrecord.id, "/integralrecordquery/jifenmingxibiaoList", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    table.setQueryParams(Integralrecord.form());
    Integralrecord.table = table.init();
});


/**
 * 根据积分类型获取积分明细
 */
Integralrecord.findIntegralType = function (type) {
    //清除数据

};

$("#type").change(function () {
    Integralrecord.findIntegralType($("#type").val());
});

Integralrecord.dataexport=function () {
    window.location.href = Feng.ctxPath + "/integralrecordquery/dataexport?limit=9999999&offset=0&"+
        "memberName=" + $("#memberName").val() +
        "&cadId=" + $("#cadId").val() +
        "&type=" + $("#type").val() +
        "&integralType=" + $("#integralType").val() +
        "&operator=" + $("#operator").val() +
        // "&memberid="+ $("#memberid").val() +
        // "&townshipid="+ $("#townshipid").val() +
        "&begindate=" + $("#begindate").val() +
        "&enddate=" + $("#enddate").val() ;

}
