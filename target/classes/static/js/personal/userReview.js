jQuery(function($){
    var selectModal = $("#userReviewModal")
    var type = {0:"个人",1:"中介",2:"园区"}
    var realNameReview = {0:"未审核",1:"用户认证",2:"企业认证",3:"正在审核"}
    var approvalType = {1:"个人认证",2:"企业认证"}
    var isEnd = {1:"未认证",2:"认证",3:"失效"}
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
                ["用户姓名:", v.user.userName, "green"],
                ["用户手机号:", v.user.userPhone, "green"],
                ["用户类别:", type[v.user.type], "green"],
                ["审批类别",approvalType[v.type],"green"],
                ["实名制审核:", realNameReview[v.user.realNameReview], "green"],
                ["是否认证:", isEnd[v.isEnd], "red"],
            ];
            var $liLine = $('<li></li>').addClass('item-orange clearfix');
            var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');
            var $h3 = $('<h3></h3>').addClass('oid').append($('<small></small>').addClass('blue').html(v.user.userPhone));
            var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');


            var $btnSelet = $('<a></a>').addClass('btn btn-minier btn-yellow').html('查看');
            $.each(propertyArr, function (index, value) {
                var $liProperty = $('<li></li>');
                $('<strong></strong>').html(value[0]).appendTo($liProperty);
                $('<b></b>').html(value[1]).addClass(value[2]).appendTo($liProperty);
                $liProperty.appendTo($ulProperty);
            });


            $divBtnWrap.append($btnSelet).appendTo($h3);

            $liLine.append($h3).append($ulProperty);

            $btnSelet.click(function () {
                selectLine(v);
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
    searcher.init(["user_userPhone"], "condList", "/realNameSystem/pageRealNameSystem", function (data) {
        displayFunc.init('resultList', data.content);
    }, "pagenation","realNameSystem");

    selectModal.on('hide.bs.modal', function () {
        $("#businessNameDiv").addClass("hidden");
        $("#enterpriseCertificationTab").addClass("hidden");
    });

    function selectLine(v){
        if(v.type == 2){
            $("#businessNameDiv").removeClass("hidden");
            $("#enterpriseCertificationTab").removeClass("hidden");
        }
        if(v.isEnd != 1){
            $("#approveBtn").addClass("hidden")
            $("#resistBtn").addClass("hidden")
        }else if(v.isEnd == 1){
            $("#approveBtn").removeClass("hidden")
            $("#resistBtn").removeClass("hidden")
        }
        com.automaticInputValue(v,selectModal);
        selectModal.modal("show");
    }

    $("#approveBtn").click(function(){
        approvalCertification(2);
    })

    $("#resistBtn").click(function(){
        approvalCertification(1);
    })

    var approvalCertification = function(isEnd){
        var options = {
            rnid :$("#rnid").val(),
            isEnd:isEnd
        };
        com.post("/realNameSystem/updateRealNameSystem",options,function(res){
            com.showAlert(com.info, "操作成功");
            searcher.doSearch();
            selectModal.modal("hide");
        })
    }

})