package com.longteng.framework.suite;

import com.longteng.framework.config.Constants;
import com.longteng.framework.util.DataUtil;
import org.apache.log4j.Logger;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class LoadSuite {
    public static Logger logger = Logger.getLogger(LoadSuite.class);
    private static DataUtil data = DataUtil.getInstance();

    /**
     * 初始化获取所有的场景、每个场景下的测试用例以及每个测试用例相对应的测试类，封装到List<XmlSuite>
     *
     * @return
     */
    public static List<XmlSuite> getXmlSuites() {
        List<Map<String, String>> seniorData = data.GetSeniorData();
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        HashMap<String, LinkedHashMap<String, String>> testCaseMap = new HashMap<String, LinkedHashMap<String, String>>();
        Map<String, String> caseClassMap = getAllTestCaseClass(); //获取data文件夹下所有的测试类文件
        int i = 1; //预防场景名称重复
        //seniorData 所有的场景名称和 协议
        for (Map<String, String> map : seniorData) {
            String seniorName = (String) map.keySet().iterator().next();
            String seniorProtocol = map.get(seniorName);
            LinkedHashMap<String, String> testCase = new LinkedHashMap<String, String>();
            XmlSuite xmlSuite = null;
            if (testCaseMap.containsKey(seniorName)) {
                seniorName = seniorName + Constants.DUP_MARK + i;
                i++;
            }
            if (seniorProtocol.equalsIgnoreCase("webDriver")) {
                testCase = data.GetTestCase(seniorName);//获取测试用例
                xmlSuite = buildXmlSuit(seniorName, testCase, caseClassMap);
                //构建testng 可以运行的测试套件
            } else {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                xmlSuite = new XmlSuite();
                xmlSuite.setName(seniorName);
                try {
                    XmlTest test = new XmlTest(xmlSuite);
                    classes = new ArrayList<XmlClass>();
                    classes.add(new XmlClass("com.longteng.testcase.InterfaceTest"));
                    test.setName("InterfaceTest");
                    test.setClassNames(classes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Map<String, String> stringStringMap = new HashMap<String, String>();
            stringStringMap.put("seniorProtocol", seniorProtocol);
            xmlSuite.setParameters(stringStringMap);
            testCaseMap.put(seniorName, testCase);//初始化测试用例 MAP  场景，MAP<用例名称,用例描述>
            data.setTestCaseMap(testCaseMap);
            suites.add(xmlSuite);
        }
        return suites;
    }

    /**
     * 构造XmlSuite  一个场景一个suite ,一个text一个testcase，一个testcase一个classes
     *
     * @param senario
     * @param testcase
     * @param caseClassMap
     * @return
     */
    private static XmlSuite buildXmlSuit(String senario, LinkedHashMap<String, String> testcase, Map<String, String> caseClassMap) {
        XmlSuite suite = new XmlSuite();
        suite.setName(senario);
        List<XmlClass> classes = new ArrayList<XmlClass>();
        Set<String> keys = testcase.keySet();
        for (String key : keys) {
            String orginkey = new String(key);
            //key 就是类名
            if (key.contains(Constants.DUP_MARK))//初始化场景 去掉重复的用例 后缀
            {
                key = key.substring(0, key.indexOf(Constants.DUP_MARK));
            }
            String caseClassPackage = caseClassMap.get(key);
            if (null != caseClassPackage) {
                XmlTest test = new XmlTest(suite);
                classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass(caseClassPackage));
                test.setName(orginkey);
                test.setClassNames(classes);
            }
        }
        return suite;
    }

    /**
     * 获取testcase文件夹下所有的java类
     *
     * @param
     * @return String
     */
    public static Map<String, String> getAllTestCaseClass() {
        Map<String, String> classNameMap = new HashMap<String, String>();
        File file = new File(com.longteng.testcase.TestCase.class.getResource(".").getFile());
        getAllTestCaseClass(file, file.getAbsolutePath(), classNameMap);
        return classNameMap;
    }

    /**
     * 获取所有的测试用例类
     *
     * @param rootFile
     * @param testCasePath
     * @param classNameMap
     * @return
     */
    private static void getAllTestCaseClass(File rootFile, String testCasePath, Map<String, String> classNameMap) {
        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                getAllTestCaseClass(file, testCasePath, classNameMap);
            }
        } else {
            String className = "";
            try {
                String filePath = rootFile.getPath(); //获取文件的绝对路径
                if (filePath.indexOf(".class") != -1) {
                    String packageName = filePath.substring(filePath.indexOf("classes") + 8).replace("\\", "."); //获取包名
                    String s = rootFile.getName();        //获取文件名
                    className = s.replace(".class", ""); //获取java 类名
                    packageName = packageName.replace(s, "");
                    if (null != className && !"".equalsIgnoreCase(className)) {
                        classNameMap.put(className, packageName + className);
                    }

                }
            } catch (ClassCastException e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) {
        Map<String, String> classNameMap = new HashMap<String, String>();
        File file = new File(com.longteng.testcase.TestCase.class.getResource(".").getFile());
        getAllTestCaseClass(file, file.getAbsolutePath(), classNameMap);
        System.out.println();
    }
}

