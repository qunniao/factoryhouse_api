
/**=============================================================
 * 标签组件（基于jQuery和typeahead），在原有ace 自带的tag.js基础上扩展了
 * 动态匹配：1.从后台获取list，2.从后台获取map
 * =============================================================
 * 使用方法
 * 1.动态key val
    var tag_input = $('#form-field-tags');
    tag_input.tag({
        placeholder:tag_input.attr('placeholder'),
        isStaticResource : false,
        flsource: {
            url : api.personel.queryUserForNameByDuty,
            postData : { "duty" : "设计师" },
            resultList : "autoFillKeyValue",
            keyPath : "key",
            valuePath : "value",
            isResultTypeMap : true, //是否是key - value 形式的列表
        },
    });

 * 2.动态 list
    var tag_input1 = $('#form-field-tags1');
    tag_input1.tag({
        placeholder:tag_input.attr('placeholder'),
        isStaticResource : false,
        flsource: {
        url : api.personel.queryUserForNameByDuty,
            postData : { "duty" : "设计师" },
            resultList : "autoFillList",
            isResultTypeMap : false, //是否是key - value 形式的列表
        },
    });

 * 3.静态 list
     var tag_input2 = $('#form-field-tags2');
     tag_input2.tag({
        placeholder:tag_input.attr('placeholder'),
        isStaticResource : true,
        source: ace.variable_US_STATES,
    });
 **/

