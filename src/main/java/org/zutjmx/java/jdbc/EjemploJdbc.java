package org.zutjmx.java.jdbc;

import org.zutjmx.java.jdbc.modelo.Categoria;
import org.zutjmx.java.jdbc.modelo.Producto;
import org.zutjmx.java.jdbc.repositorio.CategoriaRepositorioImpl;
import org.zutjmx.java.jdbc.repositorio.ProductoRepositorioImpl;
import org.zutjmx.java.jdbc.repositorio.Repositorio;
import org.zutjmx.java.jdbc.servicio.CatalogoServicio;
import org.zutjmx.java.jdbc.servicio.Servicio;
import org.zutjmx.java.jdbc.util.ConexionBD;

import java.sql.*;
import java.util.Date;

public class EjemploJdbc {
    public static void main(String[] args) throws SQLException {

        Servicio servicio = new CatalogoServicio();
        System.out.println(":::: Se prueba el método listar() ::::");
        servicio.listar().forEach(System.out::println);

        System.out.println(":::: Se prueba insertar nueva categoría ::::");
        Categoria categoria = new Categoria();
        categoria.setNombre("Salud");


        System.out.println(":::: Se prueba el método porId() ::::");
        System.out.println(servicio.porId(2L));

        System.out.println(":::: Se prueba el método guardar() ::::");
        Producto nuevoProducto = new Producto();

        nuevoProducto.setNombre("Ibuprofeno");
        nuevoProducto.setPrecio(25);
        nuevoProducto.setFechaCreacion(new Date());
        nuevoProducto.setSku("K06012022");

        servicio.guardarProductoConCategoria(nuevoProducto,categoria);

        System.out.println(":::: Producto y Categoria guardado :::: con id = " + nuevoProducto.getId());

        System.out.println(":::: Lista de productos ::::");
        servicio.listar().forEach(System.out::println);

        /*try(Connection connection = ConexionBD.getConnection()) {

            if(connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }

            try {

                Repositorio<Categoria> repositorioCategoria = new CategoriaRepositorioImpl(connection);
                System.out.println(":::: Se prueba insertar nueva categoría ::::");
                Categoria categoria = new Categoria();
                categoria.setNombre("Automotriz");
                Categoria nuevaCategoria = repositorioCategoria.guardar(categoria);

                System.out.println(":::: Categoria guardada :::: con id = " + nuevaCategoria.getId());

                Repositorio<Producto> repositorio = new ProductoRepositorioImpl(connection);

                System.out.println(":::: Se prueba el método lista() ::::");
                repositorio.listar().forEach(System.out::println);

                System.out.println(":::: Se prueba el método porId() ::::");
                System.out.println(repositorio.porId(2L));

                System.out.println(":::: Se prueba el método guardar() ::::");
                Producto nuevoProducto = new Producto();

                nuevoProducto.setNombre("Aceite De Motor");
                nuevoProducto.setPrecio(180);
                nuevoProducto.setFechaCreacion(new Date());
                nuevoProducto.setCategoria(nuevaCategoria);
                nuevoProducto.setSku("A06012022");

                repositorio.guardar(nuevoProducto);

                System.out.println(":::: Producto guardado :::: con id = " + nuevoProducto.getId());

                System.out.println(":::: Lista de productos ::::");
                repositorio.listar().forEach(System.out::println);

                connection.commit();
            } catch (SQLException sqlException) {
                connection.rollback();
                sqlException.printStackTrace();
            }

        }*/

    }
}
