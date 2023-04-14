package fr.concurence.exams;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BookExchange {
    private final Object lock = new Object();
    private final int nbReaders;

    public BookExchange(int nbReaders) {
        this.nbReaders = nbReaders;
    }

    public boolean registerInterruptibly() {
        return true;
    }

    public void addBook(String book){

    }

    public Optional<String> seeBookIfAvailable() {
        return Optional.ofNullable(null);
    }

    public String getBook() {
        return null;
    }

    public Set<Thread> readersWaiting() {
        return null;
    }

    public List<String> close() {
        return null;
    }
}
