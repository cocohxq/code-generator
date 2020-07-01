let copyNode, cutNode = null;
$(document).ready(function () {

    originEditNode = null;
    editor = null;
    fileEditor = null;
    // refreshuserTmpTreeList();
    initCodeArea();//模板编辑区域

    //更换配置重新加载
    $("#step_0 #addConfig").click(function () {
        let href = location.href;
        if (href.indexOf("?") != -1) {
            href = href.substring(0, href.indexOf("?"))
        }
        location.href = href + "?op=e";
    });

    //更换配置重新加载
    $("#step_0 #copyAddConfig").click(function () {
        let href = location.href;
        if (href.indexOf("?") != -1) {
            href = href.substring(0, href.indexOf("?"))
        }
        let selectedConfig = $("#selectedConfig").val();
        if (selectedConfig == "请选择") {
            $.messager.alert('error', '请选择配置');
            return;
        }
        location.href = href + "?op=e&cp=1&sc=" + selectedConfig;
    });

    //更换配置重新加载
    $("#step_0 #editConfig").click(function () {
        let href = location.href;
        if (href.indexOf("?") != -1) {
            href = href.substring(0, href.indexOf("?"))
        }
        let selectedConfig = $("#selectedConfig").val();
        if (selectedConfig == "请选择") {
            $.messager.alert('error', '请选择配置');
            return;
        }
        location.href = href + "?op=e&sc=" + selectedConfig;
    });

    //更换配置重新加载
    $("#step_0 #delConfig").click(function () {
        let selectedConfig = $("#selectedConfig").val();
        if (selectedConfig == "请选择") {
            $.messager.alert('error', '请选择配置');
            return;
        }
        let param = {};
        param.configName = selectedConfig;
        param.operation = "delete";

        //删除配置
        initData(0, true, param, function () {
            //重新加载页面
            let href = location.href;
            if (href.indexOf("?") != -1) {
                href = href.substring(0, href.indexOf("?"))
            }
            location.href = href;
        });
    });

    //更换配置重新加载
    $("#step_0 #saveConfig").click(function () {
        let param = {};
        if (!fillInputParam("#step_0", param)) {
            return;
        }
        param.dbType = $("#dbType").val();

        //判断操作
        let href = location.href;
        if (href.indexOf("cp=1") != -1) {
            param.operation = "copy";
        } else if (href.indexOf("sc=") != -1) {
            param.operation = "edit";
        } else {
            param.operation = "add";
        }

        //初始化库表信息
        initData(0, true, param, function () {
            //重新加载页面
            let href = location.href;
            if (href.indexOf("?") != -1) {
                href = href.substring(0, href.indexOf("?"))
            }
            location.href = href;
        });
    });

    //选择数据源-下一步
    $("#step_0 .next").click(function () {
        let param = {};
        param.add = false;
        let selectedConfig = $("#selectedConfig").val();
        if (selectedConfig == "请选择") {
            $.messager.alert('error', '请选择配置');
            return;
        }
        param.configName = selectedConfig;
        param.operation = "next";
        initData(0, true, param, step0Callback);
    });


    //选择表-下一步
    $("#step_5 .next").click(function () {
        let param = {};
        if (!fillInputParam("#step_5", param)) {
            return;
        }
        let tableNames = new Array();
        $("#tables option:selected").each(function () {
            tableNames.push($(this).val());
        });
        if (tableNames.length == 0) {
            $("#step_5 .errMsg").text("请选择需要的生成代码的表");
            return;
        }
        if (tableNames.length > 1) {
            $("#step_5 .errMsg").text("只支持按单表生成代码");
            return;
        }

        param.tableName = tableNames[0];
        initData(5, true, param, function (data) {
            return true;
        });
    });

    $("#step_5 .pre").click(function () {
        chooseBar(5, false);
    });

    $("#step_5 .field").click(function () {
        if ($("#columns option:selected").length == 0) {
            $.messager.alert('error', "请先选择字段");
            return false;
        }

        let existedVal = $("#" + $(this).attr("at")).val();

        let existedSet = new Set();
        if (existedVal) {
            let arr = existedVal.split(",");
            for (let i = 0; i < arr.length; i++) {
                existedSet.add(arr[i]);
            }
        }

        $("#columns option:selected").each(function () {
            if (!existedSet.has($(this).val())) {
                if (existedVal) {
                    existedVal += ",";
                }
                existedVal += $(this).val();
                $(this).attr("selected", false);
            }
        });
        $("#" + $(this).attr("at")).val(existedVal);
    });


    //编辑代码配置-下一步
    $("#step_8 .next").click(function () {
        let param = {};
        if (!fillInputParam("#step_8", param)) {
            return;
        }
        //代码位置
        param.codeLocationType = $("input[name='codeLocationType'][type=radio]:checked").val();

        //包名的位置
        let businessPackage = $("input[name='businessPackage']").val();
        let businessLocationType = $("input[name='businessLocationType'][type=radio]:checked").val();
        if (businessPackage) {
            if (businessLocationType == 1) {
                param.outBusiPack = businessPackage;
            } else {
                param.inBusiPack = businessPackage;
            }
        }
        //初始化库表信息
        initData(8, true, param, function () {
            refreshuserTmpTreeList();
            return true;
        });

    });

    $("#step_8 .pre").click(function () {
        chooseBar(8, false);
    });


    //选择模板-下一步
    $("#step_10 .next").click(function () {
        let param = {};
        param.tmps = getSelectedTreeNode();
        if (param.tmps) {
            param.operation = "next";
            initData(10, true, param, function (data) {
                return initFileTree(data);
            });
        } else {
            $.messager.alert('error', '请选择模板');
            return;
        }
    });

    $("#step_10 .pre").click(function () {
        chooseBar(10, false);
    });


    //预览导出
    $("#step_15 .next").click(function () {
        let param = {};
        initData(15, true, param, function (data) {
            return downloadZip(data);
        });
    });

    $("#step_15 .pre").click(function () {
        chooseBar(15, false);
    });

});


