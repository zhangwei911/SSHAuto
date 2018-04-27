package viz.intellij.plugin

import com.intellij.openapi.components.*
import com.intellij.openapi.ui.Messages
import org.jetbrains.annotations.NotNull

class SSHAutoComponent : ApplicationComponent {
    override fun initComponent() {
    }

    override fun disposeComponent() {
    }

    @NotNull
    override fun getComponentName(): String {
        return "SSHAutoComponent"
    }

    fun sayHello() {
        // Show dialog with message
        Messages.showMessageDialog(
                "Hello World!",
                "Sample",
                Messages.getInformationIcon()
        )
    }
}