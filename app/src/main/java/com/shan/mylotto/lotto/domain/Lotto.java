package com.shan.mylotto.lotto.domain;

import java.util.Date;
import java.util.List;

public class Lotto {

    private Integer id;
    private Integer lottoGameId;
    private List<Integer> numbers;
    private Integer bonusNumber;
    private String makeDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public Integer getBonusNumber() {
        return bonusNumber;
    }

    public void setBonusNumber(Integer bonusNumber) {
        this.bonusNumber = bonusNumber;
    }

    public String getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(String makeDate) {
        this.makeDate = makeDate;
    }

    public Integer getLottoGameId() {
        return lottoGameId;
    }

    public void setLottoGameId(Integer lottoGameId) {
        this.lottoGameId = lottoGameId;
    }

    @Override
    public String toString() {
        return "Lotto{" +
                "id=" + id +
                ", lottoGameId=" + lottoGameId +
                ", numbers=" + numbers +
                ", bonusNumber=" + bonusNumber +
                ", makeDate=" + makeDate +
                '}';
    }
}
