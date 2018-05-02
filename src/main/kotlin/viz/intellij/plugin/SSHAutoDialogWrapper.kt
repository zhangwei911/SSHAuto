package viz.intellij.plugin

import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.psi.PsiClass
import com.intellij.ui.DocumentAdapter
import com.intellij.util.ui.Html
import viz.intellij.plugin.setting.CtrlType
import viz.intellij.plugin.setting.FileUtil
import viz.intellij.plugin.setting.FileUtil.generateFile
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

//        jLabel_test!!.text = Html("<html><a href='settings'>设置</a></html>").text
        var moduleSourceRoot = ProjectRootManager.getInstance(this.project!!).fileIndex
        var projectName = project!!.name
        var vFiles = ProjectRootManager.getInstance(project!!).contentSourceRoots
        comboBox_code!!.removeAllItems()
        comboBox_resource!!.removeAllItems()
        for (i in vFiles.indices) {
            comboBox_code!!.addItem(vFiles[i].path)
            comboBox_resource!!.addItem(vFiles[i].path)
        }
//        textField_actionName_prefix!!.text += Random().nextInt(100)
//        textField_package!!.text += Random().nextInt(100)
//        textField_extraParams!!.text += Random().nextInt(100)
    }

    override fun doOKAction() {
        actionName_prefix = textField_actionName_prefix!!.text
        if (this.sshAutoFormatSetting.actionCodeFormat!!.isEmpty() || this.sshAutoFormatSetting.actionConfigCodeFormat!!.isEmpty() || this.sshAutoFormatSetting.actionBeanCodeFormat!!.isEmpty()) {
            Messages.showMessageDialog(
                    "请到Settings>OtherSettings>SSHAutoTestSettings配置相关信息",
                    "提示",
                    Messages.getInformationIcon()
            )
            ShowSettingsUtil.getInstance().showSettingsDialog(null,"SSHAutoTestSettings")
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
        generateFile(this.sshAutoFormatSetting.actionConfigCodeFormat!!,actionName_prefix,textField_package!!.text,comboBox_code!!.selectedItem.toString(),"",textField_extraParams!!.text, CtrlType.CONFIG)
    }

    private fun createActionBeanFromTemplate() {
        generateFile(this.sshAutoFormatSetting.actionBeanCodeFormat!!,actionName_prefix,textField_package!!.text,"",comboBox_resource!!.selectedItem.toString(),textField_extraParams!!.text, CtrlType.BEAN)
    }

    /**
     * 规则:
     * 固定属性名:ACTION_NAME_PREFIX,PACKAGE
     * 扩展属性名:'EXTRA_PARAM_'+'0...'
     * */
    private fun createActionClassFromTemplate() {
        generateFile(this.sshAutoFormatSetting.actionCodeFormat!!,actionName_prefix,textField_package!!.text,"",comboBox_resource!!.selectedItem.toString(),textField_extraParams!!.text, CtrlType.ACTION)
    }

    private fun addActionBeanToAppliction() {
        generateFile("",actionName_prefix,textField_package!!.text,"",comboBox_resource!!.selectedItem.toString(),textField_extraParams!!.text, CtrlType.ADD_BEAN_TO_APPLICATION)
    }

    private fun addActionConfigToStruts() {
        generateFile("",actionName_prefix,textField_package!!.text,"",comboBox_resource!!.selectedItem.toString(),textField_extraParams!!.text, CtrlType.ADD_CONFIG_TO_STRUTS)
    }


    override fun doValidate(): ValidationInfo? {
        return super.doValidate()
    }
}
