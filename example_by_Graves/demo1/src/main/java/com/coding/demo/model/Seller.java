package com.coding.demo.model;

import javafx.util.Pair;
import lombok.Data;

import java.util.Map;
import java.util.Vector;

@Data
public class Seller {
    int id;//id号用来和user连接
    String name;//姓名
    String IdCard;//身份证
    String introduction;//商家介绍
    String location;//店铺详细位置
    String prove;//证明
    String pictureUrl;//商家图片地址
}
