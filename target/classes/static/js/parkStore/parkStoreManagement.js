jQuery(function($){
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
                ["总栋数:",(v.allBuildingsNum+"栋"),"green"],
                ["产证:",v.productionCertificate,"green"],
                ["园区地址:",v.parkAddress,"green"],
                ["价格:",v.parkprice,"green"],
                ["建筑面积:",v.priceArea,"green"],
                ["交付时间:",v.deliveryTime,"green"],
                ["联系人:",v.contact,"green"],
                ["联系电话:",v.contactPhone,"green"],
            ];
            var $liLine = $('<li></li>').addClass('item-orange clearfix');
            var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');
            var $h3 = $('<h3></h3>').addClass('oid').append($('<small></small>').addClass('blue').html(v.title));
            var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');

            var $btnStatus = $('<div></div>').addClass('pull-right');
            var $statusSwitch = $('<input/>').addClass('ace ace-switch ace-switch-5').prop({"type" : "checkbox"});
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

            $btnSelet.click(function(){
                selectLine(v);
            });
            $btnDel.click(function () {
                delLine(v.pid);
            })

            v.status == 1 && $statusSwitch.prop("checked", true);
            $statusSwitch.switchBtn("status-lbl").on("click",function() {
                !$statusSwitch.prop("checked") ? (updateStatus(v.pid, 2, function(){$statusSwitch.prop("checked")})) : (updateStatus(v.pid, 1,function(){$statusSwitch.removeAttr("checked")}));
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

    var searcher = new Searcher();
    searcher.init( ["title"], "condList", api.serverHost+api.pageParkStore, function(data){
        displayFunc.init('resultList', data.content);
    }, "pagenation","parkStore",{"status":0});

    function updateStatus(pid,status){
        com.post(api.serverHost+api.updateParkStoreByStatus, {pid:pid,status:status}, function (res) {
            searcher.doSearch();
        });
    }

    function selectLine(v){
        params["passData"] = v
        com.switchPage(config.address + "views/parkStore/parkStoreManagementDetail.html");
    }
    function delLine(pid){
        com.post(api.serverHost+api.deleteParkStore, {"pid":pid}, function (res) {
            com.showAlert(com.info, "操作成功");
            searcher.doSearch();
        });
    }
})