jQuery(function($){

    var administrator = {
        administratorItems:{
            userId:$("#userId"),
            userPhone:$("#userPhone"),
            userName:$("#userName"),
            userPassword:$("#userPassword")
        },
        editModal: $("#editModal"),
        editBtn: $("#editBtn").on("click", editData),
        saveModal:$("#saveModal"),
        saveItems:{
            saveUserPhone :$("#saveUserPhone"),
            saveUserName :$("#saveUserName"),
            saveUserPassword :$("#saveUserPassword"),
            saveAccountNumber : $("#saveAccountNumber")
        },
        saveBtn : $("#saveBtn").on("click",saveData)
    }
    var displayFunc = (function () {

        var renderList = function (tableId, listData) {
            var $tableWrap = $('#' + tableId);
            var $ul = $('<ul></ul>').addClass('item-list fl-table-list ui-sortable');

            if (listData && listData.length) {
                $.each(listData, function (i, v) {
                    var $liLine = creatLine(v);

                    $liLine.appendTo($ul);
                });

                $ul.appendTo($tableWrap);
            }
        };

        var creatLine = function (v) {
            var propertyArr = [
                ["手机号:", v.userPhone, "green"],
                ["创建时间:", v.createTime, "green"],
            ];
            var $liLine = $('<li></li>').addClass('item-orange clearfix');
            var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');
            var $h3 = $('<h3></h3>').addClass('oid').append($('<small></small>').addClass('blue').html(v.userName));
            var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');

            var $btnStatus = $('<div></div>').addClass('pull-right');
            var $statusSwitch = $('<input/>').addClass('ace ace-switch ace-switch-5').prop({"type": "checkbox"});
            var $span = $('<span></span>').addClass('lbl');

            var $btnEdit = $('<a></a>').addClass('btn btn-minier btn-yellow').html('编辑');
            var $btnDel = $('<a></a>').addClass('btn btn-minier btn-danger ').html('删除')
            $.each(propertyArr, function (index, value) {
                var $liProperty = $('<li></li>');
                $('<strong></strong>').html(value[0]).appendTo($liProperty);
                $('<b></b>').html(value[1]).addClass(value[2]).appendTo($liProperty);
                $liProperty.appendTo($ulProperty);
            });

            $btnStatus.append($statusSwitch).append($span);

            $divBtnWrap.append($btnEdit).append($btnDel).append($btnStatus).appendTo($h3);

            $liLine.append($h3).append($ulProperty);

            $btnEdit.click(function () {
                editLine(v);
            });
            $btnDel.click(function () {
                delLine(v.aid);
            })

            v.isEnd == 1 && $statusSwitch.prop("checked", true);
            $statusSwitch.switchBtn("status-lbl").on("click", function () {
                !$statusSwitch.prop("checked") ? (updateStatus(v.aid, 2, function () {
                    $statusSwitch.prop("checked")
                })) : (updateStatus(v.aid, 1, function () {
                    $statusSwitch.removeAttr("checked")
                }));
            });

            return $liLine;
        };

        return {
            init: function (tableId, listData) {
                $('#' + tableId).html(null);
                renderList(tableId, listData);
            },
            drawLine: function (v) {
                return creatLine(v);
            }
        };
    })();

    var searcher = new Searcher();
    searcher.init([], "condList", api.serverHost+api.pageAdministratorAccount, function (data) {
        displayFunc.init('resultList', data.content);
    }, "pagenation","administrator",null,null,null,function(){

        var $adda = $('<a></a>').addClass('btn btn-minier btn-yellow pull-left').attr('name', 'addNews').css("margin", "0px 3px");
        var $addi = $('<i></i>').addClass('icon-plus bigger-110').appendTo($adda);
        var $addspan = $('<span></span>').text('添加').appendTo($adda);

        return [$adda];
    });

    //添加按钮
    $('a[name="addNews"]','#pagenation').click(function(){
        administrator.saveModal.modal("show");
    });


    function updateStatus(aid,isEnd){
        var options = {};
        options["aid"] = aid;
        options["isEnd"] = isEnd;
        com.post(api.serverHost+api.updateAdministratorAccountByIsEnd, options, function (res) {
            searcher.doSearch();
        });
    }

    function editLine (v){

        administrator.administratorItems.userId.val(v.aid)
        administrator.administratorItems.userPhone.val(v.userPhone);
        administrator.administratorItems.userName.val(v.userName);
        administrator.administratorItems.userPassword.val("")

        administrator.editModal.modal("show");
    }

    function editData (){
        if(com.validate()){
            return;
        }
        var options = {
            "aid": administrator.administratorItems.userId.val(),
            "userPhone":  administrator.administratorItems.userPhone.val(),
            "userName": administrator.administratorItems.userName.val(),
            "userPassword":  administrator.administratorItems.userPassword.val(),
        };
        com.post(api.serverHost+api.updateAdministratorAccount, options, successCallback);
        administrator.editModal.modal("hide");

    }

    function saveData(){
        if(com.validate()){
            return;
        }
        var options = {
            "userPhone":  administrator.saveItems.saveUserPhone.val(),
            "userName": administrator.saveItems.saveUserName.val(),
            "accountNumber":  administrator.saveItems.saveAccountNumber.val(),
            "userPassword":  administrator.saveItems.saveUserPassword.val(),
        };

        com.post(api.serverHost+api.addAdministratorAccount, options, successCallback);
        administrator.saveModal.modal("hide");
    }


    function successCallback() {
        com.showAlert(com.info, "操作成功");
        searcher.doSearch();
    }

    function delLine(aid){
        com.post(api.serverHost+api.deleteAdministratorAccount, {"aid":aid}, function (res) {
            com.showAlert(com.info, "操作成功");
            searcher.doSearch();
        });
    }

})