package co.lujun.betranslate;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 16/9/13 下午9:53
 */
public class ExportExcelForm extends JFrame {

    private JPanel rootPanelContainer;
    private JButton btnChooseOutputPath;
    private JButton btnChooseInputPath;
    private JTextField textFieldInput;
    private JTextField textFieldOutput;
    private JButton btnOk;
    private JButton btnCancel;
    private JLabel textHint;

    private FileChooserDescriptor inputFileChooserDesc, outputFileChooserDesc;

    public ExportExcelForm(Project project){
        super("Export 'string.xml' to Excel");
        setContentPane(rootPanelContainer);
        setPreferredSize(new Dimension(350, 150));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        initView(project);
    }

    private void initView(Project project) {
        inputFileChooserDesc = FileChooserDescriptorFactory.createSingleFileDescriptor();
        outputFileChooserDesc = FileChooserDescriptorFactory.createSingleFileDescriptor();
        btnChooseInputPath.requestFocus();

        btnChooseInputPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile[] virtualFiles = FileChooser.chooseFiles(inputFileChooserDesc, null, null);
                if (virtualFiles != null && virtualFiles.length > 0){
                    textFieldInput.setText(virtualFiles[0].getPath());
                }
            }
        });
        btnChooseOutputPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile[] virtualFiles = FileChooser.chooseFiles(outputFileChooserDesc, null, null);
                if (virtualFiles != null && virtualFiles.length > 0) {
                    textFieldOutput.setText(virtualFiles[0].getPath());
                }
            }
        });
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doExport();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void doExport(){
        if (TextUtils.isEmpty(textFieldInput.getText())){
            textHint.setText("Input file path can not be empty!");
            btnChooseInputPath.requestFocus();
            return;
        }else if (TextUtils.isEmpty(textFieldOutput.getText())){
            textHint.setText("Output path can not be empty!");
            btnChooseOutputPath.requestFocus();
            return;
        }
        String outputFilePath = textFieldOutput.getText() + "/BT_export_string_xml_" +
                System.currentTimeMillis() + ".xlsx";
        boolean exportSuccess = BeTranslateUtil.generateExcel(textFieldInput.getText(), outputFilePath);
        if (exportSuccess){
            Messages.showMessageDialog("Export file '" + outputFilePath + "' success!", "BeTranslate",
                    Messages.getInformationIcon());
            dispose();
        }else {
            Messages.showMessageDialog("Export file failed!", "BeTranslate", Messages.getWarningIcon());
        }
    }
}
