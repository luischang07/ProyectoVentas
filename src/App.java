import java.awt.Dimension;
import java.sql.Connection;

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
        setSize(900, 700);
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
        app.setContentPane(menu);
        menu.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(menu);
    }

    public static void logout() {
        vista = new Vista(app);
        app.setContentPane(vista);
        vista.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(vista);
    }

    public static void main(String[] args) throws Exception {
        new App();
    }
}
