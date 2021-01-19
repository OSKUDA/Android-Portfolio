package io.github.oskuda.mydoes;

public class MyDoes {
    String titleDoes, dateDoes, descDoes, keyDoes;


    public MyDoes() {
        //empty constructor is required for firebase interface
    }

    public MyDoes(String titleDoes, String dateDoes, String descDoes, String keyDoes) {
        this.titleDoes = titleDoes;
        this.dateDoes = dateDoes;
        this.descDoes = descDoes;
        this.keyDoes = keyDoes;
    }

    public String getKeyDoes() {
        return keyDoes;
    }

    public void setKeyDoes(String keyDoes) {
        this.keyDoes = keyDoes;
    }

    public String getTitleDoes() {
        return titleDoes;
    }

    public String getDateDoes() {
        return dateDoes;
    }

    public String getDescDoes() {
        return descDoes;
    }

}
