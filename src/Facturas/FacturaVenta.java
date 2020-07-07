
package Facturas;

import Metodos.abm;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import num_a_letras.n2t;
import rsystem.conexionBD;


public class FacturaVenta extends javax.swing.JDialog implements Printable{
    
    conexionBD cn;
    private ResultSet rs;
    DefaultTableModel modelo;
    private Object[] filas;
    DefaultTableModel modeloo;
    private boolean v_control;
    private abm abm;
    private boolean vacio;
    Calendar FechaVence;
    
    
    
    public FacturaVenta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        try {
            initComponents();
            abm=new abm();
            cn= new conexionBD();
            FechaVence = new GregorianCalendar();
            
            
            txtStock.setVisible(false);
            btnContado.setSelected(true);
            cargarclientes("");
            obtenerfecha();
            verclintes();
            cargarproducto("");
            verproducto();
            txtIVA.setVisible(false);
            txtIdcliente.setVisible(false);
            txtidfac.setVisible(false);
             rs = abm.nuevo("numerofactura", "FacturaVenta");
            try {
                rs.first();
            } catch (SQLException ex) {
                Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
                txtNumFac.setText(String.valueOf(rs.getInt("codigo") + 1));
        } catch (SQLException ex) {
            Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        idfactura();
        idfcobrocliente();
        //cargarclientedefault();
        habilitarcliente();
        calendario();
        
    }
    void calendario(){
        fecha1.setDate(FechaVence.getTime());
        
        FechaVence = fecha1.getCalendar();
                FechaVence.setTime(fecha1.getDate());
                String dia =String.valueOf(FechaVence.get(Calendar.DATE));
                String mes =String.valueOf(FechaVence.get(Calendar.MONTH)+1);
                String anio =String.valueOf(FechaVence.get(Calendar.YEAR));
                String date = anio+"/"+mes+"/"+dia;
    }

    public void cargarclientedefault(){
        txtnombre2.setText("Juan");
        txtApellido.setText("Perez");
        txtDireccion.setText("Direccion");
        txtCin.setText("12345");
        txtTelef.setText("202020");
    }

    public void habilitarbotones(boolean j){
        buscarProdu.setEnabled(j);      
    }
    public void habilitarbotonestg(boolean h){
        AgregarProducto.setEnabled(h);
        txtCantidad.setEnabled(h);
       
    }
    public void habilitarbotones2(boolean k){
        btnGuardar.setEnabled(k);
        btnLimpiar.setEnabled(k);
        btnQuitar.setEnabled(k);
        btnCredito.setEnabled(k);
    }
    public void limpiarcamposcabezara(){
        txtnombre.setText("");
        txtApellido.setText("");
        txtDireccion.setText("");
        txtCin.setText("");
        txtTelef.setText("");
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtPrecioVenta.setText("");
        txtTotal1.setText("");
        txtExentas.setText("");
        txtDies.setText("");
        txtTotal.setText("");
        txtIva5.setText("");
        txtIva10.setText("");
        txtTotalIva.setText("");
        txtCinco.setText("");
        txtTotalLetras.setText("");
        
        DefaultTableModel tablaborrar = (DefaultTableModel) this.tablafactura.getModel();
        while(tablaborrar.getRowCount()>0)tablaborrar.removeRow(0);
    }
     
    void obtenerfecha(){
        Date fecha = new Date();
        SimpleDateFormat fe = new SimpleDateFormat("yyyy/MM/dd");
        this.fecha.setText(fe.format(fecha));   
    }
    public void idfactura(){
        try {
            rs = abm.nuevo("idfacturaventa","FacturaVenta");
            try {
                rs.first();
            } catch (SQLException ex) {
                Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
            txtidfac.setText(String.valueOf(rs.getInt("codigo")+1));
        } catch (SQLException ex) {
            Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void idfcobrocliente(){
        try {
            rs = abm.nuevo("idEntregaInicial","EntregaInicial");
            try {
                rs.first();
            } catch (SQLException ex) {
                Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
            txtIdFacCredito.setText(String.valueOf(rs.getInt("codigo")+1));
        } catch (SQLException ex) {
            Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void cargarclientes(String valor ){
        String [] titulos  = {"Codigo","Nombre","Apellido","C.I.N°","Telefono","Direccion"};
        String [] registros  = new String [6];
        String sql = "select idcliente, nombre, apellido, cinro, telefono, direccion from cliente where CONCAT(idcliente,' ',nombre,' ',apellido,' ',cinro,' ',telefono,' ',direccion) LIKE '%"+valor+"%'";   
         
        // sql+=" where idproducto="+txtIdProducto.getText();
         
        modelo = new DefaultTableModel(null, titulos);
    
        conexionBD cnx = new conexionBD();
        Connection cnn = cnx.ConectarBD();
        com.mysql.jdbc.Statement  st;
        try {
            
            st = (com.mysql.jdbc.Statement) cnn.createStatement();
            ResultSet rss = st.executeQuery(sql);
            
            while(rss.next()){
                registros[0] = rss.getString("idcliente");
                registros[1] = rss.getString("nombre");
                registros[2] = rss.getString("apellido");
                registros[3] = rss.getString("cinro"); 
                registros[4] = rss.getString("telefono");
                registros[5] = rss.getString("direccion");
                modelo.addRow(registros);
                }
            tablacliente.setModel(modelo);
            
            } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                            }   
    }
     public void cargarclinetesbuscar(String valor ){
        String [] titulos  = {"Codigo","Nombre","Apellido","C.I.N°","Telefono","Direccion"};
        String [] registros  = new String [6];
        String sql = "select idcliente, nombre, apellido, cinro, telefono, direccion from cliente where cinro LIKE '%"+txtBuscarCliente.getText()+"%'";  
        
        
         
        modelo = new DefaultTableModel(null, titulos);
    
        conexionBD cnp = new conexionBD();
        Connection cnn = cnp.ConectarBD();
        com.mysql.jdbc.Statement  st;
        try {
            
            st = (com.mysql.jdbc.Statement) cnn.createStatement();
            ResultSet rst = st.executeQuery(sql);
             
            while(rst.next()){
                registros[0] = rst.getString("idcliente");
                registros[1] = rst.getString("nombre");
                registros[2] = rst.getString("apellido");
                registros[3] = rst.getString("cinro");
                registros[4] = rst.getString("telefono");
                registros[5] = rst.getString("direccion");
                 
                modelo.addRow(registros);
                }
            tablacliente.setModel(modelo);
            
            } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                            }   
    }
    void verclintes(){
        try{
            com.mysql.jdbc.Statement consultamarca = (com.mysql.jdbc.Statement) conexionBD.ConectarBD().createStatement();
            ResultSet rs = consultamarca.executeQuery("select idcliente, nombre, apellido, cinro, telefono, direccion from cliente");
            
            modelo = new DefaultTableModel();
            tablacliente.setModel(modelo);
            
            modelo.addColumn("Codigo");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");
            modelo.addColumn("C.I.N°");
            modelo.addColumn("Telefono");
            modelo.addColumn("Direccion");
            
            filas = new Object[modelo.getColumnCount()];
                        
            while(rs.next()){
                for(int i=0;i<modelo.getColumnCount();i++){
                    filas[i]=rs.getObject(i+1);
                }               
                modelo.addRow(filas);  
            }
            
            tablacliente.setModel(modelo);
    
        }catch(Exception e){
            System.out.println("Error al mostrar datos en la tabla"+e.getMessage());
        }
    }   
    public void seleccionartablacliente(){
        
        
      // DefaultTableModel tabla = (DefaultTableModel) this.tablaCliente.getModel();
       
       
       int c= tablacliente.getSelectedRow();
       
       if(c==-1){
           System.out.println("Seleccione un registro");
            }
       else{
           Integer id = Integer.parseInt(tablacliente.getValueAt(c, 0).toString());
           String nombre = (String) tablacliente.getValueAt(c, 1);
           String apellido= (String) tablacliente.getValueAt(c, 2);
           String cin= (String) tablacliente.getValueAt(c, 3);
           String telefono= (String) tablacliente.getValueAt(c, 4);
           String direccion= (String) tablacliente.getValueAt(c, 5);

         //  this.txtIdcliente.setText(id);
           txtIdcliente.setText(Integer.toString(id));
           
           this.txtnombre.setText(nombre);
           this.txtApellido.setText(apellido);
           this.txtCin.setText(cin);        
           this.txtTelef.setText(telefono);
           this.txtDireccion.setText(direccion);
           
           }       
    }
    
    public void cargarproductobuscar(){
        String [] titulos  = {"Codigo","Precio Venta","Descripcion","IVA","Existencia"};
        String [] registros  = new String [5];
        String sql=null;
         sql = "select idproducto,preciodeventa,descripcion,stock";
         sql+=",iva";
         sql+=" from productos";
         sql+=" where preciodeventa LIKE '%"+txtBuscarProdu.getText()+"%'";
         //System.out.println(sql);
         
         
        DefaultTableModel tabla = new DefaultTableModel(null, titulos); 
        //tabla = new DefaultTableModel(null, titulos);
    
        conexionBD cnb = new conexionBD();
        Connection cnn = cnb.ConectarBD();
        Statement  st;
        try {
            
            st = (Statement) cnn.createStatement();
            ResultSet rst = st.executeQuery(sql);
            
            while(rst.next()){
                registros[0] = rst.getString("idproducto");
                registros[1] = rst.getString("preciodeventa");
                registros[2] = rst.getString("descripcion");
                registros[3] = rst.getString("iva");
                registros[4] = rst.getString("stock");
                tabla.addRow(registros);
                }
            tablaProducto.setModel(tabla);
            
            } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                            }   
    }
    public void cargarproducto(String valor){
        String [] titulos  = {"Codigo","Precio Venta","Descripcion","IVA","Existencia"};
        String [] registros  = new String [5];
        String sql=null;
         sql = "select idproducto,preciodeventa,descripcion,stock";
         sql+=",iva";
         sql+=" from productos";
         sql+=" where idproducto=idproducto";
       
         
        DefaultTableModel m = new DefaultTableModel(null, titulos);
    
        conexionBD cn = new conexionBD();
        Connection cnn = cn.ConectarBD();
        Statement  st;
        try {
            
            st = (Statement) cnn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                registros[0] = rs.getString("idproducto");
                registros[1] = rs.getString("preciodeventa");
                registros[2] = rs.getString("descripcion");
                registros[3] = rs.getString("iva");
                registros[4] = rs.getString("stock");
                m.addRow(registros);
                }
            tablaProducto.setModel(m);
            
            }catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                            }   
    }
    public void seleccionartablaproducto(){
        
        
       //DefaultTableModel tabla = (DefaultTableModel) this.tablacliente.getModel();
       
       
       int c= tablaProducto.getSelectedRow();
       
       if(c==-1){
           JOptionPane.showMessageDialog(null, "Seleccione un registro");
            }
       else{
          // String id = (String) tablaProducto.getValueAt(c, 0);
           Integer id = Integer.parseInt(tablaProducto.getValueAt(c, 0).toString());
           Integer precioventa = Integer.parseInt(tablaProducto.getValueAt(c, 1).toString());
           //String precioventa = (String) tablaProducto.getValueAt(c, 1);
           String descripcion = (String) tablaProducto.getValueAt(c, 2);
          // String marca = (String) tablaProducto.getValueAt(c, 3);
           Integer iva = Integer.parseInt(tablaProducto.getValueAt(c, 3).toString());
           Integer stock = Integer.parseInt(tablaProducto.getValueAt(c, 4).toString());
           txtStock.setText(Integer.toString(stock));
           txtCodigo.setText(Integer.toString(id));
          // this.txtCodigo.setText(id);
           //this.txtPrecioVenta.setText(precioventa);
           txtPrecioVenta.setText(Integer.toString(precioventa));
           this.txtDescripcion.setText(descripcion);
         //  this.txtTotal1.setText(marca);
           txtIVA.setText(Integer.toString(iva));
           }       
    }
    void habilitarcliente(){
        if(txtnombre2.getText().isEmpty()){
            buscarProdu.setEnabled(false);
            }
       else{buscarProdu.setEnabled(true);
            }
    }
    public void habilitarguardar(){
        int x = tablafactura.getRowCount();
        
        if(tablafactura.getRowCount()!=0){
            btnCredito.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnQuitar.setEnabled(true);
        }
        else{btnCredito.setEnabled(false);
                btnGuardar.setEnabled(false);
                btnQuitar.setEnabled(false);}
    }
    
    void verproducto(){
        try{
            Statement consultamarca = (Statement) conexionBD.ConectarBD().createStatement();
            ResultSet rs = consultamarca.executeQuery("select idproducto,preciodeventa,descripcion,iva,stock  from productos order by idproducto");
            
            modelo = new DefaultTableModel();
            tablaProducto.setModel(modelo);
            
            
            modelo.addColumn("Codigo");
            modelo.addColumn("Precio Venta");
            modelo.addColumn("Descripcion");
            modelo.addColumn("IVA");
            modelo.addColumn("Existencia");
            
            filas = new Object[modelo.getColumnCount()];
            
            while(rs.next()){
                for(int i=0;i<modelo.getColumnCount();i++){
                    filas[i]=rs.getObject(i+1);
                }                
                modelo.addRow(filas);               
            }
            
            tablaProducto.setModel(modelo);
    
        }catch(Exception e){
            System.out.println("Error al mostrar datos en la tabla"+e.getMessage());
        }
    }
    
    void agregartablafactura(){
        
       DefaultTableModel tabla = (DefaultTableModel) this.tablafactura.getModel();
        
       int iva= tablafactura.getSelectedRow();
       iva = Integer.parseInt(txtIVA.getText().toString());
     
       if(iva==0){
           Object[] valor = new Object[7];
           valor[0] = txtCodigo.getText();
           valor[1] = txtDescripcion.getText();
           valor[2] = txtCantidad.getText();
           valor[3] = txtPrecioVenta.getText();
           valor[4] = txtTotal1.getText();
           valor[5] = "0";
           valor[6] = "0";
           
           tabla.addRow(valor);
       }
       else{
           if(iva==5){
           Object[] valor = new Object[7];
           valor[0] = txtCodigo.getText();
           valor[1] = txtDescripcion.getText();
           valor[2] = txtCantidad.getText();
           valor[3] = txtPrecioVenta.getText();
           valor[4] = "0";
           valor[5] = txtTotal1.getText();
           valor[6] = "0";
           
           tabla.addRow(valor);
           }
           else{
               if(iva==10){
                    Object[] valor = new Object[7];
                    valor[0] = txtCodigo.getText();
                    valor[1] = txtDescripcion.getText();
                    valor[2] = txtCantidad.getText();
                    valor[3] = txtPrecioVenta.getText();
                    valor[4] = "0";
                    valor[5] = "0";
                    valor[6] = txtTotal1.getText();

                    tabla.addRow(valor);
                }
       }
    }
 }  
    void sumadetalbla(){
        double sumatoriaTotal = 0;
        int totalRow = tablafactura.getRowCount();
        totalRow -= 1;
        double totalIva10 = 0, totalIva5 = 0, z = 0;
        for (int i = 0; i <= (totalRow); i++) {
            //SUMA TOTAL IVA 10%
            double sumatoria = Double.parseDouble(String.valueOf(tablafactura.getValueAt(i, 6)));
            totalIva10 += sumatoria;
            txtDies.setText(String.valueOf(totalIva10));
            double iva10 = (totalIva10 * 10) / 110;
            double redondeo = ((long) (iva10 * 100.0)) / 100.0;
            txtIva10.setText(String.valueOf(redondeo));
            

            //SUMA TOTAL IVA 5%
            double sumatoria1 = Double.parseDouble(String.valueOf(tablafactura.getValueAt(i, 5)));
            totalIva5 += sumatoria1;
            txtCinco.setText(String.valueOf(totalIva5));
            double iva5 = (totalIva5 * 5) / 105;
            double redondeo2 = ((long) (iva5 * 100.0)) / 100.0;
            txtIva5.setText(String.valueOf(redondeo2));
          
            //SUMA TOTAL EXENTAS
            double sumatoria2 = Double.parseDouble(String.valueOf(tablafactura.getValueAt(i, 4)));
            z += sumatoria2;
            txtExentas.setText(String.valueOf(z));
            sumatoriaTotal += (sumatoria + sumatoria1 + z);

            txtTotal.setText(String.valueOf(sumatoriaTotal));
            
            //SUMA PARA EL TOTAL DE IVA
            
            double iva5p = Double.parseDouble(String.valueOf(txtIva5.getText()));
            double iva10p = Double.parseDouble(String.valueOf(txtIva10.getText()));
            double sumaivap = iva5p+iva10p;
            txtTotalIva.setText(String.valueOf(sumaivap));
            
        }
    }
    public void limpiar(){
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtPrecioVenta.setText("");
        txtTotal1.setText("");
    }
    public void cargarcredito(){
       double valor = Double.parseDouble(String.valueOf(txtTotal.getText()));
       txtMontoPagar.setText(String.valueOf(valor));
       txtSaldo.setText(String.valueOf(valor));
    }
  /*   public void interes(){
    int saldototal, saldo, interes;
        
        saldo = Integer.parseInt(txtSaldo.getText().toString());
        interes = Integer.parseInt(txtInteres.getText().toString());
        saldototal =  saldo+( saldo*interes/100);
        txtSaldo.setText(String.valueOf(saldototal));
        }*/
    public void calcularplazo(){
        
            double plazo, saldo, total;
            saldo =  Double.parseDouble(String.valueOf(txtSaldo.getText()));
            plazo = Double.parseDouble(String.valueOf(txtPlazo.getText()));
            total = saldo/plazo;
            txtMontoCuota.setText(String.valueOf(total));
    }
    public boolean validardatos(){
         vacio = false;
        if (txtPlazo.getText().isEmpty()) {
            vacio = true;
        }
        if(txtEntrega.getText().isEmpty()){
            vacio=true;
        }
        return vacio;
}
 
      public void num_letras() throws IOException{
        double numero1 = Double.parseDouble(String.valueOf(txtTotal.getText()));//agarramis el valor del objeto y lo convertimos en entero  
        System.out.println(numero1);
		n2t numero = new n2t((int) numero1); //instanciamos la clase n2t a lavariable numero
	txtTotalLetras.setText(numero.convertirLetras((int) numero1)); //luego nos devuelve el valor en letras del numero en res		
                //txtTotalLetras.setText(res);       
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Group1 = new javax.swing.ButtonGroup();
        buscarcliente = new javax.swing.JDialog();
        txtBuscarCliente = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablacliente = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        buscarproductos = new javax.swing.JDialog();
        txtBuscarProdu = new javax.swing.JTextField();
        btnAceotarProdu = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaProducto = new javax.swing.JTable();
        jLabel42 = new javax.swing.JLabel();
        credito = new javax.swing.JDialog();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtPlazo = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtEntrega = new javax.swing.JTextField();
        btnGuardarCredito = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        txtMontoPagar = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtIdFacCredito = new javax.swing.JLabel();
        btnCancelarCredito = new javax.swing.JButton();
        txtMontoCuota = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        txtInteres = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        fecha1 = new com.toedter.calendar.JDateChooser();
        jLabel49 = new javax.swing.JLabel();
        txtMontoPagarInteres = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtTotal1 = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        buscarProdu = new javax.swing.JButton();
        FacturaImprimir = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        fecha = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNumFac = new javax.swing.JTextField();
        txtidfac = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtnombre2 = new javax.swing.JTextField();
        txtTelef = new javax.swing.JTextField();
        txtCin = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        btnCredito = new javax.swing.JRadioButton();
        btnContado = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablafactura = new javax.swing.JTable();
        txtExentas = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        txtDies = new javax.swing.JTextField();
        txtCinco = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtIva5 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtIva10 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtTotalIva = new javax.swing.JTextField();
        txtIVA = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel47 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtTotalLetras = new javax.swing.JTextField();
        txtIdcliente = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnQuitar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        AgregarProducto = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();

        buscarcliente.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBuscarCliente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarClienteActionPerformed(evt);
            }
        });
        txtBuscarCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarClienteKeyReleased(evt);
            }
        });
        buscarcliente.getContentPane().add(txtBuscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 178, -1));

        jButton2.setText("Aceptar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        buscarcliente.getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, -1, -1));

        tablacliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Apellido", "C.I.N.", "Telefono", "Direccion"
            }
        ));
        tablacliente.getTableHeader().setReorderingAllowed(false);
        tablacliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaclienteMouseClicked(evt);
            }
        });
        tablacliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaclienteKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tablacliente);

        buscarcliente.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 465, 176));

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/buscar.png"))); // NOI18N
        buscarcliente.getContentPane().add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 30, 50));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 153, 255));
        jLabel18.setText("Introducir Nº C.I .");
        buscarcliente.getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        buscarproductos.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBuscarProdu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtBuscarProdu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProduActionPerformed(evt);
            }
        });
        txtBuscarProdu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarProduKeyReleased(evt);
            }
        });
        buscarproductos.getContentPane().add(txtBuscarProdu, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 38, 197, 30));

        btnAceotarProdu.setText("Aceptar");
        btnAceotarProdu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceotarProduActionPerformed(evt);
            }
        });
        buscarproductos.getContentPane().add(btnAceotarProdu, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 42, -1, -1));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/buscar.png"))); // NOI18N
        buscarproductos.getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 28, -1, 40));

        tablaProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Descripcion", "PrecioVenta", "IVA%"
            }
        ));
        tablaProducto.getTableHeader().setReorderingAllowed(false);
        tablaProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProductoMouseClicked(evt);
            }
        });
        tablaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaProductoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaProductoKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tablaProducto);

        buscarproductos.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 79, -1, 278));

        jLabel42.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 153, 255));
        jLabel42.setText("Introducir Precio de Venta");
        buscarproductos.getContentPane().add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, 20));

        credito.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setFont(new java.awt.Font("FreeMono", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 204, 0));
        jLabel30.setText("FACTURACREDITO");
        credito.getContentPane().add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, -1, -1));

        jLabel31.setText("PLAZO:");
        credito.getContentPane().add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, -1, -1));

        txtPlazo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPlazoFocusLost(evt);
            }
        });
        txtPlazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlazoActionPerformed(evt);
            }
        });
        txtPlazo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlazoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPlazoKeyTyped(evt);
            }
        });
        credito.getContentPane().add(txtPlazo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 60, -1));

        jLabel32.setText("Saldo:");
        credito.getContentPane().add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, -1, -1));

        txtSaldo.setText("0");
        txtSaldo.setEnabled(false);
        credito.getContentPane().add(txtSaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 119, 30));

        jLabel33.setText("Monto de la Cuota:");
        credito.getContentPane().add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        jLabel35.setText("Fecha Vence");
        credito.getContentPane().add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, -1, -1));

        jLabel36.setText("Entrega Inicial:");
        credito.getContentPane().add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, -1, -1));

        txtEntrega.setText("0");
        txtEntrega.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEntregaFocusLost(evt);
            }
        });
        txtEntrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntregaActionPerformed(evt);
            }
        });
        txtEntrega.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEntregaKeyTyped(evt);
            }
        });
        credito.getContentPane().add(txtEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 120, -1));

        btnGuardarCredito.setText("Guardar");
        btnGuardarCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarCreditoActionPerformed(evt);
            }
        });
        credito.getContentPane().add(btnGuardarCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 90, -1));

        jLabel37.setFont(new java.awt.Font("DejaVu Sans", 2, 13)); // NOI18N
        jLabel37.setText("Monto a Pagar Sin Interes:");
        credito.getContentPane().add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        txtMontoPagar.setEnabled(false);
        credito.getContentPane().add(txtMontoPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 100, 30));

        jLabel38.setText("N°");
        credito.getContentPane().add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        txtIdFacCredito.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        credito.getContentPane().add(txtIdFacCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 30, 20));

        btnCancelarCredito.setText("Cancelar");
        btnCancelarCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarCreditoActionPerformed(evt);
            }
        });
        credito.getContentPane().add(btnCancelarCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, 90, -1));

        txtMontoCuota.setText("0");
        txtMontoCuota.setEnabled(false);
        credito.getContentPane().add(txtMontoCuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 120, -1));
        credito.getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 400, 10));

        txtInteres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInteresActionPerformed(evt);
            }
        });
        credito.getContentPane().add(txtInteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 50, 20));

        jLabel48.setText("Interers:");
        credito.getContentPane().add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, -1));

        jLabel39.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        credito.getContentPane().add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 280, 210));

        jLabel40.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        credito.getContentPane().add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, 90, 110));
        credito.getContentPane().add(fecha1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        jLabel49.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        jLabel49.setText("Monto a Pagar con Interes:");
        credito.getContentPane().add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 160, 20));

        txtMontoPagarInteres.setEnabled(false);
        credito.getContentPane().add(txtMontoPagarInteres, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 100, 30));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/articulos.png"))); // NOI18N
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 430, 50, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setText("Codigo");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, -1));

        txtCodigo.setEnabled(false);
        jPanel2.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 120, -1));

        txtDescripcion.setEnabled(false);
        jPanel2.add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 120, -1));

        jLabel26.setText("Descripcion");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, -1, -1));

        txtCantidad.setEnabled(false);
        txtCantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCantidadFocusLost(evt);
            }
        });
        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadKeyTyped(evt);
            }
        });
        jPanel2.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 120, -1));

        jLabel27.setText("Cantidad");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, -1, -1));

        jLabel28.setText("Precio Venta");
        jPanel2.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, -1, -1));

        txtPrecioVenta.setEnabled(false);
        jPanel2.add(txtPrecioVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 120, -1));

        jLabel24.setText("Total");
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 0, -1, -1));

        txtTotal1.setEnabled(false);
        jPanel2.add(txtTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 20, 120, -1));
        jPanel2.add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 30, -1));

        buscarProdu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/nuevacoompra.png"))); // NOI18N
        buscarProdu.setEnabled(false);
        buscarProdu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarProduActionPerformed(evt);
            }
        });
        jPanel2.add(buscarProdu, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 40, 30));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 740, 60));

        FacturaImprimir.setBackground(new java.awt.Color(255, 255, 255));
        FacturaImprimir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        FacturaImprimir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText("Fecha de Emision:");
        FacturaImprimir.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        fecha.setText("dd/mm/yy");
        FacturaImprimir.add(fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 80, 20));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel1.setText("N°");
        FacturaImprimir.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 110, 30, 20));

        txtNumFac.setBorder(null);
        txtNumFac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumFacActionPerformed(evt);
            }
        });
        txtNumFac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumFacKeyTyped(evt);
            }
        });
        FacturaImprimir.add(txtNumFac, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, 110, 30));
        FacturaImprimir.add(txtidfac, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 150, 50, 30));

        jLabel2.setBackground(new java.awt.Color(1, 1, 1));
        jLabel2.setFont(new java.awt.Font("Courier New", 1, 60)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 0));
        jLabel2.setText("RULER");
        FacturaImprimir.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 190, 40));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("de David Aranda");
        FacturaImprimir.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, -1, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("TIMBRADO N° 11768966");
        FacturaImprimir.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 140, -1));

        jLabel5.setText("TUYUTI 160 C/ Blas A. Garay");
        FacturaImprimir.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 160, -1));

        jLabel6.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        jLabel6.setText("INFORMATICA & ELECTRONICA");
        FacturaImprimir.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, 20));

        jLabel14.setText("RUC o C.I.N.:");
        FacturaImprimir.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, -1, -1));

        jLabel15.setText("Total a pagar:");
        FacturaImprimir.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 470, 80, 20));

        jLabel8.setText("Telef:");
        FacturaImprimir.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 250, -1, -1));

        txtnombre2.setEnabled(false);
        txtnombre2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombre2ActionPerformed(evt);
            }
        });
        FacturaImprimir.add(txtnombre2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 360, 20));

        txtTelef.setEnabled(false);
        FacturaImprimir.add(txtTelef, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 250, 120, 20));

        txtCin.setEnabled(false);
        FacturaImprimir.add(txtCin, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 110, -1));

        txtApellido.setEnabled(false);
        FacturaImprimir.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 360, -1));

        jLabel17.setText("Direccion:");
        FacturaImprimir.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, -1));

        txtDireccion.setEnabled(false);
        FacturaImprimir.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 470, -1));

        jLabel19.setText("Forma de Pago");
        FacturaImprimir.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, -1, -1));

        btnCredito.setBackground(new java.awt.Color(255, 255, 255));
        Group1.add(btnCredito);
        btnCredito.setText("Credito");
        btnCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreditoActionPerformed(evt);
            }
        });
        FacturaImprimir.add(btnCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 160, -1, -1));

        btnContado.setBackground(new java.awt.Color(255, 255, 255));
        Group1.add(btnContado);
        btnContado.setText("Contado");
        FacturaImprimir.add(btnContado, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 160, -1, -1));

        tablafactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Descripcion", "Cantidad", "Precio Unitario", "Excentas", "5%", "10%"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablafactura.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablafactura);
        if (tablafactura.getColumnModel().getColumnCount() > 0) {
            tablafactura.getColumnModel().getColumn(0).setMaxWidth(50);
            tablafactura.getColumnModel().getColumn(2).setMaxWidth(60);
        }

        FacturaImprimir.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 740, 120));

        txtExentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtExentas.setEnabled(false);
        FacturaImprimir.add(txtExentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 480, 110, 30));

        jLabel12.setText("Excentas");
        FacturaImprimir.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 510, -1, -1));

        jLabel20.setText("I.V.A.5%");
        FacturaImprimir.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 510, 60, -1));

        jLabel9.setText("I.V.A.10%");
        FacturaImprimir.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 510, -1, -1));

        txtTotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTotal.setEnabled(false);
        FacturaImprimir.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 530, 210, 30));

        txtDies.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtDies.setEnabled(false);
        FacturaImprimir.add(txtDies, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 480, 110, 30));

        txtCinco.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCinco.setEnabled(false);
        FacturaImprimir.add(txtCinco, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, 120, 30));

        jLabel13.setText("TOTAL A PAGAR:");
        FacturaImprimir.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 540, -1, -1));

        jLabel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        FacturaImprimir.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 460, 360, 110));

        txtIva5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtIva5.setEnabled(false);
        FacturaImprimir.add(txtIva5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 580, 70, 20));

        jLabel16.setText("(5%)");
        FacturaImprimir.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 580, -1, 20));

        jLabel10.setText("Liquidacion del IVA%:");
        FacturaImprimir.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, -1, 20));

        txtIva10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtIva10.setEnabled(false);
        FacturaImprimir.add(txtIva10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 580, 70, 20));

        jLabel11.setText("(10%)");
        FacturaImprimir.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 580, -1, 20));

        jLabel29.setText("Total IVA:");
        FacturaImprimir.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 580, 60, 20));

        txtTotalIva.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtTotalIva.setEnabled(false);
        FacturaImprimir.add(txtTotalIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 580, 140, 20));

        txtIVA.setEnabled(false);
        txtIVA.setOpaque(true);
        FacturaImprimir.add(txtIVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 580, 70, 10));

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel44.setText("FACTUTRA");
        FacturaImprimir.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 200, 50));

        jLabel43.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        FacturaImprimir.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 250, 140));

        jLabel45.setText("Coronel Oviedo-Paraguay");
        FacturaImprimir.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 140, -1));

        jLabel46.setText("  Telefono : (0521) 201203");
        FacturaImprimir.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 150, -1));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("VENTA DE PC'`S - NOTEBOOKS\nTELEVISORES LED - LCD\nCIRCUITO CERRADO DE CAMARAS\nSERVICIO TECNICO\nRECARGA DE TINTA & TONER\nSELLOS DE GOMA \nARTICULOS DE LIBRERIA\nIMPRESIONES LASER");
        jTextArea1.setBorder(null);
        jScrollPane5.setViewportView(jTextArea1);

        FacturaImprimir.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 200, 140));

        jLabel47.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        FacturaImprimir.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 470, 140));

        jLabel22.setText("Cliente:");
        FacturaImprimir.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, -1, -1));

        txtnombre.setEnabled(false);
        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });
        FacturaImprimir.add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 100, 20));

        txtTotalLetras.setEnabled(false);
        txtTotalLetras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalLetrasActionPerformed(evt);
            }
        });
        FacturaImprimir.add(txtTotalLetras, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 470, 290, 20));
        FacturaImprimir.add(txtIdcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 30, -1));

        getContentPane().add(FacturaImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 760, -1));

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clear.png"))); // NOI18N
        btnLimpiar.setEnabled(false);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 490, 50, 40));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/equis.png"))); // NOI18N
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 540, 50, 40));

        btnQuitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/basurero.png"))); // NOI18N
        btnQuitar.setEnabled(false);
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });
        getContentPane().add(btnQuitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 380, -1, 40));

        btnImprimir.setText("imprimir");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimir(evt);
            }
        });
        getContentPane().add(btnImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 290, -1, -1));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clientes.png"))); // NOI18N
        btnBuscar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 190, 50, 40));

        AgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/entrada.png"))); // NOI18N
        AgregarProducto.setEnabled(false);
        AgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarProductoActionPerformed(evt);
            }
        });
        getContentPane().add(AgregarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 330, 50, 40));

        jLabel34.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 320, 90, 280));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarcliente.setModal(true);
        buscarcliente.setSize(450, 300);
        buscarcliente.setLocationRelativeTo(this);
        buscarcliente.setVisible(true);
     //   habilitarbotones(true);
     //   btnLimpiar.setEnabled(true);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtBuscarClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarClienteKeyReleased
        if(txtBuscarCliente.getText().isEmpty()){
        cargarclientes("");
            }
       else{cargarclinetesbuscar("");
            }
    }//GEN-LAST:event_txtBuscarClienteKeyReleased

    private void tablaclienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaclienteKeyReleased
        seleccionartablacliente();
    }//GEN-LAST:event_tablaclienteKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.buscarcliente.dispose();
        habilitarbotones(true);
        btnLimpiar.setEnabled(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tablaclienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaclienteMouseClicked
          
        seleccionartablacliente();
    }//GEN-LAST:event_tablaclienteMouseClicked

    private void txtBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarClienteActionPerformed

    private void buscarProduActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarProduActionPerformed
        buscarproductos.setModal(true);
        buscarproductos.setSize(500, 300);
        buscarproductos.setLocationRelativeTo(this);
        buscarproductos.setVisible(true);
        
        
        habilitarbotonestg(true);
    }//GEN-LAST:event_buscarProduActionPerformed

    private void txtBuscarProduActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProduActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarProduActionPerformed

    private void tablaProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProductoKeyPressed
       
    }//GEN-LAST:event_tablaProductoKeyPressed

    private void tablaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductoMouseClicked
      seleccionartablaproducto();
    }//GEN-LAST:event_tablaProductoMouseClicked

    private void tablaProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProductoKeyReleased
        seleccionartablaproducto();
    }//GEN-LAST:event_tablaProductoKeyReleased

    private void txtBuscarProduKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProduKeyReleased
        if(txtBuscarProdu.getText().isEmpty()){
        cargarproducto("");
            }
       else{
            cargarproductobuscar();
            }
    }//GEN-LAST:event_txtBuscarProduKeyReleased

    private void btnAceotarProduActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceotarProduActionPerformed
       this.buscarproductos.dispose();
       txtCantidad.setText("1");
        int precioventa, cantidad, total;
        
        cantidad = Integer.parseInt(txtCantidad.getText().toString());
        precioventa = Integer.parseInt(txtPrecioVenta.getText().toString());
        total = cantidad*precioventa;
        txtTotal1.setText(String.valueOf(total));
       
    }//GEN-LAST:event_btnAceotarProduActionPerformed

    private void txtCantidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCantidadFocusLost
       
    
    }//GEN-LAST:event_txtCantidadFocusLost

    private void AgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarProductoActionPerformed
        int stock, cantidad;
        stock = Integer.parseInt(txtStock.getText().toString());
        System.out.println(stock);
        cantidad= Integer.parseInt(txtCantidad.getText().toString());
        System.out.println(cantidad);
        if(cantidad<=stock){
           agregartablafactura();
           sumadetalbla();
           limpiar();
           habilitarbotones2(true);
           habilitarbotonestg(false);
           btnQuitar.setEnabled(true);
            try {
                num_letras();
            } catch (IOException ex) {
                Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
       }else{
            if(cantidad>stock){
           JOptionPane.showMessageDialog(null,"Baja existencia del Producto");
                }
        }       
       
       
    }//GEN-LAST:event_AgregarProductoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            if (btnContado.isSelected()) {

               try {
                   
                   abm.start();
                   
                   int idempleado = abm.idUsuario;
                   
                   rs = abm.nuevo("idfacturaventa", "FacturaVenta");
                   rs.first();
                   txtidfac.setText(String.valueOf(rs.getInt("codigo") + 1));
                    
                       int iva;
                       iva = Integer.parseInt(txtIVA.getText().toString());
                       if((iva==10)||(iva==5)||(iva==0)){
                            String  anulado = "0", estado = "0", condicion = "contado";
                            int plazo=1, saldo=0, montocuota=0;
                             v_control = abm.insertar("FacturaVenta", txtidfac.getText()+","+txtNumFac.getText() +",'"
                                           + fecha.getText() + "','" + estado + "','" + anulado + "'," + txtIdcliente.getText() + "," + txtTotalIva.getText()+
                                            ",'" +condicion+"',"+idempleado+","+ txtDies.getText() +"," + txtCinco.getText() + "," + txtExentas.getText()+","+ plazo + "," + saldo +","+ montocuota+"," + txtTotal.getText()+",'"+fecha.getText()+"'");
                       System.out.println("despues del print");
                       }           
                                  
                        if (v_control == false) {
                               abm.roolback();
                         }
  
                        
                          if (v_control == true) {

                             for (int i = 0; i < tablafactura.getRowCount(); i++) {
                                 Statement consulta = (Statement) conexionBD.ConectarBD().createStatement(); 
                                   consulta.execute("update productos set stock=stock-"+tablafactura.getValueAt(i, 2) + 
                                                    " where idproducto="+tablafactura.getValueAt(i, 0));  
                                   if((iva==10)||(iva==5)||(iva==0)){
                                                  v_control = abm.insertar("DetalleVenta","'"+ tablafactura.getValueAt(i, 0) + "','"
                                                   +  txtidfac.getText()  + "','" +tablafactura.getValueAt(i, 3) + "','"
                                                   +  tablafactura.getValueAt(i, 2)+ "','" + tablafactura.getValueAt(i, 6) + "','" + tablafactura.getValueAt(i, 5) + "','" + tablafactura.getValueAt(i, 4)+"'");   
                                   System.out.println("despues del print2 ");        
                                   }                                  
                                    }
                          if (v_control == false) {
                                           abm.roolback();
                                       }

                                   }
                          

                                   //confirmo la transacion
                                   if (v_control == true) {
                                       abm.comit();
                                   }
                                   
                                   abm.end();
                                   JOptionPane.showMessageDialog(null, "Los Datos se han guardado satisfactoriamente");
                                   impresion();
                               } catch (Exception ex) {
                                   System.out.println("Error al realizar la transacion " + ex.getMessage());
                                   JOptionPane.showMessageDialog(null, "Completar todos los campos para guardar");

                               }
              
               
               
               
               }
               habilitarbotones(false);
               habilitarbotonestg(false);
               habilitarbotones2(false);                  
               limpiarcamposcabezara();
               idfactura();                                
               rs = abm.nuevo("numerofactura", "FacturaVenta");
               rs.first();      
               //txtNumFac.setText(String.valueOf(rs.getInt("codigo") + 1));
        } catch (SQLException ex) {
            Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreditoActionPerformed
       cargarcredito();
       credito.setSize(500, 350);
       credito.setLocationRelativeTo(this);
       credito.setModal(true);
       credito.setVisible(true);
       
    }//GEN-LAST:event_btnCreditoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarcamposcabezara();
        habilitarguardar();
        habilitarbotones(false);
        habilitarbotones2(false);
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        DefaultTableModel dtm = (DefaultTableModel) tablafactura.getModel();
        int i = tablafactura.getSelectedRow();

        //hacemos una condicion de que si la varialbe i es igual a - es que no se ha seleccionado ninguna fila
        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Favor seleccione una fila");

        } else {
            txtTotalIva.setText("");
            txtIva5.setText("");
            txtIva10.setText("");
            txtExentas.setText("");
            txtIva5.setText("");
            txtDies.setText("");
            txtSaldo.setText("");
            txtTotalIva.setText("");
            txtCinco.setText("");
            txtTotal.setText("");
            
           
            
            
            dtm.removeRow(tablafactura.getSelectedRow());
            sumadetalbla();
            habilitarguardar();
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void txtCantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyReleased
       
        if(txtCantidad.getText().isEmpty()){
            txtCantidad.setText("1"); 
        }else{
            int precioventa, cantidad, total;
            cantidad = Integer.parseInt(txtCantidad.getText().toString());
            precioventa = Integer.parseInt(txtPrecioVenta.getText().toString());
            total = cantidad*precioventa;
            txtTotal1.setText(String.valueOf(total));
        }
        
    }//GEN-LAST:event_txtCantidadKeyReleased

    private void txtEntregaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEntregaFocusLost
        // TODO add your handling code here:
       double entrega, montoapagar, total;
            montoapagar =  Double.parseDouble(String.valueOf(txtMontoPagarInteres.getText()));
            entrega = Double.parseDouble(String.valueOf(txtEntrega.getText()));
            total = montoapagar-entrega;
            txtSaldo.setText(String.valueOf(total));
            
            calcularplazo(); 
            
                  
    }//GEN-LAST:event_txtEntregaFocusLost

    
    
    
    private void txtEntregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntregaActionPerformed
        
    }//GEN-LAST:event_txtEntregaActionPerformed

    private void txtPlazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlazoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlazoActionPerformed

    private void txtPlazoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlazoFocusLost
              
    }//GEN-LAST:event_txtPlazoFocusLost

    private void btnGuardarCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarCreditoActionPerformed
       
            try {
            if (btnCredito.isSelected()) {

               try {
                   
                   abm.start();
                   
                   int idempleado = abm.idUsuario;
                   
                   rs = abm.nuevo("idfacturaventa", "FacturaVenta");
                   rs.first();
                   txtidfac.setText(String.valueOf(rs.getInt("codigo") + 1));
                    
                    FechaVence = fecha1.getCalendar();
                    FechaVence.setTime(fecha1.getDate());
                    String dia =String.valueOf(FechaVence.get(Calendar.DATE));
                    String mes =String.valueOf(FechaVence.get(Calendar.MONTH)+1);
                    String anio =String.valueOf(FechaVence.get(Calendar.YEAR));
                    String date = anio+"/"+mes+"/"+dia;
                   
                   
                       int iva;
                       iva = Integer.parseInt(txtIVA.getText().toString());
                       if((iva==10)||(iva==5)||(iva==0)){
                            String  anulado = "0", estado = "0", condicion = "Credito";
                           
                             v_control = abm.insertar("FacturaVenta", txtidfac.getText()+","+txtNumFac.getText() +",'"
                                           + fecha.getText() + "','" + estado + "','" + anulado + "'," + txtIdcliente.getText() + "," + txtTotalIva.getText()
                                             + ",'" +condicion+"',"+idempleado+","+ txtDies.getText() +"," + txtCinco.getText() + "," + txtExentas.getText()+","+ txtPlazo.getText() + "," + txtSaldo.getText() +","+ txtMontoCuota.getText()+"," + txtEntrega.getText()+",'"+anio+"/"+mes+"/"+dia+"'");
                       System.out.println("despues del print");
                       }           
                                  
                        if (v_control == false) {
                               abm.roolback();
                         }
  
                        
                          if (v_control == true) {

                             for (int i = 0; i < tablafactura.getRowCount(); i++) {
                                 Statement consulta = (Statement) conexionBD.ConectarBD().createStatement(); 
                                   consulta.execute("update productos set stock=stock-"+tablafactura.getValueAt(i, 2) + 
                                                    " where idproducto="+tablafactura.getValueAt(i, 0));  
                                   if((iva==10)||(iva==5)||(iva==0)){
                                                  v_control = abm.insertar("DetalleVenta","'"+ tablafactura.getValueAt(i, 0) + "','"
                                                   +  txtidfac.getText()  + "','" +tablafactura.getValueAt(i, 3) + "','"
                                                   +  tablafactura.getValueAt(i, 2)+ "','" + tablafactura.getValueAt(i, 6) + "','" + tablafactura.getValueAt(i, 5) + "','" + tablafactura.getValueAt(i, 4)+"'");   
                                   System.out.println("despues del print2 ");        
                                   }                                
                                    }
                          if (v_control == false) {
                                           abm.roolback();
                                       }

                                   }
                          

                                   //confirmo la transacion
                                   if (v_control == true) {
                                       abm.comit();
                                   }
                                   
                                   abm.end();
                                   JOptionPane.showMessageDialog(null, "Los Datos se han guardado satisfactoriamente");
                               } catch (Exception ex) {
                                   System.out.println("Error al realizar la transacion " + ex.getMessage());
                                   JOptionPane.showMessageDialog(null, "Completar todos los campos para guardar");

                               }
              
               
               }
               habilitarbotones(false);
               habilitarbotonestg(false);
               habilitarbotones2(false);                  
               limpiarcamposcabezara();
               idfactura();                                
               rs = abm.nuevo("numerofactura", "FacturaVenta");
               rs.first();      
               txtNumFac.setText(String.valueOf(rs.getInt("codigo") + 1));
               this.credito.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
        }   
    //v_control = abm.insertar("EntregaInicial", txtIdFacCredito.getText+","+ txtPlazo.getText() + "," + txtSaldo.getText() +","+ txtMontoCuota.getText()+"," + txtEntrega.getText() + ",'" + fecha1.getText()+"',"+ txtidfac.getText());                   
                                      
    }//GEN-LAST:event_btnGuardarCreditoActionPerformed

    private void btnCancelarCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarCreditoActionPerformed
        txtPlazo.setText("0");
        txtMontoCuota.setText("0");
        txtEntrega.setText("0");
        this.credito.dispose();
        btnContado.setSelected(true);
    }//GEN-LAST:event_btnCancelarCreditoActionPerformed

    private void txtPlazoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlazoKeyReleased
        double entrega, montoapagar, total;
            montoapagar =  Double.parseDouble(String.valueOf(txtMontoPagarInteres.getText()));
            entrega = Double.parseDouble(String.valueOf(txtEntrega.getText()));
            total = montoapagar-entrega;
            txtSaldo.setText(String.valueOf(total));
            
            calcularplazo(); 
            
    }//GEN-LAST:event_txtPlazoKeyReleased

