/**
 * 初始化商品追销管理详情对话框
 */
var DueToRemindInfoDlg = {
    dueToRemindInfoData : {},
    validateFields: {
        dueToRemindDays: {
            validators: {
                notEmpty: {
                    message: '到期提醒天数不能为空'
                },
                numeric: {message: '到期提醒天数只能输入数字'},
            }
        }
    }
};

/**
 * 清除数据
 */
DueToRemindInfoDlg.clearData = function() {
    this.dueToRemindInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DueToRemindInfoDlg.set = function(key, val) {
    this.dueToRemindInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DueToRemindInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
DueToRemindInfoDlg.close = function() {
    parent.layer.close(window.parent.DueToRemind.layerIndex);
}

/**
 * 收集数据
 */
DueToRemindInfoDlg.collectData = function() {
    this
    .set('id')
    .set('productId')
    .set('productName')
    .set('productname')
    .set('dueToRemindDays')
    .set('createTime')
    .set('updateTime')
    .set('createUserId')
    .set('updateUserId')
    .set('dueToRemindContext')
    .set('status');
}
DueToRemindInfoDlg.validate = function () {
    $('#integralrecordInfoTable').bootstrapValidator('validate');
    return $("#integralrecordInfoTable").data('bootstrapValidator').isValid();
};
/**
 * 提交添加
 */
DueToRemindInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/dueToRemind/add", function(data){
        Feng.success("添加成功!");
        window.parent.DueToRemind.table.refresh();
        DueToRemindInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.dueToRemindInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
DueToRemindInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/dueToRemind/update", function(data){
        Feng.success("修改成功!");
        window.parent.DueToRemind.table.refresh();
        DueToRemindInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.dueToRemindInfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("integralrecordInfoTable", DueToRemindInfoDlg.validateFields);
    //绑定商品搜索
    $('.selectpicker').selectpicker({
        'selectedText': 'cat'
    });
    $(".selectpicker" ).selectpicker('refresh');
    $(".bs-searchbox input").keyup(function(event){
        // if(event.keyCode == "13") {//判断如果按下的是回车键则执行下面的代码
        var search=($(".bs-searchbox input").val())
        //进行搜索 integralrecordtype/list
        var ajax = new $ax(Feng.ctxPath + "/integralrecordtype/list", function (data) {
            console.log(data)
            $("#productname").empty();
            if(data.rows.length>0){
                for(var i=0;i<data.rows.length;i++){
                    $("#productname").append("<option value='"+data.rows[i].id+"'>【"+data.rows[i].productname+"】 库存("+data.rows[i].productnum+")</option>");
                }
            }
            $(".selectpicker" ).selectpicker('refresh');
        }, function (data) {
        });
        ajax.set("condition", search);
        ajax.set("producttype", $("#typeId").val());
        ajax.set("limit", 9999);
        ajax.start();
        // }

    });;
    $("#typeId").change(function () {
        changeselect();


    })
    $("#productname").change(function () {
        //查询商品
        findbyid();

    })
    changeselect();
    findbyid();
    $("#play").keyup(function(event){
        console.log($("#play").val())
        changejifen();

    })
});
function changeselect() {
    var ajax = new $ax(Feng.ctxPath + "/integralrecordtype/list", function (data) {
        $("#productname").empty();
        if(data.rows.length>0){
            for(var i=0;i<data.rows.length;i++){
                $("#productname").append("<option value='"+data.rows[i].id+"' obj='"+data.rows[i].producttype+"'>【"+data.rows[i].productname+"】 库存("+data.rows[i].productnum+")</option>");
            }
        }
        $(".selectpicker" ).selectpicker('refresh');
        findbyid();
    }, function (data) {
    });
    ajax.set("producttype", $("#typeId").val());
    ajax.set("limit", 100);
    ajax.start();
}
function findbyid() {
    var ajax = new $ax(Feng.ctxPath + "/integralrecordtype/detail/"+ $("#productname").val(), function (data) {
        console.log(data)
        $("#integral").val(data.productjifen);
        temp_integral=data.productjifen;
        $("#consumptionNum").val(1)
        if(data.producttype==2||data.producttype==3){
            $("#divplay").css("display","");
        }else {
            $("#divplay").css("display","none");
        }
    }, function (data) {
    });
    ajax.set("producttype", $("#typeId").val());
    ajax.start();
}
function changejifen() {
    if($("#typeId").val()==3){
        var tempIntegral= $("#play").val()*10;
        var ajax = new $ax(Feng.ctxPath + "/integralrecordtype/detail/"+ $("#productname").val(), function (data) {
            console.log(data)
            $("#integral").val((parseInt(data.productjifen)+tempIntegral))
        }, function (data) {
        });
        ajax.start();
    }
}