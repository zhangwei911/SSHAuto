package viz.intellij.plugin.setting

import java.io.*

object FileUtil {
    private val COMMENT_START = "<!-- Automatically generated by SSHAuto Start -->"
    private val COMMENT_END = "<!-- Automatically generated by SSHAuto End -->"
    private val BEANS_END = "</beans>"
    private val STRUTS_END = "</struts>"
    fun getContent(inputFile: String): String {
        val content = StringBuilder()
        try {
            val bis = BufferedInputStream(FileInputStream(File(inputFile)))
            //10M缓存
            val `in` = BufferedReader(InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024)
            while (`in`.ready()) {
                content.append(`in`.readLine()).append("\n")
            }
            `in`.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return content.toString()
    }

    fun write(content: String, outputFile: String) {
//        if (!File(outputFile).exists()) {
//            File(outputFile).createNewFile()
//        } else {
//            Messages.showMessageDialog(
//                    "文件已存在!" + outputFile,
//                    "提示",
//                    Messages.getInformationIcon()
//            )
//            return
//        }
        var fw: FileWriter? = null
        try {
            fw = FileWriter(outputFile)
            fw.append(content)
            fw.flush()
            fw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun generateFile(format: String, actionName_prefix: String, packageName: String, codePath: String, resourcePath: String, extraParams: String, ctrlType: CtrlType) {
        var file: File? = null
        var out = false
        when (ctrlType) {
            CtrlType.ACTION -> {
                var srcFile = File(codePath, packageName.replace(".", "/"))
                if (!srcFile.exists()) {
                    srcFile.mkdirs()
                }
                file = File(srcFile, actionName_prefix + "Action.java")
            }
            CtrlType.CONFIG -> {
                var resFile = File(resourcePath, "xml/action")
                if (!resFile.exists()) {
                    resFile.mkdirs()
                }
                file = File(resFile, actionName_prefix + "ActionConfig.xml")
            }
            CtrlType.BEAN -> {
                var resFile = File(resourcePath, "xml/beans")
                resFile.mkdirs()
                file = File(resFile, actionName_prefix + "ActionBean.xml")
            }
            CtrlType.ADD_BEAN_TO_APPLICATION -> {
                var applicationContextPath = File(File(resourcePath, "xml"), "applicationContext.xml").absolutePath
                var content = FileUtil.getContent(applicationContextPath)
                var end = if (content.contains(COMMENT_END)) {
                    COMMENT_END
                } else {
                    BEANS_END
                }
                content = content.replace(end, "    $COMMENT_START\n" +
                        "    <import resource=\"classpath:xml/beans/" + actionName_prefix + "ActionBean.xml\"/>\n" +
                        "    $COMMENT_END" + "\n")
                FileUtil.write(content, applicationContextPath)
                out = true
            }
            CtrlType.ADD_CONFIG_TO_STRUTS -> {
                var strutsPath = File(File(resourcePath, "xml"), "struts.xml").absolutePath
                var content = FileUtil.getContent(strutsPath)
                var end = if (content.contains(COMMENT_END)) {
                    COMMENT_END
                } else {
                    STRUTS_END
                }
                content = content.replace(end, "    $COMMENT_START\n" +
                        "    <include file=\"xml/action/" + actionName_prefix + "ActionConfig.xml\"></include>\n" +
                        "    $COMMENT_END" + "\n")
                FileUtil.write(content, strutsPath)
                out = true
            }
        }
        if (out) {
            return
        }
        var extraParamStr = extraParams
        var fileContent =
                if (format.contains("${'$'}{ACTION_NAME_PREFIX}")) {
                    format.replace("${'$'}{ACTION_NAME_PREFIX}", actionName_prefix + "Action")
                } else {
                    format
                }
        fileContent =
                if (format.contains("${'$'}{PACKAGE}")) {
                    fileContent.replace("${'$'}{PACKAGE}", packageName)
                } else {
                    fileContent
                }
        when {
            extraParamStr!!.contains("|") -> {
                var extraParamArray = extraParamStr.split("|")
                for (i in extraParamArray.indices) {
                    if (format.contains("${'$'}{EXTRA_PARAM_$i}")) {
                        fileContent = fileContent.replace("${'$'}{EXTRA_PARAM_$i}", extraParamArray[i])
                    }
                }
            }
            extraParamStr.isNotEmpty() ->
                if (format.contains("${'$'}{EXTRA_PARAM_0}")) {
                    fileContent = fileContent.replace("${'$'}{EXTRA_PARAM_0}", extraParamStr)
                }
            else -> return
        }
        FileUtil.write(fileContent, file!!.absolutePath)
    }
}