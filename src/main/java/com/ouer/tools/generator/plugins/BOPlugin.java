/*
 * Project: test
 * 
 * File Created at 2015-01-12
 
 * Copyright 2012 Greenline.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Greenline Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Greenline.com.
 */
package com.ouer.tools.generator.plugins;

import com.ouer.tools.generator.code.OuerMyBatis3FormattingUtilities;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

/**
 *
 * @author mc
 * @version V1.0
 * @since 2015-01-12 09:41
 */
public class BOPlugin extends PluginAdapter {
    private List<TopLevelClass> topLevelClassList = new ArrayList<TopLevelClass>();
    private String targetProject;
    private String boPackage;
    private String transformPackage;

    @Override
    public boolean validate(List<String> warnings) {
        targetProject = this.properties.getProperty("targetProject");
        boPackage = this.properties.getProperty("boPackage");
        transformPackage = this.properties.getProperty("transformPackage");

        boolean valid = true;
        if(!StringUtility.stringHasValue(targetProject)) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "targetProject"));
            valid = false;
        }

        if(!StringUtility.stringHasValue(boPackage)) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "boPackage"));
            valid = false;
        }

        if(!StringUtility.stringHasValue(transformPackage)) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "transformPackage"));
            valid = false;
        }

        return valid;
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        ArrayList result = new ArrayList();
        for(TopLevelClass topLevelClass : topLevelClassList){
            //生成BO代码
            TopLevelClass boTopLevelClass = this.generatorBO(topLevelClass);
            GeneratedJavaFile gafBO = new GeneratedJavaFile(boTopLevelClass, targetProject,"utf-8", this.context.getJavaFormatter());
            result.add(gafBO);
            //生成transform代码
            GeneratedJavaFile gafTransform = new GeneratedJavaFile(this.generatorTransform(topLevelClass,boTopLevelClass), targetProject,"utf-8", this.context.getJavaFormatter());
            result.add(gafTransform);
        }

        return result;
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        topLevelClassList.add(topLevelClass);
        return true;
    }

    private TopLevelClass generatorBO(TopLevelClass modelTopLevelClass){
        String modelName = modelTopLevelClass.getType().getShortName();
        String boName = modelName.substring(0, modelName.length()-2)+"BO";
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(boPackage+"."+boName);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("java.io.Serializable"));
        topLevelClass.addImportedType("java.io.Serializable");
        topLevelClass.addImportedTypes(modelTopLevelClass.getImportedTypes());

        Field serialVersionUIDField = new Field("serialVersionUID",new FullyQualifiedJavaType("long"));
        serialVersionUIDField.setVisibility(JavaVisibility.PRIVATE);
        serialVersionUIDField.setStatic(true);
        serialVersionUIDField.setFinal(true);
        serialVersionUIDField.setInitializationString("1L");
        topLevelClass.addField(serialVersionUIDField);

        for(Field field : modelTopLevelClass.getFields()){
            topLevelClass.addField(field);
        }
        for(Method method : modelTopLevelClass.getMethods()){
            topLevelClass.addMethod(method);
        }

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass);
        return topLevelClass;
    }

    private TopLevelClass generatorTransform(TopLevelClass poTopLevelClass,TopLevelClass boTopLevelClass){
        String poName = poTopLevelClass.getType().getShortName();
        String transformName = poName.substring(0, poName.length()-2)+"Transform";
        String poCaseName = OuerMyBatis3FormattingUtilities.getFirstLowerCaseString(poName);
        String boName = poName.substring(0, poName.length()-2)+"BO";
        String boCaseName = OuerMyBatis3FormattingUtilities.getFirstLowerCaseString(boName);

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(transformPackage+"."+transformName);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addImportedType(poTopLevelClass.getType());
        topLevelClass.addImportedType(boTopLevelClass.getType());
        topLevelClass.addImportedType("java.util.ArrayList");
        topLevelClass.addImportedType("java.util.Collections");
        topLevelClass.addImportedType("java.util.List");

        Method BOToPOMethod = new Method("transform");
        BOToPOMethod.setVisibility(JavaVisibility.PUBLIC);
        BOToPOMethod.setStatic(true);
        BOToPOMethod.setReturnType(poTopLevelClass.getType());
        BOToPOMethod.addParameter(new Parameter(boTopLevelClass.getType(), boCaseName));
        BOToPOMethod.addBodyLine("if ("+boCaseName+" == null) {");
        BOToPOMethod.addBodyLine("return null;");
        BOToPOMethod.addBodyLine("}");
        BOToPOMethod.addBodyLine(""+poName+" "+poCaseName+" = new "+poName+"();");
        List<Field> fields = poTopLevelClass.getFields();
        for(Field field : fields){
            String setterMethodName = JavaBeansUtil.getSetterMethodName(field.getName());
            String getterMethodName = JavaBeansUtil.getGetterMethodName(field.getName(), field.getType());
            BOToPOMethod.addBodyLine(""+poCaseName+"."+setterMethodName+"("+boCaseName+"."+getterMethodName+"());");
        }
        BOToPOMethod.addBodyLine("return "+poCaseName+";");
        topLevelClass.addMethod(BOToPOMethod);

        Method POToBOMethod = new Method("transform");
        POToBOMethod.setVisibility(JavaVisibility.PUBLIC);
        POToBOMethod.setStatic(true);
        POToBOMethod.setReturnType(boTopLevelClass.getType());
        POToBOMethod.addParameter(new Parameter(poTopLevelClass.getType(), poCaseName));
        POToBOMethod.addBodyLine("if ("+poCaseName+" == null) {");
        POToBOMethod.addBodyLine("return null;");
        POToBOMethod.addBodyLine("}");
        POToBOMethod.addBodyLine(""+boName+" "+boCaseName+" = new "+boName+"();");
        for(Field field : fields){
            String setterMethodName = JavaBeansUtil.getSetterMethodName(field.getName());
            String getterMethodName = JavaBeansUtil.getGetterMethodName(field.getName(), field.getType());
            POToBOMethod.addBodyLine(""+boCaseName+"."+setterMethodName+"("+poCaseName+"."+getterMethodName+"());");
        }
        POToBOMethod.addBodyLine("return "+boCaseName+";");
        topLevelClass.addMethod(POToBOMethod);

        Method POToBOListMethod = new Method("transform");
        POToBOListMethod.setVisibility(JavaVisibility.PUBLIC);
        POToBOListMethod.setStatic(true);
        FullyQualifiedJavaType boListType = FullyQualifiedJavaType.getNewListInstance();
        boListType.addTypeArgument(boTopLevelClass.getType());
        POToBOListMethod.setReturnType(boListType);
        String poListName = poCaseName + "List";
        String boListName = boCaseName + "List";
        FullyQualifiedJavaType poListType = FullyQualifiedJavaType.getNewListInstance();
        poListType.addTypeArgument(poTopLevelClass.getType());
        POToBOListMethod.addParameter(new Parameter(poListType, poListName));
        POToBOListMethod.addBodyLine("if ("+poListName+" == null) {");
        POToBOListMethod.addBodyLine("return Collections.EMPTY_LIST;");
        POToBOListMethod.addBodyLine("}");
        POToBOListMethod.addBodyLine("List<"+boName+"> "+boListName+" = new ArrayList<"+boName+">();");
        POToBOListMethod.addBodyLine("for ("+poName+" "+poCaseName+" : "+poListName+") {");
        POToBOListMethod.addBodyLine(""+boListName+".add(transform("+poCaseName+"));");
        POToBOListMethod.addBodyLine("}");
        POToBOListMethod.addBodyLine("return "+boListName+";");
        topLevelClass.addMethod(POToBOListMethod);

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass);
        return topLevelClass;
    }


}
