package com.shan.mylotto.lotto.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Lotto {

    private Integer id;
    private Integer lottoGameId;
    private Integer round;
    private Integer numOne;
    private Integer numTwo;
    private Integer numThree;
    private Integer numFour;
    private Integer numFive;
    private Integer numSix;
    private String makeDate;
    private String regDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLottoGameId() {
        return lottoGameId;
    }

    public void setLottoGameId(Integer lottoGameId) {
        this.lottoGameId = lottoGameId;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getNumOne() {
        return numOne;
    }

    public void setNumOne(Integer numOne) {
        this.numOne = numOne;
    }

    public Integer getNumTwo() {
        return numTwo;
    }

    public void setNumTwo(Integer numTwo) {
        this.numTwo = numTwo;
    }

    public Integer getNumThree() {
        return numThree;
    }

    public void setNumThree(Integer numThree) {
        this.numThree = numThree;
    }

    public Integer getNumFour() {
        return numFour;
    }

    public void setNumFour(Integer numFour) {
        this.numFour = numFour;
    }

    public Integer getNumFive() {
        return numFive;
    }

    public void setNumFive(Integer numFive) {
        this.numFive = numFive;
    }

    public Integer getNumSix() {
        return numSix;
    }

    public void setNumSix(Integer numSix) {
        this.numSix = numSix;
    }

    public String getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(String makeDate) {
        this.makeDate = makeDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public List<Integer> getNumList() {
        return Arrays.asList(numOne, numTwo, numThree, numFour, numFive, numSix);
    }

    @Override
    public String toString() {
        return "Lotto{" +
                "id=" + id +
                ", lottoGameId=" + lottoGameId +
                ", round=" + round +
                ", numOne=" + numOne +
                ", numTwo=" + numTwo +
                ", numThree=" + numThree +
                ", numFour=" + numFour +
                ", numFive=" + numFive +
                ", numSix=" + numSix +
                ", makeDate='" + makeDate + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
