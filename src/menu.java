
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
    private JComboBox<String> cmbTipid;

    private JLabel lblTipid;
    private JLabel lblTipNombre;
    private JLabel lblClienteId;
    private JLabel lblClienteNombre;
    private JLabel lblClienteApellidos;
    private JLabel lblClienteSexo;
    private JLabel lblClienteLimiteCredito;

    private JTextField txtTipid;
    private JTextField txtTipNombre;
    private JTextField txtClienteId;
    private JTextField txtClienteNombre;
    private JTextField txtClienteApellidos;
    private JTextField txtClienteSexo;
    private JTextField txtClienteLimiteCredito;

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
        txtClienteId.addFocusListener(this);
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
        // llenarCombo();
        cmbTablas.addItem("clientes");
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

        lblClienteId = new JLabel("ClienteId");
        txtClienteId = new JTextField();
        lblClienteNombre = new JLabel("ClienteNombre");
        txtClienteNombre = new JTextField();
        lblClienteApellidos = new JLabel("ClienteApellidos");
        txtClienteApellidos = new JTextField();
        lblClienteSexo = new JLabel("ClienteSexo");
        txtClienteSexo = new JTextField();
        lblClienteLimiteCredito = new JLabel("ClienteLimiteCredito");
        txtClienteLimiteCredito = new JTextField();
        lblTipid = new JLabel("Tipo");
        txtTipid = new JTextField();
        lblTipNombre = new JLabel("TipNombre");
        txtTipNombre = new JTextField();
        cmbTipid = new JComboBox<>();

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
                    txtTipid.setText("*");
                    txtTipid.setEditable(false);
                    txtClienteId.setText("*");
                    txtClienteId.setEditable(false);
                }
                if (rdModificar.isSelected()) {
                    txtTipid.setText("");
                    txtTipid.setEditable(true);
                    txtClienteId.setText("");
                    txtClienteId.setEditable(true);
                }
            }
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("tipos")) {
                System.out.println("Tipos");
                showTipos(); // Si es necesario mostrar los tipos, descomenta esta línea y

                modelo.setRowCount(0);
                modelo.setColumnCount(0);

                modelo.addColumn("TipId");
                modelo.addColumn("TipNombre");
                llenarTablaTipos(); // Suponiendo que este método llena la tabla con datos de la entidad Tipos
                return;
            }
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("clientes")) {
                System.out.println("Clientes");
                txtClienteId.setEditable(true);
                showClientes(); // Si es necesario mostrar los clientes, descomenta esta
                // línea y crea el método showClientes()

                modelo.setRowCount(0);
                modelo.setColumnCount(0);

                modelo.addColumn("CliId");
                modelo.addColumn("CliNombre");
                modelo.addColumn("CliApellidos");
                modelo.addColumn("CliSexo");
                modelo.addColumn("CliLimiteCredito");
                modelo.addColumn("TipId");
                llenarTablaClientes(); // Suponiendo que este método llena la tabla con datos de la entidad Clientes
                return;
            }
        }
        if (evt.getSource() == btnLimpiar) {
            limpiar();
            return;
        }

        if (evt.getSource() == btnGuardar) {
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("tipos")) {
                if (txtTipNombre.getText().equals("") || txtTipid.getText().equals("")) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "Inserte datos en los campos");
                    return;
                }
                try {
                    Integer.parseInt(txtTipid.getText());
                } catch (NumberFormatException e) {
                    ErrorHandler.showNotification("Error: El TipId no es un número válido.");
                    System.out.println("Error: El TipId no es un número válido.");
                    return;
                }
                insertarTablaTip();
                btnGuardar.setEnabled(false);
                return;
            }
            //
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("clientes")) {
                if (txtClienteNombre.getText().equals("") || txtClienteApellidos.getText().equals("")
                        || txtClienteLimiteCredito.getText().equals("")) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "Inserte datos en los Campos Vacios");
                    return;
                }
                try {
                    Float.parseFloat(txtClienteLimiteCredito.getText());
                } catch (NumberFormatException e) {
                    ErrorHandler.showNotification("Error: El ClienteLimiteCredito no es un número válido.");
                    System.out.println("Error: El ClienteLimiteCredito no es un número válido.");
                    return;
                }
                if (!txtClienteId.getText().equals("*")) {
                    try {
                        Integer.parseInt(txtClienteId.getText());
                    } catch (NumberFormatException e) {
                        ErrorHandler.showNotification("Error: El ClienteId no es un número válido.");
                        System.out.println("Error: El ClienteId no es un número válido.");
                        return;
                    }
                }
                if (!txtClienteSexo.getText().equalsIgnoreCase("M")
                        && !txtClienteSexo.getText().equalsIgnoreCase("F")) {
                    ErrorHandler.showNotification("Error: El ClienteSexo debe ser 'M' o 'F'.");
                    return;
                }
                insertarTablaCliente();
                return;
            }
            return;
        }
        if (evt.getSource() == rdModificar) {
            btnLimpiar.setEnabled(true);
            btnGuardar.setEnabled(true);

            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("tipos")) {
                txtTipNombre.setEnabled(true);

                txtTipid.setText("");
                txtTipid.setEditable(true);
                return;
            }
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("clientes")) {
                txtClienteNombre.setEnabled(true);
                txtClienteApellidos.setEnabled(true);
                txtClienteLimiteCredito.setEnabled(true);
                txtClienteSexo.setEnabled(true);
                cmbTipid.setEnabled(true);

                txtClienteId.setText("");
                txtClienteId.setEditable(true);
                return;
            }
            return;
        }
        if (evt.getSource() == rdNuevo) {
            btnLimpiar.setEnabled(true);
            btnGuardar.setEnabled(true);

            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("tipos")) {
                txtTipid.setText("*");
                txtTipNombre.setEditable(true);
                txtTipid.setEditable(false);
                return;
            }
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("clientes")) {
                txtClienteNombre.setEnabled(true);
                txtClienteApellidos.setEnabled(true);
                txtClienteSexo.setEnabled(true);
                txtClienteLimiteCredito.setEnabled(true);
                cmbTipid.setEnabled(true);
                txtClienteId.setText("*");
                txtClienteId.setEditable(false);
                return;
            }
        }
        if (evt.getSource() == btnBuscar) {
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("tipos")) {
                System.out.println("Buscar Tipos");
                buscarTip(); // Suponiendo que este método busca los tipos según los criterios especificados
                return;
            }
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("clientes")) {
                System.out.println("Buscar Clientes");
                buscarCliente(); // Suponiendo que este método busca los clientes según los criterios
                                 // especificados
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
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("tipos")) {
                if (txtTipid.getText().isEmpty() && txtTipNombre.getText().isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                            "Ningún campo insertado, nada para eliminar");
                    return;
                }
                eliminarTipo();
                limpiar();
                return;
            }
            if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("clientes")) {
                if (txtClienteId.getText().isEmpty() && txtClienteNombre.getText().isEmpty()
                        && txtClienteApellidos.getText().isEmpty() && txtClienteSexo.getText().isEmpty()
                        && txtClienteLimiteCredito.getText().isEmpty()
                        && cmbTipid.getSelectedItem().toString().equals("Seleccione")) {
                    Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                            "Ningún campo insertado, nada para eliminar");
                    return;
                }
                eliminarCliente();
                return;
            }
        }
    }

    private void limpiar() {

        if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("tipos")) {
            if (rdNuevo.isSelected()) {
                txtTipid.setText("*");
                txtTipNombre.setText("");

                txtTipNombre.setEnabled(true);
                return;
            }
            txtTipid.setText("");
            txtTipNombre.setText("");
            return;
        }
        if (cmbTablas.getSelectedItem().toString().equalsIgnoreCase("clientes")) {
            if (rdNuevo.isSelected()) {
                txtClienteId.setText("*");
                txtClienteNombre.setText("");
                txtClienteApellidos.setText("");
                txtClienteSexo.setText("");
                txtClienteLimiteCredito.setText("");
                cmbTipid.setSelectedItem("Seleccione");

                txtClienteNombre.setEnabled(true);
                txtClienteApellidos.setEnabled(true);
                txtClienteSexo.setEnabled(true);
                txtClienteLimiteCredito.setEnabled(true);
                cmbTipid.setEnabled(true);
                return;
            }

            txtClienteId.setText("");
            txtClienteNombre.setText("");
            txtClienteApellidos.setText("");
            txtClienteSexo.setText("");
            txtClienteLimiteCredito.setText("");
            cmbTipid.setSelectedItem("Seleccione");
            return;
        }
    }

    private void eliminarCliente() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            StringBuilder consulta = new StringBuilder("DELETE FROM clientes WHERE");

            if (!cmbTipid.getSelectedItem().toString().equalsIgnoreCase("Seleccione")) {
                ResultSet re = s
                        .executeQuery("SELECT tipid FROM tipos WHERE tipnombre = '" + cmbTipid.getSelectedItem() + "'");
                re.next();
                int tipid = re.getInt("tipid");
                consulta.append(" tipid = ").append(tipid).append("  AND");
            }

            // Agregar condiciones según los campos proporcionados
            if (!txtClienteId.getText().isEmpty()) {
                consulta.append(" cliid = ").append(txtClienteId.getText()).append("  AND");
            }

            if (!txtClienteNombre.getText().isEmpty()) {
                consulta.append(" clinombre = '").append(txtClienteNombre.getText()).append("'  AND");
            }

            if (!txtClienteApellidos.getText().isEmpty()) {
                consulta.append(" cliapellidos = '").append(txtClienteApellidos.getText()).append("'  AND");
            }

            if (!txtClienteSexo.getText().isEmpty()) {
                consulta.append(" clisexo = '").append(txtClienteSexo.getText()).append("'  AND");
            }

            if (!txtClienteLimiteCredito.getText().isEmpty()) {
                consulta.append(" cliLimiteCredito = ").append(txtClienteLimiteCredito.getText()).append("  AND");
            }

            // Eliminar lo adicional al final de la consulta
            consulta.delete(consulta.length() - 5, consulta.length());

            // Ejecutar la consulta
            s.execute(consulta.toString());

            if (s.getUpdateCount() == 0) {
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                        "No se encontraron registros para eliminar.");
            } else {
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                        "Registros eliminados correctamente.");
            }

            // Procesar y mostrar los resultados (usar según tus necesidades)
            modelo.setRowCount(0);
            llenarTablaClientes();

        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println("Error: " + e.getErrorCode());
        }
    }

    private void eliminarTipo() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            StringBuilder consulta = new StringBuilder("DELETE FROM tipos WHERE");

            // agregar condiciones según los campos proporcionados
            if (!txtTipid.getText().isEmpty()) {
                consulta.append(" tipid = ").append(txtTipid.getText()).append("  AND");
            }

            if (!txtTipNombre.getText().isEmpty()) {
                consulta.append(" tipnombre = '").append(txtTipNombre.getText()).append("'  AND");
            }
            // Eliminar lo adicional al final de la consulta
            consulta.delete(consulta.length() - 5, consulta.length());

            // Ejecutar la consulta
            s.execute(consulta.toString());

            // mostrar notificación
            if (s.getUpdateCount() == 0) {
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                        "No se encontró ningún tipo para eliminar.");
            } else {
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                        "Tipo eliminado correctamente.");
            }

            // Procesar y mostrar los resultados (usar según tus necesidades)
            modelo.setRowCount(0);
            llenarTablaTipos();

        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println("Error: " + e.getErrorCode());
        }
    }

    private void buscarCliente() {
        modelo.setRowCount(0);
        try {
            Statement s = ConexionDB.conexion.createStatement();

            StringBuilder consulta = new StringBuilder(
                    "SELECT cliid, clinombre, cliapellidos, clisexo, cliLimiteCredito, t.tipnombre FROM clientes c INNER JOIN tipos t ON c.tipid = t.tipid WHERE");

            // Verificar y agregar condiciones según los campos proporcionados por el
            // usuario
            if (!txtClienteId.getText().isEmpty()) {
                consulta.append(" c.cliid = ").append(txtClienteId.getText()).append("  AND");
            }

            if (!txtClienteNombre.getText().isEmpty()) {
                consulta.append(" c.clinombre LIKE '%").append(txtClienteNombre.getText()).append("%'  AND");
            }

            if (!txtClienteApellidos.getText().isEmpty()) {
                consulta.append(" c.cliapellidos LIKE '%").append(txtClienteApellidos.getText()).append("%'  AND");
            }

            if (!txtClienteSexo.getText().isEmpty()) {
                consulta.append(" c.clisexo = '").append(txtClienteSexo.getText()).append("'  AND");
            }

            if (!txtClienteLimiteCredito.getText().isEmpty()) {
                consulta.append(" c.cliLimiteCredito = ").append(txtClienteLimiteCredito.getText()).append("  AND");
            }

            if (!cmbTipid.getSelectedItem().toString().equalsIgnoreCase("Seleccione")) {
                // Suponiendo que cmbTipid contiene los nombres de los tipos y se necesita el id
                ResultSet re = s
                        .executeQuery("SELECT tipid FROM tipos WHERE tipnombre = '" + cmbTipid.getSelectedItem() + "'");
                re.next();
                int tipid = re.getInt("tipid");
                consulta.append(" c.tipid = ").append(tipid).append("  AND");
            }

            // Eliminar el "AND" adicional al final de la consulta
            consulta.delete(consulta.length() - 5, consulta.length());

            // Ejecutar la consulta
            ResultSet rs = s.executeQuery(consulta.toString());

            while (rs.next()) {
                modelo.addRow(new Object[] { rs.getInt("cliid"), rs.getString("clinombre"),
                        rs.getString("cliapellidos"), rs.getString("clisexo"), rs.getDouble("cliLimiteCredito"),
                        rs.getString("tipnombre") });
            }

        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println("Error: " + e.getErrorCode() + ": " + e);
        }
    }

    private void buscarTip() {
        modelo.setRowCount(0);
        try {
            Statement s = ConexionDB.conexion.createStatement();

            StringBuilder consulta = new StringBuilder("SELECT * FROM tipos WHERE");

            // Verificar y agregar condiciones según los campos proporcionados por el
            // usuario
            if (!txtTipid.getText().isEmpty()) {
                consulta.append(" tipid = ").append(txtTipid.getText()).append("  AND");
            }

            if (!txtTipNombre.getText().isEmpty()) {
                consulta.append(" tipnombre LIKE '%").append(txtTipNombre.getText()).append("%'  AND");
            }

            // Eliminar el "AND" adicional al final de la consulta
            consulta.delete(consulta.length() - 5, consulta.length());

            // Ejecutar la consulta
            ResultSet rs = s.executeQuery(consulta.toString());

            // Procesar y mostrar los resultados (usar según tus necesidades)
            while (rs.next()) {
                modelo.addRow(new Object[] { rs.getInt("tipid"), rs.getString("tipnombre") });
                System.out.println("TipId: " + rs.getInt("tipid") + ", TipNombre: " + rs.getString("tipnombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void insertarTablaTip() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            // Si el tipo no existe, no se puede modificar en la tabla
            if (!txtTipid.getText().equals("*")) {
                ResultSet re = s.executeQuery("SELECT * FROM tipos WHERE tipid = " + txtTipid.getText() + ";");
                if (!re.next()) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "El Tipo no existe.");
                    return;
                }
            }

            // Verificar si el procedimiento ya existe
            ResultSet rs = s.executeQuery(
                    "SELECT COUNT(*) FROM information_schema.routines WHERE routine_name = 'Sp_MttoTipos'");
            rs.next();
            int procedureCount = rs.getInt(1);
            if (procedureCount == 0) {
                s.execute(procedimientoTipos());
                System.out.println("Procedure created successfully.");
            }

            if (txtTipid.getText().equals("*")) {
                txtTipid.setText("0");
            }

            CallableStatement cs = ConexionDB.conexion.prepareCall("{call Sp_MttoTipos(?, ?)}");

            // Configurar los parámetros de entrada y salida
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setInt(1, Integer.parseInt(txtTipid.getText())); // tipId

            cs.setString(2, txtTipNombre.getText()); // tipNombre

            cs.execute();

            // Obtener el valor del parámetro de salida tipId
            int tipIdOutput = cs.getInt(1);
            System.out.println(txtTipid.getText() + " TIP");

            modelo.setRowCount(0);
            llenarTablaTipos();

            // - pendiente de revisar si dejar output o volver a poner *
            // ---------------------------------------
            txtTipid.setText(String.valueOf(tipIdOutput));
            // txtTipid.setText("*");
            if (rdNuevo.isSelected()) {
                txtTipNombre.setEnabled(false);
            }

        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println("Error: " + e.getErrorCode());
        }
    }

    private void insertarTablaCliente() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            // Si el cliente no existe, no se puede modificar en la tabla
            if (!txtClienteId.getText().equals("*")) {
                ResultSet re = s.executeQuery(
                        "SELECT cliid FROM clientes INNER JOIN tipos t ON clientes.tipid = t.tipid WHERE cliid = "
                                + txtClienteId.getText() + ";");
                if (!re.next()) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "El Cliente no existe.");
                    re.close();
                    return;
                }
            }

            // Obtener el tipid correspondiente al nombre del tipo seleccionado
            ResultSet re = s.executeQuery(
                    "SELECT tipid FROM tipos WHERE tipnombre = '" + cmbTipid.getSelectedItem() + "'");
            if (!re.next()) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Seleccione un tipo.");
                re.close();
                return;
            }
            int tipid = re.getInt("tipid");
            re.close();

            // Verificar si el procedimiento ya existe
            ResultSet rs = s.executeQuery(
                    "SELECT COUNT(*) FROM information_schema.routines WHERE routine_name = 'Sp_MttoClientes'");
            rs.next();
            int procedureCount = rs.getInt(1);
            if (procedureCount == 0) {
                s.execute(procedimientoClientes());
                System.out.println("Procedure created successfully.");
            }
            rs.close();

            if (txtClienteId.getText().equals("*")) {
                txtClienteId.setText("0");
            }

            CallableStatement cs = ConexionDB.conexion.prepareCall("{call Sp_MttoClientes(?, ?, ?, ?, ?, ?)}");

            // Configurar los parámetros de entrada y salida
            cs.registerOutParameter(1, java.sql.Types.INTEGER); // cliId (parámetro de salida)
            cs.setInt(1, Integer.parseInt(txtClienteId.getText())); // cliId (parámetro de entrada)
            cs.setString(2, txtClienteNombre.getText()); // cliNombre
            cs.setString(3, txtClienteApellidos.getText()); // cliApellidos
            cs.setString(4, txtClienteSexo.getText()); // cliSexo
            cs.setDouble(5, Double.parseDouble(txtClienteLimiteCredito.getText())); // cliLimiteCredito
            cs.setInt(6, tipid); // tipId

            cs.execute();

            // Obtener el valor del parámetro de salida cliId
            int cliIdOutput = cs.getInt(1);
            cs.close();
            Notifications.getInstance().show(Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Cliente guardado correctamente. CliId: " + cliIdOutput);
            modelo.setRowCount(0);
            llenarTablaClientes();
            txtClienteId.setText(String.valueOf(cliIdOutput));
            if (rdNuevo.isSelected()) {
                btnGuardar.setEnabled(false);
                txtClienteNombre.setEnabled(false);
                txtClienteApellidos.setEnabled(false);
                txtClienteSexo.setEnabled(false);
                txtClienteLimiteCredito.setEnabled(false);
                cmbTipid.setEnabled(false);
            }
        } catch (SQLException e) {
            ErrorHandler.handleSqlException(e);
            System.out.println(e);
            System.out.println("Error: " + e.getErrorCode());
        }
    }

    private void llenarTablaTipos() {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM tipos");

            while (rs.next()) {
                modelo.addRow(new Object[] { rs.getInt("tipid"), rs.getString("tipnombre") });
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void llenarTablaClientes() {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery(
                    "SELECT cliid, clinombre, cliapellidos, clisexo, clilimitecredito, t.tipnombre " +
                            "FROM clientes c " +
                            "INNER JOIN tipos t ON c.tipid = t.tipid");

            while (rs.next()) {
                modelo.addRow(new Object[] {
                        rs.getInt("cliid"),
                        rs.getString("clinombre"),
                        rs.getString("cliapellidos"),
                        rs.getString("clisexo"),
                        rs.getDouble("clilimitecredito"),
                        rs.getString("tipnombre")
                });
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                    "Error al llenar la tabla");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showClientes() {
        panelContent.removeAll();
        txtTipid.setText("");

        FlatAnimatedLafChange.showSnapshot();
        panelContent.add(lblClienteId);

        panelContent.add(txtClienteId);

        panelContent.add(lblClienteNombre);

        panelContent.add(txtClienteNombre);

        panelContent.add(lblClienteApellidos);

        panelContent.add(txtClienteApellidos);

        panelContent.add(lblClienteSexo);

        panelContent.add(txtClienteSexo);

        panelContent.add(lblClienteLimiteCredito);

        panelContent.add(txtClienteLimiteCredito);

        panelContent.add(lblTipid);

        panelContent.add(cmbTipid);
        llenarComboTipos();

        pnlTabla.add(scroll, BorderLayout.CENTER);

        FlatLaf.updateUILater();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    private void llenarComboTipos() {
        cmbTipid.removeAllItems();
        cmbTipid.addItem("Seleccione");
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM tipos");
            while (rs.next()) {
                cmbTipid.addItem(rs.getString("tipnombre"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showTipos() {
        panelContent.removeAll();

        FlatAnimatedLafChange.showSnapshot();
        panelContent.add(lblTipid);

        panelContent.add(txtTipid);

        panelContent.add(lblTipNombre);

        panelContent.add(txtTipNombre);

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

    public void setModo(int modo) {
        // Common actions
        grupo.clearSelection();
        btnGuardar.setEnabled(false);
        txtClienteId.setText("");
        // txtClienteNombre.setText("");
        // txtClienteApellidos.setText("");
        // txtClienteSexo.setText("");
        // txtClienteLimiteCredito.setText("");
        txtTipid.setText("");
        // txtTipNombre.setText("");
        scroll.setVisible(true);
        panel.setVisible(true);

        if (modo == ComponenteHeader.MODO_CAPTURA) {
            // Set properties for capture mode
            band = false;
            rdModificar.setVisible(true);
            rdNuevo.setVisible(true);
            btnLimpiar.setVisible(true);
            btnGuardar.setVisible(true);
            btnBuscar.setVisible(false);
            btnEliminar.setVisible(false);
        } else if (modo == ComponenteHeader.MODO_CONSULTA) {
            band = true;
            // Set properties for consultation mode
            rdModificar.setVisible(false);
            rdNuevo.setVisible(false);
            btnLimpiar.setVisible(false);
            btnGuardar.setVisible(false);
            btnBuscar.setVisible(true);
            btnEliminar.setVisible(true);
            txtTipid.setEditable(true);
            txtClienteId.setEditable(true);
            txtClienteNombre.setEnabled(true);
            txtClienteApellidos.setEnabled(true);
            txtClienteSexo.setEnabled(true);
            txtClienteLimiteCredito.setEnabled(true);
            cmbTipid.setEnabled(true);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        componenteHeader.setBounds(0, 0, getWidth(), (int) (getHeight() * .2));
        panel.setBounds(0, componenteHeader.getHeight(), getWidth(), (int) (getHeight() * .12));

        panelContent.setBounds(0, panel.getY() + panel.getHeight(), getWidth(),
                (int) (getHeight() * .5) - panel.getHeight());

        pnlTabla.setBounds(0, panelContent.getY() + panelContent.getHeight(),
                getWidth(),
                getHeight() - panelContent.getY() - panelContent.getHeight());

        int w = panel.getWidth();
        int h = panel.getHeight();

        int anchoComponente = (int) (w * .4);
        int altoComponente = (int) (h * .8);
        // -----------------------------------------------------------
        cmbTablas.setBounds((int) ((w * .025)), (int) (h * .03),
                anchoComponente,
                altoComponente);
        cmbTablas.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 350));

        rdModificar.setBounds(cmbTablas.getX() + cmbTablas.getWidth(), (int) (h * .05), (int) (w * .20),
                (int) (h * .45));
        rdModificar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        rdNuevo.setBounds(cmbTablas.getX() + cmbTablas.getWidth(), rdModificar.getY() + rdModificar.getHeight(),
                (int) (w * .20),
                (int) (h * .45));
        rdNuevo.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnLimpiar.setBounds(rdModificar.getX() + rdModificar.getWidth(), (int) (h * .05), (int) (w * .35),
                (int) (h * .45));
        btnLimpiar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnGuardar.setBounds(rdModificar.getX() + rdModificar.getWidth(), btnLimpiar.getY() + btnLimpiar.getHeight(),
                (int) (w * .35),
                (int) (h * .45));
        btnGuardar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnEliminar.setBounds(cmbTablas.getX() + cmbTablas.getWidth() + (int) (w * .05), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnEliminar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnBuscar.setBounds(btnEliminar.getX() + btnEliminar.getWidth(), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnBuscar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

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

    private String procedimientoTipos() {
        return "CREATE PROCEDURE Sp_MttoTipos\n"
                + "@tipId INT OUTPUT, @tipNombre VARCHAR(20)\n"
                + "AS\n"
                + "BEGIN\n"
                + "    IF EXISTS(SELECT * FROM tipos WHERE tipid = @tipId)\n"
                + "    BEGIN\n"
                + "        UPDATE tipos SET tipnombre = @tipNombre WHERE tipid = @tipId;\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Actualizar en la tabla Tipos', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "    ELSE\n"
                + "    BEGIN\n"
                + "        SELECT @tipId = COALESCE(MAX(tipid), 0) + 1 FROM tipos;\n"
                + "        INSERT INTO tipos VALUES(@tipId, @tipNombre);\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Insertar en la tabla Tipos', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "END;";
    }

    private String procedimientoClientes() {
        return "CREATE PROCEDURE Sp_MttoClientes\n"
                + "@cliId INT OUTPUT, @cliNombre VARCHAR(50), @cliApellidos VARCHAR(50),\n"
                + "@cliSexo CHAR(1), @cliLimiteCredito NUMERIC(12, 2), @tipId INT\n"
                + "AS\n"
                + "BEGIN\n"
                + "    IF EXISTS(SELECT * FROM clientes WHERE cliid = @cliId)\n"
                + "    BEGIN\n"
                + "        UPDATE clientes\n"
                + "        SET clinombre = @cliNombre, cliApellidos = @cliApellidos,\n"
                + "            cliSexo = @cliSexo, cliLimiteCredito = @cliLimiteCredito,\n"
                + "            tipid = @tipId\n"
                + "        WHERE cliid = @cliId;\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Actualizar en la tabla Clientes', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "    ELSE\n"
                + "    BEGIN\n"
                + "        SELECT @cliId = COALESCE(MAX(cliid), 0) + 1 FROM clientes;\n"
                + "        INSERT INTO clientes\n"
                + "        VALUES(@cliId, @cliNombre, @cliApellidos, @cliSexo, @cliLimiteCredito, @tipId);\n"
                + "        IF @@ERROR <> 0\n"
                + "        BEGIN\n"
                + "            RAISERROR('Error al Insertar en la tabla Clientes', 16, 10);\n"
                + "        END\n"
                + "    END\n"
                + "END;";
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        // Llenar los campos de texto del cliente
        if (txtClienteId.getText().isEmpty() || txtClienteId.getText().equals("*") || band) {
            return;
        }
        try {
            Integer.parseInt(txtClienteId.getText());
        } catch (NumberFormatException ex) {
            ErrorHandler.showNotification("Error: El ClienteId no es un número válido.");
            return;
        }

        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s.executeQuery(
                    "SELECT clinombre, cliapellidos, clisexo, clilimitecredito, t.tipnombre " +
                            "FROM clientes c " +
                            "INNER JOIN tipos t ON c.tipid = t.tipid " +
                            "WHERE cliid = " + txtClienteId.getText());

            if (rs.next()) {
                txtClienteNombre.setText(rs.getString("clinombre"));
                txtClienteApellidos.setText(rs.getString("cliapellidos"));
                txtClienteSexo.setText(rs.getString("clisexo"));
                txtClienteLimiteCredito.setText(rs.getString("clilimitecredito"));
                cmbTipid.setSelectedItem(rs.getString("tipnombre"));
            } else {
                Notifications.getInstance().show(Notifications.Type.INFO,
                        Notifications.Location.TOP_CENTER,
                        "El cliente no existe.");
                txtClienteNombre.setText("");
                txtClienteApellidos.setText("");
                txtClienteSexo.setText("");
                txtClienteLimiteCredito.setText("");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            ErrorHandler.handleSqlException(ex);
        }
    }

}