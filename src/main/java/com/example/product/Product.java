package com.example.product;

import javax.persistence.*;

@Entity
public class Product {

    @Id @GeneratedValue
    Long id;
    String name;
    int stock;

    @PostPersist
    @PostUpdate
    public void onPostPersists() {
        //  이벤트 발행
        ProductChanged productChanged = new ProductChanged();
        productChanged.setProductName(this.getName());
        productChanged.setProductId(this.getId());
        productChanged.setProductStock(this.getStock());

        try{
            productChanged.publish();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("*********************** sending Product Changed ***********************");
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
