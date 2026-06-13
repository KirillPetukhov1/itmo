package view

import i18n.BundleKeys
import i18n.LocaleManager
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.PasswordField
import javafx.scene.control.Separator
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import viewmodel.AuthViewModel
import java.util.Locale

/**
 * Authentication and registration screen.
 *
 * The screen supports two modes controlled by [AuthViewModel.mode]: [AuthViewModel.Mode.LOGIN]
 * and [AuthViewModel.Mode.REGISTER]. The user switches between them via a [Hyperlink] at the
 * bottom of the form. All text labels are locale-aware and update immediately when
 * [LocaleManager.locale] changes.
 *
 * The language selector [ComboBox] is embedded in the form so the user can pick a language
 * before logging in.
 *
 * @property stage the primary stage on which the auth scene is displayed
 * @property viewModel the ViewModel that holds auth state and executes requests
 * @property onLoginSuccess callback invoked on the JavaFX Application Thread after a successful login
 */
class AuthView(
    private val stage: Stage,
    private val viewModel: AuthViewModel,
    private val onLoginSuccess: () -> Unit
) {

    private val titleLabel = Label()
    private val loginLabel = Label()
    private val passwordLabel = Label()

    private val errorLabel = Label().apply {
        textFill = Color.web("#dc2626")
        isWrapText = true
        maxWidth = 340.0
        style = "-fx-font-size: 13px;"
    }

    private val successLabel = Label().apply {
        textFill = Color.web("#16a34a")
        isWrapText = true
        maxWidth = 340.0
        style = "-fx-font-size: 13px;"
    }

    private val loginField = TextField()
    private val passwordField = PasswordField()
    private val submitButton = Button()
    private val toggleLink = Hyperlink()
    private val languageBox = ComboBox<Locale>()

    /**
     * Builds the auth scene, binds controls to the ViewModel, subscribes to locale
     * and mode changes, and shows the stage.
     */
    fun show() {
        stage.scene = buildScene()
        stage.width = 460.0
        stage.height = 560.0
        stage.isResizable = false
        stage.centerOnScreen()

        bindControls()
        setupLanguageBox()
        refreshTexts()

        LocaleManager.localeProperty.addListener { _, _, _ -> refreshTexts() }
        viewModel.mode.addListener { _, _, _ -> refreshTexts() }

        stage.show()
    }

    private fun buildScene(): Scene {
        titleLabel.style = TITLE_STYLE

        loginField.apply {
            maxWidth = Double.MAX_VALUE
            prefHeight = 42.0
            style = FIELD_STYLE
            setOnKeyPressed { e -> if (e.code == KeyCode.ENTER) passwordField.requestFocus() }
        }

        passwordField.apply {
            maxWidth = Double.MAX_VALUE
            prefHeight = 42.0
            style = FIELD_STYLE
            setOnKeyPressed { e -> if (e.code == KeyCode.ENTER) handleSubmit() }
        }

        loginLabel.style = FIELD_LABEL_STYLE
        passwordLabel.style = FIELD_LABEL_STYLE

        val loginSection = VBox(5.0, loginLabel, loginField)
        val passwordSection = VBox(5.0, passwordLabel, passwordField)

        val messageArea = VBox(4.0, errorLabel, successLabel).apply {
            minHeight = 24.0
        }

        submitButton.apply {
            maxWidth = Double.MAX_VALUE
            prefHeight = 46.0
            style = PRIMARY_BUTTON_STYLE
            setOnAction { handleSubmit() }
        }
        VBox.setVgrow(submitButton, Priority.NEVER)

        toggleLink.apply {
            style = "-fx-font-size: 13px; -fx-text-fill: #6366f1;"
            setOnAction { viewModel.toggleMode() }
        }

        val toggleRow = HBox(toggleLink).apply {
            alignment = Pos.CENTER
        }

        val separator = Separator().apply {
            VBox.setMargin(this, Insets(4.0, 0.0, 4.0, 0.0))
        }

        val langLabel = Label().apply {
            style = "-fx-font-size: 12px; -fx-text-fill: #9ca3af;"
        }
        val langRow = HBox(8.0, langLabel, languageBox).apply {
            alignment = Pos.CENTER_RIGHT
        }

        val card = VBox(14.0).apply {
            alignment = Pos.TOP_CENTER
            padding = Insets(44.0, 52.0, 40.0, 52.0)
            maxWidth = 400.0
            style = CARD_STYLE
            children.addAll(
                titleLabel,
                loginSection,
                passwordSection,
                messageArea,
                submitButton,
                toggleRow,
                separator,
                langRow
            )
        }

        val root = StackPane(card).apply {
            style = "-fx-background-color: #eef2ff;"
            alignment = Pos.CENTER
        }

        return Scene(root, 460.0, 560.0)
    }

    private fun bindControls() {
        loginField.textProperty().bindBidirectional(viewModel.loginText)
        passwordField.textProperty().bindBidirectional(viewModel.passwordText)

        errorLabel.textProperty().bind(viewModel.errorText)
        errorLabel.managedProperty().bind(viewModel.errorText.isNotEmpty)
        errorLabel.visibleProperty().bind(viewModel.errorText.isNotEmpty)

        successLabel.textProperty().bind(viewModel.successText)
        successLabel.managedProperty().bind(viewModel.successText.isNotEmpty)
        successLabel.visibleProperty().bind(viewModel.successText.isNotEmpty)

        val busy = viewModel.isLoading
        submitButton.disableProperty().bind(busy)
        loginField.disableProperty().bind(busy)
        passwordField.disableProperty().bind(busy)
        toggleLink.disableProperty().bind(busy)
    }

    private fun setupLanguageBox() {
        languageBox.items.addAll(LocaleManager.supportedLocales)
        languageBox.value = LocaleManager.locale
        val cellFactory = { _: Any? ->
            object : ListCell<Locale>() {
                override fun updateItem(item: Locale?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = item?.takeIf { !empty }?.let { LocaleManager.localeDisplayNames[it] }
                }
            }
        }
        languageBox.setCellFactory { cellFactory(null) }
        languageBox.buttonCell = cellFactory(null)
        languageBox.setOnAction {
            languageBox.value?.let { selected ->
                if (selected != LocaleManager.locale) {
                    LocaleManager.locale = selected
                }
            }
        }
    }

    private fun refreshTexts() {
        val isLogin = viewModel.mode.get() == AuthViewModel.Mode.LOGIN
        stage.title = LocaleManager[BundleKeys.AUTH_WINDOW_TITLE]
        titleLabel.text = LocaleManager[BundleKeys.AUTH_WINDOW_TITLE]
        loginLabel.text = LocaleManager[BundleKeys.AUTH_LOGIN_LABEL]
        passwordLabel.text = LocaleManager[BundleKeys.AUTH_PASSWORD_LABEL]
        submitButton.text = if (isLogin) LocaleManager[BundleKeys.AUTH_LOGIN_BUTTON]
        else LocaleManager[BundleKeys.AUTH_REGISTER_BUTTON]
        toggleLink.text = if (isLogin) LocaleManager[BundleKeys.AUTH_PROMPT_LOGIN]
        else LocaleManager[BundleKeys.AUTH_PROMPT_REGISTER]
    }

    private fun handleSubmit() {
        viewModel.submit(onLoginSuccess)
    }

    private companion object {
        const val CARD_STYLE =
            "-fx-background-color: white;" +
                    "-fx-background-radius: 14;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 24, 0, 0, 6);"

        const val TITLE_STYLE =
            "-fx-font-size: 26px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1e1b4b;"

        const val FIELD_LABEL_STYLE =
            "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #374151;"

        const val FIELD_STYLE =
            "-fx-background-radius: 8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-color: #d1d5db;" +
                    "-fx-border-width: 1;" +
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 8 12 8 12;"

        const val PRIMARY_BUTTON_STYLE =
            "-fx-background-color: #4f46e5;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 9;" +
                    "-fx-cursor: hand;"
    }
}