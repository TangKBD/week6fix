package com.example.week6fix;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Component
public class Wizards implements Serializable {
    public ArrayList<Wizard> model;

    public Wizards() {
        model = new ArrayList<>();
    }
}