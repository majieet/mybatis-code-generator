package com.ouer.tools.generator.plugins;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanwu on 2015/6/29.
 */
public class ServicePlugin extends PluginAdapter {
    private List<ServiceBean> serviceBeanList = new ArrayList<ServiceBean>();
    private String targetProject;
    private String servicePackage;
    private String serviceImplPackage;
    private String serviceRootInterface;
    private String serviceImplAbstractClass;

    @Override
    public boolean validate(List<String> warnings) {
        targetProject = this.properties.getProperty("targetProject");
        servicePackage = this.properties.getProperty("servicePackage");
        serviceImplPackage = this.properties.getProperty("serviceImplPackage");
        serviceRootInterface = this.properties.getProperty("serviceRootInterface");
        serviceImplAbstractClass = this.properties.getProperty("serviceImplAbstractClass");

        boolean valid = true;
        if (!StringUtility.stringHasValue(targetProject)) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "targetProject"));
            valid = false;
        }

        if (!StringUtility.stringHasValue(this.properties.getProperty("servicePackage"))) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "servicePackage"));
            valid = false;
        }

        if (!StringUtility.stringHasValue(this.properties.getProperty("serviceImplPackage"))) {
            warnings.add(Messages.getString("ValidationError.18", "MapperConfigPlugin", "serviceImplPackage"));
            valid = false;
        }

        return valid;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        ArrayList result = new ArrayList();
        for (ServiceBean serviceBean : serviceBeanList) {
            Interface interfaze = serviceBean.getInterface();
            IntrospectedTable introspectedTable = serviceBean.getIntrospectedTable();
            //生成Service代码
            Interface serviceInterface = this.generatorService(introspectedTable,interfaze);
            GeneratedJavaFile gafMgr = new GeneratedJavaFile(serviceInterface, targetProject, "utf-8",
                    this.context.getJavaFormatter());
            result.add(gafMgr);
            //生成ServiceImpl代码
            GeneratedJavaFile gafMgrImpl = new GeneratedJavaFile(
                    this.generatorServiceImpl(introspectedTable, interfaze, serviceInterface), targetProject, "utf-8",
                    this.context.getJavaFormatter());
            result.add(gafMgrImpl);

        }
        return result;
    }

    private Interface generatorService(IntrospectedTable introspectedTable,Interface daoInterface) {
        String daoName = daoInterface.getType().getShortName();
        String serviceName = daoName.substring(0, daoName.length() - 6) + "Service";
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(servicePackage + "." + serviceName);
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        if(StringUtility.stringHasValue(serviceRootInterface)) {
            FullyQualifiedJavaType answer = new FullyQualifiedJavaType(serviceRootInterface);
            FullyQualifiedJavaType returnType = introspectedTable.getRules()
                    .calculateAllFieldsClass();
            answer.addTypeArgument(returnType);
            answer.addTypeArgument(FullyQualifiedJavaType.getStringInstance());

            interfaze.addSuperInterface(answer);
            interfaze.addImportedType(answer);
            interfaze.addImportedType(returnType);
        }

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(interfaze);
        return interfaze;
    }

    private TopLevelClass generatorServiceImpl(IntrospectedTable introspectedTable,Interface daoInterface, Interface serviceInterface) {
        String daoName = daoInterface.getType().getShortName();
        String serviceImplName = daoName.substring(0, daoName.length() - 6) + "ServiceImpl";
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(serviceImplPackage + "." + serviceImplName);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.addSuperInterface(serviceInterface.getType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addImportedType(serviceInterface.getType());
        topLevelClass.addImportedType("org.springframework.stereotype.Service");
        topLevelClass.addAnnotation("@Service");

        if(StringUtility.stringHasValue(serviceImplAbstractClass)) {
            FullyQualifiedJavaType answer = new FullyQualifiedJavaType(serviceImplAbstractClass);
            FullyQualifiedJavaType returnType = introspectedTable.getRules()
                    .calculateAllFieldsClass();
            answer.addTypeArgument(returnType);
            answer.addTypeArgument(FullyQualifiedJavaType.getStringInstance());

            topLevelClass.setSuperClass(answer);
            topLevelClass.addImportedType(answer);
            topLevelClass.addImportedType(returnType);
        }

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass);
        return topLevelClass;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {
        ServiceBean serviceBean = new ServiceBean();
        serviceBean.setInterface(interfaze);
        serviceBean.setIntrospectedTable(introspectedTable);
        serviceBeanList.add(serviceBean);
        return true;
    }

    class ServiceBean{
        private Interface Interface;
        private IntrospectedTable introspectedTable;

        public Interface getInterface() {
            return Interface;
        }

        public void setInterface(Interface anInterface) {
            Interface = anInterface;
        }

        public IntrospectedTable getIntrospectedTable() {
            return introspectedTable;
        }

        public void setIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
        }
    }
}
