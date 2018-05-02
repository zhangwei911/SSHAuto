package viz.intellij.plugin.setting

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

import org.jdom.Element

@State(name = "SSHAutoFormatSetting", storages = [(Storage("\$APP_CONFIG$/SSHAutoFormat.xml"))])
class SSHAutoFormatSetting : PersistentStateComponent<Element> {
    var actionCodeFormat: String? = null
        get() = if (field == null) "" else field
    var actionConfigCodeFormat: String? = null
        get() = if (field == null) "" else field
    var actionBeanCodeFormat: String? = null
        get() = if (field == null) "" else field

    override fun getState(): Element? {
        //设置settings显示名称
        val element = Element("SSHAutoTestSettings")
        //设置代码保存key-value
        element.setAttribute("actionCodeFormat", this.actionCodeFormat)
        element.setAttribute("actionConfigCodeFormat", this.actionConfigCodeFormat)
        element.setAttribute("actionBeanCodeFormat", this.actionBeanCodeFormat)
        return element
    }

    override fun loadState(state: Element) {
        this.actionCodeFormat = state.getAttributeValue("actionCodeFormat")
        this.actionConfigCodeFormat = state.getAttributeValue("actionConfigCodeFormat")
        this.actionBeanCodeFormat = state.getAttributeValue("actionBeanCodeFormat")
    }

    companion object {
        val instance: SSHAutoFormatSetting
            get() = ServiceManager.getService(SSHAutoFormatSetting::class.java)
    }
}
