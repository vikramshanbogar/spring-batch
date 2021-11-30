package com.springbatchexample;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

public class StudentExcelRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(RowSet rowSet) throws Exception {
        System.out.println("mapRow() in StudentExcelRowMapper");
        User user = new User();
        user.setId(rowSet.getColumnValue(0));
        user.setName(rowSet.getColumnValue(1));
        System.out.println(rowSet.getColumnValue(2));
        user.setDept(rowSet.getColumnValue(2));
        user.setSalary(rowSet.getColumnValue(3));
        return user;
    }
}