jQuery(function($){
    /**
     * 回调函数必须提供 init(tableId, result.list)方法
     * tableId ，用来显示的div的id
     * result.list 列表的json数组
     * */
    var sex = {0:"男",1:"女"}
    var realNameReview = {0:"未审核",1:"用户认证",2:"企业认证"}
    var type = {0:"个人",1:"中介",2:"园区"}
    var walletOrderType = {0:"初始化",1:"钱包充值",2:"开通会员"}
    var walletOrderPaymentType = {0:"初始化",1:"钱包购买",2:"支付宝支付"}
    var walletOrderStatus = {0:"初始化",1:"支付成功",2:"支付成功",3:"未付款",4:"交易关闭"}
    var userInfo = {
        modal:  $("#editModal"),
        userConsumption:  $("#userConsumptionModal"),
        saveBtn: $("#saveBtn").on("click", saveData),
        userInfoItems :{
            userId:$("#userId"),
            userPhone:$("#userPhone"),
            userName:$("#userName"),
            userSex:$("#userSex"),
            userPassword:$("#userPassword")
        }
    }
    userInfo.modal.on('show.bs.modal',function(e){
        com.clearValidator();
    });
    var displayFunc  = (function(){

        var renderList = function(tableId,listData){
            var $tableWrap = $('#' + tableId);
            var $ul = $('<ul></ul>').addClass('item-list fl-table-list ui-sortable');

            if(listData && listData.length){
                $.each(listData,function(i, v){
                    var $liLine = creatLine(v);

                    $liLine.appendTo($ul);
                });

                $ul.appendTo($tableWrap);
            }
        };

        var creatLine = function(v){
            var propertyArr = [
                ["性别:",sex[v.userSex],"green"],
                ["账户余额:",v.balance,"green"],
                ["创建时间:",v.createTime,"green"],
                ["账号状态:",status[v.status],"green"],
                ["账号认证:",realNameReview[v.realNameReview],"green"],
                ["是否为会员:",(v.isEnd?"是":"否"),"green"],
                ["用户类型:",type[v.type],"green"]
            ];
            var $liLine = $('<li></li>').addClass('item-orange clearfix');
            var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');
            var $h3 = $('<h3></h3>').addClass('oid').append($('<small></small>').addClass('blue').html((v.userName?v.userName:"")+"  "+v.userPhone));
            var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');

            var $btnStatus = $('<div></div>').addClass('pull-right');
            var $statusSwitch = $('<input/>').addClass('ace ace-switch ace-switch-5').prop({"type" : "checkbox"});
            var $span = $('<span></span>').addClass('lbl');
            var $seletOrder = $('<a></a>').addClass('btn btn-minier btn-purple').html('消费记录');
            var $btnEdit = $('<a></a>').addClass('btn btn-minier btn-yellow').html('编辑');
            var $btnDel = $('<a></a>').addClass('btn btn-minier btn-danger ').html('删除')
            $.each(propertyArr, function (index, value) {
                var $liProperty = $('<li></li>');
                $('<strong></strong>').html(value[0]).appendTo($liProperty);
                $('<b></b>').html(value[1]).addClass(value[2]).appendTo($liProperty);
                $liProperty.appendTo($ulProperty);
            });
            $btnStatus.append($statusSwitch).append($span);

            $divBtnWrap.append($seletOrder).append($btnEdit).append($btnStatus).appendTo($h3);

            $liLine.append($h3).append($ulProperty);

            $seletOrder.click(function(){
                seletOrder(v.uid);
            })

            $btnEdit.click(function(){
                editLine(v);
            });
            $btnDel.click(function(){
                deleUser(v.uid);
            })

            v.status == 0 && $statusSwitch.prop("checked", true);
            $statusSwitch.switchBtn("status-lbl").on("click",function() {
                !$statusSwitch.prop("checked") ? (updateStatus(v.uid, 1, function(){$statusSwitch.prop("checked")})) : (updateStatus(v.uid, 0,function(){$statusSwitch.removeAttr("checked")}));
            });
            return $liLine;
        };

        return {
            init : function(tableId,listData){
                $('#' + tableId).html(null);
                renderList(tableId,listData);
            },
            drawLine : function(v){
                return creatLine(v);
            }
        };
    })();





    function updateStatus(uid, cvalue) {
        var options = {};
        options["uid"] = uid;
        options["status"] = cvalue;
        com.post("/personal/updateUserStatus", options, function (res) {
            searcher.doSearch();
        });
    }

    function seletOrder(uid){
        var searcher1 = new Searcher();
        searcher1.init( [""], "", "/walletOrder/pageWalletOrder", function(data){
            var $tableWrap = $('#walletResultList').empty();
            var $ul = $('<ul></ul>').addClass('item-list fl-table-list ui-sortable');

            if(data.content&& data.content.length){
                $.each(data.content,function(i, v){
                    var $liLine = createWalletOrder(v);
                    $liLine.appendTo($ul);
                });
                $ul.appendTo($tableWrap);
            }
        }, "walletPagenation","walletResultData",{"uid":uid,"type":1},null,null,null,"walletResultList");



        var searcher2 = new Searcher();
        searcher2.init( [""], "", "/walletOrder/pageWalletOrder", function(data){
            var $tableWrap = $('#joinMembershipResultList').empty();
            var $ul = $('<ul></ul>').addClass('item-list fl-table-list ui-sortable');

            if(data.content&& data.content.length){
                $.each(data.content,function(i, v){
                    var $liLine = createWalletOrder(v);
                    $liLine.appendTo($ul);
                });
                $ul.appendTo($tableWrap);
            }
        }, "joinMembershipPagenation","joinMembershipData",{"uid":uid,"type":2},null,null,null,"joinMembershipResultList");


        userInfo.userConsumption.modal("show");
    }


    function createWalletOrder(v) {
        var $liLine = $('<li></li>').addClass('item-blue clearfix');
        var $h3 = $('<h3></h3>').addClass('oid').css({"margin-bottom" : 0});

        var $label = $('<label></label>');
        var $span = $('<span></span>').addClass('lbl blue bigger-150').text(v.orderNumber);
        var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');

        var propertyArr = [
            ["订单金额:", v.orderMoney, "grey"],
            ["订单名称:", v.orderName, "grey"],
            ["商品描述:", v.description, "grey"],
            ["支付宝订单号:", v.outTradeNo, "grey"],
            ["订单类型:", walletOrderType[v.type], "grey"],
            ["支付类型:", walletOrderPaymentType[v.paymentType], "grey"],
            ["订单状态:", walletOrderStatus[v.status], "grey"],
            ["创建时间:", v.createTime, "grey"],
            ["成功支付时间:", v.successTime, "red"],
        ];

        $.each(propertyArr, function (index,value) {
            var $liProperty = $('<li></li>');
            $('<strong></strong>').html(value[0]).appendTo($liProperty);
            $('<b></b>').html(value[1]).addClass(value[2]).appendTo($liProperty);
            $liProperty.appendTo($ulProperty);
        });

        $label.append($span).appendTo($h3);
        $liLine.append($h3).append($ulProperty);

        return $liLine;
    }

    function deleUser(uid) {
        com.post("/personal/deleteUser", {"uid":uid}, function (res) {
            com.showAlert(com.info, "操作成功");
            searcher.doSearch();
        });
    }
/*    defaultFlag : $("#defaultFlag").switchBtn().on("click", function(a) {
        quotationVersion.quotationVersionItems.defaultFlag.value = 0 == quotationVersion.quotationVersionItems.defaultFlag.value ? 1 : 0
    })*/

    function saveData(){
        if(!$('#userName').valid()) return false;
        com.clearValidator();
        if (com.validate()) {
            return false;
        }

        var options = {
            "uid": userInfo.userInfoItems.userId.val(),
            "userPhone":  userInfo.userInfoItems.userPhone.val(),
            "userName": userInfo.userInfoItems.userName.val(),
            "userSex": userInfo.userInfoItems.userSex.val(),
            "userPassword":  userInfo.userInfoItems.userPassword.val(),
        };
        com.post("/personal/updateUser", options, successCallback);
        userInfo.modal.modal("hide");
    }

    function successCallback() {
        com.showAlert(com.info, "操作成功");
        searcher.doSearch();
    }
    $('#defaultFlag').removeAttr('checked').on('click', function(){
        $('.nav-pills').toggleClass('nav-stacked');
    });

})