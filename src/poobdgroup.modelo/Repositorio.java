package poobdgroup.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

//Clase Java Generics
public class Repositorio<T> implements Iterable<T>{

    private List<T> elementos;

    public Repositorio() {
        elementos = new ArrayList<>();
    }

    public void add(T elemento) {
        elementos.add(elemento);
    }

    public void addAll(Collection<? extends T> nuevosElementos) {
        elementos.addAll(nuevosElementos);
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



}
