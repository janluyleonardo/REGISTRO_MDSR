package registro;

import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sql.dataBase;

public class VENTANA extends javax.swing.JFrame {

    DefaultTableModel modeloTablaDocente = new DefaultTableModel();
    DefaultTableModel modeloTablaEstudiante = new DefaultTableModel();
    public static String userLogin = "";
    PreparedStatement ps;
    ResultSet rs;

    public VENTANA() {
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/icono/chack-list.png")).getImage());
        initComponents();
        jPDocente.setVisible(false);
        jPEstudiante.setVisible(false);
        this.setLocationRelativeTo(this);
        listar_depto(cbdepartamento);
        listar_carrera(cbCarrera);
        modeloTablaDocente.addColumn("Nombre");
        modeloTablaDocente.addColumn("Apellido");
        modeloTablaDocente.addColumn("Documento");
        modeloTablaDocente.addColumn("Teléfono");
        modeloTablaDocente.addColumn("Correo");
        jtableRegistros.setModel(modeloTablaDocente);
        modeloTablaEstudiante.addColumn("id materia");
        modeloTablaEstudiante.addColumn("id carrera");
        modeloTablaEstudiante.addColumn("Materia");
        jtableEstudiantes.setModel(modeloTablaEstudiante);
        cargaTablaDocente();
    }

    REG e = new REG();

