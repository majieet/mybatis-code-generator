/*
 * Project: test
 * 
 * File Created at 2014-12-26
 
 * Copyright 2012 Greenline.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Greenline Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Greenline.com.
 */
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
 * @since 2014-12-26 18:05
 */
public class InsertMethodGenerator extends AbstractJavaMapperMethodGenerator {
    boolean isSimple;

    public InsertMethodGenerator(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public void addInterfaceElements(Interface interfaze) {
        TreeSet importedTypes = new TreeSet();
        Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(this.introspectedTable.getInsertStatementId());
        FullyQualifiedJavaType parameterType;
        if(this.isSimple) {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        } else {
            parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();
        }

        importedTypes.add(parameterType);

        String parameterTemp = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        String parameterValue = parameterTemp.substring(0,1).toLowerCase() + parameterTemp.substring(1);
        method.addParameter(new Parameter(parameterType, parameterValue));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        this.addMapperAnnotations(interfaze, method);
        if(this.context.getPlugins().clientInsertMethodGenerated(method, interfaze, this.introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }

    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }
}
