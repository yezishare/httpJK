package com.longteng.testcase;

import com.longteng.framework.asserts.AssertUtil;
import com.longteng.framework.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class InterfaceTest extends TestCase {
    //方法内的入参是什么样的类型 取决于ExcelDataProvider返回的什么类型
    @Test(dataProvider = "data")
    public void runInterfaceTest(Map<String, String> stringStringMap) {
        //方法内的入参是什么样的类型，取决于ExcelDataProvider 返回的类型
        String caseName = stringStringMap.get("用例名称");
        String url = stringStringMap.get("接口地址(名称)");
        String methodName = stringStringMap.get("方法");
        String param = stringStringMap.get("入参");
        String assertType = stringStringMap.get("断言类型");
        String expected = stringStringMap.get("预期结果");
        String cookieStr = stringStringMap.get("cookie");
        String contentType = stringStringMap.get("content-type");


        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("url", url);
        paramMap.put("methodName", methodName);
        paramMap.put("paramBody", param);

        Map<String, String> headerMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(cookieStr)) {
            headerMap.put("Cookie", cookieStr);
        }
        //        headerMap.put("Cookie","mba_muid=15181581817831942046716; user-key=a06b6893-c994-433a-bc03-cf2c845e10b7; ipLocation=%u5317%u4eac; _jrda=5; PCSYCityID=1; visitkey=10424179205400009; shshshfp=66ad9bad3f13eac882911c45a917cc14; shshshfpa=886f2e3c-e511-f81d-5742-a341fd781eb8-1520251183; shshshfpb=096f5aa9672231b139c639033afa44afcb1d4e3fba7bf32955a9d312fa; ipLoc-djd=0-0-0-0.0; __jdv=122270672|direct|-|none|-|1520929594005; 3AB9D23F7A4B3C9B=526QE2SQC4Q5WW577PHSO664AVACCQQWDBKXW2MLPN2CAMUHJNLZDVWIRHM5VAOHGC3CSMWM6UF2N3LIE4KHIAT5SY; __jdu=15209295940031819388521; pinId=-Ze9A2mYGTe1ffsfn98I-w; pin=test_zbj; unick=test_zbj; _tp=fDdF9WWN1LcoLCjyP3ASqw%3D%3D; _pst=test_zbj; TrackID=1ui0t1YviIX3YhUmEausun_P-cUo0hiOq52D6EG_stoIIHcwpzFYU0hzU-ar_RpC_HgSy83sicCnZ7DlzH_Hjld0Gvgvy2IjK5MceokxRrRI; ceshi3.com=000; __jda=122270672.15209295940031819388521.1520929594.1520934994.1520941600.3; __jdc=122270672; thor=12E304D7CF348DFBD124B92C0F24433EFD41CA10F44FAC158B85AE873A0186FC84BD65F4AC71619486B146D997374F3D0E79EB7454EF96247A0ED20AAC0D4CE3D757822123464C9F5F3719564A65914202E97F75DF43D7792EB8ECCB3A9FE433CD2C8BC35A8ABE1A22AF07F3DF04308769C2CEC43C44BA9BE9DB483C176D1380DD56D9B66EB9365E2E82610C9937DAAE; AESKEY=0FF0D30E8208787C; QRCodeKEY=F9A284D6BF568E5EA04FF3262A433B406503AF313589F344A66C200741DCAA849D2938D1818DA81A6C2501F503C01641; UIDKEY=57113423543342998; __jdb=122270672.7.15209295940031819388521|3.1520941600; _vender_=QGF32Q2CCUJJHR7KGPD6PLVWSOFNWQDWV3MAFTW2Q7HMWB5JX7X5M7QTDFJFL64V2IINSRK7L33RICFPKINP2Q6JVHEPOATJ3YIKW6VCW4VJVXN4NB7UIFUFR36KTN3ZMV3PG4B2L5ZSSTRAYDG2RSQQXO4ZAVOASHN4DZSM3AHSGFIK26SGPHVL7BPRPKDPFDKXROLQU256N74HCA35FJD7ZHLNQSEZ4G6UVOKS3POXWOSTHM6YYVITI7GACXAGLGCFKH5C6EQ4AGIIMCIXDF22B5YTOMNBZ3MSOKTDJLSNGFADVWUELQFLE6AGZLN7JR2LIWXGNRCNW4YMPEU4OZN65Q2KQE4BVXWM7NJCNTX6PKGPXJKIK2UNQUMFTILEHNNBIUWYGFNMXYNTSUG6DAUUZ3WPT4M7OSYYO432ZYICFKN7CAB5B4OPU3ETKW6RM6ENLAFCS5FF3CSUW7P3RBPWORLUMETZZ6ZSENVR2S35ZKIL62GO2NOMCQFYADVWMGPXFSBAT4IOJ7HD4GAJF6TVJXFRDGFNHF2RXAKF7GOY6CHSHFSA");
        if (StringUtils.isNotBlank(contentType)) {
            headerMap.put("content-type", contentType);
        }
//        headerMap.put("content-type","application/json;charset=UTF-8");

        Map<String, String> resultMap = HttpClientUtil.request(paramMap, headerMap);

        String actual = "{\"code\":200,\"msg\":\"绑定成功\",\"success\":true,\"desc\":\"成功\"}";
        actual = clearStr(resultMap.get("returnBody"));
        expected = clearStr(expected);
        if (null == assertType || assertType.equalsIgnoreCase("全部比对")) {
            AssertUtil.assertEquals(actual, expected, caseName);
        } else if (assertType.equalsIgnoreCase("包含")) {
            AssertUtil.assertContains(actual, expected, caseName);
        }
    }

    private String clearStr(String actual) {
        if (!StringUtils.isBlank(actual)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(actual);
            actual = m.replaceAll("");
        }
        return actual;
    }
}

