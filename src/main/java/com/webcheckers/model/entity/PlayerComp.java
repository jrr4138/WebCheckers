package com.webcheckers.model.entity;

import java.util.Comparator;

public class PlayerComp implements Comparator<Player> {

    @Override
    public int compare(Player p1, Player p2){
        p1.calcWinPercentage();
        p2.calcWinPercentage();
        if(p1.getWinPercentage() < p2.getWinPercentage()){
            return 1;
        }
        else{
            return -1;
        }
    }
}
