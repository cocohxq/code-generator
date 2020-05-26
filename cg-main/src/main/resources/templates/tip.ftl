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
                1、表：user_info
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
                        <td>user_name</td>
                        <td>varchar(50)</td>
                        <td>用户名</td>
                    </tr>
                    <tr>
                        <td>birthday</td>
                        <td>datetime</td>
                        <td>生日</td>
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
2.1、公共值对象（所有模板都可以通过commonValueStack.getValue访问，包括一些类路径)
{
  "UserInfoManagerImpl.classPath": "com.hxq.manager.impl.UserInfoManagerImpl",
  "UserInfoVO.classPath": "com.hxq.vo.UserInfoVO",
  "UserInfoServiceImpl.classPath": "com.hxq.service.impl.UserInfoServiceImpl",
  "UserInfoDao.classPath": "com.hxq.dao.UserInfoDao",
  "UserInfoQuery.classPath": "com.hxq.query.UserInfoQuery",
  "UserInfoDaoImpl.classPath": "com.hxq.dao.impl.UserInfoDaoImpl",
  "PagedResult.classPath": "com.hxq.common.PagedResult",
  "UserInfo.classPath": "com.hxq.model.UserInfo",
  "UserInfoDTO.classPath": "com.hxq.dto.UserInfoDTO",
  "UserInfoEntity.classPath": "com.hxq.entity.UserInfoEntity",
  "UserInfoManager.classPath": "com.hxq.manager.UserInfoManager",
  "BaseDao.classPath": "com.hxq.common.BaseDao",
  "BaseDTO.classPath": "com.hxq.common.BaseDTO",
  "UserInfoDO.classPath": "com.hxq.do.UserInfoDO",
  "UserInfoService.classPath": "com.hxq.service.UserInfoService",
  "BaseDO.classPath": "com.hxq.common.BaseDO",
  "Page.classPath": "com.hxq.common.Page",
  "BaseQuery.classPath": "com.hxq.common.BaseQuery",
  "BaseEntity.classPath": "com.hxq.common.BaseEntity"
}
2.2 模板特有值对象（其中java开头属性只有java模板才有）
{
  "targetFilePath": "/Users/user/6B2E8BD3DE746842C8EE323A7C66BBD4/module/src/main/java/com/hxq/dto/UserInfoDTO.java",
  "javaImports": ["java.util.Date"],
  "dbName":"demo",
  "javaPackage": "com.hxq.dto",
  "javaClassName": "UserInfoDTO",
  "tableMeta": {
    "fields": [{
      "column": {
        "columnName": "id",
        "columnType": "BIGINT",
        "comment": ""
      },
      "fieldCamelNameMin": "id",
      "fieldCamelNameMax": "Id",
      "fieldType": "Long"
    }, {
      "column": {
        "columnName": "user_name",
        "columnType": "VARCHAR",
        "comment": "姓名"
      },
      "fieldCamelNameMin": "userName",
      "fieldCamelNameMax": "UserName",
      "fieldType": "String"
    }, {
      "column": {
        "columnName": "birthday",
        "columnType": "DATETIME",
        "comment": "生日"
      },
      "fieldCamelNameMin": "birthday",
      "fieldCamelNameMax": "Birthday",
      "fieldType": "Date"
    }],
    "table": {
      "comment": "",
      "tableName": "user_info"
    },
    "tableCamelNameMin": "userInfo",
    "tableCamelNameMax": "UserInfo"
  },
  "groupId": "com.hxq",
  "tableCamelNameMin": "userInfo",
  "tableCamelNameMax": "UserInfo",
  "createTimeStr":"created_time",
  "updateTimeStr":"update_time",
  "deleteStr":"deleted",
  "extendStr":"deleted",
  "inStr":"deleted"
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