function getSelectedTreeNode() {
    let nodes = $('#tmps').tree("getChecked", ['checked', 'indeterminate']);
    if (nodes.length == 0) {
        return;
    }
    let tmps = new Array();
    for (let i = 0; i < nodes.length; i++) {
        if (nodes[i].attributes.modulePath && nodes[i].attributes.type == "file") {
            tmps.push(nodes[i].attributes.modulePath);
        }
    }
    return tmps;
}

/**
 * 下载文件
 * @param path
 */
function downloadZip(fileName) {
    $("#zip_download").attr("href", "/downloadZip?fileName=" + fileName);
    document.getElementById("zip_download").click();//这里只能使用js原生写法才可以触发，jquery不行
    window.location.reload();
    return true;
}

/**
 * 初始化模板树
 * @param treeNode
 * @param originEditNode
 */
function initTmps(treeNode) {
    $('#tmps').tree({
        data: treeNode,
        checkbox: true,
        lines: true,
        dnd: true,
        disableDnd: true,
        onBeforeEdit: function (node) {
            originEditNode = JSON.parse(JSON.stringify(node));//这里需要采用深度克隆，保证能记录原始的数据
        },
        onAfterEdit: function (node) {
            if ("" == node.text || null == node.text) {
                $.messager.alert('error', '名称不能为空');
                node.text = originEditNode.text;
                return;
            }
            let param = {};
            param.extParams = {};
            param.extParams.fileType = originEditNode.attributes.type;
            param.extParams.tmpModulePath = originEditNode.attributes.modulePath;
            param.extParams.newTmpModulePath = node.attributes.modulePath.replace(originEditNode.text, node.text);
            if (param.extParams.newTmpModulePath.indexOf(".java") != -1 && param.extParams.newTmpModulePath.indexOf("${groupId}") == -1) {
                $.messager.alert('error', 'java文件只能放在src/main/java目录下');
                node.text = originEditNode.text;
                return;
            }
            param.operation = "editTemplateName";
            initData(10, false, param, function (data) {
                if (data == 1) {
                    loadTmpTree();
                    originEditNode = null;
                } else {
                    $.messager.alert('error', '更新模板名称失败,找不到模板');
                }
            });
        },
        onCheck: function (node, checked) {
            if (originEditNode) {
                $('#tmps').tree('endEdit', originEditNode.target);
            }
        },
        onContextMenu: function (e, node) {
            if (originEditNode) {
                $('#tmps').tree('endEdit', originEditNode.target);
            }
            e.preventDefault();
            // select the node
            $('#tmps').tree('select', node.target);
            let id = "#menuLevel_" + node.attributes.memuLevel
            $(id).menu('show', {
                left: e.pageX,
                top: e.pageY
            });
        },
        onSelect: function (node) {
            if (originEditNode) {
                $('#tmps').tree('endEdit', originEditNode.target);
            }
            if (node.attributes.type == "file") {
                let node = $('#tmps').tree('getSelected');
                let param = {};
                param.extParams = {};
                param.extParams.modulePath = node.attributes.modulePath;
                if (node.attributes.manual) {
                    param.extParams.manual = node.attributes.manual;
                }
                param.extParams.fileType = 0;
                param.operation = "loadFile";
                initData(10, false, param, function (data) {
                    editor.setValue(data);
                });
            } else {
                editor.setValue("");
            }
        }
    });
}

