package viz.intellij.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.psi.PsiClass
import viz.intellij.plugin.setting.CtrlType
import viz.intellij.plugin.setting.FileUtil
import viz.intellij.plugin.setting.SSHAutoFormatSetting
import java.awt.event.KeyEvent
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.KeyStroke

class SSHAutoDialogWrapperUI : DialogWrapper {
    private var project: Project? = null
    private var panel: JPanel? = null
    private var sshAutoFormatSetting = SSHAutoFormatSetting.instance
    private var sshAutoDialogWrapperUIDSL = SSHAutoDialogWrapperUIDSL()

    constructor(psiClass: PsiClass) : super(psiClass.project) {
        title = "SSHAuto"
        this.project = psiClass.project
        init()
    }

    constructor(project: Project?) : super(project) {
        title = "SSHAuto"
        this.project = project
        init()
    }

    override fun createCenterPanel(): JComponent? {
        panel = sshAutoDialogWrapperUIDSL.dialogPanel()
        return panel
    }

    override fun init() {
        super.init()

        panel!!.registerKeyboardAction({ }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)

        var vFiles = ProjectRootManager.getInstance(project!!).contentSourceRoots
        sshAutoDialogWrapperUIDSL.codePathField.removeAllItems()
        sshAutoDialogWrapperUIDSL.resourcePathField.removeAllItems()
        for (i in vFiles.indices) {
            sshAutoDialogWrapperUIDSL.codePathField.addItem(vFiles[i].path)
            sshAutoDialogWrapperUIDSL.resourcePathField.addItem(vFiles[i].path)
        }
    }
    override fun doOKAction() {
        if (this.sshAutoFormatSetting.actionCodeFormat!!.isEmpty() || this.sshAutoFormatSetting.actionConfigCodeFormat!!.isEmpty() || this.sshAutoFormatSetting.actionBeanCodeFormat!!.isEmpty()) {
            Messages.showMessageDialog(
                    "请到Settings>OtherSettings>SSHAutoTestSettings配置相关信息",
                    "提示",
                    Messages.getInformationIcon()
            )
            super.doCancelAction()
            return
        }
        if (sshAutoDialogWrapperUIDSL.actionPrefixField.text.isNotEmpty() && sshAutoDialogWrapperUIDSL.packageField.text.isNotEmpty()) {
//            Messages.showMessageDialog(
//                    "OK!",
//                    "Test",
//                    Messages.getInformationIcon()
//            )
            onOK()
            super.doOKAction()
        }
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
        FileUtil.generateFile(this.sshAutoFormatSetting.actionConfigCodeFormat!!, sshAutoDialogWrapperUIDSL.actionPrefixField.text, sshAutoDialogWrapperUIDSL.packageField.text, sshAutoDialogWrapperUIDSL.codePathField.selectedItem.toString(), "", sshAutoDialogWrapperUIDSL.extraParamsField.text, CtrlType.CONFIG)
    }

    private fun createActionBeanFromTemplate() {
        FileUtil.generateFile(this.sshAutoFormatSetting.actionBeanCodeFormat!!, sshAutoDialogWrapperUIDSL.actionPrefixField.text, sshAutoDialogWrapperUIDSL.packageField.text, "", sshAutoDialogWrapperUIDSL.resourcePathField.selectedItem.toString(), sshAutoDialogWrapperUIDSL.extraParamsField.text, CtrlType.BEAN)
    }

    /**
     * 规则:
     * 固定属性名:ACTION_NAME_PREFIX,PACKAGE
     * 扩展属性名:'EXTRA_PARAM_'+'0...'
     * */
    private fun createActionClassFromTemplate() {
        FileUtil.generateFile(this.sshAutoFormatSetting.actionCodeFormat!!, sshAutoDialogWrapperUIDSL.actionPrefixField.text, sshAutoDialogWrapperUIDSL.packageField.text, "", sshAutoDialogWrapperUIDSL.resourcePathField.selectedItem.toString(), sshAutoDialogWrapperUIDSL.extraParamsField.text, CtrlType.ACTION)
    }

    private fun addActionBeanToAppliction() {
        FileUtil.generateFile("", sshAutoDialogWrapperUIDSL.actionPrefixField.text, sshAutoDialogWrapperUIDSL.packageField.text, "", sshAutoDialogWrapperUIDSL.resourcePathField.selectedItem.toString(), sshAutoDialogWrapperUIDSL.extraParamsField.text, CtrlType.ADD_BEAN_TO_APPLICATION)
    }

    private fun addActionConfigToStruts() {
        FileUtil.generateFile("", sshAutoDialogWrapperUIDSL.actionPrefixField.text, sshAutoDialogWrapperUIDSL.packageField.text, "", sshAutoDialogWrapperUIDSL.resourcePathField.selectedItem.toString(), sshAutoDialogWrapperUIDSL.extraParamsField.text, CtrlType.ADD_CONFIG_TO_STRUTS)
    }


    override fun doValidate(): ValidationInfo? {
        return super.doValidate()
    }
}