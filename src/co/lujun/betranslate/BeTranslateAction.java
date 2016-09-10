package co.lujun.betranslate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;

import javax.swing.*;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2016-8-31 10:48
 */
public class BeTranslateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        BeTranslateForm form = new BeTranslateForm(e.getData(PlatformDataKeys.PROJECT));
    }
}
