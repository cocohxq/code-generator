<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>代码生成器</title>
    <link href="../static/css/bootstrap.min.css" rel="stylesheet">
    <link href="../static/css/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="../static/css/easyui.css" rel="stylesheet">
    <link href="../static/css/icon.css" rel="stylesheet">

    <link rel="stylesheet" href="/static/codemirror-5.40.2/lib/codemirror.css"/>
    <!--引入css文件，用以支持主题-->
    <link rel="stylesheet" href="/static/codemirror-5.40.2/theme/dracula.css"/>
    <link rel="stylesheet" href="/static/codemirror-5.40.2/addon/hint/show-hint.css">

    <script src="../static/js/jquery.min.js" type="text/javascript"></script>
    <script src="../static/js/jquery.easyui.min.js" type="text/javascript"></script>
    <script src="../static/js/config.js" type="text/javascript"></script>
    <script src="/static/codemirror-5.40.2/lib/codemirror.js"></script>

    <!--Java代码高亮必须引入-->
    <script src="/static/codemirror-5.40.2/addon/edit/matchbrackets.js"></script>
    <script src="/static/codemirror-5.40.2/addon/hint/show-hint.js"></script>
    <script src="/static/codemirror-5.40.2/mode/clike/clike.js"></script>

    <script src="/static/codemirror-5.40.2/mode/htmlmixed/htmlmixed.js"></script>
    <script src="/static/codemirror-5.40.2/addon/mode/multiplex.js"></script>
    <script src="/static/codemirror-5.40.2/mode/htmlembedded/htmlembedded.js"></script>
    <script src="/static/codemirror-5.40.2/mode/xml/xml.js"></script>


    <!--支持代码折叠-->
    <link rel="stylesheet" href="/static/codemirror-5.40.2/addon/fold/foldgutter.css"/>
    <script src="/static/codemirror-5.40.2/addon/fold/foldcode.js"></script>
    <script src="/static/codemirror-5.40.2/addon/fold/foldgutter.js"></script>
    <script src="/static/codemirror-5.40.2/addon/fold/brace-fold.js"></script>
    <script src="/static/codemirror-5.40.2/addon/fold/comment-fold.js"></script>

    <style>
        body {
            padding-top: 10px; /* 60px to make the container go all the way to the bottom of the topbar */
            padding-bottom: 10px;
        }

        #components {
            min-height: 600px;
        }

        #target {
            min-height: 200px;
            border: 1px solid #ccc;
            padding: 5px;
        }

        #target .component {
            border: 1px solid #fff;
        }

        #temp {
            width: 500px;
            background: white;
            border: 1px dotted #ccc;
            border-radius: 10px;
        }

        .popover-content form {
            margin: 0 auto;
            width: 213px;
        }

        .popover-content form .btn {
            margin-right: 10px
        }

        #bars {
            font-size: small;
        }

        .step {
            display: none;
        }

        #step_db {
            padding-left: 450px;
            /*display: block;*/
        }

        #step_table {
            padding-left: 50px;
        }

        #step_table select {
            height: 400px;
            widht: 200px;
        }

        #step_code {
            padding-left: 450px;
        }

        #step_preview {
            padding-left: 50px;
        }


        .bar.cur {
            color: red;
        }

        .select {
            width: 400px;
            float: left;
        }

        .clear:after {
            clear: both;
            content: " ";
            display: block;
            height: 0;
            overflow: hidden;
            visibility: hidden;
        }

        .highlight {
            color: red;
        }

        .checkbox {
            border: none
        }

    </style>
