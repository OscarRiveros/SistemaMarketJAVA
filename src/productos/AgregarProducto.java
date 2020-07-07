/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productos;

import Jcombox.categoria;
import Jcombox.marca;
import Jcombox.procedencia;
import Jcombox.proveedor;
import Metodos.abm;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import rsystem.conexionBD;

/**
 *
 * @author oscar
 */
public class AgregarProducto extends javax.swing.JDialog {

    conexionBD cn;
    private abm abm;
    private ResultSet rs,rscbo;
    private boolean v_control;
    DefaultTableModel modelo;
    private Object[] filas;
    private static char opcion;
    private boolean vacio;
     
    
    
    public AgregarProducto() {
        
        initComponents();
        
         cn= new conexionBD();        
         
                
        abm= new abm();
        rs=abm.consulta("*", "productos");
        MostrarRegistro();
        comboxMarca();
        comboxCategoria();
        comboxProveedor();
        
        // TodaLaPantalla();
       
        
        tablaProducto.setSize(2500, 2800);
        
        verproducto();
        cargarproducto("");
       // cargarproductobuscar();       
       // buscarProducto("");
    }
    public  void TodaLaPantalla(){
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(d);
		this.setResizable(false);
	}

       
    
    public void habilitarcampos(boolean h){ //metodo encargado de habilitar o deshabilitar botonoes
              txtPrecioCompra.setEnabled(h);
              txtGanancia.setEnabled(h);
              txtStock.setEnabled(h);
              txtDescriProduc.setEnabled(h);
              comboMarca.setEnabled(h);
              comboCategoria.setEnabled(h);
              comboProveedor.setEnabled(h);
              comboIva.setEnabled(h);
    }
    public void habilitarbotones(boolean h){ //metodo encargado de habilitar o deshabilitar botonoes
                 btnEditar.setEnabled(h);
                 btnEliminar.setEnabled(h);
                 btnAgregar.setEnabled(h);
        }
    public void habilitarbotones2(boolean j){ //metodo encargado de habilitar o deshabilitar botonoes
                btnCancelar.setEnabled(j);
                btnGuardar.setEnabled(j);
        }
    public void limpiar(){
            txtPrecioCompra.setText("");
            txtGanancia.setText("");
            txtPrecioVenta.setText("");
            txtStock.setText("");
            txtDescriProduc.setText("");
        }
    public  boolean validardatos(){
            vacio=false;
            if(txtPrecioCompra.getText().isEmpty()){
                vacio=true;
                 }
                return vacio;
            }
    
    
    private void comboxMarca(){
         try{
            rscbo = abm.consultasql("select * from marca order by idmarca");
            while(rscbo.next()){
                marca mar= new marca();
                mar.setCodigo(Integer.valueOf(rscbo.getString("idmarca")));
                mar.setDescripccion(rscbo.getString("descripcion"));
                comboMarca.addItem(mar);
                }
            }
        catch(Exception e){
            e.printStackTrace();
        } 
}
    
    private void comboxCategoria(){
         try{
            rscbo = abm.consultasql("select * from categoria order by idcategoria");
            while(rscbo.next()){
                categoria cat= new categoria();
                cat.setCodigo(Integer.valueOf(rscbo.getString("idcategoria")));
                cat.setDescripcion(rscbo.getString("descripcion"));
                comboCategoria.addItem(cat);
                }
            }
        catch(Exception e){
            e.printStackTrace();
        } 
}
    
   
      private void comboxProveedor(){
         try{
            rscbo = abm.consultasql("select * from proveedor order by idproveedor");
            while(rscbo.next()){
                proveedor pro= new proveedor();
                pro.setCodigo(Integer.valueOf(rscbo.getString("idproveedor")));
                pro.setNombre(rscbo.getString("nombre"));
                comboProveedor.addItem(pro);
                }
            }
        catch(Exception e){
            e.printStackTrace();
        } 
}    
    
