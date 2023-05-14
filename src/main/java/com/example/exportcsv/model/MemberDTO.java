package com.example.exportcsv.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberDTO {
    private int id;

    private String name;

    private int age;

    @Override
    public String toString() {
        return "MemberDTO{" +
                "id=" + id +
                ", name=" + name +
                ", age=" + age +
                '}';
    }
}
