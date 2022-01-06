package org.zutjmx.java.jdbc.servicio;

import org.zutjmx.java.jdbc.modelo.Categoria;
import org.zutjmx.java.jdbc.modelo.Producto;
import org.zutjmx.java.jdbc.repositorio.CategoriaRepositorioImpl;
import org.zutjmx.java.jdbc.repositorio.ProductoRepositorioImpl;
import org.zutjmx.java.jdbc.repositorio.Repositorio;
import org.zutjmx.java.jdbc.util.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CatalogoServicio implements Servicio {
    private Repositorio<Producto> productoRepositorio;
    private Repositorio<Categoria> categoriaRepositorio;

    public CatalogoServicio() {
        this.productoRepositorio = new ProductoRepositorioImpl();
        this.categoriaRepositorio = new CategoriaRepositorioImpl();
    }

    @Override
    public List<Producto> listar() throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            productoRepositorio.setConnection(connection);
            return productoRepositorio.listar();
        }
    }

    @Override
    public Producto porId(Long id) throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            productoRepositorio.setConnection(connection);
            return productoRepositorio.porId(id);
        }
    }

    @Override
    public Producto guardar(Producto producto) throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            productoRepositorio.setConnection(connection);
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            Producto nuevoProducto = null;
            try{
                nuevoProducto = productoRepositorio.guardar(producto);
                connection.commit();
            } catch (SQLException sqlException) {
                connection.rollback();
                sqlException.printStackTrace();
            }
            return nuevoProducto;
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            productoRepositorio.setConnection(connection);
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            try{
                productoRepositorio.eliminar(id);
                connection.commit();
            } catch (SQLException sqlException) {
                connection.rollback();
                sqlException.printStackTrace();
            }
        }
    }

    @Override
    public List<Categoria> listarCategoria() throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            categoriaRepositorio.setConnection(connection);
            return categoriaRepositorio.listar();
        }
    }

    @Override
    public Categoria porIdCategoria(Long id) throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            categoriaRepositorio.setConnection(connection);
            return categoriaRepositorio.porId(id);
        }
    }

    @Override
    public Categoria guardarCategoria(Categoria categoria) throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            categoriaRepositorio.setConnection(connection);
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            Categoria nuevaCategoria = null;
            try{
                nuevaCategoria = categoriaRepositorio.guardar(categoria);
                connection.commit();
            } catch (SQLException sqlException) {
                connection.rollback();
                sqlException.printStackTrace();
            }
            return nuevaCategoria;
        }
    }

    @Override
    public void eliminarCategoria(Long id) throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            categoriaRepositorio.setConnection(connection);
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            try{
                categoriaRepositorio.eliminar(id);
                connection.commit();
            } catch (SQLException sqlException) {
                connection.rollback();
                sqlException.printStackTrace();
            }
        }
    }

    @Override
    public void guardarProductoConCategoria(Producto producto, Categoria categoria) throws SQLException {
        try(Connection connection = ConexionBD.getConnection()) {
            productoRepositorio.setConnection(connection);
            categoriaRepositorio.setConnection(connection);
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            try{
                Categoria nuevaCategoria = categoriaRepositorio.guardar(categoria);
                producto.setCategoria(nuevaCategoria);
                productoRepositorio.guardar(producto);
                connection.commit();
            } catch (SQLException sqlException) {
                connection.rollback();
                sqlException.printStackTrace();
            }
        }
    }
}
