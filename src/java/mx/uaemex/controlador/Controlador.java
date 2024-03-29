/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.uaemex.controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.uaemex.modelo.Carrito;
import mx.uaemex.modelo.Producto;
import mx.uaemex.modelo.ProductoDAO;

/**
 *
 * @author sug-0
 */
@WebServlet(name = "Controlador", urlPatterns = {"/Controlador"})
public class Controlador extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    ProductoDAO pdao = new ProductoDAO();
    Producto p=new Producto();
    List<Producto> productos= new ArrayList<>();//ARRAYLIS DE PRODCUTOS 
    List<Carrito>  listaCarrito = new ArrayList<>();
    int item;
    double totalPagar=0.0;
    int cantidad =1;
    
    int idp;
    Carrito car =new Carrito();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    String accion=request.getParameter("accion");
    productos = pdao.listar();//trae los registros
    switch(accion){
        case "Comprar":
            totalPagar=0.0;
            idp = Integer.parseInt(request.getParameter("id"));
            p=pdao.listarId(idp);
            item=item+1;
            car=new Carrito();
            car.setItem(item);
            car.setIdProducto(p.getId());
            car.setNombres(p.getNombres());
            car.setDescripcion(p.getDescripcion());
            car.setPrecioCompra(p.getPrecio());
            car.setCantidad(cantidad);
            car.setSubTotal(cantidad*p.getPrecio());
            listaCarrito.add(car);
            for(int i = 0; i<listaCarrito.size();i++){
               totalPagar=totalPagar+listaCarrito.get(i).getSubTotal();// EL METODO GET()DEVUELVE EL ELEMENTO QUE ESTA EN LA POSICION 'i' DEL ARRAYLIST
            }
            request.setAttribute("totalPagar",totalPagar);
            request.setAttribute("Carrito",listaCarrito);
            request.setAttribute("contador", listaCarrito.size());
            request.getRequestDispatcher("carrito.jsp").forward(request,response);
        break;
        case "AgregarCarrito":
            int pos=0;
            cantidad = 1;
            idp = Integer.parseInt(request.getParameter("id"));//3
            
            p=pdao.listarId(idp);
            if(listaCarrito.size() > 0){
                for(int i = 0; i < listaCarrito.size(); i++){
                    if(idp==listaCarrito.get(i).getIdProducto()){
                        pos= i ;
                    }
                }     
                    if(idp==listaCarrito.get(pos).getIdProducto()){
                        cantidad = listaCarrito.get(pos).getCantidad() + cantidad;
                        double subtotal = listaCarrito.get(pos).getPrecioCompra()*cantidad;
                        listaCarrito.get(pos).setCantidad(cantidad);
                        listaCarrito.get(pos).setSubTotal(subtotal);
                    } else {
                        item=item+1;
                        car= new Carrito();
                        car.setItem(item);
                        car.setIdProducto(p.getId());
                        car.setNombres(p.getNombres());
                        car.setDescripcion(p.getDescripcion());
                        car.setPrecioCompra(p.getPrecio());
                        car.setCantidad(cantidad);
                        car.setSubTotal(cantidad*p.getPrecio());
                        listaCarrito.add(car);
                    } 
                
            } else {
            item=item+1;
            car= new Carrito();
            car.setItem(item);
            car.setIdProducto(p.getId());
            car.setNombres(p.getNombres());
            car.setDescripcion(p.getDescripcion());
            car.setPrecioCompra(p.getPrecio());
            car.setCantidad(cantidad);
            car.setSubTotal(cantidad*p.getPrecio());
            listaCarrito.add(car);
            }
            request.setAttribute("contador", listaCarrito.size());
            request.getRequestDispatcher("Controlador?accion=home").forward(request, response);
   
        break;
        case "Delete":
        int idProducto=Integer.parseInt(request.getParameter("idp"));
            for(int i = 0; i < listaCarrito.size(); i++){
                if(listaCarrito.get(i).getIdProducto()==idProducto){
                    listaCarrito.remove(i);
                }
            }
        break;
        case "ActualizarCantidad":
            int idpro=Integer.parseInt(request.getParameter("idp"));
            int cant=Integer.parseInt(request.getParameter("Cantidad"));
            for(int i = 0; i < listaCarrito.size(); i++){
                if(listaCarrito.get(i).getIdProducto()==idpro)
                    listaCarrito.get(i).setCantidad(cant);
                    double st=listaCarrito.get(i).getPrecioCompra()*cant;
                    listaCarrito.get(i).setSubTotal(st);
            }
        break;
        case "Carrito":
            totalPagar=0.0;            
            request.setAttribute("Carrito",listaCarrito);
            for(int i = 0; i<listaCarrito.size();i++){
               totalPagar=totalPagar+listaCarrito.get(i).getSubTotal();
            }
            request.setAttribute("totalPagar", totalPagar);
            request.getRequestDispatcher("carrito.jsp").forward(request,response);
            
        break;
        
        default:
        request.setAttribute("producto",productos);// El primero hace referencia a un nombre para pasarlo a index
        request.getRequestDispatcher("index.jsp").forward(request,response);
        
    }
    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
