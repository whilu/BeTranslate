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
 * Date: 2016-9-10 15:48
 */
public class BeTranslateForm extends JFrame {
    private JPanel rootPanelContainer;
    private JTextField textFieldInputSrc;
    private JButton btnChooseInput;
    private JComboBox comboBoxLang;
    private JSpinner spinnerValueColumn;
    private JSpinner spinnerKeyColumn;
    private JTextField textFieldOutputPath;
    private JButton btnChooseOutput;
    private JButton btnOk;
    private JSpinner spinnerStartRow;
    private JSpinner spinnerEndRow;
    private JSpinner spinnerSheetIndex;
    private JLabel textHint;
    private JButton btnCancel;

    private static final String[] SUPPORT_LANGS = new String[]{
        "zh-rHK", "zh-rTW", "zh-rCN", "th-rTH", "sv-rSE", "sr-rRS", "sl-rSI", "sk-rSK", "ro-rRO", "pt-rPT",
        "pt-rBR", "pl-rPL", "nl-rNL", "nl-BE", "nb-rNO", "lv-rLV", "lt-rLT", "ko-rKR", "ja-rJP", "it-rIT", "it-rCH",
        "in-rID", "hu-rHU", "hr-rHR", "hi-rIN", "iw-rIL", "fr-rFR", "fr-rCH", "fr-rCA", "fr-rBE", "fi-rFI", "es-rUS",
        "es-rES", "de-rLI", "de-rDE", "de-rCH", "de-rAT", "ca-rES", "bg-rBG", "ar-rIL", "ar-rEG", "en-rZA", "en-rSG",
        "en-rNZ", "en-rIN", "en-rIE", "en-rCA", "en-rAU", "en-rGB", "en-rUS", "vi-rVN", "uk-rUA", "r-rTR", "tl-rPH"
    };

    private FileChooserDescriptor inputFileChooserDesc, outputFileChooserDesc;

    public BeTranslateForm(Project project){
        setContentPane(rootPanelContainer);
        setPreferredSize(new Dimension(500, 240));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        initView(project);
        setVisible(true);
    }

    private void initView(Project project){
        inputFileChooserDesc = FileChooserDescriptorFactory.createSingleFileDescriptor();
        outputFileChooserDesc = FileChooserDescriptorFactory.createSingleFolderDescriptor();

        for (String lang : SUPPORT_LANGS) {
            comboBoxLang.addItem(lang);
        }
        spinnerStartRow.setValue(0);
        spinnerEndRow.setValue(-1);
        spinnerKeyColumn.setValue(0);
        spinnerValueColumn.setValue(1);
        spinnerSheetIndex.setValue(0);
        btnChooseInput.requestFocus();

        btnChooseInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile[] virtualFiles = FileChooser.chooseFiles(inputFileChooserDesc, null, null);
                if (virtualFiles != null && virtualFiles.length > 0){
                    textFieldInputSrc.setText(virtualFiles[0].getPath());
                }
            }
        });
        btnChooseOutput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile[] virtualFiles = FileChooser.chooseFiles(outputFileChooserDesc, null, null);
                if (virtualFiles != null && virtualFiles.length > 0) {
                    textFieldOutputPath.setText(virtualFiles[0].getPath());
                }
            }
        });
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGenerateTranslation();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void onGenerateTranslation(){
        if (TextUtils.isEmpty(textFieldInputSrc.getText())){
            textHint.setText("Input file path can not be empty!");
            btnChooseInput.requestFocus();
            return;
        }else if (TextUtils.isEmpty(textFieldOutputPath.getText())){
            textHint.setText("Output path can not be empty!");
            btnChooseOutput.requestFocus();
            return;
        }else if (TextUtils.isEmpty(comboBoxLang.getSelectedItem().toString())){
            textHint.setText("Target translation language can not be empty!");
            comboBoxLang.requestFocus();
            return;
        }else if (TextUtils.isEmpty(textFieldOutputPath.getText())){
            textHint.setText("Output path can not be empty!");
            textFieldOutputPath.requestFocus();
            return;
        }else if ((int)spinnerSheetIndex.getValue() < 0){
            textHint.setText("Please set the sheet index greater than 0!");
            spinnerSheetIndex.requestFocus();
            return;
        }else if ((int)spinnerStartRow.getValue() < 0){
            textHint.setText("Please set the start row number greater than 0!");
            spinnerStartRow.requestFocus();
            return;
        }/*else if ((int)spinnerEndRow.getValue() < 0){
            // end row < 0, translate all the rows in excel
            textHint.setText("Please set the end row number greater than 0!");
            spinnerEndRow.requestFocus();
            return;
        }*/else if ((int)spinnerKeyColumn.getValue() < 0){
            textHint.setText("Please set the 'key' column number greater than 0!");
            spinnerKeyColumn.requestFocus();
            return;
        }else if ((int)spinnerValueColumn.getValue() < 0){
            textHint.setText("Please set the 'value' column number greater than 0!");
            spinnerValueColumn.requestFocus();
            return;
        }

        boolean success = BeTranslateUtil.doTranslate(
                textFieldInputSrc.getText(),
                textFieldOutputPath.getText(),
                comboBoxLang.getSelectedItem().toString(),
                (int) spinnerSheetIndex.getValue(),
                (int) spinnerStartRow.getValue(),
                (int) spinnerEndRow.getValue (),
                (int) spinnerKeyColumn.getValue(),
                (int) spinnerValueColumn.getValue());
        if (success){
            Messages.showMessageDialog("Generate file '" + textFieldOutputPath.getText() + "/values-" +
                    comboBoxLang.getSelectedItem().toString() + "/strings.xml" + "' success!", "BeTranslate",
                    Messages.getInformationIcon());
            System.exit(0);
        }else {
            Messages.showMessageDialog("Generate file failed!", "BeTranslate", Messages.getWarningIcon());
        }
    }
}
