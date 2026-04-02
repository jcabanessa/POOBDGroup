package dao;

import java.util.ArrayList;

public interface ArticuloDAO {
    
    void insertar(Articulo articulo);
    
    ArrayList<Articulo> buscarTodos();
    
}