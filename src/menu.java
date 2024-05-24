
import java.sql.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import mode.Rutinas2;
import raven.toast.Notifications;

public class menu extends JPanel implements ComponentListener, ActionListener, ItemListener, FocusListener {

    private JPanel panel;
    private JPanel panelContent;
    private JPanel pnlTabla;

    private JComboBox<String> cmbTablas;
    private JComboBox<String> cmbFamid;

    private JLabel lblFamid;
    private JLabel lblFamNombre;
    private JLabel lblArtId;
    private JLabel lblArtNombre;
    private JLabel lblArtDescripcion;
    private JLabel lblArtPrecio;

    private JTextField txtFamid;
    private JTextField txtFamNombre;
    private JTextField txtArtId;
    private JTextField txtArtNombre;
    private JTextField txtArtDescripcion;
    private JTextField txtArtPrecio;

    private JButton btnLimpiar;
    private JButton btnGuardar;
    private JButton btnBuscar;
    private JButton btnEliminar;

    private JRadioButton rdNuevo;

    private JRadioButton rdModificar;

    private ButtonGroup grupo;

    private JTable table;
    private JScrollPane scroll;
    private DefaultTableModel modelo;

    private boolean selected = false;
    private boolean band = false;

    private ComponenteHeader componenteHeader;

    public menu(Connection conexion) {
        init();
        HazEscuchas();
    }

    private void HazEscuchas() {
        addComponentListener(this);
        txtArtId.addFocusListener(this);
        cmbTablas.addItemListener(this);
        cmbTablas.addActionListener(this);
        btnBuscar.addActionListener(this);
        btnEliminar.addActionListener(this);
        btnLimpiar.addActionListener(this);
        btnGuardar.addActionListener(this);
        rdModificar.addActionListener(this);
        rdNuevo.addActionListener(this);
    }

