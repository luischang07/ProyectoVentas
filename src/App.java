import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class App extends JFrame {

    private static App app;
    private static Vista vista;
    private static menu menu;
    private JPanel panel;

    public App() {
        super("App");
        app = this;
        HazInterfaz();
        vista = new Vista(app);
        setContentPane(panel);
        setVisible(true);

    }

    public void HazInterfaz() {
        setSize(500, 600);
        setMinimumSize(new Dimension(350, 350));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        panel = new Vista(app);

        add(panel);
    }

    public static void login(Connection conexion) {
        // App.app.setSize(400, 600);
        menu = new menu(conexion);
        // FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(menu);
        menu.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(menu);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void logout() {
        vista = new Vista(app);

        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(vista);
        vista.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(vista);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
        }
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        new App();
        // app.setContentPane(app.vista);
    }
}
