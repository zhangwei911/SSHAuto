package viz.intellij.plugin.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.DocumentAdapter

import org.jetbrains.annotations.Nls
import java.awt.BorderLayout

import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent

class SSHAutoFormatConfigurable : Configurable {
    private lateinit var codeField:JTextField
    private var sshAutoFormatSetting = SSHAutoFormatSetting.instance
    @Nls
    override fun getDisplayName(): String {
        return "SSHAutoTestSettings"
    }

    override fun createComponent(): JComponent? {
        val panel = JPanel(BorderLayout())
        panel.add(createNamePanel())
        return panel
    }

    override fun isModified(): Boolean {
        return this.sshAutoFormatSetting.actionCodeFormat != codeField.text
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        this.sshAutoFormatSetting.actionCodeFormat = codeField.text
    }

    override fun reset() {
        super.reset()
        codeField.text = this.sshAutoFormatSetting.actionCodeFormat
    }

    private fun createNamePanel(): JComponent {
        val labeledComponent = LabeledComponent<JTextField>()
        labeledComponent.text = "Action模版:"
        codeField = JTextField()
        labeledComponent.component = codeField
        codeField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {

            }
        })
        return labeledComponent
    }
}
