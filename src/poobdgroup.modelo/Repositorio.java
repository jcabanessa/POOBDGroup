package poobdgroup.modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Repositorio<T> implements Iterable<T>{

    private List<T> elementos;

    public Repositorio() {
        elementos = new ArrayList<>();
    }

    public void add(T elemento) {
        elementos.add(elemento);
    }

    public boolean remove(T elemento) {
        return elementos.remove(elemento);
    }

    public List<T> getAll() {
        return elementos;
    }

    public int size() {
        return elementos.size();
    }

    public boolean isEmpty() {
        return elementos.isEmpty();
    }
    @Override
    public Iterator<T> iterator() {
        return elementos.iterator();
    }

    public void addAll(ArrayList<T> articulos) {
    }
}
