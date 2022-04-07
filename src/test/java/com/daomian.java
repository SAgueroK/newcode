package com;

import com.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class daomian {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void test(){
        sensitiveFilter.init();
        String s=sensitiveFilter.filter("ds开票d");
        System.out.println(s);
    }

}
