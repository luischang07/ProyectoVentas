
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

import mode.Rutinas2;

public class menu extends JPanel implements ComponentListener, ActionListener, FocusListener {

    private JPanel panel;
    private JPanel panelContent;
    private JPanel pnlTabla;

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
        iniciarT();
        HazEscuchas();
    }

    private void HazEscuchas() {
        addComponentListener(this);
        txtClienteId.addFocusListener(this);
        btnBuscar.addActionListener(this);
        btnEliminar.addActionListener(this);
        btnLimpiar.addActionListener(this);
        btnGuardar.addActionListener(this);
        rdModificar.addActionListener(this);
        rdNuevo.addActionListener(this);
    }

    private void iniciarT() {
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
        System.out.println("Clientes");
        txtClienteId.setEditable(true);
        showClientes();

        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        modelo.addColumn("CliId");
        modelo.addColumn("CliNombre");
        modelo.addColumn("CliApellidos");
        modelo.addColumn("CliSexo");
        modelo.addColumn("CliLimiteCredito");
        modelo.addColumn("TipId");
        llenarTablaClientes();
    }

    public void init() {
        setLayout(null);
        //
        componenteHeader = new ComponenteHeader(this);
        add(componenteHeader, BorderLayout.NORTH);

        // PANEL--------------------------------------------------------
        panel = new JPanel();
        panel.setVisible(false);
        panel.setLayout(null);

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

        if (evt.getSource() == btnLimpiar) {
            limpiar();
            return;
        }

        if (evt.getSource() == btnGuardar) {
            if (txtClienteNombre.getText().equals("") || txtClienteApellidos.getText().equals("")
                    || txtClienteLimiteCredito.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Inserte datos en los Campos Vacios");
                return;
            }
            try {
                Float.parseFloat(txtClienteLimiteCredito.getText());
            } catch (NumberFormatException e) {
                ErrorHandler.showNotification("El ClienteLimiteCredito no es un número válido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.out.println("Error: El ClienteLimiteCredito no es un número válido.");
                return;
            }
            if (!txtClienteId.getText().equals("*")) {
                try {
                    Integer.parseInt(txtClienteId.getText());
                } catch (NumberFormatException e) {
                    ErrorHandler.showNotification("El ClienteId no es un número válido.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error: El ClienteId no es un número válido.");
                    return;
                }
            }
            if (!txtClienteSexo.getText().equalsIgnoreCase("M")
                    && !txtClienteSexo.getText().equalsIgnoreCase("F")) {
                ErrorHandler.showNotification("El ClienteSexo debe ser 'M' o 'F'.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            insertarTablaCliente();
            return;
        }

        if (evt.getSource() == rdModificar) {
            btnLimpiar.setEnabled(true);
            btnGuardar.setEnabled(true);

            txtClienteNombre.setEnabled(true);
            txtClienteApellidos.setEnabled(true);
            txtClienteLimiteCredito.setEnabled(true);
            txtClienteSexo.setEnabled(true);
            cmbTipid.setEnabled(true);

            txtClienteId.setText("");
            txtClienteId.setEditable(true);
            return;
        }

        if (evt.getSource() == rdNuevo) {
            btnLimpiar.setEnabled(true);
            btnGuardar.setEnabled(true);
            txtClienteNombre.setEnabled(true);
            txtClienteApellidos.setEnabled(true);
            txtClienteSexo.setEnabled(true);
            txtClienteLimiteCredito.setEnabled(true);
            cmbTipid.setEnabled(true);
            txtClienteId.setText("*");
            txtClienteId.setEditable(false);
            return;
        }

        if (evt.getSource() == btnBuscar) {
            System.out.println("Buscar Clientes");
            buscarCliente();
            return;
        }

        if (evt.getSource() == btnEliminar) {
            // Mensaje de alerta para confirmar la eliminación
            int dialogResult = JOptionPane.showConfirmDialog(this, "¿Está seguro que quiere eliminar?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (dialogResult != JOptionPane.YES_OPTION) {
                return;
            }
            if (txtClienteId.getText().isEmpty() && txtClienteNombre.getText().isEmpty()
                    && txtClienteApellidos.getText().isEmpty() && txtClienteSexo.getText().isEmpty()
                    && txtClienteLimiteCredito.getText().isEmpty()
                    && cmbTipid.getSelectedItem().toString().equals("Seleccione")) {
                JOptionPane.showMessageDialog(null, "Ningún campo insertado, nada para eliminar");
                return;
            }
            eliminarCliente();
            return;
        }
    }

    private void limpiar() {
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
                ErrorHandler.showNotification("No se encontraron registros para eliminar.", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                ErrorHandler.showNotification("Cliente eliminado correctamente.", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Procesar y mostrar los resultados (usar según tus necesidades)
            modelo.setRowCount(0);
            llenarTablaClientes();

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

    private void insertarTablaCliente() {
        try {
            Statement s = ConexionDB.conexion.createStatement();

            // Si el cliente no existe, no se puede modificar en la tabla
            if (!txtClienteId.getText().equals("*")) {
                ResultSet re = s.executeQuery(
                        "SELECT cliid FROM clientes INNER JOIN tipos t ON clientes.tipid = t.tipid WHERE cliid = "
                                + txtClienteId.getText() + ";");
                if (!re.next()) {
                    ErrorHandler.showNotification("No se puede modificar un cliente que no existe.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    re.close();
                    return;
                }
            }

            // Obtener el tipid correspondiente al nombre del tipo seleccionado
            ResultSet re = s.executeQuery(
                    "SELECT tipid FROM tipos WHERE tipnombre = '" + cmbTipid.getSelectedItem() + "'");
            if (!re.next()) {
                ErrorHandler.showNotification("No se encontró el tipo seleccionado.", "Error",
                        JOptionPane.ERROR_MESSAGE);
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
            ErrorHandler.showNotification("Cliente guardado correctamente. CliId: " + cliIdOutput, "Información",
                    JOptionPane.INFORMATION_MESSAGE);
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
            ErrorHandler.showNotification("Error al llenar la tabla de clientes.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showClientes() {
        panelContent.removeAll();
        txtTipid.setText("");

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

        SwingUtilities.updateComponentTreeUI(panelContent);
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
            btnEliminar.setVisible(true);
        } else if (modo == ComponenteHeader.MODO_CONSULTA) {
            band = true;
            // Set properties for consultation mode
            rdModificar.setVisible(false);
            rdNuevo.setVisible(false);
            btnLimpiar.setVisible(false);
            btnGuardar.setVisible(false);
            btnBuscar.setVisible(true);
            btnEliminar.setVisible(false);
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

        int anchoComponente = (int) (w * .2);
        int altoComponente = (int) (h * .8);
        // -----------------------------------------------------------
        rdModificar.setBounds((int) ((w * .025)), (int) (h * .03), (int) (w * .20),
                (int) (h * .45));
        rdModificar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        rdNuevo.setBounds(rdModificar.getX(), (int) (rdModificar.getY() + rdModificar.getHeight() + (h * .05)),
                (int) (w * .20),
                (int) (h * .45));
        rdNuevo.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnEliminar.setBounds((int) (rdNuevo.getX() * 1.1) + rdModificar.getWidth(), rdModificar.getY(),
                anchoComponente,
                altoComponente);
        btnEliminar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 350));

        btnLimpiar.setBounds((int) (btnEliminar.getX() * 1.1) + btnEliminar.getWidth(), rdModificar.getY(),
                btnEliminar.getWidth(),
                btnEliminar.getHeight());
        btnLimpiar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnGuardar.setBounds((int) (btnLimpiar.getX() * 1.07) + btnLimpiar.getWidth(), btnLimpiar.getY(),
                btnLimpiar.getWidth(),
                btnLimpiar.getHeight());
        btnGuardar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnEliminar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnBuscar.setBounds((int) (w * .70), (int) (h * .05),
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
            ErrorHandler.showNotification("El ClienteId no es un número válido.", "Error",
                    JOptionPane.ERROR_MESSAGE);
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
                ErrorHandler.showNotification("El cliente no existe", "Error", JOptionPane.ERROR_MESSAGE);
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