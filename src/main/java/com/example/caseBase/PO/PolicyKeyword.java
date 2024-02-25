package com.example.caseBase.PO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Description:
 *
 * @author OrangeSummer
 * @date Created on 2023/2/5
 */

@Data
@Accessors(chain = true)
@Entity
@Table(name = "policy_keyword")
@NoArgsConstructor
public class PolicyKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // 对应poilicy的id
    @Column(name = "pid")
    private int pid;

    // 关键词
    @Column(name = "keyword")
    private String keyword;

    // 关键词的词频
    @Column(name = "term_frequency")
    private float termFrequency;

    @ManyToOne()
    @JoinColumn(name = "pid", insertable = false, updatable = false)
    private Policy policy;
}
