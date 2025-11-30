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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "log in";

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton logIn;
    private final JButton signUp;
    private final JButton cancel;

    private LoginController loginController;

    /**
     * Constructs the LogIn View Panel.
     *
     * @param loginViewModel the ViewModel containing the login state
     * @param viewManagerModel The ViewManagerModel used for changing screens
     */
    public LoginView(LoginViewModel loginViewModel, ViewManagerModel viewManagerModel) {
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());

        // ---------- TITLE ----------
        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---------- FORM PANELS ----------
        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);

        // ---------- BUTTON PANEL ----------
        final JPanel buttonPanel = new JPanel();
        logIn = new JButton("Log In");
        signUp = new JButton("Sign Up");
        cancel = new JButton("Cancel");

        buttonPanel.add(logIn);
        buttonPanel.add(signUp);
        buttonPanel.add(cancel);

        // ---------- CENTER PANEL ----------
        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(usernameInfo);
        centerPanel.add(usernameErrorField);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(passwordInfo);
        centerPanel.add(passwordErrorField);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(buttonPanel);

        // ---------- ACTION HANDLERS ----------

        // Login button
        logIn.addActionListener(e -> {
            final LoginState currentState = loginViewModel.getState();
            loginController.execute(
                    currentState.getUsername(),
                    currentState.getPassword()
            );
        });

        // Pressing enter in password field triggers login
        passwordInputField.addActionListener(e -> {
            final LoginState currentState = loginViewModel.getState();
            loginController.execute(
                    currentState.getUsername(),
                    currentState.getPassword()
            );
        });

        // Sign Up button switches screens
        signUp.addActionListener(e -> {
            viewManagerModel.setState("sign up");
            viewManagerModel.firePropertyChange();
        });

        // Cancel clears fields
        cancel.addActionListener(this);

        // Username listener
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
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

        // Password listener
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
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

        // Wrapper panel for vertical centering
        final JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(centerPanel);
        this.add(wrapper, BorderLayout.CENTER);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == cancel) {
            usernameInputField.setText("");
            passwordInputField.setText("");

            final LoginState currentState = loginViewModel.getState();
            currentState.setUsername("");
            currentState.setPassword("");
            loginViewModel.setState(currentState);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getLoginError());
    }

    private void setFields(LoginState state) {
        usernameInputField.setText(state.getUsername());
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
