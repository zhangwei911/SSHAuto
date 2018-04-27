package viz.intellij.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.ImageIcon
import javax.swing.text.StyleConstants.setIcon
import java.util.Calendar
import com.intellij.util.ContentsUtil.addContent
import java.awt.event.ActionListener
import com.sun.java.accessibility.util.AWTEventMonitor.addActionListener
import java.awt.event.ActionEvent
import javax.swing.JPanel
import javax.swing.JButton
import javax.swing.JLabel


class MyToolWindowFactory:ToolWindowFactory{
    private var refreshToolWindowButton: JButton? = null
    private var hideToolWindowButton: JButton? = null
    private var currentDate: JLabel? = null
    private var currentTime: JLabel? = null
    private var timeZone: JLabel? = null
    private var myToolWindowContent: JPanel? = null
    private var myToolWindow: ToolWindow? = null


    constructor(){
        hideToolWindowButton!!.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                myToolWindow!!.hide(null)
            }
        })
        refreshToolWindowButton!!.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent) {
                this@MyToolWindowFactory.currentDateTime()
            }
        })
    }

    // Create the tool window content.
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        myToolWindow = toolWindow
        this.currentDateTime()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(myToolWindowContent, "", false)
        toolWindow.contentManager.addContent(content)

    }

    fun currentDateTime() {
        // Get current date and time
        val instance = Calendar.getInstance()
        currentDate!!.setText(instance.get(Calendar.DAY_OF_MONTH).toString() + "/"
                + (instance.get(Calendar.MONTH) + 1).toString() + "/" +
                instance.get(Calendar.YEAR).toString())
        currentDate!!.setIcon(ImageIcon(javaClass.getResource("/myToolWindow/Calendar-icon.png")))
        val min = instance.get(Calendar.MINUTE)
        val strMin: String
        if (min < 10) {
            strMin = "0" + min.toString()
        } else {
            strMin = min.toString()
        }
        currentTime!!.setText(instance.get(Calendar.HOUR_OF_DAY).toString() + ":" + strMin)
        currentTime!!.setIcon(ImageIcon(javaClass.getResource("/myToolWindow/Time-icon.png")))
        // Get time zone
        val gmt_Offset = instance.get(Calendar.ZONE_OFFSET).toLong() // offset from GMT in milliseconds
        var str_gmt_Offset = (gmt_Offset / 3600000).toString()
        str_gmt_Offset = if (gmt_Offset > 0) "GMT + $str_gmt_Offset" else "GMT - $str_gmt_Offset"
        timeZone!!.setText(str_gmt_Offset)
        timeZone!!.setIcon(ImageIcon(javaClass.getResource("/myToolWindow/Time-zone-icon.png")))


    }

}