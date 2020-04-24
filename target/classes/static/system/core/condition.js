/**
 * 存放查询条件的js,定义全项目的查询条件
 */

var Condition = function () {
    var _condRules = {
        /*c_drawLineStatus: {
            label: "状态",
            type: "select",
            submitConfig: {
                name: "status",
                validate: null
            },
            dataSource: {
                url: null,
                postData: null,
                listPath: [
                    {"key": "申请放线", "value": "申请放线"},
                    {"key": "放线失败", "value": "放线失败"},
                    {"key": "放线成功", "value": "放线成功"}
                ],
                keyPath: "key",
                valuePath: "value"
            }
        },*/
        title: {
            label: "标题",
            type: "text",
            submitConfig: {
                name: "title",
                validate: null
            }
        },

        contactPhone: {
            label: "委托人电话",
            type: "text",
            submitConfig: {
                name: "contactPhone",
                validate: null
            }
        },

        userPhone: {
            label: "手机号",
            type: "text",
            submitConfig: {
                name: "userPhone",
                validate: null
            }
        },
        user_userPhone: {
            label: "手机号",
            type: "text",
            submitConfig: {
                name: "user.userPhone",
                validate: null
            }
        },

    };
    return {
        getRuleByCondName: function (condName) {
            return _condRules[condName];
        }
    }
};
