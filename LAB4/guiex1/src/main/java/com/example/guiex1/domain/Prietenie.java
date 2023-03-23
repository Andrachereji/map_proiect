package com.example.guiex1.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


public class Prietenie extends Entity<Long>{
    private Long idu1;
    private Long  idu2;
    private LocalDate date;

    public Prietenie(long idu1,long idu2,LocalDate date) {
        this.idu1 = idu1;
        this.idu2 = idu2;
        this.date=date;
    }

    public Long getIdu1() {
        return idu1;
    }


    public Long getIdu2() {
        return idu2;
    }

    public LocalDate getDate() {
        return date;
    }
    @Override
    public String toString() {
        return "Utilizator{" +
                "firstUser='" + idu1 + '\'' +
                ", prieten cu " +
                ", secondUser='" + idu2 + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prietenie that = (Prietenie) o;
        return Objects.equals(idu1, that.idu1) && Objects.equals(idu2, that.idu2) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idu1, idu2, date);
    }
}