/**
 * 初始化文件树
 * @param treeNode
 */
function initFileTree(treeNode) {
    $('#file_tree').tree({
        data: treeNode,
        lines: true,
        dnd: true,
        disableDnd: true,
        onSelect: function (node) {
            if (node.attributes.type == "file") {
                let node = $('#file_tree').tree('getSelected');
                let param = {};
                param.extParams = {};
                param.extParams.modulePath = node.attributes.modulePath;
                param.extParams.fileType = 1;
                param.operation = "loadFile";
                initData(15, false, param, function (data) {
                    fileEditor.setValue(data);
                });
            }
        },
    });
    return true;
}

/**
 * 初始化代码显示区域
 */
function initCodeArea() {
    editor = CodeMirror.fromTextArea(document.getElementById("tmpContent"), {
        mode: "application/x-ejs",
        indentUnit: 4,
        indentWithTabs: true,
        mode: "text/x-java", //实现Java代码高亮
        mode: "text/x-java",
        lineNumbers: true,	//显示行号
        theme: "dracula",	//设置主题
        lineWrapping: true,	//代码折叠
        foldGutter: true,
        gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
        matchBrackets: true,	//括号匹配
        extraKeys: {
            "Ctrl-Space": function (cm) {
                CodeMirror.simpleHint(cm, CodeMirror.javascriptHint);
            }
        }
    });

    editor.setSize('1000px', '950px');

    fileEditor = CodeMirror.fromTextArea(document.getElementById("fileContent"), {
        mode: "application/x-ejs",
        indentUnit: 4,
        indentWithTabs: true,
        mode: "text/x-java", //实现Java代码高亮
        mode: "text/x-java",
        lineNumbers: true,	//显示行号
        theme: "dracula",	//设置主题
        lineWrapping: true,	//代码折叠
        foldGutter: true,
        gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
        matchBrackets: true,	//括号匹配
        extraKeys: {
            "Ctrl-Space": function (cm) {
                CodeMirror.simpleHint(cm, CodeMirror.javascriptHint);
            }
        }
    });

    fileEditor.setSize('800px', '950px');
}

/**
 * 树节点重命名
 */
function edit() {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能编辑，请提交到副本编辑");
        return;
    }
    var node = $('#tmps').tree('getSelected');
    $('#tmps').tree('beginEdit', node.target);
}

/**
 * 添加模板节点
 * @param type
 */