    public void listar_depto(JComboBox departamento) {
        String sqlDepartamento = "SELECT * FROM departamento WHERE 1";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlDepartamento);
            while (rs.next()) {
//               departamento.addItem(rs.getString("id")+" - "+rs.getString("nombre"));
                departamento.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            System.out.println("exception departamento: " + ex);
        }
    }

    public void listar_carrera(JComboBox carrera) {
        String sqlDepartamento = "SELECT * FROM carrera WHERE 1";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlDepartamento);
            while (rs.next()) {
//               departamento.addItem(rs.getString("id")+" - "+rs.getString("nombre"));
                carrera.addItem(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            System.out.println("exception carrera: " + ex);
        }
    }

    public void listar_ciudad(JComboBox ciudad, int item) {
        String sqlCiudad = "SELECT * FROM ciudad where id_departamento = '" + item + "'";

        cbciudad.removeAllItems();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCiudad);
            ciudad.addItem("---");
            while (rs.next()) {
                ciudad.addItem(rs.getString("nombre_ciudad"));
            }
        } catch (SQLException ex) {
            System.out.println("exception ciudad: " + ex);
        }
    }
    
    public void listar_materia(JComboBox materia, int item) {
        String sqlCiudad = "SELECT * FROM materias WHERE ID_Carrera = '" + item + "'";

        cbMateria.removeAllItems();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCiudad);
            materia.addItem("---");
            while (rs.next()) {
                materia.addItem(rs.getString("Nom_materia"));
            }
        } catch (SQLException ex) {
            System.out.println("exception ciudad: " + ex);
        }
    }
    
    public void validarDocumento() {
        if (txtDocumento.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Para consultar , se requiere un número de documento, ingrese uno por favor!", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtDocumento.requestFocus();
        }
    }

    public void cargaTablaDocente() {
        modeloTablaDocente.getDataVector().removeAllElements();
        jtableRegistros.updateUI();
        String sqlBusqueda = "";
        sqlBusqueda = "SELECT * FROM estudiantes WHERE Estado = 1";
        try {
            ps = con.prepareStatement(sqlBusqueda);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                String dato[] = new String[5];
                dato[0] = rs.getString("Nom_Estudiante");
                dato[1] = rs.getString("Ape_Estudiante");
                dato[2] = String.valueOf(rs.getInt("Documento"));
                dato[3] = rs.getString("Num_Telefonico");
                dato[4] = rs.getString("Correo");
                modeloTablaDocente.addRow(dato);
            }
        } catch (Exception e) {
        }        
    }
    
    public void cargaTablaEstudiante(String idAlumnoGet) {
        modeloTablaEstudiante.getDataVector().removeAllElements();
        jtableRegistros.updateUI();
        String sqlBusqueda = "";
        sqlBusqueda = "SELECT * FROM materia_inscrita WHERE Estado = 1 AND ID_alumno = ?";
        try {
            ps = con.prepareStatement(sqlBusqueda);
            ps.setString(1, idAlumnoGet);
            rs = ps.executeQuery();
            while (rs.next()) {
                String dato[] = new String[3];
                dato[0] = rs.getString("id");
                dato[1] = rs.getString("id_carrera");
                dato[2] = rs.getString("nombre");
                modeloTablaEstudiante.addRow(dato);
            }
        } catch (SQLException es) {
            System.out.println("execpcion cargando tabla estudiante: "+es);
        }        
    }
    
    public void limpiarCajasDocente() {
        txtDocumento.setText("");
        txtId.setText("");
        txtnombre.setText("");
        txtnombre.setText("");
        txtapellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        jornadaGroup.clearSelection();
        estadoGroup.clearSelection();
        generoGroup.clearSelection();
        cbdepartamento.setSelectedIndex(0);
        listar_ciudad(cbciudad, 0);
    }

    public void limpiarCajasEstudiante() {
        cbCarrera.removeAllItems();
    }

    public void consulEstudiante(String correo){
        System.out.println("recibi el correo: "+correo);
        String sqlBusqueda = "";
        sqlBusqueda = "SELECT * FROM estudiantes WHERE Correo = ?";
        try {
            ps = con.prepareStatement(sqlBusqueda);
            ps.setString(1, correo);

            rs = ps.executeQuery();

            if (rs.next()) {
                txtIdEst.setText(rs.getString("ID_Estudiante"));
                txtDocEst.setText(rs.getString("Documento")); 
                txtNomEst.setText(rs.getString("Nom_Estudiante")+" "+rs.getString("Ape_Estudiante"));
            }else{
                System.out.println("no encontre alumno con ese correo");
                JOptionPane.showMessageDialog(rootPane, "No te encuentras registrado dentro del sistema como alumno habilitado,\n comunicate con tu profesor para que te registre", "Señor estudiante", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
//                jPEstudiante.setVisible(false);
//                jPDocente.setVisible(false);
//                jPLogin.setVisible(true);
            }
        } catch (SQLException exE) {
            System.out.println("excepcio en busqueda de alumno: "+exE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jornadaGroup = new javax.swing.ButtonGroup();
        estadoGroup = new javax.swing.ButtonGroup();
        generoGroup = new javax.swing.ButtonGroup();
        jPDocente = new javax.swing.JPanel();
        lblDocente = new javax.swing.JLabel();
        jPanelEstado = new javax.swing.JPanel();
        ckbInactivo = new javax.swing.JCheckBox();
        ckbActivo = new javax.swing.JCheckBox();
        jPanleDomicilio = new javax.swing.JPanel();
        cbciudad = new javax.swing.JComboBox<>();
        cbdepartamento = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanleGenero = new javax.swing.JPanel();
        rbDiurno = new javax.swing.JRadioButton();
        rbNocturno = new javax.swing.JRadioButton();
        jPanleDatos = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtapellido = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        BTNSALIR = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jPanleGenero1 = new javax.swing.JPanel();
        rbmasculino = new javax.swing.JRadioButton();
        rbFemenino = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtableRegistros = new javax.swing.JTable();
        jPEstudiante = new javax.swing.JPanel();
        lblEstudiante = new javax.swing.JLabel();
        jPanleDatos1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtNomEst = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtDocEst = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtIdEst = new javax.swing.JTextField();
        BTNSALIR1 = new javax.swing.JButton();
        btnQuitarMateria = new javax.swing.JButton();
        btnLimpiarMateria = new javax.swing.JButton();
        cbCarrera = new javax.swing.JComboBox<>();
        cbMateria = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtableEstudiantes = new javax.swing.JTable();
        btnConsultarMateria = new javax.swing.JButton();
        btnAgregaMateria2 = new javax.swing.JButton();
        jPLogin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblContraseña = new javax.swing.JLabel();
        btnIngresar = new javax.swing.JButton();
        jpasContraseña = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MDSR - login");
        setMinimumSize(new java.awt.Dimension(700, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPDocente.setAlignmentX(0.0F);
        jPDocente.setAlignmentY(0.0F);
        jPDocente.setMaximumSize(new java.awt.Dimension(700, 600));
        jPDocente.setMinimumSize(new java.awt.Dimension(700, 600));
        jPDocente.setName(""); // NOI18N
        jPDocente.setPreferredSize(new java.awt.Dimension(700, 600));
        jPDocente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDocente.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        lblDocente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPDocente.add(lblDocente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 660, 30));

        jPanelEstado.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Estado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 12))); // NOI18N
        jPanelEstado.setOpaque(false);

        estadoGroup.add(ckbInactivo);
        ckbInactivo.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        ckbInactivo.setText("Inactivo");
        ckbInactivo.setOpaque(false);

        estadoGroup.add(ckbActivo);
        ckbActivo.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        ckbActivo.setText("Activo");
        ckbActivo.setOpaque(false);

        javax.swing.GroupLayout jPanelEstadoLayout = new javax.swing.GroupLayout(jPanelEstado);
        jPanelEstado.setLayout(jPanelEstadoLayout);
        jPanelEstadoLayout.setHorizontalGroup(
            jPanelEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstadoLayout.createSequentialGroup()
                .addComponent(ckbActivo)
                .addGap(18, 18, 18)
                .addComponent(ckbInactivo)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanelEstadoLayout.setVerticalGroup(
            jPanelEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstadoLayout.createSequentialGroup()
                .addGroup(jPanelEstadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckbActivo)
                    .addComponent(ckbInactivo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPDocente.add(jPanelEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, 180, 55));

        jPanleDomicilio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Residencia", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 14))); // NOI18N
        jPanleDomicilio.setOpaque(false);

        cbciudad.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        cbciudad.setMaximumRowCount(10);
        cbciudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---" }));

        cbdepartamento.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        cbdepartamento.setMaximumRowCount(10);
        cbdepartamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---" }));
        cbdepartamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbdepartamentoItemStateChanged(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Ciudad:");

        jLabel10.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel10.setText("Departamento:");

        javax.swing.GroupLayout jPanleDomicilioLayout = new javax.swing.GroupLayout(jPanleDomicilio);
        jPanleDomicilio.setLayout(jPanleDomicilioLayout);
        jPanleDomicilioLayout.setHorizontalGroup(
            jPanleDomicilioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanleDomicilioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanleDomicilioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanleDomicilioLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addContainerGap(151, Short.MAX_VALUE))
                    .addGroup(jPanleDomicilioLayout.createSequentialGroup()
                        .addGroup(jPanleDomicilioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cbciudad, javax.swing.GroupLayout.Alignment.LEADING, 0, 218, Short.MAX_VALUE)
                            .addComponent(cbdepartamento, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanleDomicilioLayout.setVerticalGroup(
            jPanleDomicilioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanleDomicilioLayout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbdepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPDocente.add(jPanleDomicilio, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 260, 140));

        jPanleGenero.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Jornada de estudio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 14))); // NOI18N
        jPanleGenero.setOpaque(false);

        jornadaGroup.add(rbDiurno);
        rbDiurno.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        rbDiurno.setText("Diurno");
        rbDiurno.setOpaque(false);

        jornadaGroup.add(rbNocturno);
        rbNocturno.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        rbNocturno.setText("Nocturno");
        rbNocturno.setOpaque(false);

        javax.swing.GroupLayout jPanleGeneroLayout = new javax.swing.GroupLayout(jPanleGenero);
        jPanleGenero.setLayout(jPanleGeneroLayout);
        jPanleGeneroLayout.setHorizontalGroup(
            jPanleGeneroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanleGeneroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbNocturno)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(rbDiurno)
                .addGap(20, 20, 20))
        );
        jPanleGeneroLayout.setVerticalGroup(
            jPanleGeneroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanleGeneroLayout.createSequentialGroup()
                .addGroup(jPanleGeneroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbDiurno)
                    .addComponent(rbNocturno, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPDocente.add(jPanleGenero, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 260, 55));

        jPanleDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del estudiante:", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 14))); // NOI18N
        jPanleDatos.setOpaque(false);

        jLabel11.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Documento:");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel11.setInheritsPopupMenu(false);

        txtnombre.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtnombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtapellido.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtapellido.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel12.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Nombre:");

        jLabel13.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Apellido:");

        txtDocumento.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtDocumento.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel14.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Id:");

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtId.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel17.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Correo:");

        txtCorreo.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtCorreo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel19.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Teléfono:");

        txtTelefono.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtTelefono.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanleDatosLayout = new javax.swing.GroupLayout(jPanleDatos);
        jPanleDatos.setLayout(jPanleDatosLayout);
        jPanleDatosLayout.setHorizontalGroup(
            jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanleDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanleDatosLayout.createSequentialGroup()
                        .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanleDatosLayout.createSequentialGroup()
                                .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanleDatosLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)))
                        .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnombre)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanleDatosLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtapellido, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanleDatosLayout.createSequentialGroup()
                                .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtId))))
                    .addGroup(jPanleDatosLayout.createSequentialGroup()
                        .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanleDatosLayout.setVerticalGroup(
            jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanleDatosLayout.createSequentialGroup()
                .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(txtapellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanleDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPDocente.add(jPanleDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 400, 190));

        btnEliminar.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPDocente.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, -1, -1));

        btnBuscar.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPDocente.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, -1, -1));

        BTNSALIR.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        BTNSALIR.setText("SALIR");
        BTNSALIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNSALIRActionPerformed(evt);
            }
        });
        jPDocente.add(BTNSALIR, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 560, -1, -1));

        btnLimpiar.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jPDocente.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 300, -1, -1));

        btnEditar.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnEditar.setText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jPDocente.add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 300, -1, -1));

        btnGuardar.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPDocente.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 300, -1, -1));

        jPanleGenero1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Genero", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 14))); // NOI18N
        jPanleGenero1.setOpaque(false);

        generoGroup.add(rbmasculino);
        rbmasculino.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        rbmasculino.setText("Masculino");
        rbmasculino.setOpaque(false);

        generoGroup.add(rbFemenino);
        rbFemenino.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        rbFemenino.setText("Femenino");
        rbFemenino.setOpaque(false);

        javax.swing.GroupLayout jPanleGenero1Layout = new javax.swing.GroupLayout(jPanleGenero1);
        jPanleGenero1.setLayout(jPanleGenero1Layout);
        jPanleGenero1Layout.setHorizontalGroup(
            jPanleGenero1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanleGenero1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbmasculino)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(rbFemenino)
                .addGap(25, 25, 25))
        );
        jPanleGenero1Layout.setVerticalGroup(
            jPanleGenero1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanleGenero1Layout.createSequentialGroup()
                .addGroup(jPanleGenero1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbmasculino)
                    .addComponent(rbFemenino))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPDocente.add(jPanleGenero1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 185, 260, 55));

        jtableRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtableRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtableRegistrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtableRegistros);

        jPDocente.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 660, 210));

        getContentPane().add(jPDocente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPEstudiante.setMaximumSize(new java.awt.Dimension(700, 600));
        jPEstudiante.setMinimumSize(new java.awt.Dimension(700, 600));
        jPEstudiante.setPreferredSize(new java.awt.Dimension(700, 600));
        jPEstudiante.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEstudiante.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        lblEstudiante.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstudiante.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPEstudiante.add(lblEstudiante, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 660, 30));

        jPanleDatos1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos alumno autenticado", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 1, 14))); // NOI18N
        jPanleDatos1.setOpaque(false);

        jLabel15.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Documento:");
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel15.setInheritsPopupMenu(false);

        txtNomEst.setEditable(false);
        txtNomEst.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtNomEst.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel16.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Nombres:");

        txtDocEst.setEditable(false);
        txtDocEst.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtDocEst.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel18.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Id:");

        txtIdEst.setEditable(false);
        txtIdEst.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        txtIdEst.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanleDatos1Layout = new javax.swing.GroupLayout(jPanleDatos1);
        jPanleDatos1.setLayout(jPanleDatos1Layout);
        jPanleDatos1Layout.setHorizontalGroup(
            jPanleDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanleDatos1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanleDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanleDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanleDatos1Layout.createSequentialGroup()
                        .addComponent(txtDocEst, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(130, 130, 130)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdEst, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                    .addComponent(txtNomEst))
                .addContainerGap())
        );
        jPanleDatos1Layout.setVerticalGroup(
            jPanleDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanleDatos1Layout.createSequentialGroup()
                .addGroup(jPanleDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDocEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel18)
                    .addComponent(txtIdEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanleDatos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNomEst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPEstudiante.add(jPanleDatos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 650, 90));

        BTNSALIR1.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        BTNSALIR1.setText("SALIR");
        BTNSALIR1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNSALIR1ActionPerformed(evt);
            }
        });
        jPEstudiante.add(BTNSALIR1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 550, -1, -1));

        btnQuitarMateria.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnQuitarMateria.setText("QUITAR");
        btnQuitarMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarMateriaActionPerformed(evt);
            }
        });
        jPEstudiante.add(btnQuitarMateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 190, -1, -1));

        btnLimpiarMateria.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnLimpiarMateria.setText("LIMPIAR");
        btnLimpiarMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarMateriaActionPerformed(evt);
            }
        });
        jPEstudiante.add(btnLimpiarMateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 190, -1, -1));

        cbCarrera.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        cbCarrera.setMaximumRowCount(10);
        cbCarrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---" }));
        cbCarrera.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbCarreraItemStateChanged(evt);
            }
        });
        jPEstudiante.add(cbCarrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 180, -1));

        cbMateria.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        cbMateria.setMaximumRowCount(10);
        cbMateria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---" }));
        jPEstudiante.add(cbMateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, 240, -1));

        jLabel20.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Carrera:");
        jPEstudiante.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));

        jLabel21.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Materia:");
        jPEstudiante.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 150, -1, -1));

        jtableEstudiantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jtableEstudiantes);

        jPEstudiante.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 650, 310));

        btnConsultarMateria.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnConsultarMateria.setText("CONSULTAR");
        btnConsultarMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarMateriaActionPerformed(evt);
            }
        });
        jPEstudiante.add(btnConsultarMateria, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, -1, -1));

        btnAgregaMateria2.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
        btnAgregaMateria2.setText("AGREGAR");
        btnAgregaMateria2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaMateria2ActionPerformed(evt);
            }
        });
        jPEstudiante.add(btnAgregaMateria2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 190, -1, -1));

        getContentPane().add(jPEstudiante, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPLogin.setMaximumSize(new java.awt.Dimension(700, 600));
        jPLogin.setMinimumSize(new java.awt.Dimension(700, 600));
        jPLogin.setPreferredSize(new java.awt.Dimension(700, 600));
        jPLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Bienvenidos Sistema digital");
        jPLogin.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 70));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("MDSR");
        jPLogin.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 71, 700, 70));

        lblUsuario.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblUsuario.setText("Usuario:");
        jPLogin.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, -1, -1));

        txtUsuario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPLogin.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 253, 30));

        lblContraseña.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblContraseña.setText("Contraseña:");
        jPLogin.add(lblContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, -1, -1));

        btnIngresar.setText("INGRESAR");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        jPLogin.add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 410, 260, 30));

        jpasContraseña.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPLogin.add(jpasContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, 260, 30));

        getContentPane().add(jPLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        String sql = "", resul="", userDB = "", correoDB="", passDB = "", txtUser = "", txtPass = "", rolDB = "", nombresDB = "";
        int res = 0;
        txtUser = txtUsuario.getText();
        txtPass = jpasContraseña.getText();

        if (txtUser.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Debe ingresar el usuario, el campo es obligatorio", "Señor usuario", JOptionPane.ERROR_MESSAGE);
            txtUsuario.requestFocus();
        } else if (txtPass.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Debe ingresar la contrsaeña, el campo es obligatorio", "Señor usuario", JOptionPane.ERROR_MESSAGE);
            jpasContraseña.requestFocus();
        } else {
            sql = "SELECT * FROM usuarios WHERE Correo = ? AND estado = 1";

            try {
                ps = con.prepareStatement(sql);
                ps.setString(1, txtUser);

                rs = ps.executeQuery();

                if (rs.next()) {
                    userDB = rs.getString("Correo");
                    passDB = rs.getString("password");
                    rolDB = rs.getString("Perfil");
                    nombresDB = rs.getString("Nombres");
                    correoDB = rs.getString("Correo");
                } else {
                    System.out.println("La consulta no trae datos");
                }

                if (userDB.equals(txtUser) && passDB.equals(txtPass)) {

                    if (rolDB.equals("Docente")) {
                        jPLogin.setVisible(false);
                        jPEstudiante.setVisible(false);
                        jPDocente.setVisible(true);
                        this.setTitle("MDSR - Docentes");
                        lblDocente.setText("Bienvenido profesor " + nombresDB);
                    } else if (rolDB.equals("Estudiante")) {
                        this.setTitle("MDSR - Estudiantes");
                        jPDocente.setVisible(false);
                        jPLogin.setVisible(false);
                        jPEstudiante.setVisible(true);
                        lblEstudiante.setText("Bienvenido estudiante " + nombresDB);
                        consulEstudiante(correoDB);
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Las credenciales suministradas no concuerdan \n ó el usuario no existe en el sistema", "Señor usuario", JOptionPane.ERROR_MESSAGE);
//                    this.dispose();
                }
            } catch (HeadlessException | SQLException e) {
                System.out.println("Exceptionn tryCatch consulta usuario login: " + e);
            }
        }
    }//GEN-LAST:event_btnIngresarActionPerformed

    private void BTNSALIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNSALIRActionPerformed
        System.exit(0);
    }//GEN-LAST:event_BTNSALIRActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        validarDocumento();
        String sqlBusqueda = "", generoDB = "", estadoDB = "", jornadaDB = "";
        sqlBusqueda = "SELECT * FROM estudiantes WHERE Documento = ?";
        try {
            ps = con.prepareStatement(sqlBusqueda);
            ps.setInt(1, Integer.parseInt(txtDocumento.getText()));

            rs = ps.executeQuery();

            if (rs.next()) {
                txtDocumento.setText(rs.getString("Documento"));
                txtnombre.setText(rs.getString("Nom_Estudiante"));
                txtapellido.setText(rs.getString("Ape_Estudiante"));
                txtId.setText(rs.getString("ID_Estudiante"));
                cbdepartamento.setSelectedItem(rs.getString("Departamento"));
                cbciudad.setSelectedItem(rs.getString("Ciudad"));
                txtCorreo.setText(rs.getString("Correo"));
                txtTelefono.setText(rs.getString("Num_Telefonico"));
                generoDB = rs.getString("Genero");
                estadoDB = rs.getString("Estado");
                jornadaDB = rs.getString("jornada");
                if (generoDB.equals("Masculino")) {
                    rbmasculino.setSelected(true);
                } else {
                    rbFemenino.setSelected(true);
                }
                if (estadoDB.equals("1")) {
                    ckbActivo.setSelected(true);
                } else {
                    ckbInactivo.setSelected(true);
                }
                if (jornadaDB.equals("Nocturno")) {
                    rbNocturno.setSelected(true);
                } else {
                    rbDiurno.setSelected(true);
                }
                cargaTablaDocente();
            } else {
                JOptionPane.showMessageDialog(null, "La consulta no arroja datos con el documento ingresado", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String sqlInsert = "", setJornada = "", setGenero = "";
        boolean setEstado;
        setEstado = true;
        if (rbmasculino.isSelected()) {
            setGenero = "Masculino";
        } else if (rbFemenino.isSelected()) {
            setGenero = "Femenino";
        }
        if (ckbActivo.isSelected()) {
            setEstado = true;
        } else if (ckbInactivo.isSelected()) {
            setEstado = false;
        }
        if (rbNocturno.isSelected()) {
            setJornada = "Nocturno";
        } else if (rbDiurno.isSelected()) {
            setJornada = "Diurno";
        }
        sqlInsert = "INSERT INTO estudiantes "
                + "(Nom_Estudiante,Ape_Estudiante,Documento,Jornada,Num_Telefonico,Correo, Ciudad, Departamento, Genero, Estado)"
                + "VALUES"
                + "(?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sqlInsert);

            ps.setString(1, txtnombre.getText());
            ps.setString(2, txtapellido.getText());
            ps.setInt(3, Integer.parseInt(txtDocumento.getText()));
            ps.setString(4, setJornada);
            ps.setString(5, txtTelefono.getText());
            ps.setString(6, txtCorreo.getText());
            ps.setString(7, cbciudad.getSelectedItem().toString());
            ps.setString(8, cbdepartamento.getSelectedItem().toString());
            ps.setString(9, setGenero);
            ps.setBoolean(10, setEstado);

            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro almacenado OK ");
                cargaTablaDocente();
                limpiarCajasDocente();
            } else {
                JOptionPane.showMessageDialog(null, "Error en registro ");
                limpiarCajasDocente();
            }
        } catch (HeadlessException | SQLException e) {
            System.err.println("error en transaccion insert into persona: " + e);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        String sqlUpdate = "", setJornada = "", setGenero = "";
        boolean setEstado;
        setEstado = true;
        if (rbmasculino.isSelected()) {
            setGenero = "Masculino";
        } else if (rbFemenino.isSelected()) {
            setGenero = "Femenino";
        }
        if (ckbActivo.isSelected()) {
            setEstado = true;
        } else if (ckbInactivo.isSelected()) {
            setEstado = false;
        }
        if (rbNocturno.isSelected()) {
            setJornada = "Nocturno";
        } else if (rbDiurno.isSelected()) {
            setJornada = "Diurno";
        }
        sqlUpdate = "UPDATE `estudiantes` SET "
                + "Nom_Estudiante='" + txtnombre.getText() + "',"
                + "Ape_estudiante='" + txtapellido.getText() + "',"
                + "Documento=" + Integer.parseInt(txtDocumento.getText()) + ","
                + "Jornada='" + setJornada + "',"
                + "Num_Telefonico='" + txtTelefono.getText() + "',"
                + "Correo='" + txtCorreo.getText() + "',"
                + "Ciudad='" + cbciudad.getSelectedItem().toString() + "',"
                + "Departamento='" + cbdepartamento.getSelectedItem().toString() + "',"
                + "Genero='" + setGenero + "',"
                + "Estado=" + setEstado + " "
                + "WHERE ID_Estudiante = " + Integer.parseInt(txtId.getText()) + "";

        try {
            ps = con.prepareStatement(sqlUpdate);

            int res = ps.executeUpdate();

            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro actualizado OK ");
                cargaTablaDocente();
                limpiarCajasDocente();
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar registro ");
//                limpiarCajas();
            }
        } catch (HeadlessException | SQLException e) {
            System.err.println("error en transaccion update persona: " + e);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        String sqlDelete = "";
        if (txtId.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "No hay datos para eliminar, \n ingrese un documento, por favor!", "ERROR", JOptionPane.ERROR_MESSAGE);
            txtDocumento.requestFocus();
        } else {
            if (JOptionPane.showConfirmDialog(jPDocente, "Esta Seguro de eliminar el registro?") == JOptionPane.YES_OPTION) {
                sqlDelete = "DELETE FROM `estudiantes` WHERE ID_Estudiante = ?";

                try {
                    ps = con.prepareStatement(sqlDelete);
                    ps.setInt(1, Integer.parseInt(txtId.getText()));

                    int res = ps.executeUpdate();
                    if (res > 0) {
                        JOptionPane.showMessageDialog(null, "Registro eliminado OK ");
                        cargaTablaDocente();
                        limpiarCajasDocente();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar tregistro");
                        limpiarCajasDocente();
                    }
                } catch (HeadlessException | SQLException e) {
                    System.err.println("error en transaccion delete persona: " + e);
                }
            } else {
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCajasDocente();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void cbdepartamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbdepartamentoItemStateChanged
        int depSel = cbdepartamento.getSelectedIndex();

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (this.cbdepartamento.getSelectedIndex() > 0) {
                listar_ciudad(cbciudad, depSel);
            }
        }
    }//GEN-LAST:event_cbdepartamentoItemStateChanged

    private void BTNSALIR1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNSALIR1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_BTNSALIR1ActionPerformed

    private void jtableRegistrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtableRegistrosMouseClicked
        String datosMostrar="",Nombre="",Apellido="",Documento="",Telefono="",Correo="",Ciudad="";
        Nombre = jtableRegistros.getValueAt(jtableRegistros.getSelectedRow(), 0).toString();
        
        Apellido = jtableRegistros.getValueAt(jtableRegistros.getSelectedRow(), 1).toString();
        
        Documento = jtableRegistros.getValueAt(jtableRegistros.getSelectedRow(), 2).toString();
        
        Telefono = jtableRegistros.getValueAt(jtableRegistros.getSelectedRow(), 3).toString();
        
        Correo = jtableRegistros.getValueAt(jtableRegistros.getSelectedRow(), 4).toString();
        datosMostrar = "1. "+Nombre+" "+Apellido+"\n "
                + "2. "+Documento+"\n "
                + "3. "+Telefono+"\n "
                + "4. "+Correo+"\n ";
        JOptionPane.showMessageDialog(null, datosMostrar,"Datos seleccionados",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jtableRegistrosMouseClicked

    private void cbCarreraItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbCarreraItemStateChanged
        int carrSel = cbCarrera.getSelectedIndex();

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (this.cbCarrera.getSelectedIndex() > 0) {
                listar_materia(cbMateria, carrSel);
            }
        }
    }//GEN-LAST:event_cbCarreraItemStateChanged

    private void btnConsultarMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarMateriaActionPerformed
        String idAlumnoset = txtIdEst.getText();
        cargaTablaEstudiante(idAlumnoset);
    }//GEN-LAST:event_btnConsultarMateriaActionPerformed

    private void btnAgregaMateria2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaMateria2ActionPerformed
        String sqlInsertMateria ="", idAlumnoset="";
        idAlumnoset = txtIdEst.getText();
        
        sqlInsertMateria = "INSERT INTO materia_inscrita "
                + "(nombre,ID_alumno,Id_carrera,Estado)"
                + "VALUES"
                + "(?,?,?,?)";
        try {
            ps = con.prepareStatement(sqlInsertMateria);

            ps.setString(1, cbMateria.getSelectedItem().toString());
            ps.setInt(2, Integer.parseInt(txtIdEst.getText()));
            ps.setInt(3, cbCarrera.getSelectedIndex());
            ps.setBoolean(4, true);
            
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro almacenado OK ");
                cargaTablaEstudiante(idAlumnoset);
                limpiarCajasDocente();
            } else {
                JOptionPane.showMessageDialog(null, "Error en registro ");
                limpiarCajasDocente();
            }
        } catch (Exception exM) {
            System.err.println("error en transaccion insert into materia: " + exM);
        }
    }//GEN-LAST:event_btnAgregaMateria2ActionPerformed

    private void btnLimpiarMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarMateriaActionPerformed
        limpiarCajasEstudiante();
        cbCarrera.addItem("---");
        listar_carrera(cbCarrera);
        cbMateria.removeAllItems();
        cbMateria.addItem("---");
    }//GEN-LAST:event_btnLimpiarMateriaActionPerformed

    private void btnQuitarMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarMateriaActionPerformed
        String idMateria = "",idCarrera = "", materia = "",sqlDelete="",idAlumnoset="";
        idAlumnoset = txtIdEst.getText();
        int fila = jtableEstudiantes.getSelectedRow();
        System.out.println("" + fila);
        if (fila >= 0) {
            //lbl_cod.setText(jt_lista.getValueAt(fila, 0).toString());
            idMateria = jtableEstudiantes.getValueAt(fila, 0).toString();
            idCarrera = jtableEstudiantes.getValueAt(fila, 0).toString();
            materia = jtableEstudiantes.getValueAt(fila, 1).toString();
            System.out.println("id: " + idCarrera + " materia: " + materia);
            if (JOptionPane.showConfirmDialog(jPDocente, "Esta Seguro de eliminar el registro?") == JOptionPane.YES_OPTION) {

                sqlDelete = "DELETE FROM `materia_inscrita` WHERE id =? AND ID_alumno = ?";
                System.out.println("sqlDelete: "+sqlDelete);
                try {
                    ps = con.prepareStatement(sqlDelete);
                    ps.setString(1, idMateria);
                    ps.setInt(2, Integer.parseInt(txtIdEst.getText()));

                    int res = ps.executeUpdate();
                    if (res > 0) {
                        JOptionPane.showMessageDialog(null, "Registro eliminado OK ");                        
                        cargaTablaEstudiante(idAlumnoset);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar tregistro");
                        cargaTablaEstudiante(idAlumnoset);
                    }
                } catch (HeadlessException | SQLException e) {
                    System.err.println("error en transaccion delete materia: " + e);
                }
            } else {
                this.setVisible(true);
            }
        } else {
            System.out.println("Fila no seleccionada !!!");
            JOptionPane.showMessageDialog(null, "No selecciono fila para eliminar !!!", "SEÑOR USUARIO", JOptionPane.ERROR_MESSAGE);
        }
