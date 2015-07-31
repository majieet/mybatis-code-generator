package com.ouer.tools.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.List;

/**
 * Created by xuanwu on 2015/5/27.
 */
public class AutoIdHandlerPlugin extends PluginAdapter {
    private String text;
    @Override
    public boolean validate(List<String> warnings) {
        text = this.properties.getProperty("text");
        boolean valid = true;
        if(!StringUtility.stringHasValue(text)) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "text"));
            valid = false;
        }
        return valid;
    }

    public void initialized(IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> introspectedColumnList = introspectedTable.getAllColumns();
        for(IntrospectedColumn column : introspectedColumnList){
            if(column.getRemarks().contains(text)){
                column.setTypeHandler("idHandler");
                column.setFullyQualifiedJavaType(new FullyQualifiedJavaType(
                        "java.lang.String"));
            }
        }

    }
}
