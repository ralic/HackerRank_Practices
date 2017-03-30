package edu.MIT.AdvancedSoftwareConstructionInJava.Reading_1;

/**
 * Created by raliclo on 21/03/2017.
 */
public class Cons<E> implements ImList<E> {
    private E e;
    private ImList<E> rest;
    public Cons(E e, ImList<E> rest) {
        this.e = e;
        this.rest = rest;
    }
    public ImList<E> cons(E e) { return new Cons<E> (e, this); }
    public E first() { return e; }
    public ImList<E> rest() { return rest; }
}