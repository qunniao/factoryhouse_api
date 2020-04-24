jQuery(function($) {
        var fplhouseType = {0: "初始", 1: "厂房", 2: "仓库", 3: "土地"}
        var fplhouseStatus = {0: "初始", 1: "出租", 2: "出售"}
        var subleaseType = {0: "分租", 1: "不分租"}
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
                    ["类别:", fplhouseType[v.types], "green"],
                    ["状态:", fplhouseStatus[v.status], "green"],
                    ["是否分租:", subleaseType[v.subleaseType], "green"],
                    ["租金/售价:", v.rent, "green"],
                    ["面积:", (v.area + v.areaUnit), "green"],
                    ["联系人:", (v.contact), "green"],
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
                    deleLine(v.fid);
                })

                v.isEnd == 1 && $statusSwitch.prop("checked", true);
                $statusSwitch.switchBtn("status-lbl").on("click", function () {
                    !$statusSwitch.prop("checked") ? (updateStatus(v.fid, 2, function () {
                        $statusSwitch.prop("checked")
                    })) : (updateStatus(v.fid, 1, function () {
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
        searcher.init(["userPhone"], "condList", api.serverHost+api.pageFplHouse, function (data) {
            displayFunc.init('resultList', data.content);
        }, "pagenation","fplHouse",{"isEnd" : 0});

        function selectLine(v) {
            params["passData"] = v;
            com.switchPage(config.address + "views/information/rentalSaleManagementDetail.html");
        }

        function deleLine(fid) {
            com.post(api.serverHost+api.deleteFplHouse, {"fid":fid}, function (res) {
                com.showAlert(com.info, "操作成功");
                searcher.doSearch();
            });
        }
        function updateStatus(fid,cvalue){
            var options = {};
            options["fid"] = fid;
            options["isEnd"] = cvalue;
            com.post(api.serverHost+api.updateFplHouseByIsEnd, options, function (res) {
                searcher.doSearch();
            });
        }
})