function append(type) {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能编辑，请提交到副本编辑");
        return;
    }
    let node = $('#tmps').tree('getSelected');
    let param = {};
    param.extParams = {};
    if (type == 1) {
        param.extParams.tmpModulePath = node.attributes.modulePath + "/new_dir";
    } else {
        if (node.attributes.modulePath.indexOf("${groupId}") == -1) {
            param.extParams.tmpModulePath = node.attributes.modulePath + "/${tableCamelNameMax}_NEW.xml.ftl";
        } else {
            param.extParams.tmpModulePath = node.attributes.modulePath + "/${tableCamelNameMax}_NEW.java.ftl";
        }
    }
    param.extParams.fileType = type;

    param.operation = "addPath";
    initData(10, false, param, function (data) {
        if (data == 1) {
            loadTmpTree();
            let nodes = $("#tmps").tree("getChildren", node.target);
            for (let i = 0; i < nodes.length; i++) {
                if (nodes[i].attributes.modulePath == param.extParams.tmpModulePath) {
                    $("#tmps").tree("select", nodes[i].target);//选中刚新增的项
                    break;
                }
            }
        } else {
            $.messager.alert('error', '同名文件已存在');
            return;
        }
    });
}


/**
 * 异步请求
 * @param url
 * @param param
 * @param sucCallback
 */
function ajaxLoad(url, param, sucCallback) {
    $.ajax({
        type: 'POST',
        url: url,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(param),
        async: false,
        dataType: "json",
        success: sucCallback,
        error: function (e) {
            console.log(e);
        }
    });
}

/**
 * step0下一步回调函数
 * @param data
 */
function step0Callback(data) {
    if (data && data) {
        let tables = data.tableList;
        if (tables.length > 0) {
            $("#tables option").remove();
            $("#columns option").remove();
        }
        for (let i = 0; i < tables.length; i++) {
            let tableName = tables[i].table.tableName;
            let tableComment = tables[i].table.comment;
            if (!tableComment) {
                tableComment = "";
            } else {
                tableComment = "&nbsp;&nbsp;&nbsp;&nbsp;" + tableComment;
            }
            $("#tables").append("<option value=" + tableName + ">" + tableName + tableComment + "</option>");
        }
        $("#tables option").click(function () {
            let tableName = $(this).val();
            for (let i = 0; i < tables.length; i++) {
                if (tables[i].table.tableName == tableName) {
                    let fields = tables[i].fields;
                    $("#columns option").remove();
                    for (let j = 0; j < fields.length; j++) {
                        let columnName = fields[j].column.columnName;
                        let columnComment = fields[j].column.comment;
                        if (!columnComment) {
                            columnComment = "";
                        } else {
                            columnComment = "&nbsp;&nbsp;&nbsp;&nbsp;" + columnComment;
                        }
                        $("#columns").append("<option value='" + columnName + "'>" + columnName + columnComment + "</option>");
                    }
                }
            }
        });
        $("#tables").focus();
        return true;
    } else {
        $.messager.alert('error', '数据库表数据初始化失败，请检查配置');
        return false;
    }

}

/**
 * 下一步的请求
 * @param curStep
 * @param next
 * @param param
 * @param succ_callback
 */
function initData(curStep, next, param, succ_callback) {
    let result = true;
    let stepId = "#step_" + curStep;
    param.stepType = $(stepId).attr("type");//根据step找到stepType
    $.ajax({
        type: 'POST',
        url: '/initData',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(param),
        async: false,
        dataType: "json",
        success: function (data) {
            if (data.error && data.error.length > 0) {
                let err = data.error.join("<br>");
                $.messager.alert('error', err);
                return;
            }
            result = succ_callback(data.stepResult);
            if (result && next) {
                chooseBar(curStep, next);
            }
        },
        error: function (e) {
            console.log(e);
        }
    });
    return result;
}


/**
 *
 * 导航条切换
 */
function chooseBar(curStep, next) {
    $("#bars .bar").removeClass("cur");
    let stepId = "#step_" + curStep;
    $(stepId).hide();
    let barId = "#bar_" + $(stepId).attr("type");
    let barIndex = $("#bars .bar").index($(barId));
    if (next) {
        $(stepId).next().show();//内容切换
        $("#bars .bar").eq(barIndex + 1).addClass("cur");//导航条切换
    } else {
        $(stepId).prev().show();
        $("#bars .bar").eq(barIndex - 1).addClass("cur");
    }
}