    public void init() {
        setLayout(null);

        componenteHeader = new ComponenteHeader(this);
        add(componenteHeader, BorderLayout.NORTH);

        // PANEL--------------------------------------------------------
        panel = new JPanel();
        panel.setVisible(false);
        panel.setLayout(null);
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        cmbTablas = new JComboBox<String>(new String[] { "Seleccione" });
        llenarCombo();
        panel.add(cmbTablas);

        rdModificar = new JRadioButton("Modificar");
        rdModificar.setEnabled(false);
        rdNuevo = new JRadioButton("Nuevo");
        rdNuevo.setEnabled(false);
        panel.add(rdModificar);
        panel.add(rdNuevo);

        grupo = new ButtonGroup();
        grupo.add(rdModificar);
        grupo.add(rdNuevo);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setEnabled(false);
        panel.add(btnLimpiar);
        btnGuardar = new JButton("Guardar");
        btnGuardar.setEnabled(false);
        panel.add(btnGuardar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setEnabled(false);
        panel.add(btnBuscar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        panel.add(btnEliminar);

        add(panel);
        // FIN.PANEL-------------------------------------------------------

        panelContent = new JPanel();
        panelContent.setLayout(new Content());
        panelContent.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");
        add(panelContent);

        lblArtId = new JLabel("ArtId");
        txtArtId = new JTextField();
        lblArtNombre = new JLabel("ArtNombre");
        txtArtNombre = new JTextField();
        lblArtDescripcion = new JLabel("ArtDescripcion");
        txtArtDescripcion = new JTextField();
        lblArtPrecio = new JLabel("ArtPrecio");
        txtArtPrecio = new JTextField();
        lblFamid = new JLabel("Familia");
        txtFamid = new JTextField();
        lblFamNombre = new JLabel("FamNombre");
        txtFamNombre = new JTextField();
        cmbFamid = new JComboBox<>();

        // PNLTABLA-------------------------------------------------------
        pnlTabla = new JPanel();
        pnlTabla.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        modelo = new DefaultTableModel();
        table = new JTable(modelo);
        table.setDefaultEditor(Object.class, null);

        scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVisible(false);
        pnlTabla.add(scroll, BorderLayout.CENTER);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        pnlTabla.setLayout(null);
        add(pnlTabla);
        // PNLTABLA-------------------------------------------------------

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == cmbTablas) {
            rdModificar.setEnabled(true);
            rdNuevo.setEnabled(true);
            btnBuscar.setEnabled(true);
            btnEliminar.setEnabled(true);

            if (!band) {
                if (rdNuevo.isSelected()) {
                    txtFamid.setText("*");
                    txtFamid.setEditable(false);
                    txtArtId.setText("*");
                    txtArtId.setEditable(false);
                }
                if (rdModificar.isSelected()) {
                    txtFamid.setText("");
                    txtFamid.setEditable(true);
                    txtArtId.setText("");
                    txtArtId.setEditable(true);
                }
            }

            if (cmbTablas.getSelectedItem().equals("familias")) {
                System.out.println("Familias");
                showFamilias();

                modelo.setRowCount(0);
                modelo.setColumnCount(0);

                modelo.addColumn("FamId");
                modelo.addColumn("FamNombre");
                llenaTablaFam();
                return;
            }
            if (cmbTablas.getSelectedItem().equals("articulos")) {
                System.out.println("Articulos");
                txtFamid.setText("");
                txtFamid.setEditable(true);
                showArticulos();

                modelo.setRowCount(0);
                modelo.setColumnCount(0);

                modelo.addColumn("ArtId");
                modelo.addColumn("ArtNombre");
                modelo.addColumn("ArtDescripcion");
                modelo.addColumn("ArtPrecio");
                modelo.addColumn("FamNombre");
                llenaTablaArt();
                return;
            }
        }
        if (evt.getSource() == btnLimpiar) {
            limpiar();
        }
        if (evt.getSource() == btnGuardar) {
            if (cmbTablas.getSelectedItem().equals("familias")) {
                if (txtFamNombre.getText().equals("") || txtFamid.getText().equals("")) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "Inserte datos en los campos");
                    return;
                }
                insertarTablaFam();
                return;
            }
            //
            if (cmbTablas.getSelectedItem().equals("articulos")) {
                if (txtArtNombre.getText().equals("") || txtArtDescripcion.getText().equals("")
                        || txtArtPrecio.getText().equals("")) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "Inserte datos en los Campos Vacios");
                    return;
                }
                try {
                    Float.parseFloat(txtArtPrecio.getText());
                } catch (NumberFormatException e) {
                    ErrorHandler.showNotification("Error: El ArtPrecio no es un número válido.");
                    System.out.println("Error: El precio no es un número válido.");
                    return;
                }
                if (!txtArtId.getText().equals("*")) {
                    try {
                        Integer.parseInt(txtArtId.getText());
                    } catch (NumberFormatException e) {
                        ErrorHandler.showNotification("Error: El artId no es un número válido.");
                        System.out.println("Error: El artId no es un número válido.");
                        return;
                    }
                }
                insertarTablaArticulo();
                return;
            }
        }
        if (evt.getSource() == rdModificar) {
            btnLimpiar.setEnabled(true);
            btnGuardar.setEnabled(true);
            txtArtNombre.setEnabled(true);
            txtArtDescripcion.setEnabled(true);
            txtArtPrecio.setEnabled(true);
            cmbFamid.setEnabled(true);

            if (cmbTablas.getSelectedItem().equals("familias")) {
                txtFamid.setText("");
                txtFamid.setEditable(true);
                return;
            }
            if (cmbTablas.getSelectedItem().equals("articulos")) {
                txtArtId.setText("");
                txtArtId.setEditable(true);
                return;
            }
        }
        if (evt.getSource() == rdNuevo) {
            btnLimpiar.setEnabled(true);
            btnGuardar.setEnabled(true);
            showFamilias();
            if (cmbTablas.getSelectedItem().equals("familias")) {
                txtFamid.setText("*");
                txtFamid.setEditable(false);
                return;
            }
            if (cmbTablas.getSelectedItem().equals("articulos")) {
                showArticulos();
                txtArtId.setText("*");
                txtArtId.setEditable(false);
                return;
            }
        }
        if (evt.getSource() == btnBuscar) {
            if (cmbTablas.getSelectedItem().equals("familias")) {
                // if (txtFamid.getText().equals("") && txtFamNombre.getText().equals("")) {
                // Notifications.getInstance().show(Notifications.Type.INFO,
                // Notifications.Location.TOP_CENTER,
                // "Inserte datos en los campos");
                // return;
                // }
                System.out.println("Buscar Fam");

                buscarFam();
                return;
            }
            if (cmbTablas.getSelectedItem().equals("articulos")) {
                // if (txtArtId.getText().equals("") && txtArtNombre.getText().equals("")
                // && txtArtDescripcion.getText().equals("") &&
                // txtArtPrecio.getText().equals("")
                // && txtFamid.getText().equals("")) {
                // Notifications.getInstance().show(Notifications.Type.INFO,
                // Notifications.Location.TOP_CENTER,
                // "Inserte datos en los campos");
                // return;
                // }
                buscarArt();
                return;
            }
        }
        if (evt.getSource() == btnEliminar) {
            // Mensaje de alerta para confirmar la eliminación
            int dialogResult = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el registro?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (dialogResult != JOptionPane.YES_OPTION) {
                return;
            }
            if (cmbTablas.getSelectedItem().equals("familias")) {
                if (txtFamid.getText().equals("") && txtFamNombre.getText().equals("")) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "Ningun campo insertado, nada para eliminar");
                    return;
                }
                eliminarFam();
                return;
            }
            if (cmbTablas.getSelectedItem().equals("articulos")) {
                // if (txtArtId.getText().equals("") && txtArtNombre.getText().equals("")
                // && txtArtDescripcion.getText().equals("") &&
                // txtArtPrecio.getText().equals("")) {
                // Notifications.getInstance().show(Notifications.Type.INFO,
                // Notifications.Location.TOP_CENTER,
                // "Ningun campo insertado, nada para eliminar");
                // return;
                // }
                eliminarArt();
                return;
            }
        }
    }

    private void limpiar() {
        if (cmbTablas.getSelectedItem().equals("familias")) {
            if (rdNuevo.isSelected()) {
                txtFamid.setText("*");
                txtFamNombre.setText("");
                return;
            }
            txtFamid.setText("");
            txtFamNombre.setText("");
            return;
        }
        if (cmbTablas.getSelectedItem().equals("articulos")) {
            if (rdNuevo.isSelected()) {
                txtArtId.setText("*");
                txtArtNombre.setText("");
                txtArtDescripcion.setText("");
                txtArtPrecio.setText("");

                txtArtNombre.setEnabled(true);
                txtArtDescripcion.setEnabled(true);
                txtArtPrecio.setEnabled(true);
                cmbFamid.setEnabled(true);
                return;
            }
            txtArtId.setText("");
            txtArtNombre.setText("");
            txtArtDescripcion.setText("");
            txtArtPrecio.setText("");

            return;
        }
    }

    private void eliminarArt() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            ResultSet re = s.executeQuery(
                    "select famid, famnombre from familias where famnombre = '" + cmbFamid.getSelectedItem() + "';");
            re.next();
            int famid = re.getInt("famid");

            StringBuilder consulta = new StringBuilder("DELETE FROM articulos WHERE");

            consulta.append(" famid = ").append(famid).append("  AND");

            // agregar condiciones según los campos proporcionados
            if (!txtArtId.getText().isEmpty()) {
                consulta.append(" artid = ").append(txtArtId.getText()).append("  AND");
            }

            if (!txtArtNombre.getText().isEmpty()) {
                consulta.append(" artnombre = '").append(txtArtNombre.getText()).append("'  AND");
            }

            if (!txtArtDescripcion.getText().isEmpty()) {
                consulta.append(" artdescripcion = '").append(txtArtDescripcion.getText()).append("'  AND");
            }

            if (!txtArtPrecio.getText().isEmpty()) {
                consulta.append(" artprecio = ").append(txtArtPrecio.getText()).append("  AND");
            }

            // Eliminar lo adicional al final de la consulta
            consulta.delete(consulta.length() - 5, consulta.length());

            // Ejecutar la consulta
            s.execute(consulta.toString());

            // Procesar y mostrar los resultados (usar según tus necesidades)
            modelo.setRowCount(0);
            llenaTablaArt();

        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println("Error: " + e.getErrorCode());
        }

    }

    private void eliminarFam() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            StringBuilder consulta = new StringBuilder("DELETE FROM familias WHERE");

            // agregar condiciones según los campos proporcionados
            if (!txtFamid.getText().isEmpty()) {
                consulta.append(" famid = ").append(txtFamid.getText()).append("  AND");
            }

            if (!txtFamNombre.getText().isEmpty()) {
                consulta.append(" famnombre = '").append(txtFamNombre.getText()).append("'  AND");
            }

            // Eliminar lo adicional al final de la consulta
            consulta.delete(consulta.length() - 5, consulta.length());

            // Ejecutar la consulta
            s.execute(consulta.toString());

            // Procesar y mostrar los resultados (usar según tus necesidades)
            modelo.setRowCount(0);
            llenaTablaFam();

        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println("Error: " + e.getErrorCode());
        }
    }

    private void buscarArt() {
        modelo.setRowCount(0);
        try {
            Statement s = ConexionDB.conexion.createStatement();

            StringBuilder consulta = new StringBuilder(
                    "SELECT artid, artnombre, artdescripcion, artprecio, f.FamNombre from articulos inner join familias f on articulos.famid = f.famid WHERE");

            consulta.append(" f.famnombre = '").append(cmbFamid.getSelectedItem()).append("'  AND");

            // Verificar y agregar condiciones según los campos proporcionados por el
            // usuario
            if (!txtArtId.getText().isEmpty()) {
                consulta.append(" artid = ").append(txtArtId.getText()).append("  AND");
            }

            if (!txtArtNombre.getText().isEmpty()) {
                consulta.append(" artnombre LIKE '%").append(txtArtNombre.getText()).append("%'  AND");
            }

            if (!txtArtDescripcion.getText().isEmpty()) {
                consulta.append(" artdescripcion LIKE '%").append(txtArtDescripcion.getText()).append("%'  AND");
            }

            if (!txtArtPrecio.getText().isEmpty()) {
                consulta.append(" artprecio = ").append(txtArtPrecio.getText()).append("  AND");
            }

            // Eliminar el "AND" adicional al final de la consulta
            consulta.delete(consulta.length() - 5, consulta.length());

            // Ejecutar la consulta
            ResultSet rs = s.executeQuery(consulta.toString());

            while (rs.next()) {
                modelo.addRow(new Object[] { rs.getInt("artid"), rs.getString("artnombre"),
                        rs.getString("artdescripcion"), rs.getDouble("artprecio"), rs.getString("famnombre") });
            }

        } catch (SQLException e) {
            System.out.println("auq");
            ErrorHandler.handleSqlException(e);
            System.out.println("Error: " + e.getErrorCode() + ": " + e);
        }
    }

    private void buscarFam() {
        modelo.setRowCount(0);
        try {
            Statement s = ConexionDB.conexion.createStatement();

            StringBuilder consulta = new StringBuilder("SELECT * FROM familias WHERE");

            // Verificar y agregar condiciones según los campos proporcionados por el
            // usuario
            if (!txtFamid.getText().isEmpty()) {
                consulta.append(" famid = ").append(txtFamid.getText()).append("  AND");
            }

            if (!txtFamNombre.getText().isEmpty()) {
                consulta.append(" famnombre LIKE '%").append(txtFamNombre.getText()).append("%'  AND");
            }

            // Eliminar el "AND" adicional al final de la consulta
            consulta.delete(consulta.length() - 5, consulta.length());

            // Ejecutar la consulta
            ResultSet rs = s.executeQuery(consulta.toString());

            // Procesar y mostrar los resultados (usar según tus necesidades)
            while (rs.next()) {
                modelo.addRow(new Object[] { rs.getInt("famid"), rs.getString("famnombre") });
                System.out.println("FamId: " + rs.getInt("famid") + ", FamNombre: " + rs.getString("famnombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void insertarTablaFam() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            // si la familia no existe no se puede modificar en la tabla
            if (!txtFamid.getText().equals("*")) {
                ResultSet re = s.executeQuery("select * from familias where famid = " + txtFamid.getText() + ";");
                if (!re.next()) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "La Familia no existe.");
                    return;
                }
            }

            // Verificar si el procedimiento ya existe
            ResultSet rs = s.executeQuery(
                    "SELECT COUNT(*) FROM information_schema.routines WHERE routine_name = 'Sp_MttoFamilias'");
            rs.next();
            int procedureCount = rs.getInt(1);
            if (procedureCount == 0) {
                s.execute(procedimientoFam());
                System.out.println("Procedure created successfully.");
            }

            if (txtFamid.getText().equals("*")) {
                txtFamid.setText("0");

            }

            CallableStatement cs = ConexionDB.conexion.prepareCall("{call Sp_MttoFamilias(?, ?)}");

            // Configurar los parámetros de entrada y salida
            // -pendiente de revisar si dejar output o volver a poner *
            // ---------------------------------------
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setInt(1, Integer.parseInt(txtFamid.getText())); // famId

            cs.setString(2, txtFamNombre.getText()); // famNombre

            cs.execute();

            // Obtener el valor del parámetro de salida famId
            int famIdOutput = cs.getInt(1);
            System.out.println(txtFamid.getText() + " FAM");

            modelo.setRowCount(0);
            llenaTablaFam();

            txtFamid.setText(String.valueOf(famIdOutput));
            // txtFamid.setText("*");
            txtFamNombre.setText("");

        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println("Error: " + e.getErrorCode());
        }
    }

    private void insertarTablaArticulo() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            // si el articulo no existe no se puede modificar en la tabla
            if (!txtArtId.getText().equals("*")) {
                ResultSet re = s.executeQuery(
                        "select * from articulos inner join familias f on articulos.famid = f.famid where artid = "
                                + txtArtId.getText() + ";");
                if (!re.next()) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "El Articulo no existe.");
                    return;
                }
            }

            ResultSet re = s.executeQuery(
                    "select famid, famnombre from familias where famnombre = '" + cmbFamid.getSelectedItem() + "';");
            re.next();
            int famid = re.getInt("famid");
            String famnombre = re.getString("famnombre");

            // Verificar si el procedimiento ya existe
            ResultSet rs = s.executeQuery(
                    "SELECT COUNT(*) FROM information_schema.routines WHERE routine_name = 'Sp_MttoArticulos'");
            rs.next();
            int procedureCount = rs.getInt(1);
            if (procedureCount == 0) {
                s.execute(procedimientoArt());
                System.out.println("Procedure created successfully.");
            }

            if (txtArtId.getText().equals("*")) {
                txtArtId.setText("0");
            }

            CallableStatement cs = ConexionDB.conexion.prepareCall("{call Sp_MttoArticulos(?, ?, ?, ?, ?)}");

            // Configurar los parámetros de entrada y salida
            cs.registerOutParameter(1, java.sql.Types.INTEGER); // artId (parámetro de salida)
            cs.setInt(1, Integer.parseInt(txtArtId.getText())); // artId (parámetro de entrada)
            System.out.println("FAMNOMBRE: " + famnombre + " FAMID: " + famid);

            cs.setString(2, txtArtNombre.getText()); // artNombre
            cs.setString(3, txtArtDescripcion.getText()); // artDescripcion
            cs.setDouble(4, Double.parseDouble(txtArtPrecio.getText())); // artPrecio
            cs.setInt(5, famid); // famId

            cs.execute();

            // Obtener el valor del parámetro de salida artId
            int artIdOutput = cs.getInt(1);

            modelo.setRowCount(0);
            System.out.println("Antes");
            llenaTablaArt();
            System.out.println("Despu");
            // txtArtId.setText(String.valueOf(artIdOutput));
            txtArtId.setText(artIdOutput + "");
            if (rdNuevo.isSelected()) {
                txtArtNombre.setEnabled(false);
                txtArtDescripcion.setEnabled(false);
                txtArtPrecio.setEnabled(false);
                cmbFamid.setEnabled(false);
            }
        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println(e);
            System.out.println("Error: " + e.getErrorCode());
        }
    }

    private void llenaTablaFam() {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery("select * from familias");

            while (rs.next()) {
                modelo.addRow(new Object[] { rs.getInt("famid"), rs.getString("famnombre") });
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void llenaTablaArt() {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery(
                    "select artid, artnombre, artdescripcion, artprecio, f.FamNombre  from articulos inner join familias f on articulos.famid = f.famid");

            while (rs.next()) {
                modelo.addRow(new Object[] { rs.getInt("artid"), rs.getString("artnombre"),
                        rs.getString("artdescripcion"), rs.getDouble("artprecio"), rs.getString("Famnombre") });
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                    "Error al llenar la tabla");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showArticulos() {
        panelContent.removeAll();
        txtFamid.setText("");

        FlatAnimatedLafChange.showSnapshot();
        panelContent.add(lblArtId);

        panelContent.add(txtArtId);

        panelContent.add(lblArtNombre);

        panelContent.add(txtArtNombre);

        panelContent.add(lblArtDescripcion);

        panelContent.add(txtArtDescripcion);

        panelContent.add(lblArtPrecio);

        panelContent.add(txtArtPrecio);

        panelContent.add(lblFamid);

        panelContent.add(cmbFamid);
        llenarComboFam();

        pnlTabla.add(scroll, BorderLayout.CENTER);

        FlatLaf.updateUILater();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    private void llenarComboFam() {
        cmbFamid.removeAllItems();
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery("select * from familias");
            while (rs.next()) {
                cmbFamid.addItem(rs.getString("FamNombre"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showFamilias() {
        panelContent.removeAll();

        FlatAnimatedLafChange.showSnapshot();
        panelContent.add(lblFamid);

        panelContent.add(txtFamid);

        panelContent.add(lblFamNombre);

        panelContent.add(txtFamNombre);

        pnlTabla.add(scroll, BorderLayout.CENTER);

        FlatLaf.updateUILater();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    private void llenarCombo() {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery("select TABLE_NAME from INFORMATION_SCHEMA.TABLES");
            while (rs.next()) {
                cmbTablas.addItem(rs.getString("TABLE_NAME"));
            }
            cmbTablas.setSelectedIndex(0);
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                    "Error No tiene permisos en una tabla");
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modoConsulta() {
        band = true;
        grupo.clearSelection();
        txtArtId.setText("");
        txtArtNombre.setText("");
        txtArtDescripcion.setText("");
        txtArtPrecio.setText("");

        panel.setVisible(true);
        btnBuscar.setVisible(true);
        btnEliminar.setVisible(true);
        rdModificar.setVisible(false);
        rdNuevo.setVisible(false);
        btnLimpiar.setVisible(false);
        btnGuardar.setVisible(false);
        scroll.setVisible(true);

        txtFamid.setEditable(true);
        txtFamid.setText("");
        txtArtId.setEditable(true);
        txtArtId.setText("");
    }

    public void modoCaptura() {
        band = false;
        grupo.clearSelection();

        txtArtId.setText("");
        txtArtNombre.setText("");
        txtArtDescripcion.setText("");
        txtArtPrecio.setText("");

        panel.setVisible(true);
        rdModificar.setVisible(true);
        rdNuevo.setVisible(true);
        btnLimpiar.setVisible(true);
        btnGuardar.setVisible(true);
        scroll.setVisible(true);
        btnBuscar.setVisible(false);
        btnEliminar.setVisible(false);

    }

    @Override
    public void componentResized(ComponentEvent e) {
        componenteHeader.setBounds(0, 0, getWidth(), (int) (getHeight() * .2));
        panel.setBounds(0, componenteHeader.getHeight(), getWidth(), (int) (getHeight() * .12));

        panelContent.setBounds(0, panel.getY() + panel.getHeight(), getWidth(),
                (int) (getHeight() * .45) - panel.getHeight());

        pnlTabla.setBounds(0, panelContent.getY() + panelContent.getHeight(),
                getWidth(),
                getHeight() - panelContent.getY() - panelContent.getHeight());

        int w = panel.getWidth();
        int h = panel.getHeight();

        int anchoComponente = (int) (w * .4);
        int altoComponente = (int) (h * .9);
        // -----------------------------------------------------------
        cmbTablas.setBounds((int) ((w * .025)), (int) (h * .03),
                anchoComponente,
                altoComponente);
        cmbTablas.setFont(Rutinas2.getFont("SegoeUI", false, 12, getWidth(), getHeight(), 350));

        rdModificar.setBounds(cmbTablas.getX() + cmbTablas.getWidth(), (int) (h * .05), (int) (w * .20),
                (int) (h * .45));
        rdModificar.setFont(Rutinas2.getFont("SegoeUI", false, 12, getWidth(), getHeight(), 400));

        rdNuevo.setBounds(cmbTablas.getX() + cmbTablas.getWidth(), rdModificar.getY() + rdModificar.getHeight(),
                (int) (w * .20),
                (int) (h * .45));
        rdNuevo.setFont(Rutinas2.getFont("SegoeUI", false, 12, getWidth(), getHeight(), 400));

        btnLimpiar.setBounds(rdModificar.getX() + rdModificar.getWidth(), (int) (h * .05), (int) (w * .35),
                (int) (h * .45));
        btnLimpiar.setFont(Rutinas2.getFont("SegoeUI", false, 12, getWidth(), getHeight(), 400));

        btnGuardar.setBounds(rdModificar.getX() + rdModificar.getWidth(), btnLimpiar.getY() + btnLimpiar.getHeight(),
                (int) (w * .35),
                (int) (h * .45));
        btnGuardar.setFont(Rutinas2.getFont("SegoeUI", false, 12, getWidth(), getHeight(), 400));

        btnEliminar.setBounds(cmbTablas.getX() + cmbTablas.getWidth() + (int) (w * .05), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnEliminar.setFont(Rutinas2.getFont("SegoeUI", false, 12, getWidth(), getHeight(), 400));

        btnBuscar.setBounds(btnEliminar.getX() + btnEliminar.getWidth(), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnBuscar.setFont(Rutinas2.getFont("SegoeUI", false, 12, getWidth(), getHeight(), 400));

        // -----------------------------------------------------------

        scroll.setBounds((int) (pnlTabla.getWidth() * .03), (int) (pnlTabla.getHeight() * .05),
                (int) (pnlTabla.getWidth() * .95), (int) (pnlTabla.getHeight() * .9));

    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        if (evt.getSource() == cmbTablas) {
            if (!selected) {
                cmbTablas.removeItem("Seleccione");
                selected = true;
            }
        }
    }

    private String procedimientoFam() {
        return "CREATE PROCEDURE Sp_MttoFamilias\n"
                + "@famId INT OUTPUT, @famNombre VARCHAR(40)\n"
                + "AS\n"
                + "BEGIN\n"
                + "    IF EXISTS(SELECT * FROM familias WHERE famid = @famId)\n"
                + "    BEGIN\n"
                + "        UPDATE familias SET famnombre = @famNombre WHERE famid = @famId;\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Actualizar en la tabla Familias', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "    ELSE\n"
                + "    BEGIN\n"
                + "        -- SI LA LLAVE PRIMARIA NO ES IDENTITY, SE BUSCA LA ULTIMA CLAVE MAS UNO\n"
                + "        SELECT @famId = COALESCE(MAX(famid), 0) + 1 FROM familias;\n"
                + "        INSERT INTO familias VALUES(@famId, @famNombre);\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Actualizar en la tabla Familias', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "END";
    }

    private String procedimientoArt() {
        return "CREATE PROCEDURE Sp_MttoArticulos\n"
                + "@artId INT OUTPUT, @artNombre VARCHAR(40), @artDescripcion VARCHAR(40), @artPrecio FLOAT, @famid int\n"
                + "AS\n"
                + "BEGIN\n"
                + "    IF EXISTS(SELECT * FROM articulos WHERE artid = @artId)\n"
                + "    BEGIN\n"
                + "        UPDATE articulos SET artnombre = @artNombre, artdescripcion = @artDescripcion, artprecio = @artPrecio, famid=@famid WHERE artid = @artId;\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Actualizar en la tabla Articulos', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "    ELSE\n"
                + "    BEGIN\n"
                + "        -- SI LA LLAVE PRIMARIA NO ES IDENTITY, SE BUSCA LA ULTIMA CLAVE MAS UNO\n"
                + "        SELECT @artId = COALESCE(MAX(artid), 0) + 1 FROM articulos;\n"
                + "        INSERT INTO articulos VALUES(@artId, @artNombre, @artDescripcion, @artPrecio, @famid);\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Actualizar en la tabla Articulos', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "END";
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

        // llenar los campos txt de articulos
        if (txtArtId.getText().equals("") || txtArtId.getText().equals("*") || band)
            return;

        System.out.println("entra");

        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery(
                    "select artnombre, artdescripcion, artprecio, f.FamNombre  from articulos inner join familias f on articulos.famid = f.famid where artid = "
                            + txtArtId.getText() + "");
            if (rs.next()) {
                txtArtNombre.setText(rs.getString("artnombre"));
                txtArtDescripcion.setText(rs.getString("artdescripcion"));
                txtArtPrecio.setText(rs.getString("artprecio"));
                cmbFamid.setSelectedItem(rs.getString("famNombre"));
            } else {
                Notifications.getInstance().show(Notifications.Type.INFO,
                        Notifications.Location.TOP_CENTER,
                        "El Articulo no existe.");
                txtArtNombre.setText("");
                txtArtDescripcion.setText("");
                txtArtPrecio.setText("");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            ErrorHandler.handleSqlException(ex);
        }
    }

}