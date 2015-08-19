package com.ouer.tools.generator.code.javamapper.elements;

import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-26 18:07
 */
public class UpdateMethodGenerator extends AbstractJavaMapperMethodGenerator {

    public UpdateMethodGenerator() {
    }

    public void addInterfaceElements(Interface interfaze) {
        TreeSet importedTypes = new TreeSet();
        FullyQualifiedJavaType parameterType;
        if(this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        }

        importedTypes.add(parameterType);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(this.introspectedTable.getUpdateByPrimaryKeySelectiveStatementId());

        String parameterTemp = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        String parameterValue = parameterTemp.substring(0,1).toLowerCase() + parameterTemp.substring(1);
        method.addParameter(new Parameter(parameterType, parameterValue));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(interfaze, method);
        if(this.context.getPlugins().clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, this.introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }

    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }
}
