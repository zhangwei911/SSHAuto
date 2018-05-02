package viz.intellij.plugin.setting

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.Html

import org.jetbrains.annotations.Nls
import viz.intellij.plugin.SSHAutoSettingsForm
import viz.intellij.plugin.SSHAutoSettingsFormUIDSL

import javax.swing.JComponent

class SSHAutoFormatConfigurable : Configurable {
    //    private lateinit var codeField:JTextField
    private var sshAutoFormatSetting = SSHAutoFormatSetting.instance
    private var sshAutoSettingsForm: SSHAutoSettingsForm? = null
//    private var sshAutoSettingsForm: SSHAutoSettingsFormUIDSL? = null
    @Nls
    override fun getDisplayName(): String {
        return "SSHAutoTestSettings"
    }

    override fun createComponent(): JComponent? {
//        val panel = JPanel(BorderLayout())
//        panel.add(createNamePanel())
//        return panel

        if (null == this.sshAutoSettingsForm) {
            this.sshAutoSettingsForm = SSHAutoSettingsForm()
//            this.sshAutoSettingsForm = SSHAutoSettingsFormUIDSL()
        }
        var mainPanel = this.sshAutoSettingsForm!!.mainPanel

        this.sshAutoSettingsForm!!.label_desc!!.text = Html("<html>default params:<br>" +
                "${'$'}{ACTION_NAME_PREFIX}<br>" +
                "${'$'}{PACKAGE}<br>" +
                "extra params:<br>" +
                "prefix:EXTRA_PARAM_<br>" +
                "suffix:0...<html>").text

        return mainPanel
    }

    override fun isModified(): Boolean {
//        return this.sshAutoFormatSetting.actionCodeFormat != codeField.text
        return this.sshAutoFormatSetting.actionCodeFormat != this.sshAutoSettingsForm!!.textArea_action!!.text || this.sshAutoFormatSetting.actionConfigCodeFormat != this.sshAutoSettingsForm!!.textArea_actionConfig!!.text || this.sshAutoFormatSetting.actionBeanCodeFormat != this.sshAutoSettingsForm!!.textArea_actionBean!!.text
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
//        this.sshAutoFormatSetting.actionCodeFormat = codeField.text
        this.sshAutoFormatSetting.actionCodeFormat = this.sshAutoSettingsForm!!.textArea_action!!.text
        this.sshAutoFormatSetting.actionConfigCodeFormat = this.sshAutoSettingsForm!!.textArea_actionConfig!!.text
        this.sshAutoFormatSetting.actionBeanCodeFormat = this.sshAutoSettingsForm!!.textArea_actionBean!!.text
    }

    override fun reset() {
        super.reset()
//        codeField.text = this.sshAutoFormatSetting.actionCodeFormat
        this.sshAutoSettingsForm!!.textArea_action!!.text = this.sshAutoFormatSetting.actionCodeFormat
        this.sshAutoSettingsForm!!.textArea_actionConfig!!.text = this.sshAutoFormatSetting.actionConfigCodeFormat
        this.sshAutoSettingsForm!!.textArea_actionBean!!.text = this.sshAutoFormatSetting.actionBeanCodeFormat
    }

//    private fun createNamePanel(): JComponent {
//        val labeledComponent = LabeledComponent<JTextField>()
//        labeledComponent.text = "Action模版:"
//        codeField = JTextField()
//        labeledComponent.component = codeField
//        codeField.document.addDocumentListener(object : DocumentAdapter() {
//            override fun textChanged(e: DocumentEvent) {
//
//            }
//        })
//        return labeledComponent
//    }
}
