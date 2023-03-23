package com.example.guiex1;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;


public class Main {
    public static void main(String[] args) {
        System.out.println("ok");
        System.out.println("Reading data from file");
        String username="postgres";
        String pasword="Andra.2002";
        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        HelloApplication.main(args);

    }
}


