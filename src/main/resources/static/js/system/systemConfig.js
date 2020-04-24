jQuery(function($){

    var systermConfig = {
        uploadPictureModal : $("#uploadPictureModal"),
        uploadPicture : $("#uploadPicture").click(function(){
            systermConfig.uploadPictureModal.modal("show")
        }),
        updataBtn : $("#updataBtn").click(function(){
            updataSystem()
        }),
        systemPicture : $("#systemPicture"),
    }


    $('#fuelux-wizard').ace_wizard({"status": true}).on('change', function(e, info) {

        var curstep = 0;

        info.direction === "previous" ? curstep = info.step - 1 : curstep = info.step;

        if (curstep == 1) {

        } else if (curstep == 2){
        }
    }).on('finished', function(e) {

    }).on('stepclick', function(e,info){

        if (info.step == 1) {

        }else if (info.step == 2){

        }
    }).on('next', function(e, info) {
    }).on('previous', function(e, info) {
    });





    var colorbox_params = {
        reposition:true,
        scalePhotos:true,
        scrolling:false,
        previous:'<i class="icon-arrow-left"></i>',
        next:'<i class="icon-arrow-right"></i>',
        close:'&times;',
        current:'{current} of {total}',
        maxWidth:'100%',
        maxHeight:'100%',
        onOpen:function(){
            document.body.style.overflow = 'hidden';
        },
        onClosed:function(){
            document.body.style.overflow = 'auto';
        },
        onComplete:function(){
            $.colorbox.resize();
        }
    };



    var myDropzone;
    try {
        $(".dropzone").dropzone({
            paramName: "file", // The name that will be used to transfer the file
            maxFilesize: 10, //MB
            //method:"post",
            //paramName:"files",
            addRemoveLinks : true,
            maxFiles:5,
            acceptedFiles: ".jpg,.gif,.png,.pdf,.jpeg", //上传的类型
            dictDefaultMessage :'<span class="bigger-150 bolder"><i class="icon-caret-right red"></i> Drop files</span> to upload \
				<span class="smaller-80 grey">(or click)</span> <br /> \
				<i class="upload-icon icon-cloud-upload blue icon-3x"></i>',
            //previewsContainer:"#adds", //显示的容器
            autoProcessQueue: false,
            parallelUploads: 10,
            dictFallbackMessage:"上传失败,稍后再试",
            dictResponseError: '文件上传失败!',
            uploadMultiple:true,
            dictFileTooBig:"文件过大({{filesize}}MB). 上传文件最大支持: {{maxFilesize}}MB.",
            dictCancelUpload:"重新上传",
            dictCancelUploadConfirmation:"确认删除上传图片吗",
            dictRemoveFile: "删除图片",
            dictMaxFilesExceeded:"超过上传图片上限",
            //change the previewTemplate to use Bootstrap progress bars
            previewTemplate: "<div class=\"dz-preview dz-file-preview\">\n  <div class=\"dz-details\">\n    <div class=\"dz-filename\"><span data-dz-name></span></div>\n    <div class=\"dz-size\" data-dz-size></div>\n    <img data-dz-thumbnail />\n  </div>\n  <div class=\"dz-success-mark\"><span></span></div>\n  <div class=\"dz-error-mark\"><span></span></div>\n  <div class=\"dz-error-message\"><span data-dz-errormessage></span></div>\n</div>",
            init: function() {
                myDropzone = this;
                this.on("addedfile", function (file) {
                });
                this.on('removedfile', function (file) {
                });
                this.on('maxfilesexceeded', function (file) {
                    com.showAlert(com.warning, "超过上传数量");
                });
            },

        });
    } catch(e) {
        alert("上传文件不支持旧版浏览器");
    }
    $("#savePictureBtn").click(function(){
        var length = myDropzone.files.length;

        length<1 || length>5?length<1?com.showAlert(com.error, "上传文件不能为空"):com.showAlert(com.error, "上传文件超过上限"):(function(myDropzone){
            var $files = new FormData();
            $.each(myDropzone.files,function(i,v){
                $files.append("files",v)
            })
            $files.append("token",params.token)
            $.ajax({
                data : $files,
                type : "POST",
                url : api.serverHost+api.uploadSystemPicture,
                cache : false,
                contentType : false,
                processData : false,
                success : function(res) {
                    myDropzone.removeAllFiles(true)
                    selectSystemPicture()
                    systermConfig.uploadPictureModal.modal("hide")
                }, error: function(msg) {
                    com.showAlert(com.error, "图片上传失败")
                },
            });
        })(myDropzone)
    })
    selectSystemPicture()
    function selectSystemPicture(){
        com.post(api.serverHost+api.selectSystemPicture,{},function(res){
            var $ul = pictureHtml(res.data);
            systermConfig.systemPicture.html($ul)
            $('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
        })
    }


   var  pictureHtml =(function(data){
       if(data.length<1) return false;
       var $ul = $("<ul></ul>").addClass("ace-thumbnails")
       $.each(data,function(i,v){
           var $li = $("<li></li>");
           var $a = $("<a></a>").attr("data-rel","colorbox").attr("href",config.imgServer+v.url)
           var $img = $("<img/>").attr("src",config.imgServer+v.url).attr("style","height:20em;width: 25em;").attr("alt","150x150")
           var $div = $("<div></div>").addClass("tools tools-bottom")
           var $tools = $("<a></a>").attr("href","#")
           var $i = $("<i></i>").addClass("icon-remove red")

           $li.append($a.append($img),$div.append($tools.append($i))).appendTo($ul)

           $tools.click(function(){
                com.post(api.serverHost+api.delSystemPicture,{"psid":v.psid},function(res){
                    res.data?selectSystemPicture():com.showAlert(com.error, "删除图片失败")
                })
           })
       })
       return $ul;
   })

    com.post(api.serverHost+api.querySystemConfig,{},function(res){
        $.each(res.data,function(i,v){
            $("#"+v.key).val(v.value);
        })
    })

    function updataSystem(){
        if(com.validate()){
            return;
        }
        var options = new Array();
        options.push({key:"consumerHotline", value:$("#consumerHotline").val()})
        $.ajax({
            data : JSON.stringify(options),
            type : "POST",
            contentType:"application/json; charset=utf-8",
            url : api.serverHost+api.updateSystemConfig,
            success : function(res) {
                com.showAlert(com.info, "操作成功");
            }, error: function(msg) {
                com.showAlert(com.error, "修改失败")
            },
        });
    }

})