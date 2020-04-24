jQuery(function($){
    var rentalSaleManagementDetail = {
        rentalSaleForm :$("#rentalSaleForm"),
        rentalSaleDetail : params.passData,
        btnBack:$("#btnBack").on("click",function(){
            com.switchPage(config.address + "views/information/rentalSaleManagement.html");
        }),
    }

    com.automaticInputValue(rentalSaleManagementDetail.rentalSaleDetail,rentalSaleManagementDetail.rentalSaleForm);

    rentalSaleManagementDetail.rentalSaleDetail.titlePicture ? (function (){
        var pane = $("<div></div>").addClass("col-md-3 widget-container-span ui-sortable header").attr("style","border-bottom:0px solid").appendTo($("#rentalSalePicture"));
        var paneImg = $('<img onerror="javascript:this.src=\'images/picture404.jpg\';" />').attr("src","http://39.106.78.98/"+ rentalSaleManagementDetail.rentalSaleDetail.titlePicture).css("margin-right", "20px").css("width", "24em").css("height", "18em").css("margin-bottom", "10px").appendTo(pane);
    })() :$("<div>当前信息没有首页图片!</div>").addClass("alert alert-info").appendTo($("#rentalSalePicture"));

    rentalSaleManagementDetail.rentalSaleDetail.pictureStorage.length != 0 ? $.each(rentalSaleManagementDetail.rentalSaleDetail.pictureStorage,function(i,v){
        var pane = $("<div></div>").addClass("col-md-3 widget-container-span ui-sortable header").attr("style","border-bottom:0px solid").appendTo($("#rentalSalePictureDetails"));
        var paneImg = $('<img onerror="javascript:this.src=\'images/picture404.jpg\';" />').attr("src","http://39.106.78.98/"+ v.url).css("margin-right", "20px").css("width", "24em").css("height", "18em").css("margin-bottom", "10px").appendTo(pane);
    }) :$("<div>当前信息没有详情图片!</div>").addClass("alert alert-info").appendTo($("#rentalSalePictureDetails"));
})