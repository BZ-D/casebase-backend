package com.example.caseBase.PO;

import com.example.caseBase.VO.DocVO;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "document",indexes = {@Index(columnList = "docId")})
public class Doc implements Serializable {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long docId;

    String name;                //行政处罚名称

    String docNum;              //行政处罚决定文号

    /*
     * type: 0 个人处罚
     * type: 1 单位处罚
     * type: 2 个人+单位处罚
     * */

    int type;                //处罚类型

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "docId", cascade = CascadeType.PERSIST)
    List<Litigant> litigants = new ArrayList<>();     //负责人

    @Column(columnDefinition = "text")
    String cause;               //案由(主要违法违规事实)

    String basis;               //行政处罚依据

    String decision;            //行政处罚决定

    String organ;               //行政处罚的机关名称

    String date;                //作出处罚决定的日期

    /*
     * state: 0 未发布
     * state: 1 已发布
     * 默认爬取的所有页面都是已发布的。
     * */
    int state;               //状态

    @Column(columnDefinition = "longtext")
    String html;

    @Column(columnDefinition = "longtext")
    String content;

    Date publishDate;

    String highLightedContent;

    public String getHighLightedContent() {
        return highLightedContent;
    }

    public void setHighLightedContent(String highLightedContent) {
        this.highLightedContent = highLightedContent;
    }

    public Doc(DocVO doc) {
        this.setDocId(doc.getDocId());
        this.setBasis(doc.getBasis());
        this.setCause(doc.getCause());
        this.setDocNum(doc.getDocNum());
        this.setContent(doc.getContent());
        this.setDate(doc.getDate());
        this.setDecision(doc.getDecision());
        this.setHtml(doc.getHtml());
        this.setName(doc.getName());
        this.setOrgan(doc.getOrgan());
        this.setLitigants(doc.getLitigants());
        this.setState(doc.getState());
        this.setType(doc.getType());
    }

    public Doc() {
        litigants = new ArrayList<>();
    }

    public void addLitigant(Litigant litigant) {
        this.litigants.add(litigant);
    }

    public String getLitigantText() {
        StringBuilder text = new StringBuilder();
        for (Litigant li : this.litigants) {
            if (li.unit != null) {
                text.append(li.getName() + " ");
                text.append(li.getUnit()).append("\n");
            }
            if (li.chargeMan != null) {
                text.append(li.getName() + " ");
                text.append(li.getChargeMan() + "\n");
            }
        }
        return text.toString();
    }

    public Map<String, Object> getJsonMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("docId", this.getDocId());
        map.put("name", this.getName());
        map.put("docNum", this.getDocNum());
        map.put("type", this.getType());
        map.put("litigants", this.getLitigantText());
        map.put("cause", this.getCause());
        map.put("basis", this.getBasis());
        map.put("decision", this.getDecision());
        map.put("organ", this.getOrgan());
        map.put("date", this.getDate());
        map.put("state", this.getState());
        map.put("content", this.getContent());
        map.put("publishDate", this.getPublishDate());
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Doc doc = (Doc) o;
        return id != null && Objects.equals(id, doc.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", docId=" + docId +
                ", name='" + name + '\'' +
                ", docNum='" + docNum + '\'' +
                ", type=" + type +
                ", cause='" + cause + '\'' +
                ", basis='" + basis + '\'' +
                ", decision='" + decision + '\'' +
                ", organ='" + organ + '\'' +
                ", date='" + date + '\'' +
                ", state=" + state +
                ", html='" + html + '\'' +
                ", content='" + content + '\'' +
                ", publishDate=" + publishDate +
                ", highLightedContent='" + highLightedContent + '\'' +
                '}';
    }
}
/**
 * @Program: basecase
 * @description: doc class
 * @author: Zhu Wei
 * @create 2021-09-22-23:15
 **/