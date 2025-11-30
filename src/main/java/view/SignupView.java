package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

/**
 * The View for the Signup Use Case.
 */
public class SignupView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "sign up";

    private final SignupViewModel signupViewModel;

    // Fields
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);

    // Buttons
    private final JButton singUpBtn;
    private final JButton cancelBtn;
    private final JButton toLoginBtn;

    // Controller
    private SignupController signupController;

    /**
     * Constructs the SingUp View Panel.
     * @param signupViewModel  the viewmodel containing the singup state
     */
    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());

        // ---------- TITLE ----------
        final JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---------- FORM PANELS ----------
        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.USERNAME_LABEL), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.PASSWORD_LABEL), passwordInputField);
        final LabelTextPanel repeatPasswordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.REPEAT_PASSWORD_LABEL), repeatPasswordInputField);

        // ---------- BUTTON PANEL ----------
        final JPanel buttonPanel = new JPanel();
        toLoginBtn = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        singUpBtn = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        cancelBtn = new JButton(SignupViewModel.CANCEL_BUTTON_LABEL);

        buttonPanel.add(toLoginBtn);
        buttonPanel.add(singUpBtn);
        buttonPanel.add(cancelBtn);

        // ---------- CENTER PANEL ----------
        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(usernameInfo);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(passwordInfo);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(repeatPasswordInfo);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(buttonPanel);

        // ---------- ACTION HANDLERS ----------

        // Sign Up Button
        singUpBtn.addActionListener(e -> {
            final SignupState currentState = signupViewModel.getState();

            signupController.execute(
                    currentState.getUsername(),
                    currentState.getPassword(),
                    currentState.getRepeatPassword()
            );
        });

        // To Log In button
        toLoginBtn.addActionListener(e -> {
            signupController.switchToLoginView();
        });

        // Pressing enter in repeat password field triggers Sign up
        repeatPasswordInputField.addActionListener(e -> {
            final SignupState currentState = signupViewModel.getState();

            signupController.execute(
                    currentState.getUsername(),
                    currentState.getPassword(),
                    currentState.getRepeatPassword()
            );
        });

        // Cancel Button clears fields
        cancelBtn.addActionListener(this);

        addUsernameListener();
        addPasswordListener();
        addRepeatPasswordListener();

        // Wrapper panel for vertical centering
        final JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(centerPanel);
        this.add(wrapper, BorderLayout.CENTER);
    }

    private void addUsernameListener() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    private void addPasswordListener() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    private void addRepeatPasswordListener() {
        repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == cancelBtn) {
            usernameInputField.setText("");
            passwordInputField.setText("");
            repeatPasswordInputField.setText("");

            final SignupState currentState = signupViewModel.getState();
            currentState.setUsername("");
            currentState.setPassword("");
            currentState.setRepeatPassword("");
            signupViewModel.setState(currentState);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }
}
