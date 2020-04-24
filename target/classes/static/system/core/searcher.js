/**
 * Created by Administrator on 2016/4/7.
 * eval()方法使用,通过方法名字符串调用方法
 */


var Searcher = function(){

    /*各种设置*/
    var basicConf = {
        condCodeArrs : null,
        url : null,
        historyName : null,//保留搜索条件的参数名
        $searcher : null,
        $pager : null,
        displayFunc : null,
        extraParams : {},//额外的查询参数
        noPagingFlag : false,
        resultListName : null,
        resultListId: null // 显示内容标签id
    };

    /*查询条件各种操作*/
    var searchCond = {
        /**
         * 根据查询条件的编号列表初始化页面查询条件区域
         * params:
         *      condCodeArrs:数组(查询条编号的数组)
         *      searchId:放查询条件的div的id
         *      url:查询调用的URL
         *      displayFunc:向后台请求的回调函数
         * */
        renderConds : function(){
            var that = this;
            var $searchBtn = $('<button></button>').addClass('btn btn-xs btn-info btn-search').html('查询');
            var $cleanBtn = $('<button></button>').addClass('btn btn-xs btn-danger btn-clean').css('margin', '0px 3px').html('重置');

            basicConf.$searcher.empty();

            basicConf.condCodeArrs && basicConf.condCodeArrs.length &&(
                $.each(basicConf.condCodeArrs,function(i,val){
                    var condRule = Condition.getRuleByCondName(val);
                    var $item = null;
                    condRule && ($item = that.createCondItem(val,condRule));
                    $item && $item.appendTo(basicConf.$searcher);
                }),
                    basicConf.$searcher.append($searchBtn),
                    basicConf.$searcher.append($cleanBtn),
                    $searchBtn.click(function(){
                        if(basicConf.noPagingFlag){
                            _params = getParams();
                            if(_params){
                                queryData(_params);
                            }

                        }else{
                            basicConf.$pager.hide();

                            paging.nowPage["pageNum"] = 0;
                            paging.nowPostParams = null;
                            var _params = paging.getParams();
                            if(_params){
                                _params["pageNum"] += 1;
                                paging.getDataByPage( _params);
                            }
                        }
                    }),
                    $cleanBtn.click(function(){
                        basicConf.$searcher.find('.inline').each(function(i, v){
                            $($(v).children()[1]) && $($(v).children()[1]).removeAttr('data-typeahead-key').val('');
                        });
                    })
            );
        },
        /**
         * 根据查询条件名和查询条件的规则创建一个查询条件的标签并返回
         * params:
         *    condName:查询条件编号
         *    condRule:
         * return:
         *      $item:一条查询条件的标签
         * */
        createCondItem : function(condCode, condRule){

            var $item = $('<span></span>').addClass('inline');
            var $label = $('<label></label>');

            var $showItem,$valueItem;

            var _rlabel = condRule['label'];//查询条件对应的中文提示
            var _rtype = condRule['type'];//标签类型

            var _rname = condRule['submitConfig']['name'];//向后台提交提交时的参数名
            //var _rop = condRule['submitConfig']['op'];//提交给后台时的查询条件:等于或者包含
            if(condRule['dataSource']){
                var _rurl = condRule['dataSource']['url'];//autofill和select标签获取可选值的接口
                var _rkey = condRule['dataSource']["keyPath"];//autofill和select取值时调用接口后处理返回值时用来作为提交项的参数名
                var _rvalue = condRule['dataSource']["valuePath"];//autofill和select取值时调用接口后处理返回值时用来显示的参数名
                var _rpostParam = condRule['dataSource']["postData"];//autofill和select取值时调用接口时传的参数
                var _rresult = condRule['dataSource']["listPath"];//autofill和select取值时调用接口后取值用的eg:data.deptList
            }

            var setOption = function(data){
                data && data.length &&(
                    $.each(data,function(i,v){
                        if("object" !== typeof v){
                            $('<option></option>').attr("value" , v).html(v).appendTo($showItem);
                        }else{
                            $('<option></option>').attr("value" , v[_rkey]).html(v[_rvalue]).appendTo($showItem);
                        }

                    })
                );
            };

            $label.text(_rlabel + ':').appendTo($item);

            if(_rtype=="text"){
                $showItem = $('<input>').attr({
                    "type" : "text",
//                    "placeholder" : "请输入" + _rlabel,
                    "name" : condCode,
                    "data-rqs" : _rname,
                    //"data-op" : _rop,
                    "data-type":_rtype
                }).appendTo($item);
                // $line.addClass("text");
            }

            if(_rtype == "dateRangePicker"){
                $showItem = $('<input>').attr({
                    "type": "text",
//                    "placeholder": "请输入" + _rlabel,
                    "name": condCode,
                    //"data-op": _rop,
                    "data-rqs": _rname,
                    "readonly": true,
                    "data-type":_rtype
                }).appendTo($item);

                $showItem.daterangepicker(com.dateramgepickerdefault).prev().on(ace.click_event, function(){
                    $(this).next().focus();
                });
            }

            if(_rtype == "datePicker"){
                $showItem = $('<input>').attr({
                    "type": "text",
//                    "placeholder": "请输入" + _rlabel,
                    "name": condCode,
                    //"data-op": _rop,
                    "data-rqs": _rname,
                    "data-date-format": "dd-mm-yyyy",
                    "readonly": true,
                    "data-type":_rtype
                }).appendTo($item);

                $showItem.datepicker({format:'yyyy-mm-dd' , autoclose:true});
            }

            if(_rtype=="autofill"){
                $showItem = $('<input>').attr({
                    "type": "text",
//                    "placeholder": "请输入" + _rlabel,
                    "name": condCode,
                    "autocomplete": "off",
                    //"data-op": _rop,
                    "data-rqs": _rname,
                    "data-type":_rtype
                }).appendTo($item);

                $showItem.typeahead({
                    url: _rurl,
                    list: [],
                    postData: _rpostParam,
                    source: function (query, process) {
                        var options = $.extend({pinyinName: query}, this.options.postData);
                        com.post(this.options.url, options, $.proxy(this.options.callback, this));
                    }, matcher: function (item) {
                        return true;
                    }, callback: function(data){
                        this.options.list = data[_rresult];
                        this.flprocess(this.options.list,[_rkey],[_rvalue]);
                    },
                    isResultTypeMap: true //必填值为true,获取key,value的标识,
                    , flupdater: function (k, v) {//下拉列表选中时的一些处理,必填对选中项的key和value做处理
                        if(v.indexOf('-')!=-1){
                            v = v.split('-')[0]
                        }
                        return v;
                    }
                });
            }

            if(_rtype == "select"){

                $showItem = $('<select></select>').attr({
                    "name" : condCode,
                    "data-rqs" : _rname,
                    //"data-op" : _rop,
                    "data-type":_rtype
                }).append('<option value="">全部</option>').appendTo($item);

                if(_rurl){
                    com.post(_rurl, _rpostParam, function(result){
                        var data = result[_rresult];
                        setOption(data);
                    }, null, false);

                }else{
                    $.each(_rresult, function(i, v){
                        if("object" !== typeof v){
                            $('<option></option>').attr("value" , v).html(v).appendTo($showItem);
                        }else{
                            $('<option></option>').attr("value" , v[_rkey]).html(v[_rvalue]).appendTo($showItem);
                        }
                    });
                }
            }

            if(_rtype == "selectCaching") {
                $showItem = $('<select></select>').attr({
                    "name": condCode,
                    "data-rqs": _rname,
                    //"data-op": _rop,
                    "data-type": _rtype
                }).append('<option value="">全部</option>').appendTo($item);

                var data = {};
                if (_rurl) {
                    if (!jQuery.isEmptyObject(params.selectCaching[condCode])) {
                        data = params.selectCaching[condCode];
                    } else {
                        com.post(_rurl, _rpostParam, function (result) {
                            data = result[_rresult];
                            params.selectCaching[condCode] = data;
                        }, null, false);

                    }
                }
                setOption(data);
            }
            return $item;
        }
    };

    /**分页*/
    var paging = {
        nowPage : {
            "pageNum" : 1,
            "pageSize" : 10,
            "totalRecords" : 0,
            "totalPage" : 0
        },
        nowPostParams : null,
        /*searchId:存放查询条件的div的id*/
        getParams : function(){
            var _params = {};
            var _condArr = basicConf.condCodeArrs;

            if(this.nowPostParams){
                return this.nowPostParams;
            }

            _condArr && _condArr.length &&(
                $.each(_condArr,function(i,val){
                    var $input = $('[name=' + val + ']', basicConf.$searcher);
                    var _inputType = $input.attr("data-type");
                    var _postName = $input.attr("data-rqs");
                    var _rule = Condition.getRuleByCondName(val);
                    var _reg = _rule.submitConfig.validate;
                    var _inputValue = $input.val();
                    var _inputKey = _inputValue!=""?$input.attr("data-typeahead-key"):$input.removeAttr("data-typeahead-key");
                    //var _op = $('[name=' + val + ']', basicConf.$searcher).attr('data-op');
                    if(_inputValue){

                        if(_reg){
                            if(!(_reg.reg).test(_inputValue)){
                                $input.addClass('tooltip-info').attr({
                                    "data-rel" : "tooltip",
                                    "data-placement" : "top",
                                    "data-original-title" : _reg.msg
                                }).tooltip('show');
                                _params = null;
                                return false;

                            }else{
                                $input.removeClass("tooltip-info").removeAttr("data-rel").removeAttr("data-placement").removeAttr("data-original-title");
                                //_params['filters[' + _count + '].data'] = _inputType=="autofill"?_inputKey:_inputValue;
                                //_params['filters[' + _count + '].field'] = _postName;
                                //_params['filters[' + _count + '].op'] = _op;
                                //_params['filters[' + _count + '].inputValue'] = _inputValue;
                                //_count +=1;
                                _params[_postName] = _inputValue;
                            }
                        }else{
                            $input.removeClass("tooltip-info").removeAttr("data-rel").removeAttr("data-placement").removeAttr("data-original-title");
                            // _params['filters[' + _count + '].data'] = _inputType=="autofill"?_inputKey:_inputValue;
                            // _params['filters[' + _count + '].field'] = _postName;
                            // _params['filters[' + _count + '].op'] = _op;
                            // _params['filters[' + _count + '].inputValue'] = _inputValue;
                            // _count +=1;
                            _params[_postName] = _inputValue;
                        }
                    }
                })
            );

            if(_params){
                _params["pageNum"] = this.nowPage["pageNum"];
                _params["pageSize"] = this.nowPage["pageSize"];
                _params = $.extend(true,{},_params,basicConf.extraParams);
            }

            return _params;
        },
        getDataByPage : function(_params){
            com.post(basicConf.url, _params, $.proxy(function(result){
                this.nowPage["pageNum"] = result.data.number+1;
                this.nowPage["pageSize"] = result.data.size;
                this.nowPage["totalRecords"] = result.data.totalElements;
                this.nowPage["totalPage"] = result.data.totalPages;
                this.showPagenation((result.data.number+1), result.data.size, result.data.totalPages, result.data.totalElements);
                //params全局变量,给全局变量赋当前查询条件的值
                params[basicConf.historyName] = _params;

                (result.data.content && result.data.content.length > 0) ? basicConf.displayFunc(result.data) : emptyData();
            }, this), null, false);
        },
        showPagenation: function(page, rows, totalPage, totalRecords){

            $('li[name="prev"]', basicConf.$pager).attr('class', page == 1 || totalPage == 0 ? 'previous disabled' : 'previous');
            $('li[name="next"]', basicConf.$pager).attr('class', page == totalPage || totalPage == 0 ? 'next disabled' : 'next');

            $('span[name="nowPage"]', basicConf.$pager).html(page);
            $('span[name="rows"]', basicConf.$pager).html(rows);
            $('span[name="totalPage"]', basicConf.$pager).html(totalPage);
            $('span[name="totalRecords"]', basicConf.$pager).html(totalRecords);

            $(basicConf.$pager).show();
        },

        /*给分页的上一页下一页绑定点击事件*/
        init : function(){
            var _pagenation = this;

            //增加分页操作元素
            basicConf.$pager.html('');

            basicConf.$cusBtns && $.each(basicConf.$cusBtns, function(i, v){
                v.appendTo(basicConf.$pager);
            });

            var $infodiv = $('<div></div>').addClass('pager-info text-center').appendTo(basicConf.$pager);
            var $di = $('<span></span>').text('第').appendTo($infodiv);
            var $nowPage = $('<span></span>').addClass('bigger-120 blue').attr('name', 'nowPage').appendTo($infodiv);
            var $di = $('<span></span>').text('页, 共').appendTo($infodiv);
            var $totalPage = $('<span></span>').addClass('bigger-120 blue').attr('name', 'totalPage').appendTo($infodiv);
            var $di = $('<span></span>').text('页, 每页显示').appendTo($infodiv);
            var $rows = $('<span></span>').addClass('bigger-120 blue').attr('name', 'rows').appendTo($infodiv);
            var $di = $('<span></span>').text('条, 共').appendTo($infodiv);
            var $totalRecords = $('<span></span>').addClass('bigger-120 blue').attr('name', 'totalRecords').appendTo($infodiv);
            var $di = $('<span></span>').text('条, 前往').appendTo($infodiv);
            var $input = $('<input></input>').addClass('fl-page-input').attr('type', 'text').css('font-size', '10px').attr('id', 'pginput').appendTo($infodiv);
            var $di = $('<span></span>').text('页, 条数').appendTo($infodiv);
            var $select = $('<select></select>').addClass('fl-page-select').bindData([{'key': '10', 'value': '10'}, {'key': '30', 'value': '30'}], 'key', 'value').appendTo($infodiv);

            var $ul = $('<ul></ul>').addClass('pager pull-right').appendTo($infodiv);
            var $prevli = $('<li></li>').addClass('previous').attr('name', 'prev').appendTo($ul);
            var $preva = $('<a></a>').attr('href', 'javascript:void(0);').text('< 上一页').appendTo($prevli);
            var $nextli = $('<li></li>').addClass('next').attr('name', 'next').appendTo($ul);
            var $nexta = $('<a></a>').attr('href', 'javascript:void(0);').text('下一页 >').appendTo($nextli);

            //页面初始化的时候隐藏分页
            basicConf.$pager.hide();

            //上一页下一页绑定
            $prevli.click(function(){
                var _params;
                _pagenation.nowPage["pageNum"] > 1 && (
                    _params = _pagenation.getParams(),
                    _params && (_params["pageNum"] -= 1, _pagenation.getDataByPage(_params))
                );
            });
            $nextli.click(function(){
                var _params;
                _pagenation.nowPage["pageNum"] < _pagenation.nowPage["totalPage"] && (
                    _params = _pagenation.getParams(),
                    _params && (_params["pageNum"] += 1, _pagenation.getDataByPage(_params))
                );
            });
            $input.bind('keypress',function(event){

                var _p = $(this).val();

                if(event.keyCode == "13" && /\d+/.test(_p)){
                    var _params;

                    (_p <= _pagenation.nowPage["totalPage"] && _p >= 1) && (
                        _params = _pagenation.getParams(),
                        _params && (_params["pageNum"] = parseInt(_p), _pagenation.getDataByPage(_params))
                    );
                }
            });
            $select.change(function(){
                _params = _pagenation.getParams(),
                _params && (_params["pageSize"] = $(this).val(), _pagenation.getDataByPage(_params))
            });
        }
    };

    var emptyData = function(){
        var $resultList = $('#' + basicConf.resultListId).html('');
        var $zone = $('<div></div>').addClass('dropzone dz-clickable center').css("cursor","default").css('margin', '20px').css('border', '0px').appendTo($resultList);
        var $span = $('<h1></h1>').text('无可用数据提供').appendTo($zone);
    };

    var queryData = function(_params){
        com.post(basicConf.url, _params, $.proxy(function(result){
            //params全局变量,给全局变量赋当前查询条件的值
            params[basicConf.historyName] = _params;

            (result[basicConf.resultListName] && result[basicConf.resultListName].length > 0) ? basicConf.displayFunc(result[basicConf.resultListName]) : emptyData();
        }, this), null, false);
    };

    var searchByPresentParams = function(){
        var _count = 0;
        var _params = params[basicConf.historyName] ? params[basicConf.historyName] : {};

        //给查询条件赋值
        !jQuery.isEmptyObject(_params) && $.each(_params, function(key, val){
            var _pname,_pvalue,_pkey;
            key.indexOf("field") != -1 && (
                _pname = val,
                    _pvalue = _params[key.substring(0,key.length-5) + "inputValue"],
                    _pkey = _params[key.substring(0,key.length-5) + "data"],
                    $('[data-rqs="'+ _pname +'"]',basicConf.$searcher).attr('data-typeahead-key',_pkey).val(_pvalue),
                    _count++
            );
        });

        if(!basicConf.noPagingFlag){
            jQuery.isEmptyObject(_params) && (_params["pageNum"] = paging.nowPage["pageNum"], _params["pageSize"] = paging.nowPage["pageSize"]);

            _params = $.extend({},_params,basicConf.extraParams);
            paging.nowPostParams = _params;

            $('.fl-page-input').val(_params["pageNum"]);
            $('.fl-page-select').val(_params["pageSize"]);

            paging.getDataByPage(_params);
        }else{
            _params = $.extend({},_params,basicConf.extraParams);
            queryData(_params);
        }
    };

    return{
        /**
         * 插件的初始化方法
         * params:
         *     condCodeArrs:查询条件编号的数组
         *     searchId:查询控件显示标签id
         *     url:分页查询路径
         *     displayFunc:分页查询的回调函数(画列表)
         *     pagerId:分页控件的标签id
         *     historyName:保存历史查询条件的查询条件名
         *     extraParams: 分页查询的其他查询参数{key:val}形式
         *     noPagingFlag: 不分页标识,true,标识不分页
         * */
        init : function(condCodeArrs, searchId, url, displayFunc, pagerId, historyName, extraParams, noPagingFlag, resultListName, cusBtns, resultListId){
            /*各种设置*/
            basicConf.condCodeArrs = condCodeArrs;
            basicConf.$searcher = $('#' + searchId);
            basicConf.$pager = $('#' + pagerId);
            basicConf.url = url;
            basicConf.historyName = historyName;
            basicConf.displayFunc = displayFunc;
            extraParams && typeof extraParams == "object" && (basicConf.extraParams = extraParams);
            noPagingFlag && (basicConf.noPagingFlag = noPagingFlag);
            basicConf.resultListName = resultListName;
            basicConf.resultListId = resultListId ? resultListId : "resultList";

            cusBtns && (basicConf.$cusBtns = cusBtns());

            //绑定上一页下一页事件
            !noPagingFlag && paging.init();
            //初始化查询条件
            searchCond.renderConds();

            searchByPresentParams();
        },
        /*
         * 根据当前条件查询分页信息
         * */
        doSearch : function () {
            setTimeout(function(){ searchByPresentParams() }, 500);
        }
    }
};
