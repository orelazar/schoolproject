package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class StoreUser implements Comparable<StoreUser>{
    private @Id @GeneratedValue Long id;
    private String userName;

    @JsonIgnore @OneToMany (mappedBy = "storeUser")
    private List<StoreOrder> storeOrders = new ArrayList<>();

    public StoreUser(String name){
        this.userName = name;
    }

    @Override
    public int compareTo(StoreUser o) {
        return Long.compare(this.id, o.id);
    }

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }

        if (!(o instanceof StoreUser)) {
            return false;
        }

        StoreUser storeUser = (StoreUser) o;

        return this.getId() == storeUser.getId();
    }
}
