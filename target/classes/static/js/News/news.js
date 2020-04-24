jQuery(function($){

    var news = {
        newsModal :$("#newsModal"),
        saveBtn: $("#saveBtn").on("click", saveData),
    }
    var realNameReview = {1:"滴滴有话说",2:"监测与分析",3:"项目招商",4:"园区动态",5:"指数分析"}
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
                ["创建时间:",v.createTime,"green"],
                ["创建用户名:",v.createName,"green"],
                ["资讯类型:",realNameReview[v.type],"green"],
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
            $btnDel.click(function(){
                deleLine(v.nid);
            })

            v.isEnd == 1 && $statusSwitch.prop("checked", true);
            $statusSwitch.switchBtn("status-lbl").on("click",function() {
                !$statusSwitch.prop("checked") ? (updateStatus(v.nid, 2, function(){$statusSwitch.prop("checked")})) : (updateStatus(v.nid, 1,function(){$statusSwitch.removeAttr("checked")}));
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
    searcher.init( ["title"], "condList", api.serverHost+api.pageNews, function(data){
        displayFunc.init('resultList', data.content);
    }, "pagenation",'newsController',{"isEnd" : 0},null,null,function(){

        var $adda = $('<a></a>').addClass('btn btn-minier btn-yellow pull-left').attr('name', 'addNews').css("margin", "0px 3px");
        var $addi = $('<i></i>').addClass('icon-plus bigger-110').appendTo($adda);
        var $addspan = $('<span></span>').text('添加').appendTo($adda);

        return [$adda];
    });

    //添加按钮
    $('a[name="addNews"]','#pagenation').click(function(){
        $('#summernote').summernote('reset');
        news.newsModal.modal("show");
    });

    function selectLine(v){
        $("#newsDetails").html(v.content);
        $("#selectModal").modal("show");
    }

    function updateStatus(nid,isEnd){
        var options = {};
        options["nid"] = nid;
        options["isEnd"] = isEnd;
        com.post(api.serverHost+api.updateNewsByIsEnd, options, function (res) {
            searcher.doSearch();
        });
    }



    function deleLine(nid){
        com.post(api.serverHost+api.deleteNews, {"nid":nid}, function (res) {
            com.showAlert(com.info, "操作成功");
            searcher.doSearch();
        });
    }

    $('#newsModal input[type=file]').ace_file_input({
        style:'well',
        btn_choose:'标题图片',
        btn_change:null,
        no_icon:'icon-cloud-upload',
        droppable:true,
        thumbnail:'large'
    })

    $(document).ready(function() {
        $('#summernote').summernote({
            lang: 'zh-CN',
            height : 300,
            dialogsFade : true,
            focus: true,
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['image',['floatLeft','floatRight','floatNone']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']],
                ['Insert',['picture','table','hr']],
                ['Misc',['undo','redo','fullscreen']],
            ],
            callbacks: {
                onImageUpload: function(files) {
                    var $that = $(this)
                    var $files = new FormData();
                    $.each(files,function(i,v){
                        $files.append("files",v)
                    })

                    $.ajax({
                        data : $files,
                        type : "POST",
                        url :api.serverHost+api.uploadPicture,
                        cache : false,
                        contentType : false,
                        processData : false,
                        success : function(res) {
                            $.each(res.data,function(i,v){
                                $that.summernote('insertImage', config.imgServer + v, function($image) {
                                    $image.attr("style","width: 525px; height: 371px;")
                                });
                            })
                        }, error: function(msg) {
                            com.showAlert(com.error, "图片添加失败")
                            news.newsModal.modal("hide");
                        },
                    });


                }
            }
        });
        //$('#summernote').summernote('destroy');
    });

    function saveData(){
        if(com.validate()){
            return;
        }
        if ($('#summernote').summernote('isEmpty')) { com.showAlert(com.error, "编辑器为空");news.newsModal.modal("hide"); return false};
        var summernote = $('#summernote').summernote('code');
        var $files = new FormData();
        $files.append("titlePictureFile",$('#titleFlie')[0].files[0])
        $files.append("title",$('#title').val())
        $files.append("type",$('#type').val())
        $files.append("content",summernote)
        $files.append("token",params.token)
        $.ajax({
            data : $files,
            type : "POST",
            url : api.serverHost+api.addNews,
            cache : false,
            contentType : false,
            processData : false,
            success : function(res) {
                searcher.doSearch();
                news.newsModal.modal("hide");
                com.showAlert(com.info, "操作成功");
            }, error: function(msg) {
                com.showAlert(com.error, "图片添加失败")
                news.newsModal.modal("hide");
            },
        });
    }

})