private void txtCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyTyped
        char caracter=evt.getKeyChar();
        if((caracter<'0'||(caracter>'9'))&&(caracter!='\b')){
            evt.consume();
        }
}//GEN-LAST:event_txtCantidadKeyTyped

private void txtPlazoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlazoKeyTyped
        char caracter=evt.getKeyChar();
        if((caracter<'0'||(caracter>'9'))&&(caracter!='\b')){
            evt.consume();
        }
}//GEN-LAST:event_txtPlazoKeyTyped

private void txtEntregaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntregaKeyTyped
        char caracter=evt.getKeyChar();
        if((caracter<'0'||(caracter>'9'))&&(caracter!='\b')){
            evt.consume();
        }
}//GEN-LAST:event_txtEntregaKeyTyped

    private void imprimir(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimir
       
        impresion();
    }//GEN-LAST:event_imprimir

    public void impresion(){
         try{
            PrinterJob gap= PrinterJob.getPrinterJob();
            gap.setPrintable(this);
            
            boolean top= gap.printDialog();
            if(top){
                gap.print();
            }
        }
        catch(PrinterException print){
            JOptionPane.showMessageDialog(null,"Error de programa", "Error\n" + print, JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    private void txtNumFacKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumFacKeyTyped
        char caracter=evt.getKeyChar();
        if((caracter<'0'||(caracter>'9'))&&(caracter!='\b')){
            evt.consume();
        }
    }//GEN-LAST:event_txtNumFacKeyTyped

    private void txtNumFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumFacActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumFacActionPerformed

    private void txtnombre2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombre2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombre2ActionPerformed

    private void txtnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreActionPerformed

    private void txtTotalLetrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalLetrasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalLetrasActionPerformed

    private void txtInteresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInteresActionPerformed
        // TODO add your handling code here:
       double totalfactura, totalfacturainteres, interes;
      //   double iva5p = Double.parseDouble(String.valueOf(txtIva5.getText()));
        totalfactura = Double.parseDouble(String.valueOf(txtMontoPagar.getText()));
        interes = Double.parseDouble(String.valueOf(txtInteres.getText()));
        totalfacturainteres= totalfactura+(totalfactura*interes/100);
        txtMontoPagarInteres.setText(String.valueOf(totalfacturainteres));
        
    }//GEN-LAST:event_txtInteresActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
        // TODO add your handling code here:
        int stock, cantidad;
        stock = Integer.parseInt(txtStock.getText().toString());
        System.out.println(stock);
        cantidad= Integer.parseInt(txtCantidad.getText().toString());
        System.out.println(cantidad);
        if(cantidad<=stock){
           agregartablafactura();
           sumadetalbla();
           limpiar();
           habilitarbotones2(true);
           habilitarbotonestg(false);
           btnQuitar.setEnabled(true);
            try {
                num_letras();
            } catch (IOException ex) {
                Logger.getLogger(FacturaVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
       }else{
            if(cantidad>stock){
           JOptionPane.showMessageDialog(null,"Baja existencia del Producto");
                }
        }       
    }//GEN-LAST:event_txtCantidadActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FacturaVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FacturaVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FacturaVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FacturaVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                FacturaVenta dialog = new FacturaVenta(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AgregarProducto;
    private javax.swing.JPanel FacturaImprimir;
    private javax.swing.ButtonGroup Group1;
    private javax.swing.JButton btnAceotarProdu;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelarCredito;
    private javax.swing.JRadioButton btnContado;
    private javax.swing.JRadioButton btnCredito;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardarCredito;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton buscarProdu;
    private javax.swing.JDialog buscarcliente;
    private javax.swing.JDialog buscarproductos;
    private javax.swing.JDialog credito;
    private javax.swing.JLabel fecha;
    private com.toedter.calendar.JDateChooser fecha1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tablaProducto;
    private javax.swing.JTable tablacliente;
    private javax.swing.JTable tablafactura;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscarCliente;
    private javax.swing.JTextField txtBuscarProdu;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCin;
    private javax.swing.JTextField txtCinco;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtDies;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEntrega;
    private javax.swing.JTextField txtExentas;
    private javax.swing.JLabel txtIVA;
    private javax.swing.JLabel txtIdFacCredito;
    private javax.swing.JTextField txtIdcliente;
    private javax.swing.JTextField txtInteres;
    private javax.swing.JTextField txtIva10;
    private javax.swing.JTextField txtIva5;
    private javax.swing.JTextField txtMontoCuota;
    private javax.swing.JTextField txtMontoPagar;
    private javax.swing.JTextField txtMontoPagarInteres;
    private javax.swing.JTextField txtNumFac;
    private javax.swing.JTextField txtPlazo;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtSaldo;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTelef;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotal1;
    private javax.swing.JTextField txtTotalIva;
    private javax.swing.JTextField txtTotalLetras;
    private javax.swing.JTextField txtidfac;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtnombre2;
    // End of variables declaration//GEN-END:variables

    @Override
    public int print(Graphics graf, PageFormat formatopagina, int numpag) throws PrinterException {
       
        if(numpag>0)
            {
             return NO_SUCH_PAGE;
            }
        Graphics2D hub = (Graphics2D) graf;
        hub.translate(formatopagina.getImageableX() + 8 ,formatopagina.getImageableY() + 20);
        hub.scale(0.6, 0.6);
        
        FacturaImprimir.printAll(graf);
        return PAGE_EXISTS;
    }
}
