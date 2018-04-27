package viz.intellij.plugin.setting

import com.ctt.format.setting.StatementGenerator
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

import org.jdom.Element

@State(name = "SSHAutoFormatSetting", storages = [(Storage("\$APP_CONFIG$/SSHAutoFormat.xml"))])
class SSHAutoFormatSetting : PersistentStateComponent<Element> {
    var actionCodeFormat: String? = null
        get() = if (field == null) "" else field

    override fun getState(): Element? {
        val element = Element("SSHAutoTestSettings")
        element.setAttribute("actionCodeFormat", this.actionCodeFormat)
        return element
    }

    override fun loadState(state: Element) {
        this.actionCodeFormat = state.getAttributeValue("setActionCodeFormat")
    }

    companion object {
        val instance: SSHAutoFormatSetting
            get() = ServiceManager.getService(SSHAutoFormatSetting::class.java)
    }
}
