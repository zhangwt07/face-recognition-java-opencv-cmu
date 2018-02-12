/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.controller;

import java.util.ArrayList;

/**
 *
 * @author chenjiaxin
 */
public class Addrecord {

    public void addrec(ArrayList<String> result) {

        System.out.println(result.get(0));

        StoreStudent ss = new StoreStudent();

        // get the number of students in the DB
        int num = ss.getCount();
        //System.out.println("count:"+num);
        // the new person's label
        int nextLabel = num + 1;

        // store the data into database
        ss.store(result, nextLabel);
    }

}
