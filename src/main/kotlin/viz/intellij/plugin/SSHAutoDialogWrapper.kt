package viz.intellij.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.psi.PsiClass
import com.intellij.ui.DocumentAdapter
import com.intellij.util.ui.Html
import viz.intellij.plugin.setting.SSHAutoFormatSetting

import java.awt.event.KeyEvent
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import javax.swing.*

import javax.swing.event.DocumentEvent

class SSHAutoDialogWrapper : DialogWrapper {
    private var project: Project? = null
    private var contentPane: JPanel? = null
    private var textField_actionName_prefix: JTextField? = null
    private var textField_package: JTextField? = null
    private var actionName_prefix: String = null.toString()
    private var jLabel_test: JLabel? = null
    private var comboBox_code: JComboBox<String>? = null
    private var comboBox_resource: JComboBox<String>? = null
    private var textField_extraParams: JTextField? = null
    private var sshAutoFormatSetting = SSHAutoFormatSetting.instance

    private enum class CtrlType {
        ACTION,
        CONFIG,
        BEAN,
        ADD_BEAN_TO_APPLICATION,
        ADD_CONFIG_TO_STRUTS
    }

    constructor(psiClass: PsiClass) : super(psiClass.project) {
        title = "SSHAuto"
        this.project = psiClass.project
        init()
    }

    constructor(project: Project) : super(project) {
        title = "SSHAuto"
        this.project = project
        init()
    }

    override fun init() {
        super.init()

        // call onCancel() on ESCAPE
        contentPane!!.registerKeyboardAction({ }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)

        jLabel_test!!.text = Html("<html><a href='settings'>设置</a></html>").text
        var moduleSourceRoot = ProjectRootManager.getInstance(this.project!!).fileIndex
        var projectName = project!!.name
        var vFiles = ProjectRootManager.getInstance(project!!).contentSourceRoots
        comboBox_code!!.removeAllItems()
        comboBox_resource!!.removeAllItems()
        for (i in vFiles.indices) {
            comboBox_code!!.addItem(vFiles[i].path)
            comboBox_resource!!.addItem(vFiles[i].path)
        }
        textField_actionName_prefix!!.text += Random().nextInt(100)
        textField_package!!.text += Random().nextInt(100)
        textField_extraParams!!.text += Random().nextInt(100)
    }

    override fun doOKAction() {
        actionName_prefix = textField_actionName_prefix!!.text
        if (this.sshAutoFormatSetting.actionCodeFormat!!.isEmpty() || this.sshAutoFormatSetting.actionConfigCodeFormat!!.isEmpty() || this.sshAutoFormatSetting.actionBeanCodeFormat!!.isEmpty() || this.sshAutoFormatSetting.xmlPath!!.isEmpty()) {
            Messages.showMessageDialog(
                    "请到Settings>OtherSettings>SSHAutoTestSettings配置相关信息",
                    "提示",
                    Messages.getInformationIcon()
            )
            super.doCancelAction()
            return
        }
        if (actionName_prefix.isNotEmpty() && textField_package!!.text.isNotEmpty()) {
//            Messages.showMessageDialog(
//                    "OK!",
//                    "Test",
//                    Messages.getInformationIcon()
//            )
            onOK()
            super.doOKAction()
        }
    }

    override fun doCancelAction() {
        super.doCancelAction()
//        Messages.showMessageDialog(
//                "Cancel!",
//                "Test",
//                Messages.getInformationIcon()
//        )
    }

    override fun createCenterPanel(): JComponent? {
        return contentPane
    }

