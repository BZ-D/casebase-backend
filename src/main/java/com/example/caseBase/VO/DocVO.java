package com.example.caseBase.VO;
import com.example.caseBase.PO.Litigant;
import lombok.Data;

import java.util.List;

@Data
public class DocVO {

    Long docId;

    String name;                //行政处罚名称

    String docNum;              //行政处罚决定文号

    int type;                //处罚类型

    List<Litigant> litigants;     //负责人

    String cause;               //案由(主要违法违规事实)

    String basis;               //行政处罚依据

    String decision;            //行政处罚决定

    String organ;               //行政处罚的机关名称

    String date;                //作出处罚决定的日期

    int state;               //状态

    String html;

    String content;

}
