package com.yc.atm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//   银行账户
@Data
@AllArgsConstructor //由lombok生成有参构造方法
@NoArgsConstructor  //由lombok生成无产构造方法
public class BankAccount {
    private int id;
    private double balance;
}
