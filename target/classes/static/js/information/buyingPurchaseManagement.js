jQuery(function($){
    var buyingPurchaseType = {0: "初始", 1: "厂房", 2: "仓库", 3: "土地"}
    var buyingPurchaseStatus = {0: "初始", 1: "求租", 2: "求购"}
    var buyingPurchaseForm = $("#buyingPurchaseForm")
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
                ["房源编号:", v.productId, "green"],
                ["区域信息:", v.region, "green"],
                ["类别:", buyingPurchaseType[v.types], "green"],
                ["状态:", buyingPurchaseStatus[v.status], "green"],
                ["求租面积:", (v.areaCap+"-"+v.areaLower), "green"],
                ["功能用途:", v.functionalUse, "green"],
                ["配套需求:", v.supportingDemand, "green"],
                ["发布时间:", v.createTime, "green"],
                ["联系人:", v.contact, "green"],
                ["联系人电话:", v.contactPhone, "green"]
            ];
            var $liLine = $('<li></li>').addClass('item-orange clearfix');
            var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');
            var $h3 = $('<h3></h3>').addClass('oid').append($('<small></small>').addClass('blue').html(v.title));
            var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');

            var $btnStatus = $('<div></div>').addClass('pull-right');
            var $statusSwitch = $('<input/>').addClass('ace ace-switch ace-switch-5').prop({"type": "checkbox"});
            var $span = $('<span></span>').addClass('lbl');

            var $btnSelet = $('<a></a>').addClass('btn btn-minier btn-yellow').html('查看');
            var $btnDel = $('<a></a>').addClass('btn btn-minier btn-danger ').html('删除')
            $.each(propertyArr, function (index, value) {
                var $liProperty = $('<li></li>');
                $('<strong></strong>').html(value[0]).appendTo($liProperty);
                $('<b></b>').html(value[1]).addClass(value[2]).appendTo($liProperty);
                $liProperty.appendTo($ulProperty);
            });
            $btnStatus.append($statusSwitch).append($span);

            $divBtnWrap.append($btnSelet).append($btnDel).append($btnStatus).appendTo($h3);

            $liLine.append($h3).append($ulProperty);

            $btnSelet.click(function () {
                selectLine(v);
            });
            $btnDel.click(function () {
                deleLine(v.bpid);
            })

            v.isEnd == 1 && $statusSwitch.prop("checked", true);
            $statusSwitch.switchBtn("status-lbl").on("click", function () {
                !$statusSwitch.prop("checked") ? (updateStatus(v.bpid, 2, function () {
                    $statusSwitch.prop("checked")
                })) : (updateStatus(v.bpid, 1, function () {
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
    searcher.init(["userPhone"], "condList", api.serverHost+api.pageBuyingPurchase, function (data) {
        displayFunc.init('resultList', data.content);
    }, "pagenation","buyingPurchase",{"isEnd" : 0});

    function selectLine(data){
        com.automaticInputValue(data,buyingPurchaseForm);
        $("#buyingPurchaseModal").modal("show")
    }
    function deleLine(bpid){
        com.post(api.serverHost+api.deleteBuyingPurchase, {"bpid":bpid}, function (res) {
            com.showAlert(com.info, "操作成功");
            searcher.doSearch();
        });
    }
    function updateStatus(bpid,cvalue){
        if(com.validate()){
            return;
        }
        var options = {};
        options["bpid"] = bpid;
        options["isEnd"] = cvalue;
        com.post(api.serverHost+api.updateBuyingPurchaseByIsEnd, options, function (res) {
            searcher.doSearch();
        });
    }
})