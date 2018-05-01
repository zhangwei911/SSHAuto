package viz.intellij.plugin.setting

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.VirtualFile

import org.jetbrains.annotations.Nls
import viz.intellij.plugin.SSHAutoSettingsForm

import javax.swing.JComponent

class SSHAutoFormatConfigurable : Configurable {
//    private lateinit var codeField:JTextField
    private var sshAutoFormatSetting = SSHAutoFormatSetting.instance
    private var sshAutoSettingsForm: SSHAutoSettingsForm? = null
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
        }
        var mainPanel = this.sshAutoSettingsForm!!.mainPanel

        this.sshAutoSettingsForm!!.button_xml!!.addActionListener {
            var fileChooserDescriptor = FileChooserDescriptor(false,true,false,false,false,false)
            var selectDirectoryXml = FileChooser.chooseFile(fileChooserDescriptor,null,null)
            this.sshAutoSettingsForm!!.textField_xml!!.text = selectDirectoryXml!!.path
        }

        return mainPanel
    }

    override fun isModified(): Boolean {
//        return this.sshAutoFormatSetting.actionCodeFormat != codeField.text
        return this.sshAutoFormatSetting.actionCodeFormat != this.sshAutoSettingsForm!!.textArea_action!!.text || this.sshAutoFormatSetting.actionConfigCodeFormat != this.sshAutoSettingsForm!!.textArea_actionConfig!!.text || this.sshAutoFormatSetting.actionBeanCodeFormat != this.sshAutoSettingsForm!!.textArea_actionBean!!.text || this.sshAutoFormatSetting.xmlPath != this.sshAutoSettingsForm!!.textField_xml!!.text
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
//        this.sshAutoFormatSetting.actionCodeFormat = codeField.text
        this.sshAutoFormatSetting.actionCodeFormat = this.sshAutoSettingsForm!!.textArea_action!!.text
        this.sshAutoFormatSetting.actionConfigCodeFormat = this.sshAutoSettingsForm!!.textArea_actionConfig!!.text
        this.sshAutoFormatSetting.actionBeanCodeFormat = this.sshAutoSettingsForm!!.textArea_actionBean!!.text
        this.sshAutoFormatSetting.xmlPath = this.sshAutoSettingsForm!!.textField_xml!!.text
    }

    override fun reset() {
        super.reset()
//        codeField.text = this.sshAutoFormatSetting.actionCodeFormat
        this.sshAutoSettingsForm!!.textArea_action!!.text = this.sshAutoFormatSetting.actionCodeFormat
        this.sshAutoSettingsForm!!.textArea_actionConfig!!.text = this.sshAutoFormatSetting.actionConfigCodeFormat
        this.sshAutoSettingsForm!!.textArea_actionBean!!.text = this.sshAutoFormatSetting.actionBeanCodeFormat
        this.sshAutoSettingsForm!!.textField_xml!!.text = this.sshAutoFormatSetting.xmlPath
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
