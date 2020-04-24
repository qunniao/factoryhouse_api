jQuery(function($){
    var type = {0:"初始化",1:"会员"}
    var productList = {
        modal: $("#editModal"),
        productListForm:$("#productList"),
        saveBtn:$("#saveBtn").on("click", saveData),
    }
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
                ["商品价格:",v.balance,"green"],
                ["商品类型:",type[v.type],"green"],
                ["商品说明:",v.content,"green"],
            ];
            var $liLine = $('<li></li>').addClass('item-orange clearfix');
            var $ulProperty = $('<ul></ul>').addClass('list-unstyled fl-inline-list clearfix');
            var $h3 = $('<h3></h3>').addClass('oid').append($('<small></small>').addClass('blue').html(v.productName));
            var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');


            var $btnEdit = $('<a></a>').addClass('btn btn-minier btn-primary').html('编辑');
            var $btnDel = $('<a></a>').addClass('btn btn-minier btn-danger ').html('删除')
            $.each(propertyArr, function (index, value) {
                var $liProperty = $('<li></li>');
                $('<strong></strong>').html(value[0]).appendTo($liProperty);
                $('<b></b>').html(value[1]).addClass(value[2]).appendTo($liProperty);
                $liProperty.appendTo($ulProperty);
            });

            $divBtnWrap.append($btnEdit).appendTo($h3);

            $liLine.append($h3).append($ulProperty);

            $btnEdit.click(function(){
                editLine(v);
            });
            $btnDel.click(function () {
                delLine(v.plid);
            })

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
    searcher.init( [], "condList", api.serverHost+api.pageProductList, function(data){
        displayFunc.init('resultList', data.content);
    }, "pagenation","productList",null,null,null,function(){

        var $adda = $('<a></a>').addClass('btn btn-minier btn-yellow pull-left').attr('name', 'addNews').css("margin", "0px 3px");
        var $addi = $('<i></i>').addClass('icon-plus bigger-110').appendTo($adda);
        var $addspan = $('<span></span>').text('添加').appendTo($adda);

        return [$adda];
    });
    function saveData(){
        if(com.validate()){
            return;
        }
        var clickType = productList.modal.attr("clickType")
        if(clickType == "edit"){
            var options = com.packagedData(productList.productListForm)
            com.post(api.serverHost+api.updateProductList, options, successCallback);
        }else if(clickType == "save"){
            var options = com.packagedData(productList.productListForm,["plid"])
            com.post(api.serverHost+api.addProductList, options, successCallback);
        }

        productList.modal.modal("hide")
    }

    function successCallback() {
        com.showAlert(com.info, "操作成功");
        searcher.doSearch();
    }

    function editLine(v){
        productList.modal.attr("clickType","edit")
        com.automaticInputValue(v,productList.productListForm)
        productList.modal.modal("show")
    }

    function delLine(plid){
        com.post(api.serverHost+api.deleteProductList, {"plid":plid}, function (res) {
            successCallback()
        });
    }

    //添加按钮
    $('a[name="addNews"]','#pagenation').click(function(){
        productList.modal.attr("clickType","save")
        productList.modal.modal("show")
    });

    productList.modal.on('hide.bs.modal', function () {
        productList.modal.removeAttr("clickType")
    });

})