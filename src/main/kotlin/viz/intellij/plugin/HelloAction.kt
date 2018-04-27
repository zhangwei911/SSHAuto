package viz.intellij.plugin

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import java.io.File


class HelloAction : AnAction("Hello") {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project as Project
//        Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon())
        val application = ApplicationManager.getApplication()
        var sshAutoComponent = application.getComponent(SSHAutoComponent::class.java)
        sshAutoComponent.sayHello()

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

//        var dialogWrapper = SSHAutoDialogWrapper(project)
//        dialogWrapper.show()
    }
}