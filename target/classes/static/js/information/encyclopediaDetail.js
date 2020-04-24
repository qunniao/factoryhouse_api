jQuery(function($){
    var encyclopediaDetail = {
        encyclopediaForm:$('#encyclopediaForm'),
        encyclopediaData:params.passData,
        encyclopediaAnswer:$("#encyclopediaAnswer"),
        btnBack:$("#backBtn").on("click",function(){
            com.switchPage(config.address + "views/information/encyclopedia.html");
        }),
    }
    com.automaticInputValue(encyclopediaDetail.encyclopediaData,encyclopediaDetail.encyclopediaForm)

    encyclopediaDetail.encyclopediaData.encyclopediaAnswer.length>0 ? $.each(encyclopediaDetail.encyclopediaData.encyclopediaAnswer,function(i,v){
        var $divBtnWrap = $('<div></div>').addClass('pull-right action-buttons');
        var $btnDel = $('<a></a>').addClass('btn btn-minier btn-danger ').html('删除')
        var $p = $("<p></p>").addClass("lead").attr("style","margin-left:30px").html(v.content)
        var $h2 = $("<h2></h2>").addClass("lighter block green").html(v.createName).append($divBtnWrap.append($btnDel))
        var $createTime = $("<p></p>").html(v.createTime)
        var $group = $("<div></div>").addClass("form-group well")
        v.isEnd == 2 ? encyclopediaDetail.encyclopediaAnswer.prepend($group.attr("style","background-color:#d9edf7").append($h2,$p,$createTime),$("<hr/>")):encyclopediaDetail.encyclopediaAnswer.append($group.append($h2,$p,$createTime),$("<hr/>"))
        $btnDel.click(function () {
            var that = $(this)
            delLine(v.eaid,that);
        })
    }):$("<div>当前百科无人回答!</div>").addClass("alert alert-info").appendTo($("#encyclopediaAnswer"))

    function delLine(eaid,that){
        var $group = that.parents(".form-group.well")
        var $hr = $group.next("hr")
        com.post(api.serverHost+api.deleteEncyclopediaAnswer, {"eaid":eaid}, function (res) {
            com.showAlert(com.info, "操作成功");
            $group.remove()
            $hr.remove()
        });
    }


})