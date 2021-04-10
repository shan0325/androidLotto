package com.shan.mylotto.lotto.domain;

import java.util.Date;
import java.util.List;

public class LottoGame {

    private Integer id;
    private Integer round;
    private List<Lotto> lottos;
    private String regDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public List<Lotto> getLottos() {
        return lottos;
    }

    public void setLottos(List<Lotto> lottos) {
        this.lottos = lottos;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "LottoGame{" +
                "id=" + id +
                ", round=" + round +
                ", lottos=" + lottos +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
