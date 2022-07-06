package eu.jozoproductions.panels;

import eu.jozoproductions.Cached;
import eu.jozoproductions.Main;
import eu.jozoproductions.firebase.DS_GameVersion;
import eu.jozoproductions.firebase.FirebaseManager;
import eu.jozoproductions.ui.FontManager;
import eu.jozoproductions.ui.ImageUI;
import eu.jozoproductions.ui.Text;
import eu.jozoproductions.ui.WindowBar;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WelcomePanel extends WizardPanel {

    //UI
    public static ImageUI headerImage;
    public static Text headerTitle, descriptionText;
    public static Text installerTitle, gameTitle;
    public static ImageUI viewInstallerAgreement, uninstallGame, installGame;

    @Override
    public void OnUISetup() {
        try {
            setLayout(null);

            //-<UI>-
            //Header image
            headerImage = new ImageUI(this, 10, 10, 64, 64, "icon", true, true, false);

            //Side titles
            installerTitle = new Text(this, 10, Main.WIN_HEIGHT-WindowBar.BAR_HEIGHT - 84, 140, 24, "Installer");
            installerTitle.setFont(FontManager.exoLightFont, Font.BOLD, 19);
            installerTitle.setVerticalAlignment(SwingConstants.CENTER); installerTitle.setHorizontalAlignment(SwingConstants.CENTER);

            gameTitle = new Text(this, Main.WIN_WIDTH-235, Main.WIN_HEIGHT-WindowBar.BAR_HEIGHT - 144, 300, 24, "SOW: Annihilation");
            gameTitle.setFont(FontManager.exoLightFont, Font.BOLD, 19);
            gameTitle.setVerticalAlignment(SwingConstants.CENTER); gameTitle.setHorizontalAlignment(SwingConstants.CENTER);

            //Action buttons
            viewInstallerAgreement = new ImageUI(this, 10, Main.WIN_HEIGHT-WindowBar.BAR_HEIGHT - 50, 140, 40, "installer_agreement", true, true, true);
            uninstallGame = new ImageUI(this, Main.WIN_WIDTH-150, Main.WIN_HEIGHT-WindowBar.BAR_HEIGHT - 100, 140, 40, "game_uninstall", true, true, true);
            installGame = new ImageUI(this, Main.WIN_WIDTH-150, Main.WIN_HEIGHT-WindowBar.BAR_HEIGHT - 50, 140, 40, "download_game", true, true, true);

            //Listeners
            viewInstallerAgreement.setClickCallback(() -> {
                try {
                    //Read file content
                    String content = new String(Files.readAllBytes(Paths.get(getClass().getResource("/agreement.txt").toURI())), StandardCharsets.UTF_8);
                    Main.licensePanel.setValues("Installer agreement", content);

                    WizardPanel.ChangePanels(WelcomePanel.this, Main.licensePanel, WizardPanel.SwipeSide.LEFT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            uninstallGame.setClickCallback(() -> {
                Main.uninstallPanel.set_uninstallGame();
                WizardPanel.ChangePanels(WelcomePanel.this, Main.uninstallPanel, SwipeSide.LEFT);
            });

            installGame.setClickCallback(new ImageUI.ClickCallback() {
                @Override
                public void OnClick() {
                    FirebaseManager.initGameVersions(() -> {
                        //Update version select box model
                        String[] items = new String[Cached.gameVersions.size()];
                        for (int i = 0; i < items.length; i++) {
                            DS_GameVersion gv = Cached.gameVersions.get(i);
                            items[i] = gv.ver + " (" + gv.tag + ")";
                        }
                        Main.downloadSOWPanel.versionSelect.setModel(new DefaultComboBoxModel(items));
                    });

                    WizardPanel.ChangePanels(WelcomePanel.this, Main.downloadSOWPanel, SwipeSide.LEFT);
                }
            });

            //Title & description
            headerTitle = new Text(this, 84, 10, Main.WIN_WIDTH, 64, "Welcome to SOW:A Setup Wizard!");
            headerTitle.setFont(FontManager.exoLightFont, Font.BOLD, 25);
            headerTitle.setVerticalAlignment(SwingConstants.CENTER);

            descriptionText = new Text(this, 10, 84, Main.WIN_WIDTH-20, 400, "<html>This Setup Wizard helps you with downloading State of War: Annihilation. Make sure you are connected to internet before we begin.</html>");
            descriptionText.setFont(FontManager.exoLightFont, Font.PLAIN, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
