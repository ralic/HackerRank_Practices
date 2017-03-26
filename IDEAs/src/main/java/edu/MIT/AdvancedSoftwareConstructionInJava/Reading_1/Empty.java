package edu.MIT.AdvancedSoftwareConstructionInJava.Reading_1;

/**
 * Created by raliclo on 21/03/2017.
 */
public class Empty<E> implements ImList<E> {
    public Empty() {
    }

    public ImList<E> cons(E e) {
        return new Cons<E>(e, this);
    }

    public E first() {
        throw new UnsupportedOperationException();
    }

    public ImList<E> rest() {
        throw new UnsupportedOperationException();
    }
}