</head>
<body>
<div class="form-horizontal">
    <fieldset>
        <div id="legend">
            <legend class=""><a href="/" style="text-decoration: #0a001f">构建配置</a>
                <#list view.steps as step>
                <span id="bars">>>
                    <#if step_index == 0>
                        <span class="bar cur" id="bar_${step.code}" code="${step.code}">
                    <#else >
                        <span class="bar" id="bar_${step.code}" code="${step.code}">
                    </#if>
                            ${step.name}</span>
                </#list>
            </span>
                <span style="float: right"><img src="/static/img/help.png" style="width: 16px; height: 16px; "><a
                            href="/tip" target="_blank">模板属性值说明</a></span>
            </legend>
        </div>
        <!--选择数据源-->
        <div id="step_${view.steps[0].code}" code="${view.steps[0].code}" class="step">
            <#if data ?? && !data.operation??>
                <div class="control-group" style="height: 350px">
                    <label class="control-label"><span class="highlight">*</span>数据库配置</label>
                    <div class="controls">
                        <select class="input-large" id="selectedConfig">
                            <#list data.dbConfigList as dbConfig>
                                <option class="input-large" value="${dbConfig}">${dbConfig}</option>
                            </#list>
                        </select>
                        <button class="btn add" id="addConfig">新增</button>
                        <button class="btn add" id="copyAddConfig">复制新增</button>
                        <button class="btn edit" id="editConfig">编辑</button>
                        <button class="btn edit" id="delConfig">删除</button>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <button class="btn btn-success input-large next">下一步</button>
                        <span class="errMsg" style="color: red"></span>
                    </div>
                </div>
            </#if>
            <#if data ?? && data.operation??>
                <div class="control-group">
                    <label class="control-label" for="input01"><span class="highlight">*</span>配置名称</label>
                    <div class="controls">
                        <input id="configName" type="text" placeholder="配置名称" class="input-large"
                               value="${data.dbConfig.configName!""}" notNull="true">
                        <p class="help-block"></p>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label"><span class="highlight">*</span>数据库类型</label>
                    <div class="controls">
                        <select class="input-large" id="dbType">
                            <#list view.effectiveDbs as dbType>
                                <option class="input-large" value="${dbType.type}"
                                        <#if data.dbConfig.dbType?? && data.dbConfig.dbType == dbType.type>selected</#if>>${dbType.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="input01"><span class="highlight">*</span>ip(host)</label>
                    <div class="controls">
                        <input id="ip" type="text" placeholder="数据库ip" class="input-large"
                               value="${data.dbConfig.ip!""}" notNull="true">
                        <p class="help-block"></p>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="input01"><span class="highlight">*</span>port</label>
                    <div class="controls">
                        <input id="port" type="text" placeholder="端口" class="input-large"
                               value="${data.dbConfig.port!""}" notNull="true">
                        <p class="help-block"></p>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="input01"><span class="highlight">*</span>数据库名</label>
                    <div class="controls">
                        <input id="dbName" type="text" placeholder="需连接的数据库" class="input-large"
                               value="${data.dbConfig.dbName!""}" notNull="true">
                        <p class="help-block"></p>
                    </div>
                </div>


                <div class="control-group">
                    <label class="control-label" for="input01"><span class="highlight">*</span>用户名</label>
                    <div class="controls">
                        <input id="username" type="text" placeholder="数据库用户名" class="input-large"
                               value="${data.dbConfig.username!""}" notNull="true">
                        <p class="help-block"></p>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="input01"><span class="highlight">*</span>密码</label>
                    <div class="controls">
                        <input id="pwd" type="text" placeholder="数据库密码" class="input-large"
                               value="${data.dbConfig.pwd!""}" notNull="true">
                        <p class="help-block"></p>
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <button class="btn btn-success" id="saveConfig">保存</button>
                        <span class="errMsg" style="color: red"></span>
                    </div>
                </div>
            </#if>
        </div>

        <!-- 选择表-->
        <div id="step_${view.steps[1].code}" code="${view.steps[1].code}" class="step">
            <div class="control-group">

                <!-- Select Multiple -->
                <div class="select">
                    <label class="control-label">表</label>
                    <div class="controls">
                        <select id="tables" class="input-xlarge" multiple="multiple">
                        </select>
                    </div>
                </div>

                <div class="select">
                    <label class="control-label">字段</label>
                    <div class="controls">
                        <select id="columns" class="input-xlarge" multiple="multiple">
                        </select>
                    </div>
                </div>

                <div class="controls" style="margin-left:850px;">
                    <button class="btn btn-success field" at="createTimeStr">选为新增时间</button>
                    <br>
                    <button class="btn btn-success field" at="updateTimeStr">选为更新时间</button>
                    <br>
                    <button class="btn btn-success field" at="deleteStr">选为删除标识</button>
                    <br>
                    <button class="btn btn-success field" at="extendStr">选为extend字段</button>
                    <br>
                    <button class="btn btn-success field" at="inStr">选为in字段</button>
                    <br>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="input01">时间戳-新增</label>
                <div class="controls">
                    <input id="createTimeStr" type="text" placeholder="create_time,created_time" class="input-large"
                           value="">(用于创建sql生成识别，表字段不同用","分隔)
                    <p class="help-block"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="input01">时间戳-更新</label>
                <div class="controls">
                    <input id="updateTimeStr" type="text" placeholder="update_time,last_modified_time"
                           class="input-large" value="">(用于更新sql生成识别,表字段不同用","分隔)
                    <p class="help-block"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="input01">逻辑删除标记</label>
                <div class="controls">
                    <input id="deleteStr" type="text" placeholder="deleted,is_deleted" class="input-large" value="">(用于删除sql生成识别,不配置默认物理删除，表字段不同用","分隔)
                    <p class="help-block"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="input01">pojo类extend字段</label>
                <div class="controls">
                    <input id="extendStr" type="text" placeholder="id,created_time,last_modified_time"
                           class="input-large" value="">(pojo类[model,DO,DTO,query,Entity]的某些字段可能在继承的model基类中已经定义，生成pojo时进行排除)
                    <p class="help-block"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="input01">支持in查询字段</label>
                <div class="controls">
                    <input id="inStr" type="text" placeholder="xxx1,xxx2" class="input-large" value="">(在query和sql生成的时候支持in条件)
                    <p class="help-block"></p>
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <button class="btn btn-success pre">上一步</button>
                    <button class="btn btn-success next">下一步</button>
                    <span class="errMsg" style="color: red"></span>
                </div>
            </div>
        </div>


        <!-- 编辑代码配置 -->
        <div id="step_${view.steps[2].code}" code="${view.steps[2].code}" class="step">
            <div class="control-group">
                <label class="control-label" for="input01"><span class="highlight">*</span>groupId</label>
                <div class="controls">
                    <input id="groupId" type="text" placeholder="com.xxx.xxx" class="input-large" value=""
                           notNull="true">
                    <p class="help-block"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label"><span class="highlight">*</span>代码输出方式</label>
                <div class="controls">
                    <label class="radio inline">
                        <input name="codeLocationType" type="radio" value="1" checked> 树形目录输出
                    </label>
                    <label class="radio inline">
                        <input name="codeLocationType" type="radio" value="2" checked> 同一目录输出
                        &nbsp;&nbsp;&nbsp;&nbsp;(全量创建代码工程建议树形，增量创建代码建议同一目录输出)
                    </label>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="input01">businessPackage</label>
                <div class="controls">
                    <input name="businessPackage" type="text" placeholder="业务包名" class="input-large" value="">
                    <p class="help-block"></p>
                    <label class="radio inline">
                        <input name="businessLocationType" type="radio" value="1" checked> 相对外置
                    </label>
                    <label class="radio inline">
                        <input name="businessLocationType" type="radio" value="2"> 相对内置
                        &nbsp;&nbsp;&nbsp;&nbsp;(包名相对dao、service包的放置位置)
                    </label>
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <button class="btn btn-success pre">上一步</button>
                    <button class="btn btn-success next">下一步</button>
                    <span class="errMsg" style="color: red"></span>
                </div>
            </div>

        </div>

        <!-- 选择/编辑模板 -->
        <div id="step_${view.steps[3].code}" code="${view.steps[3].code}" class="step">

            <div class="control-group">

                <div style="float: left;width: 30%">
                    <select class="input-large" id="userTmpTreeList" onchange="loadTmpTree()">
                    </select>
                    <br><span style="color:red">
                    <table style="border: none">
                        <tr>
                            <td>Tip:</td>
                            <td>1.右键可编辑模板树</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>2.非ftl结尾模板需要仅供参考，需根据实际情况修改</td>
                        </tr>
                    </table>
                    </span>
                    <ul id="tmps"></ul>
                    <button onclick="saveUserTmpTree()" class="btn btn-success">复制选中到副本</button>
                </div>

                <div id="menuLevel_0" class="easyui-menu" style="width:120px;">
                    <div onclick="append(0)" data-options="iconCls:'icon-add'">添加模板</div>
                    <div onclick="append(1)" data-options="iconCls:'icon-add'">添加目录</div>
                    <div onclick="paste()" data-options="iconCls:'icon-add'">粘贴</div>
                </div>

                <div id="menuLevel_1" class="easyui-menu" style="width:120px;">
                    <div onclick="append(0)" data-options="iconCls:'icon-add'">添加模板</div>
                    <div onclick="append(1)" data-options="iconCls:'icon-add'">添加目录</div>
                    <div onclick="edit()" data-options="iconCls:'icon-add'">重命名</div>
                    <div onclick="del()" data-options="iconCls:'icon-add'">删除</div>
                    <div onclick="copy()" data-options="iconCls:'icon-add'">复制</div>
                    <div onclick="cut()" data-options="iconCls:'icon-add'">剪切</div>
                    <div onclick="paste()" data-options="iconCls:'icon-add'">粘贴</div>
                </div>

                <div id="menuLevel_2" class="easyui-menu" style="width:120px;">
                    <div onclick="edit()" data-options="iconCls:'icon-add'">重命名</div>
                    <div onclick="del()" data-options="iconCls:'icon-add'">删除</div>
                    <div onclick="copy()" data-options="iconCls:'icon-add'">复制</div>
                    <div onclick="cut()" data-options="iconCls:'icon-add'">剪切</div>
                </div>


                <div style="width: 65%;float:left">
                    <textarea id="tmpContent" class="form-control"></textarea>
                    <button onclick="commitCode()" class="btn btn-success">提交代码</button>
                </div>
            </div>


            <div class="control-group" style="margin-left: 450px">
                <div class="controls">
                    <button class="btn btn-success pre">上一步</button>
                    <button class="btn btn-success next">下一步</button>
                    <span class="errMsg" style="color: red"></span>
                </div>
            </div>
        </div>


        <!-- 预览/导出文件 -->
        <div id="step_${view.steps[4].code}" code="${view.steps[4].code}" class="step">

            <div class="control-group">

                <ul id="file_tree" style="float: left;width: 30%"></ul>

                <div style="width: 65%;float:left">
                    <textarea id="fileContent" class="form-control"></textarea>
                </div>
            </div>


            <div class="control-group" style="margin-left: 450px">
                <div class="controls">
                    <button class="btn btn-success pre">上一步</button>
                    <button class="btn btn-success next">导出</button>
                    <span class="errMsg" style="color: red"></span>
                </div>
            </div>

        </div>
    </fieldset>

</div>

<a id="zip_download" href="" style="display: none" download="code.zip" target="_blank"/></a>

<div id="dd" style="display: none">
    <div style="padding-top: 50px;padding-left: 20px">
        <span style="color:red;">*将勾选的模板提交到以下模板树：</span><br>
        模板树名称：<input class="input-large" id="userTmpTreeName">
    </div>
</div>

<div style="border-top: 1px solid #e5e5e5;padding-left:600px;margin-top:150px">
        <span>
                Copyright &copy; 2018-${.now?string('yyyy')} &nbsp;by hanxianqiang&nbsp;
                <a href="https://github.com/cocohxq/code-generator" target="_blank">github</a>
        </span>
</div>

</body>

<script type="application/javascript" >
    let error = "${error!''}";
</script>
</html>