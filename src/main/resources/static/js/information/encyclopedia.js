jQuery(function($) {
    var type = {1: "企业经营", 2: "投资融资", 3: "政策法规", 4: "厂房装修", 5: "地产交易"}
    var isEnd = {1: "未解决", 2: "已解决"}
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
                ["创建时间:", v.createTime, "green"],
                ["创建人姓名:", v.createName, "green"],
                ["百科类型:", type[v.type], "green"],
                ["是否解决:", isEnd[v.isEnd], "red"],
            ];
            var $liLine = $('<li></li>').addClass('item-orange clearfix');
            var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');
            var $h3 = $('<h3></h3>').addClass('oid').append($('<small></small>').addClass('blue').html(v.title));
            var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');
            var $btnSelet = $('<a></a>').addClass('btn btn-minier btn-yellow').html('查看');
            var $btnDel = $('<a></a>').addClass('btn btn-minier btn-danger ').html('删除')
            $.each(propertyArr, function (index, value) {
                var $liProperty = $('<li></li>');
                $('<strong></strong>').html(value[0]).appendTo($liProperty);
                $('<b></b>').html(value[1]).addClass(value[2]).appendTo($liProperty);
                $liProperty.appendTo($ulProperty);
            });

            $divBtnWrap.append($btnSelet).append($btnDel).appendTo($h3);

            $liLine.append($h3).append($ulProperty);

            $btnSelet.click(function () {
                selectLine(v);
            });
            $btnDel.click(function () {
                delLine(v.elid);
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
    searcher.init(["title"], "condList", "/encyclopedia/pageEncyclopedia", function (data) {
        displayFunc.init('resultList', data.content);
    }, "pagenation","encyclopedia");

    function selectLine(v) {
        params["passData"] = v;
        com.switchPage(config.address + "views/information/encyclopediaDetail.html");

    }

    function delLine(elid){
        com.post(api.serverHost+api.deleteEncyclopedia, {"elid":elid}, function (res) {
            com.showAlert(com.info, "操作成功");
            searcher.doSearch();
        });
    }


})