package viz.intellij.plugin

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JTextField
class SSHAutoDialogWrapperUIDSL {
    val actionPrefixField = JTextField()
    val packageField = JTextField()
    val extraParamsField = JTextField()
    val codePathField = ComboBox<String>()
    val resourcePathField = ComboBox<String>()

    fun dialogPanel(): JPanel {
        return panel {
            row("Action Prefix:") { actionPrefixField() }
            row("Package:") { packageField() }
            row("Extra Params:") { extraParamsField() }
            row("Code Path:") { codePathField(CCFlags.growX,CCFlags.pushX) }
            row("Resource Path:") { resourcePathField(CCFlags.growX,CCFlags.pushX) }
        }
    }

}