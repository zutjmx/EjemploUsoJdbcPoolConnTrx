package org.zutjmx.java.jdbc.repositorio;

import org.zutjmx.java.jdbc.modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositorioImpl implements Repositorio<Categoria> {
    private Connection connection;

    public CategoriaRepositorioImpl(Connection connection) {
        this.connection = connection;
    }

    public CategoriaRepositorioImpl() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from categorias")) {
            while (resultSet.next()) {
                categorias.add(crearCategoria(resultSet));
            }
        }
        return categorias;
    }

    @Override
    public Categoria porId(Long id) throws SQLException {
        Categoria categoria = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from categorias where id = ?")) {
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    categoria = crearCategoria(resultSet);
                }
            }
        }
        return categoria;
    }

    @Override
    public Categoria guardar(Categoria categoria) throws SQLException {
        String sql = null;

        if (categoria.getId() != null && categoria.getId() > 0) {
            sql = "update categorias set nombre = ? where id = ?";
        } else {
            sql = "insert into categorias (nombre) values(?)";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1,categoria.getNombre());
            if (categoria.getId() != null && categoria.getId() > 0) {
                preparedStatement.setLong(2,categoria.getId());
            }
            preparedStatement.executeUpdate();

            if (categoria.getId() == null) {
                try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if(resultSet.next()) {
                        categoria.setId(resultSet.getLong(1));
                    }
                }
            }

        }

        return categoria;
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete from categorias where id = ?")) {
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        }
    }

    private Categoria crearCategoria(ResultSet resultSet) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(resultSet.getLong("id"));
        categoria.setNombre(resultSet.getString("nombre"));
        return categoria;
    }
}
