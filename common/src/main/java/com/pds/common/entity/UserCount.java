package com.pds.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class UserCount {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @OneToOne
    @JoinColumn(nullable = false)
    private Users users;
    @ColumnDefault("0")
    private int totalCount;
    @ColumnDefault("0")
    private int todayCount;

    public UserCount(Users users){
        this.users = users;
    }
    public void updateTodayCountCount(){
        this.todayCount+=1;
        this.totalCount+=1;
    }
}
