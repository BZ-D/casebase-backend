import pymysql
import jieba
import jieba.analyse

def getItems(conn):
    cur = conn.cursor()
    sql = 'select * from `policy`'  # TODO: 补充选取条目的sql语句
    cur.execute(sql)
    items = cur.fetchall()

    print("获取内规"+str(len(items))+"条")

    cur.close()
    return items


"""
    基于TF-IDF算法的关键词抽取：计算给定条目中的关键词和权重
    input:  items，list of (条目id，内规标题，条目内容)
    output: tfidfs，list of (条目id，关键词，tfidf)
    参考链接：https://github.com/fxsjy/jieba#readme
"""
def calTFIDF(items):
    tfidfs = list()
    for item in items:
        print("正在提取条目"+str(item[0]))
        sentence = item[3]+item[4]+item[5]  # TODO: 更改为对应列名
        keywords = jieba.analyse.extract_tags(sentence,
                                   topK=20,
                                   withWeight=True,
                                   allowPOS=('n',
                                             'f',
                                             's',
                                             't',
                                             'v',
                                             'PER',
                                             'LOC',
                                             'ORG'))
        for keyword in keywords:
            tfidf = (keyword[0], item[0], keyword[1])
            tfidfs.append(tfidf)
    return tfidfs


def saveToDB(tfidfs,conn):
    cur = conn.cursor()
    presql = 'insert into `policy_keyword` (keyword, pid, term_frequency) values '
    i = 0
    print("共" + str(len(tfidfs)) + "条")
    for tfidf in tfidfs:
        i += 1
        print("正在存入第"+str(i)+"个关键词")
        sql = presql+str(tfidf)
        try:
            cur.execute(sql)
            if i==1000:
                conn.commit()
        except:
            conn.rollback()
    cur.close()
    conn.commit()
    return


if __name__ == "__main__":
    # conn = pymysql.connect(host='localhost',
    #                        user='root',
    #                        password='xxxxxxxx',
    #                        port=3306,
    #                        db='caseBase',
    #                        charset='utf8')

    conn = pymysql.connect(host='124.222.0.181',
                           user='caseBase',
                           password='JZPCEC473XixhGKp',
                           port=11451,
                           db='casebase',
                           charset='utf8')

    jieba.enable_parallel(4)
    jieba.enable_paddle()


    items = getItems(conn)
    tfidfs = calTFIDF(items)
    saveToDB(tfidfs, conn)

    conn.close()


