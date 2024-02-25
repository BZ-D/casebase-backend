package com.example.caseBase.PO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "policy")
@NoArgsConstructor
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "department")
    private String department;

    // 文件标题
    @Column(name = "title")
    private String title;

    // 内容
    @Column(name = "content",length = 8000)
    private String content;

    // 第x章
    @Column(name = "chapter")
    private int chapter;

    // 第x条
    @Column(name = "article")
    private int article;
}
