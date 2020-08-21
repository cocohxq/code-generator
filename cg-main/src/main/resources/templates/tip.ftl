<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>代码构建</title>
    <link href="../static/css/bootstrap.min.css" rel="stylesheet">
    <link href="../static/css/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="../static/css/easyui.css" rel="stylesheet">
    <link href="../static/css/icon.css" rel="stylesheet">
    <script src="../static/js/jquery.min.js" type="text/javascript"></script>
    <style>
        body {
            padding-top: 10px; /* 60px to make the container go all the way to the bottom of the topbar */
            padding-bottom: 10px;
        }
        #components{
            min-height: 600px;
        }
        #target{
            min-height: 200px;
            border: 1px solid #ccc;
            padding: 5px;
        }
        #target .component{
            border: 1px solid #fff;
        }
        #temp{
            width: 500px;
            background: white;
            border: 1px dotted #ccc;
            border-radius: 10px;
        }

        .popover-content form {
            margin: 0 auto;
            width: 213px;
        }
        .popover-content form .btn{
            margin-right: 10px
        }
        #bars{
            font-size:small;
        }
        #step0{
            padding-left:450px;
        }

        #step1{
            padding-left:50px;
            display: none;
        }
        #step1 select{
            height:400px;
            widht:200px;
        }

        #step2{
            padding-left:50px;
            display: none;
        }

        #step3{
            padding-left:50px;
            display: none;
        }


        .bar.cur{
            color: red;
        }

        .select {width:400px; float:left;}

        .clear:after {
            clear: both;
            content: " ";
            display: block;
            height: 0;
            overflow: hidden;
            visibility: hidden;
        }

        .tip {outline: 1px solid #ccc; padding: 5px; margin: 5px; }
        .string { color: green; }
        .number { color: darkorange; }
        .boolean { color: blue; }
        .null { color: magenta; }
        .key { color: red; }

        table tr td{
            border: 1px dashed #e5e5e5;
        }
    </style>

</head>
<body>
<div class="form-horizontal">
    <fieldset>
        <div class="control-group">

            <div style="border-bottom: 1px solid #e5e5e5;">
                解析字段示例说明：<br>
                1、表：user
                <table style="width:40%;text-align: left">
                    <tr>
                        <td>Field</td>
                        <td>Type</td>
                        <td>Comment</td>
                    </tr>
                    <tr>
                        <td>id</td>
                        <td>bigint(20)</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>name</td>
                        <td>varchar(50)</td>
                        <td>用户名</td>
                    </tr>
                    <tr>
                        <td>age</td>
                        <td>int</td>
                        <td>年龄</td>
                    </tr>
                    <tr>
                        <td>creator_id</td>
                        <td>bigint</td>
                        <td>创建人</td>
                    </tr>
                    <tr>
                        <td>created_time</td>
                        <td>datetime</td>
                        <td>创建时间</td>
                    </tr>
                    <tr>
                        <td>last_modifier_id</td>
                        <td>bigint</td>
                        <td>最后修改人</td>
                    </tr>
                    <tr>
                        <td>last_modified_time</td>
                        <td>datetime</td>
                        <td>最后修改时间</td>
                    </tr>
                    <tr>
                        <td>deleted</td>
                        <td>int</td>
                        <td>是否删除</td>
                    </tr>
                </table>


                2、解析出的模板构建对象：<br>
                <pre id="tip" class="tip">

                </pre>

            </div>
        </div>
    </fieldset>

</div>


<textarea id="tipObj" style="display: none">
2.模板变量值说明
a. commonValueStack属于公共值对象（所有模板都可以通过commonValueStack.getValue访问，包括一些类路径)
b. 模板特有值对象（其中java开头属性只有java模板才有）
{
    "creatorIdStr":"creator_id",
    "extendStr":"id",
    "javaImports":[
        "java.util.Date"
    ],
    "tableMeta":{
        "fields":[
            {
                "column":{
                    "columnName":"id",
                    "columnType":"bigint(20)",
                    "comment":"主键id",
                    "nullable":false
                },
                "fieldCamelNameMax":"Id",
                "fieldCamelNameMin":"id",
                "fieldClazz":"java.lang.Long",
                "fieldType":"Long"
            },
            {
                "column":{
                    "columnName":"name",
                    "columnType":"varchar(200)",
                    "comment":"名称",
                    "nullable":false
                },
                "fieldCamelNameMax":"Name",
                "fieldCamelNameMin":"name",
                "fieldClazz":"java.lang.String",
                "fieldType":"String"
            },
            {
                "column":{
                    "columnName":"age",
                    "columnType":"int(3)",
                    "comment":"年龄",
                    "nullable":true
                },
                "fieldCamelNameMax":"Age",
                "fieldCamelNameMin":"age",
                "fieldClazz":"java.lang.Integer",
                "fieldType":"Integer"
            },
            {
                "column":{
                    "columnName":"creator_id",
                    "columnType":"bigint(20)",
                    "comment":"创建人",
                    "nullable":true
                },
                "fieldCamelNameMax":"CreatorId",
                "fieldCamelNameMin":"creatorId",
                "fieldClazz":"java.lang.Long",
                "fieldType":"Long"
            },
            {
                "column":{
                    "columnDefault":"CURRENT_TIMESTAMP",
                    "columnName":"created_time",
                    "columnType":"datetime",
                    "comment":"创建时间",
                    "nullable":true
                },
                "fieldCamelNameMax":"CreatedTime",
                "fieldCamelNameMin":"createdTime",
                "fieldClazz":"java.util.Date",
                "fieldType":"Date"
            },
            {
                "column":{
                    "columnName":"last_modifier_id",
                    "columnType":"bigint(20)",
                    "comment":"最后修改者ID",
                    "nullable":true
                },
                "fieldCamelNameMax":"LastModifierId",
                "fieldCamelNameMin":"lastModifierId",
                "fieldClazz":"java.lang.Long",
                "fieldType":"Long"
            },
            {
                "column":{
                    "columnName":"last_modified_time",
                    "columnType":"datetime",
                    "comment":"最后修改时间",
                    "nullable":true
                },
                "fieldCamelNameMax":"LastModifiedTime",
                "fieldCamelNameMin":"lastModifiedTime",
                "fieldClazz":"java.util.Date",
                "fieldType":"Date"
            },
            {
                "column":{
                    "columnName":"deleted",
                    "columnType":"tinyint(2)",
                    "comment":"是否已删除，1:是，0:否",
                    "nullable":true
                },
                "fieldCamelNameMax":"Deleted",
                "fieldCamelNameMin":"deleted",
                "fieldClazz":"java.lang.Integer",
                "fieldType":"Integer"
            }
        ],
        "table":{
            "comment":"人员表",
            "tableName":"user"
        },
        "tableCamelNameMax":"User",
        "tableCamelNameMin":"user"
    },
    "groupId":"com.demo",
    "dbName":"xxxx",
    "modifierIdStr":"last_modifier_id",
    "betweenStr":"age",
    "password":"xxxx",
    "appId":"demo-app",
    "deleteStr":"deleted",
    "tableCamelNameMin":"user",
    "createTimeStr":"created_time",
    "inBusiPack":"user",
    "updateTimeStr":"last_modified_time",
    "commonValueStack":{
        "commonRelatedMap":{
            "AbstractManager.classPath":"com.demo.common.AbstractManager",
            "UserVO.classPath":"com.demo.vo.user.UserVO",
            "UserDTO.classPath":"com.demo.api.dto.user.UserDTO",
            "UserManager.classPath":"com.demo.manager.user.UserManager",
            "UserQuery.classPath":"com.demo.api.query.user.UserQuery",
            "UserManagerImpl.classPath":"com.demo.manager.user.impl.UserManagerImpl",
            "BaseDTO.classPath":"com.demo.api.common.BaseDTO",
            "BaseVO.classPath":"com.demo.common.BaseVO",
            "UserService.classPath":"com.demo.api.service.user.UserService",
            "BaseMapper.classPath":"com.demo.common.BaseMapper",
            "DruidMonitorController.classPath":"com.demo.common.DruidMonitorController",
            "PagedResultsResponse.classPath":"com.demo.api.common.PagedResultsResponse",
            "ResponseConstants.classPath":"com.demo.api.common.ResponseConstants",
            "AppApplication.classPath":"com.demo.AppApplication",
            "Response.classPath":"com.demo.api.common.Response",
            "User.classPath":"com.demo.model.user.User",
            "PagedResult.classPath":"com.demo.api.common.PagedResult",
            "UserServiceImpl.classPath":"com.demo.service.user.impl.UserServiceImpl",
            "AbstractController.classPath":"com.demo.common.AbstractController",
            "Manager.classPath":"com.demo.common.Manager",
            "UserMapper.classPath":"com.demo.mapper.user.UserMapper",
            "Page.classPath":"com.demo.api.common.Page",
            "BaseDO.classPath":"com.demo.common.BaseDO",
            "BaseQuery.classPath":"com.demo.api.common.BaseQuery",
            "Pagination.classPath":"com.demo.api.common.Pagination",
            "UserDO.classPath":"com.demo.domain.user.UserDO",
            "UserController.classPath":"com.demo.controller.user.UserController"
        }
    },
    "ip":"xxxx",
    "userName":"xxxx",
    "codeLocationType":1,
    "url":"jdbc:mysql://localhost:3306/xxxx?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE&useSSL=false&connectTimeout=180000&allowMultiQueries=true",
    "tableCamelNameMax":"User",
    "port":"3306",
    "inStr":"id",
    "driverName":"com.mysql.jdbc.Driver",
    "outBusiPack":""
}
</textarea>

<div style="border-top: 1px solid #e5e5e5;padding-left:600px;margin-top:150px">
        <span>
                Copyright &copy; 2018-${.now?string('yyyy')} &nbsp;by hanxianqiang&nbsp;
                <a href="https://github.com/cocohxq/code-generator" target="_blank" >github</a>
        </span>
</div>

</body>
</html>


<script>
    function jsonFormat(json) {
        if (typeof json != 'string') {
            json = JSON.stringify(json, undefined, 2);
        }
        json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
            var cls = 'number';
            if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                    cls = 'key';
                } else {
                    cls = 'string';
                }
            } else if (/true|false/.test(match)) {
                cls = 'boolean';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            return '<span class="' + cls + '">' + match + '</span>';
        });
    }

    //展示模板提示
    $('#tip').html(jsonFormat($("#tipObj").text()));
</script>