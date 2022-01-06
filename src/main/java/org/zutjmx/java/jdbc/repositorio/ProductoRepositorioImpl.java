package org.zutjmx.java.jdbc.repositorio;

import org.zutjmx.java.jdbc.modelo.Categoria;
import org.zutjmx.java.jdbc.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorioImpl implements Repositorio<Producto> {
    private static String consultaSqlTodos = "SELECT p.*, c.nombre AS nombre_categoria FROM productos AS p INNER JOIN categorias AS c ON (p.categoria_id = c.id)";
    private static String consultaSqlById = consultaSqlTodos.concat(" WHERE p.id = ?");
    private static String insertaSql = "insert into productos (nombre, precio, categoria_id, sku, fecha_registro) values(?,?,?,?,?)";
    private static String actualizaSql = "update productos set nombre = ?, precio = ?, categoria_id = ?, sku = ? where id = ?";
    private static String borrarSlq = "delete from productos where id = ?";

    private Connection connection;

    public ProductoRepositorioImpl(Connection connection) {
        this.connection = connection;
    }

    public ProductoRepositorioImpl() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(consultaSqlTodos)) {
            while (resultSet.next()) {
                Producto producto = crearProducto(resultSet);
                productos.add(producto);
            }
        }
        return productos;
    }

    @Override
    public Producto porId(Long id) throws SQLException {
        Producto producto = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement(consultaSqlById)) {
            preparedStatement.setLong(1,id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    producto = crearProducto(resultSet);
                }
                preparedStatement.close();
            }
        }
        return producto;
    }

    @Override
    public Producto guardar(Producto producto) throws SQLException {
        String cadenaSlq;
        if(producto.getId() != null && producto.getId() > 0) {
            cadenaSlq = actualizaSql;
        } else {
            cadenaSlq = insertaSql;
        }
        try(PreparedStatement preparedStatement = connection.prepareStatement(cadenaSlq,Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1,producto.getNombre());
            preparedStatement.setLong(2,producto.getPrecio());
            preparedStatement.setLong(3,producto.getCategoria().getId());
            preparedStatement.setString(4,producto.getSku());
            if (producto.getId() != null && producto.getId() > 0) {
                preparedStatement.setLong(5, producto.getId());
            } else {
                preparedStatement.setDate(5,new Date(producto.getFechaCreacion().getTime()));
            }
            preparedStatement.executeUpdate();

            if (producto.getId() == null) {
                try(ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getLong(1));
                    }
                }
            }

            return producto;
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(borrarSlq)) {
            statement.setLong(1,id);
            statement.executeUpdate();
        }
    }

    private Producto crearProducto(ResultSet resultSet) throws SQLException {
        Producto producto = new Producto();
        producto.setId(resultSet.getLong("id"));
        producto.setNombre(resultSet.getString("nombre"));
        producto.setPrecio(resultSet.getInt("precio"));
        producto.setFechaCreacion(resultSet.getDate("fecha_registro"));
        producto.setSku(resultSet.getString("sku"));
        Categoria categoria = new Categoria();
        categoria.setId(resultSet.getLong("categoria_id"));
        categoria.setNombre(resultSet.getString("nombre_categoria"));
        producto.setCategoria(categoria);
        return producto;
    }

}