    private fun createNamePanel(): JComponent {
        val labeledComponent = LabeledComponent<JTextField>()
        labeledComponent.text = "Action前缀:"
        val nameField = JTextField()
        //15623297885
        labeledComponent.component = nameField
        nameField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {

            }
        })
        return labeledComponent
    }

    private fun onOK() {
        // add your code here
        createActionClassFromTemplate()
        createActionBeanFromTemplate()
        createActionConfigFromTemplate()
        createActionConfigFromTemplate()
        addActionBeanToAppliction()
        addActionConfigToStruts()
        dispose()
    }

    private fun createActionConfigFromTemplate() {
        generateFile(this.sshAutoFormatSetting.actionConfigCodeFormat!!, CtrlType.CONFIG)
    }

    private fun createActionBeanFromTemplate() {
        generateFile(this.sshAutoFormatSetting.actionBeanCodeFormat!!, CtrlType.BEAN)
    }

    /**
     * 规则:
     * 固定属性名:ACTION_NAME_PREFIX,PACKAGE
     * 扩展属性名:'EXTRA_PARAM_'+'0...'
     * */
    private fun createActionClassFromTemplate() {
        generateFile(this.sshAutoFormatSetting.actionCodeFormat!!, CtrlType.ACTION)
    }

    private fun addActionBeanToAppliction() {
        generateFile("", CtrlType.ADD_BEAN_TO_APPLICATION)
    }

    private fun addActionConfigToStruts() {
        generateFile("", CtrlType.ADD_CONFIG_TO_STRUTS)
    }

    private fun generateFile(format: String, ctrlType: CtrlType) {
        var file: File? = null
        var out = false
        when (ctrlType) {
            CtrlType.ACTION -> {
                var srcFile = File(comboBox_code!!.selectedItem.toString(), textField_package!!.text.replace(".", "/"))
                if (!srcFile.exists()) {
                    srcFile.mkdirs()
                }
                file = File(srcFile, actionName_prefix + "Action.java")
            }
            CtrlType.CONFIG -> {
                var resFile = File(comboBox_resource!!.selectedItem.toString(), "xml/action")
                if (!resFile.exists()) {
                    resFile.mkdirs()
                }
                file = File(resFile, actionName_prefix + "ActionConfig.xml")
            }
            CtrlType.BEAN -> {
                var resFile = File(comboBox_resource!!.selectedItem.toString(), "xml/beans")
                resFile.mkdirs()
                file = File(resFile, actionName_prefix + "ActionBean.xml")
            }
            CtrlType.ADD_BEAN_TO_APPLICATION -> {
                var applicationContextPath = File(File(comboBox_resource!!.selectedItem.toString(), "xml"), "applicationContext.xml").absolutePath
                var content = getContent(applicationContextPath)
                content = content.replace("</beans>", "<!-- 由SSHAuto自动生成Start --!>\n    <import resource=\"classpath:xml/beans/" + actionName_prefix + "ActionBean.xml\"/>\n<!-- 由SSHAuto自动生成End --!>" + "\n</beans>")
                write(content, applicationContextPath)
                out = true
            }
            CtrlType.ADD_CONFIG_TO_STRUTS -> {
                var strutsPath = File(File(comboBox_resource!!.selectedItem.toString(), "xml"), "struts.xml").absolutePath
                var content = getContent(strutsPath)
                content = content.replace("</struts>", "<!-- 由SSHAuto自动生成Start --!>\n    <include file=\"xml/action/" + actionName_prefix + "ActionConfig.xml\"></include>\n" +
                        "<!-- 由SSHAuto自动生成End --!>" + "\n</struts>")
                write(content, strutsPath)
                out = true
            }
        }
        if (out) {
            return
        }
        var extraParamStr = textField_extraParams!!.text
        var fileContent =
                if (format.contains("${'$'}{ACTION_NAME_PREFIX}")) {
                    format.replace("${'$'}{ACTION_NAME_PREFIX}", textField_actionName_prefix!!.text + "Action")
                } else {
                    format
                }
        fileContent =
                if (format.contains("${'$'}{PACKAGE}")) {
                    fileContent.replace("${'$'}{PACKAGE}", textField_package!!.text)
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
        write(fileContent, file!!.absolutePath)
    }

    private fun getContent(inputFile: String): String {
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

    private fun write(content: String, outputFile: String) {
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

    override fun doValidate(): ValidationInfo? {
        return super.doValidate()
    }
}
