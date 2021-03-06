package viz.intellij.plugin

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.AnActionEvent
import viz.intellij.plugin.setting.SSHAutoFormatSetting


class SSHAutoAction : AnAction("SSHAuto") {
    private var sshAutoFormatSetting:SSHAutoFormatSetting? = null

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project as Project
//        Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon())
        val application = ApplicationManager.getApplication()
        var sshAutoComponent = application.getComponent(SSHAutoComponent::class.java)
//        sshAutoComponent.sayHello()

//        object : WriteCommandAction.Simple<EmptyEntity>(project) {
//            @Throws(Throwable::class)
//            protected override fun run() {
//                var file = File("test.txt")
//                if(file.exists()){
//                    file.delete()
//                }
//                file.createNewFile()
//            }
//        }.execute()

        this.sshAutoFormatSetting = SSHAutoFormatSetting.instance
        var psiClass = getPsiClassFromContext(event)
        println(project.basePath)
        var dialogWrapper = SSHAutoDialogWrapper(project)
//        var dialogWrapper = SSHAutoDialogWrapperUI(project)
        dialogWrapper.show()

    }

    private fun getPsiClassFromContext(e: AnActionEvent): PsiClass? {
        val psiFile = e.getData(LangDataKeys.PSI_FILE)
        val editor = e.getData(PlatformDataKeys.EDITOR)

        if (psiFile == null || editor == null) {
            return null
        }

        val offset = editor.caretModel.offset
        val element = psiFile.findElementAt(offset)

        return PsiTreeUtil.getParentOfType(element, PsiClass::class.java)
    }
}