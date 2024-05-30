
import java.sql.*;

import raven.toast.Notifications;

public class ConexionDB {

    private String servidor;
    private String basededatos;
    private String usuario;
    private String password;

    static Connection conexion;
    static String conexionUrl;

    public ConexionDB(String servidor, String basededatos, String usuario, String password) {
        this.servidor = servidor;
        this.basededatos = basededatos;
        this.usuario = usuario;
        this.password = password;
    }

    public void getConexion() {

        conexionUrl = "jdbc:sqlserver://" + servidor + ";"
                + "database=" + basededatos + ";"
                + "user=" + usuario + ";"
                + "password=" + password + ";"
                + "trustServerCertificate=true;"
                + "loginTimeout=5;";
        // conexionUrl = "jdbc:sqlserver://" + "DESKTOP-BP14MON" + ";"
        // + "database=" + "Envios" + ";"
        // + "user=" + usuario + ";"
        // + "password=" + password + ";"
        // + "trustServerCertificate=true;"
        // + "loginTimeout=5;";
        try {

            conexion = DriverManager.getConnection(conexionUrl);
            if (conexion != null) {
                System.out.println("Conectado a la base de datos");
                App.login(conexion);
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                    "Error al conectar a la base de datos");
            System.err.println(e.getMessage());
        }

    }

}
