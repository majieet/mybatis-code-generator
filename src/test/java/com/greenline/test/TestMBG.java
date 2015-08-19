package com.greenline.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-23 18:07
 */
public class TestMBG {

    @Test
    public void TestGenerateFile() throws Exception{
        List<String> warnings = new ArrayList<String>();
        final boolean overwrite = true;
        File configFile = new File("F:\\share\\code\\mybatis-generator_org.xml");
//        File configFile = new File("F:\\share\\code\\mybatis-generator-ouer.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration configuration = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator mybatisGenerator = new MyBatisGenerator(configuration, callback, warnings);
        mybatisGenerator.generate(null);
        System.out.println("end");
    }

}
