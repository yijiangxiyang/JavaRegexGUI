import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUi extends Application implements ItemListener {
    private JTextField etRegex;
    private JTextArea etMsg;
    private JButton btnMatch;
    private JButton btnGetCode;
    private JButton btnHelp;
    private JRadioButton rbMatch;
    private JRadioButton rbNumber;

    private JRadioButton rbLetter;
    private JRadioButton rbCombination;
    private JTextArea etResult;
    private JPanel root;
    private JRadioButton rbNumberL;
    private JRadioButton rbLetterL;
    private JRadioButton rbCombinationL;

    private String regex;
    private String msg;

    public static void main(String[] args) {
        JFrame frame = new JFrame("正则测试");
        frame.setSize(760, 580);
        frame.setContentPane(new RegexUi().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        int xPos = (width - frame.getWidth()) / 2;
        int yPos = (height - frame.getWidth()) / 2;
        frame.setLocation(xPos, yPos);
        frame.pack();
        frame.setVisible(true);
    }

    public RegexUi() {
        btnMatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                match();
            }
        });
        btnGetCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCode();
            }
        });
        btnHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                help();
            }
        });

        rbMatch.addItemListener(this);
        rbNumber.addItemListener(this);
        rbLetter.addItemListener(this);
        rbCombination.addItemListener(this);
        rbNumberL.addItemListener(this);
        rbLetterL.addItemListener(this);
        rbCombinationL.addItemListener(this);
        ButtonGroup toggleGroup = new ButtonGroup();
        toggleGroup.add(rbMatch);
        toggleGroup.add(rbNumber);
        toggleGroup.add(rbLetter);
        toggleGroup.add(rbCombination);
        toggleGroup.add(rbNumberL);
        toggleGroup.add(rbLetterL);
        toggleGroup.add(rbCombinationL);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }


    private void match() {
        regex = etRegex.getText();
        msg = etMsg.getText();
        boolean match;
        ArrayList<String> results = getMatchList(msg, regex);
        match = !results.isEmpty();
        if (match) {
            StringBuilder sb = new StringBuilder("匹配到 "+results.size()+" 条:\n\n");
            for (String result: results) {
                sb.append(result).append("\n\n");
            }
            etResult.setText(sb.toString());
        } else {
            etResult.setText("不匹配");
        }
    }

    private ArrayList<String> getMatchList(String msg, String regex) {
        ArrayList<String> result = new ArrayList<>();
        try {
            String[] msgs = msg.split("\n");
            for (String msg1: msgs) {
                boolean match = Pattern.matches(regex, msg1);
                if (match) {
                    result.add(msg1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void getCode() {
        String code = null;
        try {
            regex = etRegex.getText();
            msg = etMsg.getText().replaceAll("[\r\n\t]", "");
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(msg);
            if (m.find()) {
                code = m.group(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (code == null || code.isEmpty()) {
            etResult.setText("获取验证码失败，请检查正则和内容是否正确！");
        } else {
            etResult.setText("验证码： " + code);
        }
    }

    private void help() {
        StringBuilder sb = new StringBuilder();
        sb.append("通用匹配正则：(?i).*key1.*key2.*key3.*").append("\n");
        sb.append("说明：“(?i)”表示忽略大小写，“.*” 表示匹配任意长度任何字符，“key1”,“key2”,“key3”表示要匹配的关键词，关键词个数不限，越多越准确。");
        sb.append("关键词的顺序必须按在原内容中的顺序填写.").append("\n\n");
        sb.append("举例：").append("\n");
        sb.append("回复内容：You are requesting for Garmeland Weekly Service.Daily. To start with Autorenewal, send Y to 16306.").append("\n");
        sb.append("拦截内容：You have requested for Garmeland Weekly Service.Daily charge 9.33tk , send N to 16306 to stop.").append("\n");
        sb.append("明显 Garmeland 是主要关键词，但在回复和拦截中都存在，因此回复正则和拦截正则都至少还需要一个关键词，而且还要能把这两个正则区分开来以正确匹配，");
        sb.append("回复正则可以选 “start” 或 “send Y”, 拦截正则则选 “stop” 或 “send N”, 再把关键词按顺序填到公式里去。因此可以这样写：").append("\n");
        sb.append("回复正则：(?i).*Garmeland.*start.* 或 (?i).*Garmeland.*send Y.*").append("\n");
        sb.append("拦截正则：(?i).*Garmeland.*stop.* 或 (?i).*Garmeland.*send N.*").append("\n\n");

        sb.append("数字验证码正则：(?i).*(\\d{4}).*  表示截取4个数字，或 (?i).*xxx(\\d{4})yyy.* 表示截取“xxx”和“yyy”之间的4个数字").append("\n");
        sb.append("字母验证码正则：(?i).*([a-zA-Z]{4}).*  表示截取4个字母，或 (?i).*xxx([a-zA-Z]{4})yyy.* 表示截取“xxx”和“yyy”之间的4个字母").append("\n");
        sb.append("字母+数字验证码正则：(?i).*([a-zA-Z0-9]{4}).*  表示截取4个字母数字组合，或 (?i).*xxx([a-zA-Z0-9{4})yyy.* 表示截取“xxx”和“yyy”之间的4个字母数字。").append("\n");
        sb.append("说明：第二个小括号内表示要截取的内容。大括号内的4为验证码长度，“xxx”和“yyy”为验证码前后的内容，请根据实际情况填写！").append("\n");
        sb.append("举例：\n");
        sb.append("回复内容：Your verification code is: 5235, You are logging in game for green tiger.").append("\n");
        sb.append("因验证码为4个数字，则可以用：(?i).*(\\d{4}).*").append("\n");
        sb.append("当验证码为字母：Your verification code is: mlcy, You are logging in game for green tiger.").append("\n");
        sb.append("因为内容和验证码都为英文，如果我们简单套用 (?i).*([a-zA-Z]{4}).* 则内容中任意4个相连的字母都满足，获取到的验证码不准，所以要加限定词来准确定位。");
        sb.append("可以把验证码前后的内容加到正则里，前面说过小括号内表示要截取的内容，所以取验证码前的“code is: ” 填到小括号前面和验证码后的 “,” 填到小括号后面， 则 (?i).*code is: ([a-zA-Z]{4}),.* 注意要原封不动拷贝，包括符号空格。");
        sb.append("表示明确要截取的是 “code is: ” 和 “,” 之间的4个字母。当然这里辅助定位的内容可以灵活配置，也可以是 “is: ” 和 “, You”，则 (?i).*is: ([a-zA-Z]{4}), You.* ").append("\n");
        etResult.setText(sb.toString());
        etResult.setCaretPosition(0);//滚动到开始位置
    }

    private void fillMatchRegex() {
        etRegex.setText("(?i).*key1.*key2.*");
    }
    private void fillNumberRegex() {
        etRegex.setText("(?i).*(\\d{4}).*");
    }
    private void fillNumberRegexL() {
        etRegex.setText("(?i).*xxx(\\d{4})xxx.*");
    }
    private void fillLetterRegex() {
        etRegex.setText("(?i).*([a-zA-Z]{4}).*");
    }
    private void fillLetterRegexL() {
        etRegex.setText("(?i).*xxx([a-zA-Z]{4})yyy.*");
    }
    private void fillCombinationRegex() {
        etRegex.setText("(?i).*([a-zA-Z0-9]{4}).*");
    }
    private void fillCombinationRegexL() {
        etRegex.setText("(?i).*xxx([a-zA-Z0-9]{4})yyy.*");
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == rbMatch) {
            fillMatchRegex();
        } else if (e.getSource() == rbNumber) {
            fillNumberRegex();
        } else if (e.getSource() == rbNumberL) {
            fillNumberRegexL();
        } else if (e.getSource() == rbLetter) {
            fillLetterRegex();
        } else if (e.getSource() == rbLetterL) {
            fillLetterRegexL();
        } else if (e.getSource() == rbCombination) {
            fillCombinationRegex();
        } else if (e.getSource() == rbCombinationL) {
            fillCombinationRegexL();
        }
    }
}