//        String sqlDelete = "", idAlumnoset = "";
//        idAlumnoset = txtIdEst.getText();

    }//GEN-LAST:event_btnQuitarMateriaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNSALIR;
    private javax.swing.JButton BTNSALIR1;
    private javax.swing.JButton btnAgregaMateria2;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnConsultarMateria;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLimpiarMateria;
    private javax.swing.JButton btnQuitarMateria;
    private javax.swing.JComboBox<String> cbCarrera;
    private javax.swing.JComboBox<String> cbMateria;
    private javax.swing.JComboBox<String> cbciudad;
    private javax.swing.JComboBox<String> cbdepartamento;
    public javax.swing.JCheckBox ckbActivo;
    public javax.swing.JCheckBox ckbInactivo;
    public static javax.swing.ButtonGroup estadoGroup;
    public static javax.swing.ButtonGroup generoGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPDocente;
    private javax.swing.JPanel jPEstudiante;
    private javax.swing.JPanel jPLogin;
    private javax.swing.JPanel jPanelEstado;
    private javax.swing.JPanel jPanleDatos;
    private javax.swing.JPanel jPanleDatos1;
    private javax.swing.JPanel jPanleDomicilio;
    private javax.swing.JPanel jPanleGenero;
    private javax.swing.JPanel jPanleGenero1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.ButtonGroup jornadaGroup;
    private javax.swing.JPasswordField jpasContraseña;
    private javax.swing.JTable jtableEstudiantes;
    private javax.swing.JTable jtableRegistros;
    private javax.swing.JLabel lblContraseña;
    public static javax.swing.JLabel lblDocente;
    public static javax.swing.JLabel lblEstudiante;
    private javax.swing.JLabel lblUsuario;
    public javax.swing.JRadioButton rbDiurno;
    public static javax.swing.JRadioButton rbFemenino;
    public javax.swing.JRadioButton rbNocturno;
    public static javax.swing.JRadioButton rbmasculino;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDocEst;
    public javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdEst;
    private javax.swing.JTextField txtNomEst;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JTextField txtapellido;
    private javax.swing.JTextField txtnombre;
    // End of variables declaration//GEN-END:variables
dataBase ct = new dataBase();
    Connection con = ct.conectar();
}
