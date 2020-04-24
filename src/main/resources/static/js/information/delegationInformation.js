jQuery(function($){
    var type = {1: "厂房", 2: "仓库", 3: "土地"}
    var status = {1: "出租", 2: "出售", 3: "求租",4:"求购"}
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
                ["委托人电话:", v.contactPhone, "green"],
                ["委托类型:", type[v.type], "green"],
                ["委托状态:", status[v.status], "red"],
                ["创建时间:", v.createTime, "green"],
                ["详情:", v.content, "green"],
            ];
            var $liLine = $('<li></li>').addClass('item-orange clearfix');
            var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');
            var $h3 = $('<h3></h3>').addClass('oid').append($('<small></small>').addClass('blue').html(v.contact));
            var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');
            var $btnDel = $('<a></a>').addClass('btn btn-minier btn-danger ').html('删除')
            $.each(propertyArr, function (index, value) {
                var $liProperty = $('<li></li>');
                $('<strong></strong>').html(value[0]).appendTo($liProperty);
                $('<b></b>').html(value[1]).addClass(value[2]).appendTo($liProperty);
                $liProperty.appendTo($ulProperty);
            });

            $divBtnWrap.append($btnDel).appendTo($h3);

            $liLine.append($h3).append($ulProperty);

            $btnDel.click(function () {
                deleLine(v.did);
            })

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
    searcher.init(["contactPhone"], "condList", api.serverHost+api.pageDelegationInformation, function (data) {
        displayFunc.init('resultList', data.content);
    }, "pagenation","delegationInformation");

    function deleLine(did) {
        com.post(api.serverHost+api.deleteDelegationInformation, {"did":did}, function (res) {
            com.showAlert(com.info, "操作成功");
            searcher.doSearch();
        });
    }

})