!
    function(c) {
        var b = function(e, d) {
            this.element = c(e);
            this.options = c.extend(true, {}, c.fn.tag.defaults, d);
            this.values = c.grep(c.map(this.element.val().split(","), c.trim), function(f) {
                return f.length > 0
            });
            this.show()
        };
        b.prototype = {
            constructor: b,
            show: function() {
                var d = this;
                d.element.parent().prepend(d.element.detach().hide());
                d.element.wrap(c('<div class="tags">')).parent().on("click", function() {
                    d.input.focus()
                });
                if (d.values.length) {
                    c.each(d.values, function() {
                        d.createBadge(this)
                    })
                }
                d.input = c('<input type="text">').attr("placeholder", d.options.placeholder).insertAfter(d.element).on("focus", function() {
                    d.element.parent().addClass("tags-hover")
                }).on("blur", function() {
                    if (!d.skip) {
                        d.process();
                        d.element.parent().removeClass("tags-hover");
                        d.element.siblings(".tag").removeClass("tag-important")
                    }
                    d.skip = false
                }).on("keydown", function(g) {
                    if (g.keyCode == 188 || g.keyCode == 13 || g.keyCode == 9) {
                        if (c.trim(c(this).val()) && (!d.element.siblings(".typeahead").length || d.element.siblings(".typeahead").is(":hidden"))) {
                            if (g.keyCode != 9) {
                                g.preventDefault()
                            }
                            d.process()
                        } else {
                            if (g.keyCode == 188) {
                                if (!d.element.siblings(".typeahead").length || d.element.siblings(".typeahead").is(":hidden")) {
                                    g.preventDefault()
                                } else {
                                    d.input.data("typeahead").select();
                                    g.stopPropagation();
                                    g.preventDefault()
                                }
                            }
                        }
                    } else {
                        if (!c.trim(c(this).val()) && g.keyCode == 8) {
                            var f = d.element.siblings(".tag").length;
                            if (f) {
                                var e = d.element.siblings(".tag:eq(" + (f - 1) + ")");
                                if (e.hasClass("tag-important")) {
                                    d.remove(f - 1)
                                } else {
                                    e.addClass("tag-important")
                                }
                            }
                        } else {
                            d.element.siblings(".tag").removeClass("tag-important")
                        }
                    }
                }).typeahead( c.extend(

                    d.options.isStaticResource ? {
                        source: d.options.source,
                        matcher: function(e) {
                            return ~e.toLowerCase().indexOf(this.query.toLowerCase()) && (d.inValues(e) == -1 || d.options.allowDuplicates)
                        },
                        updater: c.proxy(d.add, d)
                    } : {
                        /*source: d.options.source,
                         matcher: function(e) {
                         return ~e.toLowerCase().indexOf(this.query.toLowerCase()) && (d.inValues(e) == -1 || d.options.allowDuplicates)
                         },
                         updater: c.proxy(d.add, d)*/

                        url: d.options.flsource.url,
                        list: [],
                        postData: d.options.flsource.postData,
                        source: function (query, process) {
                            var options = $.extend({pinyinName: query}, this.options.postData);
                            com.post(this.options.url, options, $.proxy(this.options.callback, this));
                        }, matcher: function (item) {
                            return true;
                        }, callback: function(data){
                            this.options.list = data[d.options.flsource.resultList];
                            d.options.flsource.isResultTypeMap ? this.flprocess(this.options.list,d.options.flsource.keyPath, d.options.flsource.valuePath) : this.process(this.options.list);

                        },
                        isResultTypeMap : d.options.flsource.isResultTypeMap ? d.options.flsource.isResultTypeMap : false,

                    },
                    d.options.isStaticResource ? {} : d.options.flsource.isResultTypeMap ?{
                        flupdater: function(k,v){
                            c.proxy(d.fladd(k,v), d)
                        }
                    } : {
                        updater: c.proxy(d.add, d)
                    }
                ));
                c(d.input.data("typeahead").$menu).on("mousedown", function() {
                    d.skip = true
                });
                this.element.trigger("shown")
            },
            inValues: function(e) {
                if (this.options.caseInsensitive) {
                    var d = -1;
                    c.each(this.values, function(f, g) {
                        if (g.toLowerCase() == e.toLowerCase()) {
                            d = f;
                            return false
                        }
                    });
                    return d
                } else {
                    return c.inArray(e, this.values)
                }
            },
            createBadge: function(e) {
                var d = this;
                c("<span/>", {
                    "class": "tag"
                }).text(e).append(c('<button type="button" class="close">&times;</button>').on("click", function() {
                    d.remove(d.element.siblings(".tag").index(c(this).closest(".tag")))
                })).insertBefore(d.element)
            },
            add: function(g) {
                var f = this;
                if (!f.options.allowDuplicates) {
                    var e = f.inValues(g);
                    if (e != -1) {
                        var d = f.element.siblings(".tag:eq(" + e + ")");
                        d.addClass("tag-warning");
                        setTimeout(function() {
                            c(d).removeClass("tag-warning")
                        }, 500);
                        return
                    }
                }
                this.values.push(g);
                this.createBadge(g);
                this.element.val(this.values.join(", "));
                this.element.trigger("added", [g])
            },
            fladd: function(k,v) {
                var f = this;
                if (!f.options.allowDuplicates) {
                    var e = f.inValues(v);
                    if (e != -1) {
                        var d = f.element.siblings(".tag:eq(" + e + ")");
                        d.addClass("tag-warning");
                        setTimeout(function() {
                            c(d).removeClass("tag-warning")
                        }, 500);
                        return
                    }
                }
                this.values.push(k);
                this.createBadge(v);
                this.element.val(this.values.join(", "));
                this.element.trigger("added", k)
            },
            remove: function(d) {
                if (d >= 0) {
                    var e = this.values.splice(d, 1);
                    this.element.siblings(".tag:eq(" + d + ")").remove();
                    this.element.val(this.values.join(", "));
                    this.element.trigger("removed", [e])
                }
            },
            process: function() {
                var d = c.grep(c.map(this.input.val().split(","), c.trim), function(f) {
                        return f.length > 0
                    }),
                    e = this;
                c.each(d, function() {
                    e.add(this)
                });
                this.input.val("")
            },
            skip: false
        };
        var a = c.fn.tag;
        c.fn.tag = function(d) {
            return this.each(function() {
                var f = c(this),
                    g = f.data("tag"),
                    e = typeof d == "object" && d;
                if (!g) {
                    f.data("tag", (g = new b(this, e)))
                }
                if (typeof d == "string") {
                    g[d]()
                }
            })
        };
        c.fn.tag.defaults = {
            allowDuplicates: false,
            caseInsensitive: true,
            placeholder: "",
            source: []
        };
        c.fn.tag.Constructor = b;
        c.fn.tag.noConflict = function() {
            c.fn.tag = a;
            return this
        };
        c(window).on("load", function() {
            c('[data-provide="tag"]').each(function() {
                var d = c(this);
                if (d.data("tag")) {
                    return
                }
                d.tag(d.data())
            })
        })
    }(window.jQuery);