$(function () {
    Condition = Condition();
    var asicConf = {
        $Module: $(".nav,.nav-list"),
        $PageContent: $("#displayLoad-content"),
        $ModulePath:$("#breadcrumb"),
        $ModulePathList:{}
    }

    com.post(api.serverHost+api.adminLoginByPassWord,{"accountNumber":"admin","userPassword":"123"},function (res) {
        params.token = res.data.token;
    },"",false);
    com.post(api.serverHost+api.findAllModule, {}, function (res) {
        displayFunc.init(res.data)
    });
    var displayFunc = (function () {

        var firstModule = function (index, data) {
            var v = data
            var i = index;
            if (v.level == 1) {
                var html = renderModule(v.level, data)
                asicConf.$Module.append(html)
            } else {
                var $fatherLi = asicConf.$Module.find("[level=" + (v.level - 1) + "][moduleId=" + v.fatherModuleId + "]");
                var $NewUl = $fatherLi.children("ul").last();
                var html = renderModule(i, v)
                $NewUl.append(html)
                if ($fatherLi.children("a").first().children("b").length == 0) {
                    $fatherLi.children("a").first().append('<b class="arrow icon-angle-down"></b>')
                }
                if(!$fatherLi.children("a").hasClass("dropdown-toggle")){
                    $fatherLi.children("a").addClass("dropdown-toggle")
                }
            }
        }

        var renderModule = function (level, v) {
            var $li = $("<li></li>").attr("level", v.level).attr("moduleId", v.mid).attr("moduleName",v.name);
            var $i = $("<i></i>").addClass(v.icon);
            var $span = $("<span></span>").text(v.name)
            var $ul = $("<ul></ul>").addClass("submenu").attr("style","display:none;");
            var $a = $("<a></a>").attr("href", v.url).on("click", function (e) {
                var html;
                //$li.siblings().removeClass("active open").find('li').removeClass("active open");
                $li.siblings(".active").removeClass('active open').find("ul").attr("style","display:none");
                $li.siblings().find(".active").removeClass("active");
                $li.addClass("active");
                if (!(v.url == null || v.url == '' || v.url == "#")) {
                    asicConf.$PageContent.load(config.address + v.url);
                }
                asicConf.$ModulePathList[v.level] = "<li moduleId='"+v.mid+"'><i class='"+(v.level==1?"slight-right-shift "+v.icon:"")+"'></i><a href=\"#\">"+v.name+"</a></li>";
               for(var i=1;i<=v.level;i++){
                    if(html == null){
                        html =asicConf.$ModulePathList[i]
                    }else{
                        html+=asicConf.$ModulePathList[i]
                    }
               }
                asicConf.$ModulePath.html(html)
                e.preventDefault();
            })
            level == 1? $span.addClass("menu-text"):""
            return $li.append($a.append($i,$span),$ul);
        }


        return {
            init: function (data) {
                $.each(data, function (i, v) {
                    firstModule(i, v)
                })
            }
        }
    })()

    $('body').on('hidden.bs.modal', '.modal', function () {
        if($(this).parents(".modal").length>0) return false;
        com.clearValidator();
        $(this).find("input").val("")
    });
})