/**
 * 更新用户模板列表
 */
function refreshuserTmpTreeList() {

    let param = {};
    param.operation = "refreshUserTmpTreeList";
    initData(10, false, param, function (data) {
        if (data) {
            $("#userTmpTreeList option").remove();
            for (let i = 0; i < data.length; i++) {
                if (data[i] == "default_tmps") {
                    $("#userTmpTreeList").append('<option class="input-large" value="' + data[i] + '" selected>' + data[i] + '</option>');
                } else {
                    $("#userTmpTreeList").append('<option class="input-large" value="' + data[i] + '">' + data[i] + '</option>');
                }
                loadTmpTree();
            }
        } else {
            $.messager.alert('info', '没有找到用户模板');
        }
    });
}

/**
 * 加载用户模板树
 * @param userTmpPath
 */
function loadTmpTree() {
    let tmpTreeName = $("#userTmpTreeList option:selected").val();
    let param = {};
    param.extParams = {};
    param.operation = "loadTmpTree";
    param.extParams.tmpTreeName = tmpTreeName;

    initData(10, false, param, function (data) {
        if (data) {
            initTmps(data);
        } else {
            $.messager.alert('info', '没有找到用户模板树');
        }
    });
}

/**
 * 加载用户模板树
 * @param userTmpPath
 */
function saveUserTmpTree() {
    //不是默认树就默认提交到当前树
    if (!isDefaultTree()) {
        $("#userTmpTreeName").val($("#userTmpTreeList option:selected").val());
    }
    $('#dd').dialog({
        title: '保存模板树',
        width: 400,
        height: 200,
        closed: false,
        cache: false,
        modal: true,
        buttons: [{
            text: '保存',
            handler: function () {
                //获取用户命名的模板树
                let userTmpTreeName = $("#userTmpTreeName").val();
                if (!userTmpTreeName) {
                    $.messager.alert('error', '用户模板名称为空');
                    return;
                }
                if (userTmpTreeName == "default_tmps") {
                    $.messager.alert('error', '不能修改默认模板树,请提交到新模板树或者原模板树');
                    return;
                }
                let param = {};
                param.extParams = {};
                param.extParams.userTmpTreeName = userTmpTreeName;

                let node = $('#tmps').tree('getSelected');
                //如果代码编辑区域有值，也需要保存
                let templateContent = editor.getValue();
                if (node && node.attributes.type == "file" && templateContent) {
                    param.extParams.templateContent = templateContent;
                    param.extParams.tmpModulePath = node.attributes.modulePath;
                }

                param.extParams.tmps = getSelectedTreeNode();
                if (!param.extParams.tmps) {
                    $.messager.alert('error', '没有选择保存的模板');
                    return;
                }

                param.operation="saveUserTmpTree";
                initData(10, false, param, function (data) {
                    $('#dd').dialog('close');
                    if (data.msg == "提交成功") {
                        refreshuserTmpTreeList();
                        $("#userTmpTreeList").val(userTmpTreeName);//切换到新增的分支
                        loadTmpTree();
                    } else {
                        $.messager.alert('info', data.msg);
                    }
                });

            }
        }, {
            text: '关闭',
            handler: function () {
                $('#dd').dialog('close');
            }
        }],
        onClose: function () {
            $("#userTmpTreeName").val("");
        }
    });
}

function del(node) {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能编辑，请提交到副本编辑");
        return;
    }
    if (!node) {
        node = $('#tmps').tree('getSelected');
    }
    let param = {};
    param.extParams = {};
    param.extParams.modulePath = node.attributes.modulePath;

    param.operation = "deleteTmp";
    initData(10, false, param, function (data) {
        if (data.msg == "删除成功") {
            loadTmpTree();
        } else {
            $.messager.alert('info', data.msg);
        }
    });
}


