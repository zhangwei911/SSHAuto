package viz.intellij.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.psi.PsiClass
import com.intellij.ui.DocumentAdapter

import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.util.Objects

import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.KeyStroke
import javax.swing.event.DocumentEvent

class SSHAutoDialogWrapper : DialogWrapper {
    private var contentPane: JPanel? = null
    private var buttonOK: JButton? = null
    private var buttonCancel: JButton? = null
    private var textField_actionName_prefix: JTextField? = null
    private var actionName_prefix: String? = null

    constructor(psiClass: PsiClass) : super(psiClass.project) {
        title = "SSHAuto"
        init()
    }

    constructor(project: Project) : super(project) {
        title = "SSHAuto"
        init()
    }

    override fun init() {
        super.init()
        buttonOK!!.addActionListener { onOK() }

        buttonCancel!!.addActionListener { }

        // call onCancel() on ESCAPE
        contentPane!!.registerKeyboardAction({ }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(BorderLayout())
        panel.add(createNamePanel())
        return panel
    }

    private fun createNamePanel(): JComponent {
        val labeledComponent = LabeledComponent<JTextField>()
        labeledComponent.text = "Action前缀:"
        val nameField = JTextField()
        //15623297885
        labeledComponent.component = nameField
        nameField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {

            }
        })
        return labeledComponent
    }

    private fun onOK() {
        // add your code here
        actionName_prefix = textField_actionName_prefix!!.text
        createActionClassFromTemplate()
        createActionBeanFromTemplate()
        createActionConfigFromTemplate()
        dispose()
    }

    private fun createActionConfigFromTemplate() {

    }

    private fun createActionBeanFromTemplate() {

    }

    private fun createActionClassFromTemplate() {

    }

    private fun getContent(inputFile: String, outputFile: String): String {
        val content = StringBuilder()
        try {
            val bis = BufferedInputStream(FileInputStream(File(inputFile)))
            //10M缓存
            val `in` = BufferedReader(InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024)
            while (`in`.ready()) {
                content.append(`in`.readLine()).append("\n")
            }
            `in`.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return content.toString()
    }

    private fun write(content: String, outputFile: String) {
        var fw: FileWriter? = null
        try {
            fw = FileWriter(outputFile)
            fw.append(content)
            fw.flush()
            fw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun doValidate(): ValidationInfo? {
        return super.doValidate()
    }
}
