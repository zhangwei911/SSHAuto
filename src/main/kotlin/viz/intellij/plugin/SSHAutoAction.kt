package viz.intellij.plugin

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import java.io.File
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.AnActionEvent




class SSHAutoAction : AnAction("SSHAuto") {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project as Project
//        Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon())
        val application = ApplicationManager.getApplication()
        var sshAutoComponent = application.getComponent(SSHAutoComponent::class.java)
//        sshAutoComponent.sayHello()

        object : WriteCommandAction.Simple<EmptyEntity>(project) {
            @Throws(Throwable::class)
            protected override fun run() {
                var file = File("test.txt")
                if(file.exists()){
                    file.delete()
                }
                file.createNewFile()
            }
        }.execute()

        var psiClass = getPsiClassFromContext(event)
        var dialogWrapper = SSHAutoDialogWrapper(project)
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