function copy() {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能编辑，请提交到副本编辑");
        return;
    }
    cutNode = null;
    copyNode = $('#tmps').tree('getSelected');//获取复制的节点
}

function cut() {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能编辑，请提交到副本编辑");
        return;
    }
    copyNode = null;
    cutNode = $('#tmps').tree('getSelected');//获取剪切的节点
}

function paste() {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能编辑，请提交到副本编辑");
        return;
    }
    if (!cutNode && !copyNode) {
        $.messager.alert('error', "请先选择复制或剪切的节点");
        return;
    }
    if (cutNode) {
        doMove(cutNode);
        cutNode = null;
    } else {
        doCopy(copyNode);
    }
}

function doCopy(node) {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能编辑，请提交到副本编辑");
        return;
    }
    let targetNode = $('#tmps').tree('getSelected');
    let tmpTreeName = $("#userTmpTreeList option:selected").val();
    let param = {};
    param.extParams = {};
    param.extParams.tmpTreeName = tmpTreeName;
    param.extParams.sourceTmpModulePath = node.attributes.modulePath;
    param.extParams.targetTmpModulePath = targetNode.attributes.modulePath;

    param.operation = "copyPath";
    initData(10, false, param, function (data) {
        if (data == 1) {
            loadTmpTree();
            let nodes = $("#tmps").tree("getChildren", node.target);
            for (let i = 0; i < nodes.length; i++) {
                if (nodes[i].attributes.modulePath == param.extParams.tmpModulePath) {
                    $("#tmps").tree("select", nodes[i].target);//选中刚新增的项
                    break;
                }
            }
        } else {
            $.messager.alert('error', '同名文件已存在');
            return;
        }
    });
}

function doMove(node) {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能编辑，请提交到副本编辑");
        return;
    }
    let targetNode = $('#tmps').tree('getSelected');
    let tmpTreeName = $("#userTmpTreeList option:selected").val();
    let param = {};
    param.extParams = {};
    param.extParams.tmpTreeName = tmpTreeName;
    param.extParams.sourceTmpModulePath = node.attributes.modulePath;
    param.extParams.targetTmpModulePath = targetNode.attributes.modulePath;

    param.operation = "movePath";
    initData(10, false, param, function (data) {
        if (data == 1) {
            loadTmpTree();
            let nodes = $("#tmps").tree("getChildren", node.target);
            for (let i = 0; i < nodes.length; i++) {
                if (nodes[i].attributes.modulePath == param.extParams.tmpModulePath) {
                    $("#tmps").tree("select", nodes[i].target);//选中刚新增的项
                    break;
                }
            }
        } else {
            $.messager.alert('error', '同名文件已存在');
            return;
        }
    });


}

/**
 * 提交代码
 */
function commitCode() {
    if (isDefaultTree()) {
        $.messager.alert('error', "默认分支不能修改代码,请提交到副本编辑");
        return;
    }
    let node = $('#tmps').tree('getSelected');
    let templateContent = editor.getValue();
    if (node && node.attributes.type == "file" && templateContent) {
        let param = {};
        param.extParams = {};
        param.extParams.templateContent = templateContent;
        param.extParams.tmpModulePath = node.attributes.modulePath;

        param.operation = "commit";
        initData(10, false, param, function (data) {
            if (data == 0) {
                $.messager.alert('info', "提交失败");
            }
        });

    } else {
        $.messager.alert('error', "请选择可以提交的模板，并输入有效的模板内容");
    }
}

function isDefaultTree() {
    let userTmpTreeName = $("#userTmpTreeList option:selected").val();
    if (userTmpTreeName == "default_tmps") {
        return true;
    }
    return false;
}

function fillInputParam(id, param) {
    let next = true;
    $(id + " input").each(function (item) {
        if (!$(this).val() && $(this).attr("notNull")) {
            $(id + ".errMsg").text("必填项不能为空");
            next = false;
            return;
        }
        let param_id = $(this).attr("id");
        if (param_id) {
            let value = $.trim($(this).val());
            if (value != "") {
                param[param_id] = value;
            }
        }
    });
    return next;
}