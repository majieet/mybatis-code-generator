package com.ouer.tools.generator.plugins;

import com.ouer.tools.generator.code.OuerMyBatis3FormattingUtilities;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuanwu on 2015/6/29.
 */
public class ServicePlugin extends PluginAdapter {
    private List<Interface> interfaceList = new ArrayList<Interface>();
    private Map<String, TopLevelClass> modelDaoMap = new HashMap<String, TopLevelClass>();
    private String targetProject;
    private String servicePackage;
    private String serviceImplPackage;

    @Override
    public boolean validate(List<String> warnings) {
        targetProject = this.properties.getProperty("targetProject");
        servicePackage = this.properties.getProperty("servicePackage");
        serviceImplPackage = this.properties.getProperty("serviceImplPackage");

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
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        ArrayList result = new ArrayList();
        for (Interface interfaze : interfaceList) {
            //生成Mgr代码
            Interface mgrInterface = this.generatorMgr(interfaze);
            GeneratedJavaFile gafMgr = new GeneratedJavaFile(mgrInterface, targetProject, "utf-8",
                    this.context.getJavaFormatter());
            result.add(gafMgr);
            //生成MgrImpl代码
            GeneratedJavaFile gafMgrImpl = new GeneratedJavaFile(
                    this.generatorMgrImpl(interfaze, mgrInterface), targetProject, "utf-8",
                    this.context.getJavaFormatter());
            result.add(gafMgrImpl);

        }
        return result;
    }

    private Interface generatorMgr(Interface daoInterface) {
        String daoName = daoInterface.getType().getShortName();
        String serviceName = daoName.substring(0, daoName.length() - 6) + "Service";
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(servicePackage + "." + serviceName);
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        interfaze.addImportedType(FullyQualifiedJavaType.getNewListInstance());

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(interfaze);
        return interfaze;
    }

    private TopLevelClass generatorMgrImpl(Interface daoInterface, Interface serviceInterface) {
        String daoName = daoInterface.getType().getShortName();
        String serviceImplName = daoName.substring(0, daoName.length() - 6) + "ServiceImpl";
        TopLevelClass poClass = modelDaoMap.get(daoName);
        String poName = poClass.getType().getShortName();
        String poCaseName = OuerMyBatis3FormattingUtilities.getFirstLowerCaseString(poName);
        FullyQualifiedJavaType daoType = daoInterface.getType();
        String daoCaseName = OuerMyBatis3FormattingUtilities.getFirstLowerCaseString(daoType.getShortName());


        FullyQualifiedJavaType type = new FullyQualifiedJavaType(serviceImplPackage + "." + serviceImplName);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.addSuperInterface(serviceInterface.getType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addImportedType(serviceInterface.getType());
        topLevelClass.addImportedType(daoInterface.getType());
        topLevelClass.addImportedType(poClass.getType());
        topLevelClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        topLevelClass.addImportedType("org.springframework.stereotype.Service");
        topLevelClass.addImportedType("java.util.List");

        topLevelClass.addAnnotation("@Service(\"" + OuerMyBatis3FormattingUtilities
                .getFirstLowerCaseString(serviceInterface.getType().getShortName()) + "\")");
        Field mapperField = new Field(daoCaseName, daoType);
        mapperField.addAnnotation("@Autowired");
        topLevelClass.addField(mapperField);

        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass);
        return topLevelClass;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {
        interfaceList.add(interfaze);
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String modelName = topLevelClass.getType().getShortName();
        String daoName = modelName + "Mapper";
        modelDaoMap.put(daoName, topLevelClass);
        return true;
    }
}
