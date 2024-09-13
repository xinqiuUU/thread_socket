package com.yc.atm;

import java.util.ArrayList;
import java.util.List;

/*
    银行类  ： 账户集合  =》多线程操作的资源  =>锁  =》对象锁
    账户操作 类
 */
public class Bank {
    private List<BankAccount> accounts = new ArrayList<>();

    public Bank() {
        //初始化一些账号
        for (int i=1;i<=10;i++){
            accounts.add( new BankAccount(i,10));
        }
    }
    //查询：无需加锁
    public BankAccount search( int id ) throws Exception {
        for ( BankAccount ba:accounts ){
            if ( ba.getId()==id ){
                return ba;
            }
        }
        throw new Exception("查无此账户:"+id);
    }
    //存款: 加锁
    public BankAccount deposite( int id , double money ) throws Exception {
        BankAccount ba = search(id);
        synchronized ( ba ){  //锁粒度细化
            ba.setBalance( ba.getBalance() + money );
        }
        return ba;
    }
    //取款
    public BankAccount withdraw(int id , double money)throws Exception{
        BankAccount ba = search(id);
        synchronized ( ba ){
            if(  money > ba.getBalance() ){
                throw new Exception("余额不足");
            }
            ba.setBalance(  ba.getBalance()-money  );
        }
        return ba;
    }


}
