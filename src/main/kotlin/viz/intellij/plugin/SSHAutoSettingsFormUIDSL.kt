package viz.intellij.plugin

import com.intellij.ui.layout.panel
import javax.swing.*

class SSHAutoSettingsFormUIDSL {
    val label_desc = JLabel()
    val textArea_action = JTextArea()
    val textArea_actionConfig = JTextArea()
    val textArea_actionBean = JTextArea()
    var mainPanel = JPanel()

    constructor(){
        settingsPanel()
    }

    fun settingsPanel(): JPanel {
        mainPanel = panel {
            row{ label_desc() }
            row("Action Template:") { textArea_action() }
            row("ActionConfig Template:") { textArea_actionConfig() }
            row("ActionBean Template:") { textArea_actionBean() }
        }
        return mainPanel
    }

}