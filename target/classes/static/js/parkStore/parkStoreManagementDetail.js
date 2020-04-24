jQuery(function ($) {
    var parkStoreManagementDetail={
        parkStoreForm : $("#parkStoreForm"),
        btnBack:$("#backBtn").on("click",function(){
            com.switchPage(config.address + "views/parkStore/parkStoreManagement.html");
        }),
        parkStoreDeail:params.passData
    }

    com.automaticInputValue(parkStoreManagementDetail.parkStoreDeail,parkStoreManagementDetail.parkStoreForm)

    parkStoreManagementDetail.parkStoreDeail.pictureStorage.length !=0 ? $.each(parkStoreManagementDetail.parkStoreDeail.pictureStorage,function(i,v){
        var pane = $("<div></div>").addClass("col-md-3 widget-container-span ui-sortable header").attr("style","border-bottom:0px solid").appendTo($("#parkStorePicture"));
        var paneImg = $('<img onerror="javascript:this.src=\'images/picture404.jpg\';" />').attr("src","http://39.106.78.98/"+ v.url).css("margin-right", "20px").css("width", "24em").css("height", "18em").css("margin-bottom", "10px").appendTo(pane);
    }) :$("<div>当前园区没有图片!</div>").addClass("alert alert-info").appendTo($("#parkStorePicture"));

    parkStoreManagementDetail.parkStoreDeail.parkStoreHouseType.length !=0?$.each(parkStoreManagementDetail.parkStoreDeail.parkStoreHouseType,function(i,v){
        var pane = $("<div></div>").addClass("col-md-3 widget-container-span ui-sortable slight-bottom-shift").appendTo($("#parkStoreHouseType"));
        var paneImg = $('<img onerror="javascript:this.src=\'images/picture404.jpg\';" />').attr("src","http://39.106.78.98/"+v.imgUrl).css("margin-right", "20px").css("width", "14em").css("height", "10em").css("margin-bottom", "10px").appendTo(pane);
        var div = $("<div></div>").addClass("disPlay-inline-block").appendTo(pane)
        var constructerName = $("<div></div>").css("margin-right", "20px").html(v.type).appendTo(div);
        var constructerDuty = $("<div></div>").html("建筑面积:"+v.priceArea).appendTo(div);
        var constructerDuty = $("<div></div>").html("层高:"+v.layerHeight).appendTo(div);
    }):$("<div>当前园区没有户型图!</div>").addClass("alert alert-info").appendTo($("#parkStoreHouseType"));
})