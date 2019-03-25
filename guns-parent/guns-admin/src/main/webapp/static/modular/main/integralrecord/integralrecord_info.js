/**
 * 初始化新增积分详情对话框
 */
var IntegralrecordInfoDlg = {
    integralrecordInfoData : {},
    validateFields: {
        integral: {
            validators: {
                notEmpty: {
                    message: '新增积分不能为空'
                },
                numeric: {message: '新增积分只能输入数字'},
                greaterThan: {
                    value: 0.001,
                    message: "新增积分最小输入值为 0.001"
                }
            }
        }
    }
};

/**
 * 清除数据
 */
IntegralrecordInfoDlg.clearData = function() {
    this.integralrecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IntegralrecordInfoDlg.set = function(key, val) {
    this.integralrecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IntegralrecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
IntegralrecordInfoDlg.close = function() {
    hidenBtn()
    //执行打印
    // document.getElementById("iframe_map_canvasInfo").contentWindow.commit();
    //显示dom
    showBtn()
    // document.getElementById("iframe_map_canvasInfo").contentWindow.commit();
}

/**
 * 收集数据
 */
IntegralrecordInfoDlg.collectData = function() {
    this
    .set('integral')
    .set('productname')
    .set('playType')
    .set('play')
    .set('scannerTypeContent')
    .set('consumptionNum')
    .set('typeId')
    .set('memberId');
}


/**
 * 验证数据是否为空
 */
IntegralrecordInfoDlg.validate = function () {
    $('#integralrecordInfoTable').bootstrapValidator('validate');
    return $("#integralrecordInfoTable").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
var timeClose=null;
var tempData=new Array();
var yingshouTemp=0;
var shishouTemp=0;
var zhekouTemp=0;
var jifenTemp=0;
IntegralrecordInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/integralrecord/add", function(data){
        //隐藏dom
        // hidenBtn()
        //执行打印
        // document.getElementById("iframe_map_canvasInfo").contentWindow.commit();
        //显示dom
         tempData=products;
        yingshouTemp=yingshou;
        shishouTemp=shishou;
        zhekouTemp=zhekou;
        jifenTemp=jifen;

        // viewToWord(products)
        // alert("正在打印。。。")
        // showBtn()
        Feng.success("操作成功!");
        $("#introducerName").val("");
        $("#name").val("");
        $("#memberId").val("");
        $("#tel").val("");
        $("#address").val("");
        $("#integralSum").val("");
        $("#countPrice").val("");
        $("#levelID").val("");
        $("#integral").val("");
        $("#play").val("");
        $("#verificationcode").val("");

        //清除页面数据
        products = new Array()
        loadProduct()
        //执行打印操作
        // IntegralrecordInfoDlg.addSubmit.trigger('done');
        timeClose= window.setInterval("viewToWord(tempData)",3000);
    },function(data){
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.integralrecordInfoData);
    ajax.set({productMianFeiNums:productMianFeiNums,productIds:productIds,productNums:productNums,verificationcode:$("#verificationcode").val(),tableNase64Data:tableNase64Data});
    ajax.start();
    
}
// IntegralrecordInfoDlg.addSubmit.on('done', viewToWord(tempData));
/**
 * 提交修改
 */
IntegralrecordInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/integralrecord/update", function(data){
        Feng.success("修改成功!");
        window.parent.Integralrecord.table.refresh();
        IntegralrecordInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.integralrecordInfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("integralrecordInfoTable", IntegralrecordInfoDlg.validateFields);

    //绑定商品搜索
    // $('.selectpicker').selectpicker({
    //     'selectedText': 'cat'
    // });
    // $(".selectpicker" ).selectpicker('refresh');
    // $(".bs-searchbox input").keyup(function(event){
    //     // if(event.keyCode == "13") {//判断如果按下的是回车键则执行下面的代码
    //         var search=($(".bs-searchbox input").val())
    //         //进行搜索 integralrecordtype/list
    //         var ajax = new $ax(Feng.ctxPath + "/integralrecordtype/list", function (data) {
    //             console.log(data)
    //             $("#productname").empty();
    //             if(data.rows.length>0){
    //                 for(var i=0;i<data.rows.length;i++){
    //                     $("#productname").append("<option value='"+data.rows[i].id+"'>【"+data.rows[i].productname+"】 库存("+data.rows[i].productnum+")</option>");
    //                 }
    //             }
    //             $(".selectpicker" ).selectpicker('refresh');
    //         }, function (data) {
    //         });
    //         ajax.set("condition", search);
    //         ajax.set("producttype", $("#typeId").val());
    //         ajax.set("limit", 9999);
    //         ajax.start();
    //     // }
    //
    // });;
    // $("#typeId").change(function () {
    //     changeselect();
    //
    //
    // })
    // $("#productname").change(function () {
    //   //查询商品
    //     findbyid();
    //
    // })
    // changeselect();
    // findbyid();
    // $("#play").keyup(function(event){
    //     console.log($("#play").val())
    //    changejifen();
    //
    // })
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
function hidenBtn() {
    // $("#read").css("display","none")
    // $("#ensure").css("display","none")
    // $("#cancel").css("display","none")
    $("#hiddenDiv0").css("display","block")
    $("#hiddenDiv1").css("display","none")

}
function showBtn() {
    $("#hiddenDiv0").css("display","none")
    $("#hiddenDiv1").css("display","block")
    // $("#read").css("display","block")
    // $("#ensure").css("display","block")
    // $("#cancel").css("display","block")
}
// 打印小票

function viewToWord(temp) {console.log("--执行")
    var products=temp;
    //当前时间
    var time = new Date();   // 程序计时的月从0开始取值后+1
    var m = time.getMonth() + 1;
    var t = time.getFullYear() + "-" + m + "-"
        + time.getDate() + " " + time.getHours() + ":"
        + time.getMinutes() + ":" + time.getSeconds();

    var fileName=new Date().getTime();
    try {
        // 创建ActiveXObject对象
        wdapp = new ActiveXObject("Word.Application");
    }
    catch (e) {
        console.log("无法调用Office对象，！", e)
        wdapp = null;
        return;
    }
    wdapp.Documents.Open("D:\\xingxie\\shouyinxiaopiaoTemp4.doc"); //打开本地(客户端)word模板
    wddoc = wdapp.ActiveDocument;
    wddoc.Bookmarks("time").Range.Text = t + "";
    wddoc.Bookmarks("yingshou").Range.Text = yingshouTemp+ "";
    wddoc.Bookmarks("shishou").Range.Text = shishouTemp + "";
    wddoc.Bookmarks("zhekou").Range.Text = zhekouTemp + "";
    wddoc.Bookmarks("jifen").Range.Text = jifenTemp + "";
    wddoc.Bookmarks("staffName").Range.Text = $("#staffName").val() + "";
    //添加表格
    // var myTable = wddoc.Tables.Add (wddoc.Bookmarks("OrderCart").Range,3,4);//(赋值区域,行数,列数)
    var myTable = wddoc.Tables.Add (wddoc.Bookmarks("OrderCart").Range,products.length,4);//(赋值区域,行数,列数)
    //隐藏边框
    var table=wdapp.ActiveDocument.Tables(1);
    // table.PreferredWidth =140
    table.Columns(1).Width = 56
    table.Columns(2).Width = 30
    table.Columns(3).Width = 28
    table.Columns(4).Width = 28
    table.Range.ParagraphFormat.Alignment = 1
    table.Borders(-1).LineStyle=0;
    table.Borders(-2).LineStyle=0;
    table.Borders(-3).LineStyle=0;
    table.Borders(-4).LineStyle=0;
    // for(i=1;i<=3;i++){//行
    for(i=0;i<products.length;i++){//行
        //第一列
        with (myTable.Cell(i+1,1).Range){
            font.Size = 5;//调整字体大小
            InsertAfter(products[i].val.productname);//插入的内容 商品名称
            ParagraphFormat.Alignment=2;

        }
        //第二列
        with(myTable.Cell(i+1,2).Range){
            font.Size = 8;
            InsertAfter(products[i].val.consumptionNum); //数量
            ParagraphFormat.Alignment=1;//表格内容对齐:0-左对齐 1-居中 2-右对齐
        }
        //第三列
        with(myTable.Cell(i+1,3).Range){
            font.Size = 8;
            InsertAfter(products[i].val.productpice); //单价
            ParagraphFormat.Alignment=1;
        }
        //第四列
        with(myTable.Cell(i+1,4).Range){
            font.Size = 8;
            InsertAfter(products[i].val.consumptionNum*products[i].val.productpice);//金额
            ParagraphFormat.Alignment=0;
        }
    }
    wdapp.visible = false;
    wddoc.saveAs("D:\\xingxie\\PrinterTemp_"+fileName+".doc"); //保存临时文件word
    // wddoc.Bookmarks("TotalPrice").Range.Text = "无价" + "\n";
    // wddoc.Bookmarks("Time").Range.Text = Time;
    //wdapp.ActiveDocument.ActiveWindow.View.Type = 1;
    // wdapp.visible = false; //word模板是否可见
    wdapp.Application.Printout(); //调用自动打印功能
    wdapp.Application.Printout(); //调用自动打印功能
    wdapp.quit();
    wdapp = null;
    window.clearTimeout(timeClose);
}