    public void MostrarRegistro(){//metodo creado poara mostrar datos
    try{
       
        if(rs.getRow()!=0){ //devuelve numero de filas de una objeto de tipo resultset
            txtIdProducto.setText(rs.getString(1));
            txtPrecioCompra.setText(rs.getString(2));
            txtPrecioVenta.setText(rs.getString(3));
            txtStock.setText(rs.getString(4));
            txtDescriProduc.setText(rs.getString(5));             
            txtGanancia.setText(rs.getString(6));  
            // combo marca•••••
            marca m=new marca();
            m.setCodigo(Integer.valueOf(rs.getString("idmarca")));
            comboMarca.setSelectedItem(m);
            //combo categoria
            categoria c = new categoria();
            c.setCodigo(Integer.valueOf(rs.getString("idcategoria")));
           // System.out.println("que paso?1-3");
            comboCategoria.setSelectedItem(c);
            //combor Proveedor
            proveedor pro = new proveedor();
            pro.setCodigo(Integer.valueOf(rs.getString("idproveedor")));
            comboProveedor.setSelectedItem(pro);  
        }    
        }catch(Exception e){
            System.out.println("error al mostrar resultados este es "+e.getMessage());
            }
  }
    
void verproducto(){
        try{
            Statement consultamarca = (Statement) conexionBD.ConectarBD().createStatement();
            ResultSet rs = consultamarca.executeQuery("select * from productos");
            
            modelo = new DefaultTableModel();
            tablaProducto.setModel(modelo);
            
            
            modelo.addColumn("Codigo");
            modelo.addColumn("Precio Compra");
            modelo.addColumn("Ganancia");
            modelo.addColumn("Precio Venta");
            modelo.addColumn("Stock");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Marca");
            modelo.addColumn("Categoria");
            modelo.addColumn("Proveedor");
            
            
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
    
     public void cargarproducto(String valor ){
        String [] titulos  = {"Codigo","Precio Compra","Precio Venta","Ganancia","Descripcion","Stock","Marca","Proveedor","Categoria","IVA%"};
        String [] registros  = new String [10];
        String sql=null;
         sql = "select idproducto,preciodecompra,preciodeventa,stock,p.descripcion";
         sql+=",gananciaproducto,marca.descripcion as marca,nombre as proveedor,c.descripcion as categoria,iva";
         sql+=" from productos as p inner join marca on(marca.idmarca=p.idmarca)";
         sql+=" inner join categoria as c on(c.idcategoria=p.idcategoria)";
         sql+=" inner join proveedor as pv on(pv.idproveedor=p.idproveedor) order by idproducto";
        // sql+=" where idproducto="+txtIdProducto.getText();
         
        modelo = new DefaultTableModel(null, titulos);
    
        conexionBD cn = new conexionBD();
        Connection cnn = (Connection) cn.ConectarBD();
        Statement  st;
        try {
            
            st = (Statement) cnn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                registros[0] = rs.getString("idproducto");
                registros[1] = rs.getString("preciodecompra");
                registros[2] = rs.getString("preciodeventa");
                registros[3] = rs.getString("gananciaproducto");
                registros[4] = rs.getString("descripcion");
                registros[5] = rs.getString("stock");
                registros[6] = rs.getString("marca");
                registros[7] = rs.getString("proveedor");
                registros[8] = rs.getString("categoria");
                registros[9] = rs.getString("iva");
                modelo.addRow(registros);
                }
            tablaProducto.setModel(modelo);
            
            } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                            }   
    }
     public void cargarproductobuscar(){
        String [] titulos  = {"Codigo","Precio Compra","Precio Venta","Ganancia","Descripcion","Stock","Marca","Proveedor","Categoria","IVA"};
        String [] registros  = new String [11];
        String sql=null;
         sql = "select idproducto,preciodecompra,preciodeventa,stock,p.descripcion";
         sql+=",gananciaproducto,marca.descripcion as marca,nombre as proveedor,c.descripcion as categoria, p.iva";
         sql+=" from productos as p inner join marca on(marca.idmarca=p.idmarca)";
         sql+=" inner join categoria as c on(c.idcategoria=p.idcategoria)";
         sql+=" inner join proveedor as pv on(pv.idproveedor=p.idproveedor)";
         sql+=" where p.descripcion LIKE '%"+txtBuscar.getText()+"%'";
         //System.out.println(sql);
         
        modelo = new DefaultTableModel(null, titulos);
    
        conexionBD cn = new conexionBD();
        Connection cnn = (Connection) cn.ConectarBD();
        Statement  st;
        try {
            
            st = (Statement) cnn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                registros[0] = rs.getString("idproducto");
                registros[1] = rs.getString("preciodecompra");
                registros[2] = rs.getString("preciodeventa");
                registros[3] = rs.getString("gananciaproducto");
                registros[4] = rs.getString("descripcion");
                registros[5] = rs.getString("stock");
                registros[6] = rs.getString("marca");
                registros[7] = rs.getString("proveedor");
                registros[8] = rs.getString("categoria");
                registros[9] = rs.getString("iva");
                modelo.addRow(registros);
                }
            tablaProducto.setModel(modelo);
            
            } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex);
                            }   
    }
      public void seleccionartabla(){
        
        
      // DefaultTableModel tabla = (DefaultTableModel) this.tablaProducto.getModel();
       
       
       int c= tablaProducto.getSelectedRow();
       
       if(c==-1){
           System.out.println("Seleccione un registro");
            }
       else{
           String id = (String) tablaProducto.getValueAt(c, 0);
           String prcompra = (String) tablaProducto.getValueAt(c, 1);
           String preventa = (String) tablaProducto.getValueAt(c, 2);
           String ganancia = (String) tablaProducto.getValueAt(c, 3);
           String descripcion = (String) tablaProducto.getValueAt(c, 4);
           String stock = (String) tablaProducto.getValueAt(c, 5);
           String marca = (String) tablaProducto.getValueAt(c, 6);
           String proveedor = (String) tablaProducto.getValueAt(c, 7);
           String categoria = (String) tablaProducto.getValueAt(c, 8);
           String iva = (String) tablaProducto.getValueAt(c, 9);
           
     
           this.txtIdProducto.setText(id);
           this.txtPrecioCompra.setText(prcompra);
           this.txtPrecioVenta.setText(preventa);
           this.txtGanancia.setText(ganancia);           
           this.txtStock.setText(stock);
           this.txtDescriProduc.setText(descripcion);  
           
        
           
           for(int i=0; i<this.comboMarca.getItemCount();i++){
               if(((marca)(this.comboMarca.getItemAt(i))).getDescripccion().equals(marca)){
                   this.comboMarca.setSelectedIndex(i);
                   this.comboMarca.repaint();
                 
                   break;
               }
           }
           
           for(int i=0; i<this.comboProveedor.getItemCount();i++){
               if(((proveedor)(this.comboProveedor.getItemAt(i))).getNombre().equals(proveedor)){
                   this.comboProveedor.setSelectedIndex(i);
                   this.comboProveedor.repaint();
                   break;
                   }
                }  
           
           
           
           for(int i=0; i<this.comboCategoria.getItemCount();i++){
               if(((categoria)(this.comboCategoria.getItemAt(i))).getDescripcion().equals(categoria)){
                   this.comboCategoria.setSelectedIndex(i);
                   this.comboCategoria.repaint();
                   break;
                   }
                }           
           
    }
}
      

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtDescriProduc = new javax.swing.JTextField();
        txtGanancia = new javax.swing.JTextField();
        txtPrecioCompra = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        txtIdProducto = new javax.swing.JTextField();
        comboCategoria = new javax.swing.JComboBox();
        comboMarca = new javax.swing.JComboBox();
        comboProveedor = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProducto = new javax.swing.JTable();
        btnSalir = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnBuscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        comboIva = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 204, 0));
        jLabel3.setText(" Productos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(286, 286, 286)
                .addComponent(jLabel3)
                .addContainerGap(508, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3))
        );

        jSplitPane1.setTopComponent(jPanel1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("Categoria:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, -1, -1));

        jLabel9.setText("Codigo:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, -1, -1));

        jLabel10.setText("Precio Compra:");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        jLabel11.setText("Precio Venta:");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, 20));

        jLabel12.setText("Stock:");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, -1, -1));

        jLabel13.setText("Descripcion:");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, -1, -1));

        jLabel14.setText("Ganancia:");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, -1, -1));

        txtDescriProduc.setEnabled(false);
        jPanel2.add(txtDescriProduc, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 400, 150, 50));

        txtGanancia.setEnabled(false);
        txtGanancia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGananciaFocusLost(evt);
            }
        });
        jPanel2.add(txtGanancia, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 340, 50, -1));

        txtPrecioCompra.setEnabled(false);
        txtPrecioCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioCompraKeyTyped(evt);
            }
        });
        jPanel2.add(txtPrecioCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 280, 100, -1));

        txtPrecioVenta.setEnabled(false);
        txtPrecioVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioVentaActionPerformed(evt);
            }
        });
        jPanel2.add(txtPrecioVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 310, 100, -1));

        jLabel15.setText("Marca:");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 280, -1, -1));

        jLabel16.setText("Proveedor:");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 350, -1, -1));

        txtStock.setEnabled(false);
        txtStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockKeyTyped(evt);
            }
        });
        jPanel2.add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 370, 100, -1));

        txtIdProducto.setEnabled(false);
        jPanel2.add(txtIdProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 50, -1));

        comboCategoria.setEnabled(false);
        comboCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCategoriaActionPerformed(evt);
            }
        });
        jPanel2.add(comboCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 310, 130, -1));

        comboMarca.setEnabled(false);
        jPanel2.add(comboMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 270, 130, -1));

        comboProveedor.setEnabled(false);
        jPanel2.add(comboProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 350, 130, -1));

        tablaProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Producto", "Precio Compra", "Precio Venta", "Stock", "Descripcion", "Ganancia", "Marca", "Proveedor", "Procedencia", "Categoria", "IVA"
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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaProductoKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tablaProducto);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 960, 200));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/equis.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel2.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 410, 100, -1));

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel2.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 290, -1, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/articulos.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 350, -1, -1));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/actualizar.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jPanel2.add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 410, 110, -1));

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/basureronegro.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel2.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 290, -1, -1));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/cancel.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setEnabled(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel2.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 350, -1, -1));

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });
        jPanel2.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 200, 30));

        jLabel1.setText("%");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, 20, -1));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 230, 20, 230));

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/buscar.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 10, 110, 30));

        jLabel4.setText("IVA:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 400, -1, -1));

        comboIva.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Agregar", "10", "5", "0" }));
        comboIva.setEnabled(false);
        jPanel2.add(comboIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, 90, -1));

        jLabel6.setText("%");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 400, 20, 30));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 204, 0));
        jLabel5.setText("Introduzca Descripcion del Producto");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 210, -1));

        jSplitPane1.setRightComponent(jPanel2);

        getContentPane().add(jSplitPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 520));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        try {
                opcion='n';
                limpiar();
                habilitarbotones(false);
                habilitarcampos(true);
                habilitarbotones2(true);
                rs = abm.nuevo("idproducto", "productos");
                rs.first();
                txtIdProducto.setText(String.valueOf(rs.getInt("codigo")+1));
                txtPrecioCompra.requestFocus();//mantiene el enfoque en un objeto
                rs.close();
                }catch(Exception e){
                JOptionPane.showMessageDialog(null,"error al generar el codigo "+e);          
                }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
       vacio=validardatos();
        if(vacio==true){
            JOptionPane.showMessageDialog(null,"Completar los datos , no pueden quedar en blanco");
        }else{
            switch(opcion){
                case 'n':
                        conexionBD cn = new conexionBD();// se crea la conexion 
                        Connection cnn = (Connection) cn.ConectarBD();
                        Statement  st; 
                        try{
                            String consulta="";
                            consulta=("Select * from productos where descripcion='"+txtDescriProduc.getText()+"'"); //se traen todos los registros
                            st = (Statement) cnn.createStatement();
                            ResultSet rs = st.executeQuery(consulta);
                            rs.first();
                            if (rs.getRow() != 0){ // si es distinto a 0 ya existe y no agrega
                                            JOptionPane.showMessageDialog(null,"Este producto ya existe");                                           
                                            txtPrecioCompra.setText("");
                                            txtPrecioCompra.requestFocus();
                                            }
                            else{
                                    marca mar =  (marca) comboMarca.getSelectedItem();
                                    categoria cat =  (categoria) comboCategoria.getSelectedItem();
                                    proveedor prove =  (proveedor) comboProveedor.getSelectedItem();
                                    String iva="";
                                    iva = comboIva.getSelectedItem().toString();
                                    v_control=abm.insertar("productos",txtIdProducto.getText()+","+txtPrecioCompra.getText()+","+txtPrecioVenta.getText()+","+txtStock.getText()+",'"+txtDescriProduc.getText()+"',"+txtGanancia.getText()+","+mar.getCodigo()+","+cat.getCodigo()+","+prove.getCodigo()+","+iva);
                                    if (v_control==true){
                                                JOptionPane.showMessageDialog(null,"Se ha guardado los datos");              
                                                }
                                }
                        }catch(Exception e){
                                    System.out.println("Error al mostrar datos en la tabla"+e.getMessage());  
                                    }           
                break;
                case 'm':
                        marca mar =  (marca) comboMarca.getSelectedItem();
                        categoria cat =  (categoria) comboCategoria.getSelectedItem();
                        proveedor prove =  (proveedor) comboProveedor.getSelectedItem();
                        String iva="";
                        iva = comboIva.getSelectedItem().toString();
                        v_control= abm.modificar("productos", " preciodecompra="+txtPrecioCompra.getText()+", "+" preciodeventa="+txtPrecioVenta.getText()+", "
                                +" stock="+txtStock.getText()+", "+" descripcion='"+txtDescriProduc.getText()+"', "+" gananciaproducto="+txtGanancia.getText()+", "+
                                " idmarca="+mar.getCodigo()+", "+" idcategoria="+cat.getCodigo()+", "+" idproveedor="+prove.getCodigo()+", "+" iva="+iva
                                , "idproducto="+txtIdProducto.getText());
                        if(v_control==true){
                          JOptionPane.showMessageDialog(null,"Datos actualizados congratuleishon");
                            }
                        break;
               }
        habilitarcampos(false);
        habilitarbotones(true);
        habilitarbotones2(false);
        rs=abm.consulta("*", "productos");        
        MostrarRegistro();
        verproducto();
        cargarproducto("");        
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void comboCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCategoriaActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
    this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        opcion='m';
        habilitarcampos(true);
        habilitarbotones(false);
        habilitarbotones2(true);
        txtPrecioCompra.requestFocus();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        habilitarbotones2(false);
        habilitarbotones(true);
        habilitarcampos(false);
        rs=abm.consulta("*", "productos");
        MostrarRegistro();
        verproducto();
        cargarproducto("");
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtGananciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGananciaFocusLost
        int precioventa, preciocompra, ganancia;
        
        preciocompra = Integer.parseInt(txtPrecioCompra.getText().toString());
        ganancia = Integer.parseInt(txtGanancia.getText().toString());
        precioventa = preciocompra+(preciocompra*ganancia/100);
        txtPrecioVenta.setText(String.valueOf(precioventa));
    }//GEN-LAST:event_txtGananciaFocusLost

    private void tablaProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductoMouseClicked
       seleccionartabla();
    }//GEN-LAST:event_tablaProductoMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        v_control = abm.eliminar("productos", "idproducto="+txtIdProducto.getText());
        if(v_control==true){
            rs= abm.consulta("*", "productos");
            MostrarRegistro();
            JOptionPane.showMessageDialog(null,"Datos Eliminados congratuleishon");
            }
        verproducto();
        cargarproducto("");
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tablaProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProductoKeyPressed
      
    }//GEN-LAST:event_tablaProductoKeyPressed

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        
    }//GEN-LAST:event_txtBuscarKeyPressed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
        
       if(txtBuscar.getText().isEmpty()){
        cargarproducto("");
            }
       else{cargarproductobuscar();
            }
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void tablaProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProductoKeyReleased
         seleccionartabla();
    }//GEN-LAST:event_tablaProductoKeyReleased

    private void tablaProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProductoKeyTyped
       
    }//GEN-LAST:event_tablaProductoKeyTyped

    private void txtPrecioVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioVentaActionPerformed

private void txtPrecioCompraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioCompraKeyTyped
        char caracter=evt.getKeyChar();
        if((caracter<'0'||(caracter>'9'))&&(caracter!='\b')){
            evt.consume();
        }
}//GEN-LAST:event_txtPrecioCompraKeyTyped

private void txtStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockKeyTyped
char caracter=evt.getKeyChar();
        if((caracter<'0'||(caracter>'9'))&&(caracter!='\b')){
            evt.consume();
        }
}//GEN-LAST:event_txtStockKeyTyped

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
            java.util.logging.Logger.getLogger(AgregarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgregarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgregarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgregarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new AgregarProducto().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox comboCategoria;
    private javax.swing.JComboBox comboIva;
    private javax.swing.JComboBox comboMarca;
    private javax.swing.JComboBox comboProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable tablaProducto;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDescriProduc;
    private javax.swing.JTextField txtGanancia;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtPrecioCompra;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
