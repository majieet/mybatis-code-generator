package com.ouer.tools.generator.plugins;

import com.ouer.tools.generator.code.OuerMyBatis3FormattingUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

/**
 * @author mc
 * @version V1.0
 * @since 2015-01-12 16:22
 */
@Deprecated
public class MgrPlugin extends PluginAdapter {
    private List<Interface> interfaceList = new ArrayList<Interface>();
    private Map<String, TopLevelClass> modelDaoMap = new HashMap<String, TopLevelClass>();
    private String targetProject;
    private String boPackage;
    private String transformPackage;
    private String mgrPackage;
    private String mgrImplPackage;

    @Override
    public boolean validate(List<String> warnings) {
        targetProject = this.properties.getProperty("targetProject");
        boPackage = this.properties.getProperty("boPackage");
        transformPackage = this.properties.getProperty("transformPackage");
        mgrPackage = this.properties.getProperty("mgrPackage");
        mgrImplPackage = this.properties.getProperty("mgrImplPackage");

        boolean valid = true;
        if (!StringUtility.stringHasValue(targetProject)) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "targetProject"));
            valid = false;
        }

        if (!StringUtility.stringHasValue(boPackage)) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "boPackage"));
            valid = false;
        }

        if (!StringUtility.stringHasValue(transformPackage)) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "transformPackage"));
            valid = false;
        }

        if (!StringUtility.stringHasValue(this.properties.getProperty("mgrPackage"))) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "mgrPackage"));
            valid = false;
        }

        if (!StringUtility.stringHasValue(this.properties.getProperty("mgrImplPackage"))) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "mgrImplPackage"));
            valid = false;
        }

        return valid;
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        ArrayList result = new ArrayList();
        for (Interface interfaze : interfaceList) {
            String modelName = interfaze.getType().getShortName();
            String boName = modelName.substring(0, modelName.length() - 3) + "BO";
            FullyQualifiedJavaType boType = new FullyQualifiedJavaType(boPackage + "." + boName);
            TopLevelClass boClass = new TopLevelClass(boType);

            String transformName = modelName.substring(0, modelName.length() - 3) + "Transform";
            FullyQualifiedJavaType transformType = new FullyQualifiedJavaType(transformPackage + "." + transformName);
            TopLevelClass transformClass = new TopLevelClass(transformType);
            //生成Mgr代码
            Interface mgrInterface = this.generatorMgr(interfaze, boClass);
            GeneratedJavaFile gafMgr = new GeneratedJavaFile(mgrInterface, targetProject, "utf-8",
                    this.context.getJavaFormatter());
            result.add(gafMgr);
            //生成MgrImpl代码
            GeneratedJavaFile gafMgrImpl = new GeneratedJavaFile(
                    this.generatorMgrImpl(interfaze, mgrInterface, boClass,transformClass), targetProject, "utf-8",
                    this.context.getJavaFormatter());
            result.add(gafMgrImpl);

        }
        return result;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        interfaceList.add(interfaze);
        return true;
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String modelName = topLevelClass.getType().getShortName();
        String daoName = modelName.substring(0, modelName.length() - 2) + "DAO";
        modelDaoMap.put(daoName, topLevelClass);
        return true;
    }

    private Interface generatorMgr(Interface daoInterface, TopLevelClass boClass) {
        String boCaseName = OuerMyBatis3FormattingUtilities.getFirstLowerCaseString(boClass.getType().getShortName());
        String modelName = daoInterface.getType().getShortName();
        String mgrName = modelName.substring(0, modelName.length() - 3) + "Mgr";
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(mgrPackage + "." + mgrName);
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        interfaze.addImportedType(boClass.getType());

        for (Method method : daoInterface.getMethods()) {
            Method newMethod = new Method(method);
            if ("get".equals(method.getName())) {
                newMethod.setReturnType(boClass.getType());
            } else if ("insert".equals(method.getName())) {
                newMethod.setReturnType(new FullyQualifiedJavaType("java.lang.Long"));
                newMethod.getParameters().clear();
                newMethod.addParameter(new Parameter(boClass.getType(), boCaseName));
            } else if ("update".equals(method.getName())) {
                newMethod.getParameters().clear();
                newMethod.addParameter(new Parameter(boClass.getType(), boCaseName));
            } else if ("list".equals(method.getName())) {
                FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
                listType.addTypeArgument(boClass.getType());
                newMethod.setReturnType(listType);
                newMethod.getParameters().clear();
                newMethod.addParameter(new Parameter(boClass.getType(), boCaseName));
            }
            interfaze.addMethod(newMethod);
        }

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(interfaze);
        return interfaze;
    }

    private TopLevelClass generatorMgrImpl(Interface daoInterface, Interface mgrInterface, TopLevelClass boClass,
            TopLevelClass transformClass) {
        String boName = boClass.getType().getShortName();
        String boCaseName = OuerMyBatis3FormattingUtilities.getFirstLowerCaseString(boName);
        String modelName = daoInterface.getType().getShortName();
        String mgrImplName = modelName.substring(0, modelName.length() - 3) + "MgrImpl";
        TopLevelClass poClass = modelDaoMap.get(modelName);
        String poName = poClass.getType().getShortName();
        String poCaseName = OuerMyBatis3FormattingUtilities.getFirstLowerCaseString(poName);
        String transformName = transformClass.getType().getShortName();
        FullyQualifiedJavaType daoType = daoInterface.getType();
        String daoCaseName = OuerMyBatis3FormattingUtilities.getFirstLowerCaseString(daoType.getShortName());
        Modelbean modelbean = new Modelbean();
        modelbean.setBoName(boName);
        modelbean.setBoCaseName(boCaseName);
        modelbean.setPoName(poName);
        modelbean.setPoCaseName(poCaseName);
        modelbean.setDaoCaseName(daoCaseName);
        modelbean.setTransformName(transformName);

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(mgrImplPackage + "." + mgrImplName);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.addSuperInterface(mgrInterface.getType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addImportedType(mgrInterface.getType());
        topLevelClass.addImportedType(daoInterface.getType());
        topLevelClass.addImportedType(boClass.getType());
        topLevelClass.addImportedType(poClass.getType());
        topLevelClass.addImportedType(transformClass.getType());
        topLevelClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        topLevelClass.addImportedType("org.springframework.stereotype.Service");
        topLevelClass.addImportedType("java.util.List");
        List<Field> fields = poClass.getFields();
        if(fieldContains(fields, "gmtCreated", "java.util.Date") || fieldContains(fields, "gmtModified", "java.util.Date")){
            topLevelClass.addImportedType("java.util.Date");
        }

        topLevelClass.addAnnotation("@Service(\"" + OuerMyBatis3FormattingUtilities
                .getFirstLowerCaseString(mgrInterface.getType().getShortName()) + "\")");
        Field mapperField = new Field(daoCaseName, daoType);
        mapperField.addAnnotation("@Autowired");
        topLevelClass.addField(mapperField);

        for (Method method : daoInterface.getMethods()) {
            Method newMethod = new Method(method);
            newMethod.addAnnotation("@Override");
            if ("get".equals(method.getName())) {
                newMethod.setReturnType(boClass.getType());
                this.gtGet(newMethod,modelbean);
            }else if ("delete".equals(method.getName())) {
                this.gtDelete(newMethod,modelbean);
            }else if ("insert".equals(method.getName())) {
                newMethod.setReturnType(new FullyQualifiedJavaType("java.lang.Long"));
                newMethod.getParameters().clear();
                newMethod.addParameter(new Parameter(boClass.getType(), boCaseName));
                this.gtInsert(newMethod,modelbean,poClass);
            } else if ("update".equals(method.getName())) {
                newMethod.getParameters().clear();
                newMethod.addParameter(new Parameter(boClass.getType(), boCaseName));
                this.gtUpdate(newMethod,modelbean,poClass);
            } else if ("list".equals(method.getName())) {
                FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
                listType.addTypeArgument(boClass.getType());
                newMethod.setReturnType(listType);
                newMethod.getParameters().clear();
                newMethod.addParameter(new Parameter(boClass.getType(), boCaseName));
                this.gtList(newMethod,modelbean);
            }
            topLevelClass.addMethod(newMethod);
        }

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass);
        return topLevelClass;
    }

    private void gtGet(Method method,Modelbean modelbean){
        method.addBodyLine(modelbean.getPoName()+" "+modelbean.getPoCaseName()+" = "+modelbean.getDaoCaseName()+".get("+method.getParameters().get(0).getName()+");");
        method.addBodyLine("return "+modelbean.getTransformName()+".transform("+modelbean.getPoCaseName()+");");
    }

    private void gtInsert(Method method,Modelbean modelbean,TopLevelClass poClass){
        method.addBodyLine(modelbean.getPoName()+" "+modelbean.getPoCaseName()+" = "+modelbean.getTransformName()+".transform("+modelbean.getBoCaseName()+");");
        List<Field> fields = poClass.getFields();
        if(fieldContains(fields, "gmtCreated", "java.util.Date")){
            method.addBodyLine("Date nowDate = new Date();");
            method.addBodyLine(modelbean.getPoCaseName()+".setGmtCreated(nowDate);");
            if(fieldContains(fields, "gmtModified", "java.util.Date")){
                method.addBodyLine(modelbean.getPoCaseName()+".setGmtModified(nowDate);");
            }
        }
        if(fieldContains(fields, "isDelete", "java.lang.Boolean")){
            method.addBodyLine(modelbean.getPoCaseName()+".setIsDelete(Boolean.FALSE);");
        }
        if(fieldContains(fields, "staffModified", "java.lang.String")){
            method.addBodyLine(modelbean.getPoCaseName()+".setStaffModified("+modelbean.getBoCaseName()+".getStaffCreated());");
        }
        method.addBodyLine(modelbean.getDaoCaseName()+".insert("+modelbean.getPoCaseName()+");");
        method.addBodyLine("return "+modelbean.getPoCaseName()+".getId();");
    }

    private void gtDelete(Method method,Modelbean modelbean){
        method.addBodyLine("return "+modelbean.getDaoCaseName()+".delete("+method.getParameters().get(0).getName()+");");
    }

    private void gtUpdate(Method method,Modelbean modelbean,TopLevelClass poClass){
        method.addBodyLine(modelbean.getPoName()+" "+modelbean.getPoCaseName()+" = "+modelbean.getTransformName()+".transform("+modelbean.getBoCaseName()+");");
        List<Field> fields = poClass.getFields();
        if(fieldContains(fields, "gmtModified", "java.util.Date")){
            method.addBodyLine(modelbean.getPoCaseName()+".setGmtModified(new Date());");
        }
        method.addBodyLine("return "+modelbean.getDaoCaseName()+".update("+modelbean.getPoCaseName()+");");
    }

    private void gtList(Method method,Modelbean modelbean){
        method.addBodyLine(modelbean.getPoName()+" "+modelbean.getPoCaseName()+" = "+modelbean.getTransformName()+".transform("+modelbean.getBoCaseName()+");");
        method.addBodyLine("List<"+modelbean.getPoName()+"> "+modelbean.getPoCaseName()+"List = "+modelbean.getDaoCaseName()+".list("+modelbean.getPoCaseName()+");");
        method.addBodyLine("return "+modelbean.getTransformName()+".transform("+modelbean.getPoCaseName()+"List);");
    }

    private boolean fieldContains(List<Field> fields,String name,String typeName){
        for(Field field : fields){
            if(field.getName().equals(name) && field.getType().getFullyQualifiedName().equals(typeName)){
                return true;
            }
        }
        return false;
    }

}
