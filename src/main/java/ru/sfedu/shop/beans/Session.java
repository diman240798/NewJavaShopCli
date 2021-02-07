package ru.sfedu.shop.beans;

import java.util.Objects;
import java.util.UUID;

public class Session {
    private long id;
    private String session = UUID.randomUUID().toString();
    private long date = System.currentTimeMillis();

    public Session() {}

    public Session(long id, String session) {
        setId(id);
        setSession(session);
    }

    public Session(long id) {
        setId(id);
    }

    public Session(long id, String session, long date) {
        this(id, session);
        setDate(date);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session1 = (Session) o;
        return getDate() == session1.getDate() &&
                Objects.equals(getSession(), session1.getSession());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSession(), getDate());
    }

    @Override
    public String toString() {
        return "Session{" +
                "session=" + session +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
