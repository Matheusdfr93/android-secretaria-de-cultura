package com.example.matheus.secretaria;

import java.util.ArrayList;

public class Sessao {

    public static ArrayList<MainActivity.pontos> ponto = new ArrayList();

    public static String getCategoriaselecionada() {
        return categoriaselecionada;
    }

    public static void setCategoriaselecionada(String categoriaselecionada) {
        Sessao.categoriaselecionada = categoriaselecionada;
    }


    public static String categoriaselecionada;

    public static boolean isTpmapa() {
        return tpmapa;
    }

    public static void setTpmapa(boolean tpmapa) {
        Sessao.tpmapa = tpmapa;
    }

    public static boolean tpmapa = true;

    public static boolean isSpt() {
        return spt;
    }

    public static void setSpt(boolean spt) {
        Sessao.spt = spt;
    }

    public static boolean isSpi() {
        return spi;
    }

    public static void setSpi(boolean spi) {
        Sessao.spi = spi;
    }

    public static boolean isSpn() {
        return spn;
    }

    public static void setSpn(boolean spn) {
        Sessao.spn = spn;
    }

    public static boolean isSho() {
        return sho;
    }

    public static void setSho(boolean sho) {
        Sessao.sho = sho;
    }

    public static boolean isSre() {
        return sre;
    }

    public static void setSre(boolean sre) {
        Sessao.sre = sre;
    }

    public static boolean isSart() {
        return sart;
    }

    public static void setSart(boolean sart) {
        Sessao.sart = sart;
    }

    public static boolean isSnoti() {
        return snoti;
    }

    public static void setSnoti(boolean snoti) {
        Sessao.snoti = snoti;
    }

    static boolean spt = true,spi = true,spn = true,sho = true,sre = true,sart = true,snoti = true;



    //Variável utilizada p/ passar informação da activity Ponto para activity de categorias divpontos
    public static String categorias;

    public static String getCategorias() {
        return categorias;
    }

    public static void setCategorias(String categorias) {
        Sessao.categorias = categorias;
    }

    //Shared Preferences


}
